package com.android.settings.fuelgauge;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.BatteryConsumer;
import android.os.BatteryUsageStats;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UidBatteryConsumer;
import android.os.UserBatteryConsumer;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.SparseArray;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.internal.os.PowerProfile;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;

public class BatteryAppListPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnPause, OnDestroy {
    static final boolean USE_FAKE_DATA = false;
    static Config sConfig = new Config() {
        public boolean shouldShowBatteryAttributionList(Context context) {
            if (new PowerProfile(context).getAveragePower("screen.full") >= 10.0d) {
                return true;
            }
            return BatteryAppListPreferenceController.USE_FAKE_DATA;
        }
    };
    /* access modifiers changed from: private */
    public final SettingsActivity mActivity;
    PreferenceGroup mAppListGroup;
    private BatteryUsageStats mBatteryUsageStats;
    BatteryUtils mBatteryUtils;
    private final InstrumentedPreferenceFragment mFragment;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            SettingsActivity access$100;
            int i = message.what;
            if (i == 1) {
                BatteryEntry batteryEntry = (BatteryEntry) message.obj;
                PowerGaugePreference powerGaugePreference = (PowerGaugePreference) BatteryAppListPreferenceController.this.mAppListGroup.findPreference(batteryEntry.getKey());
                if (powerGaugePreference != null) {
                    powerGaugePreference.setIcon(BatteryAppListPreferenceController.this.mUserManager.getBadgedIconForUser(batteryEntry.getIcon(), new UserHandle(UserHandle.getUserId(batteryEntry.getUid()))));
                    powerGaugePreference.setTitle((CharSequence) batteryEntry.name);
                    if (batteryEntry.isAppEntry()) {
                        powerGaugePreference.setContentDescription(batteryEntry.name);
                    }
                }
            } else if (i == 2 && (access$100 = BatteryAppListPreferenceController.this.mActivity) != null) {
                access$100.reportFullyDrawn();
            }
            super.handleMessage(message);
        }
    };
    private final PackageManager mPackageManager;
    private Context mPrefContext;
    private ArrayMap<String, Preference> mPreferenceCache;
    private final String mPreferenceKey;
    /* access modifiers changed from: private */
    public final UserManager mUserManager;

    public interface Config {
        boolean shouldShowBatteryAttributionList(Context context);
    }

    public boolean isAvailable() {
        return true;
    }

    public BatteryAppListPreferenceController(Context context, String str, Lifecycle lifecycle, SettingsActivity settingsActivity, InstrumentedPreferenceFragment instrumentedPreferenceFragment) {
        super(context);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
        this.mPreferenceKey = str;
        this.mBatteryUtils = BatteryUtils.getInstance(context);
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mPackageManager = context.getPackageManager();
        this.mActivity = settingsActivity;
        this.mFragment = instrumentedPreferenceFragment;
    }

    public void onPause() {
        BatteryEntry.stopRequestQueue();
        this.mHandler.removeMessages(1);
    }

    public void onDestroy() {
        if (this.mActivity.isChangingConfigurations()) {
            BatteryEntry.clearUidCache();
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPrefContext = preferenceScreen.getContext();
        PreferenceGroup preferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(this.mPreferenceKey);
        this.mAppListGroup = preferenceGroup;
        preferenceGroup.setTitle((CharSequence) this.mPrefContext.getString(R.string.power_usage_list_summary));
    }

    public String getPreferenceKey() {
        return this.mPreferenceKey;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!(preference instanceof PowerGaugePreference)) {
            return USE_FAKE_DATA;
        }
        PowerGaugePreference powerGaugePreference = (PowerGaugePreference) preference;
        AdvancedPowerUsageDetail.startBatteryDetailPage(this.mActivity, this.mFragment, powerGaugePreference.getInfo(), powerGaugePreference.getPercent());
        return true;
    }

    public void refreshAppListGroup(BatteryUsageStats batteryUsageStats, boolean z) {
        BatteryEntry batteryEntry;
        if (isAvailable()) {
            this.mBatteryUsageStats = batteryUsageStats;
            this.mAppListGroup.setTitle((int) R.string.power_usage_list_summary);
            cacheRemoveAllPrefs(this.mAppListGroup);
            PreferenceGroup preferenceGroup = this.mAppListGroup;
            boolean z2 = USE_FAKE_DATA;
            preferenceGroup.setOrderingAsAdded(USE_FAKE_DATA);
            int i = 1;
            if (sConfig.shouldShowBatteryAttributionList(this.mContext)) {
                int dischargePercentage = getDischargePercentage(batteryUsageStats);
                List<BatteryEntry> coalescedUsageList = getCoalescedUsageList(z, true);
                double consumedPower = batteryUsageStats.getConsumedPower();
                int size = coalescedUsageList.size();
                int i2 = 0;
                boolean z3 = false;
                while (true) {
                    if (i2 >= size) {
                        z2 = z3;
                        break;
                    }
                    BatteryEntry batteryEntry2 = coalescedUsageList.get(i2);
                    BatteryEntry batteryEntry3 = batteryEntry2;
                    double calculateBatteryPercent = this.mBatteryUtils.calculateBatteryPercent(batteryEntry2.getConsumedPower(), consumedPower, dischargePercentage);
                    if (((int) (0.5d + calculateBatteryPercent)) >= i) {
                        UserHandle userHandle = new UserHandle(UserHandle.getUserId(batteryEntry3.getUid()));
                        Drawable badgedIconForUser = this.mUserManager.getBadgedIconForUser(batteryEntry3.getIcon(), userHandle);
                        CharSequence badgedLabelForUser = this.mUserManager.getBadgedLabelForUser(batteryEntry3.getLabel(), userHandle);
                        String key = batteryEntry3.getKey();
                        PowerGaugePreference powerGaugePreference = (PowerGaugePreference) getCachedPreference(key);
                        if (powerGaugePreference == null) {
                            batteryEntry = batteryEntry3;
                            powerGaugePreference = new PowerGaugePreference(this.mPrefContext, badgedIconForUser, badgedLabelForUser, batteryEntry);
                            powerGaugePreference.setKey(key);
                        } else {
                            batteryEntry = batteryEntry3;
                        }
                        batteryEntry.percent = calculateBatteryPercent;
                        powerGaugePreference.setTitle((CharSequence) batteryEntry.getLabel());
                        powerGaugePreference.setOrder(i2 + 1);
                        powerGaugePreference.setPercent(calculateBatteryPercent);
                        powerGaugePreference.shouldShowAnomalyIcon(USE_FAKE_DATA);
                        setUsageSummary(powerGaugePreference, batteryEntry);
                        this.mAppListGroup.addPreference(powerGaugePreference);
                        if (this.mAppListGroup.getPreferenceCount() - getCachedCount() > 21) {
                            z2 = true;
                            break;
                        }
                        z3 = true;
                    }
                    i2++;
                    i = 1;
                }
            }
            if (!z2) {
                addNotAvailableMessage();
            }
            removeCachedPrefs(this.mAppListGroup);
            BatteryEntry.startRequestQueue();
        }
    }

    public List<BatteryEntry> getBatteryEntryList(BatteryUsageStats batteryUsageStats, boolean z) {
        this.mBatteryUsageStats = batteryUsageStats;
        if (!sConfig.shouldShowBatteryAttributionList(this.mContext)) {
            return null;
        }
        int dischargePercentage = getDischargePercentage(batteryUsageStats);
        List<BatteryEntry> coalescedUsageList = getCoalescedUsageList(z, USE_FAKE_DATA);
        double consumedPower = batteryUsageStats.getConsumedPower();
        for (int i = 0; i < coalescedUsageList.size(); i++) {
            BatteryEntry batteryEntry = coalescedUsageList.get(i);
            batteryEntry.percent = this.mBatteryUtils.calculateBatteryPercent(batteryEntry.getConsumedPower(), consumedPower, dischargePercentage);
        }
        return coalescedUsageList;
    }

    private int getDischargePercentage(BatteryUsageStats batteryUsageStats) {
        int dischargePercentage = batteryUsageStats.getDischargePercentage();
        if (dischargePercentage < 0) {
            return 0;
        }
        return dischargePercentage;
    }

    private List<BatteryEntry> getCoalescedUsageList(boolean z, boolean z2) {
        boolean shouldHideUidBatteryConsumer;
        SparseArray sparseArray = new SparseArray();
        ArrayList arrayList = new ArrayList();
        List uidBatteryConsumers = this.mBatteryUsageStats.getUidBatteryConsumers();
        int size = uidBatteryConsumers.size();
        int i = 0;
        while (true) {
            int i2 = 1000;
            if (i >= size) {
                break;
            }
            UidBatteryConsumer uidBatteryConsumer = (UidBatteryConsumer) uidBatteryConsumers.get(i);
            int uid = uidBatteryConsumer.getUid();
            if (isSharedGid(uidBatteryConsumer.getUid())) {
                uid = UserHandle.getUid(0, UserHandle.getAppIdFromSharedAppGid(uidBatteryConsumer.getUid()));
            }
            if (!isSystemUid(uid) || "mediaserver".equals(uidBatteryConsumer.getPackageWithHighestDrain())) {
                i2 = uid;
            }
            String[] packagesForUid = this.mPackageManager.getPackagesForUid(uidBatteryConsumer.getUid());
            if (!this.mBatteryUtils.shouldHideUidBatteryConsumerUnconditionally(uidBatteryConsumer, packagesForUid) && (!(shouldHideUidBatteryConsumer = this.mBatteryUtils.shouldHideUidBatteryConsumer(uidBatteryConsumer, packagesForUid)) || z)) {
                int indexOfKey = sparseArray.indexOfKey(i2);
                if (indexOfKey < 0) {
                    sparseArray.put(i2, new BatteryEntry(this.mContext, this.mHandler, this.mUserManager, uidBatteryConsumer, shouldHideUidBatteryConsumer, packagesForUid, (String) null, z2));
                } else {
                    ((BatteryEntry) sparseArray.valueAt(indexOfKey)).add(uidBatteryConsumer);
                }
            }
            i++;
        }
        BatteryConsumer aggregateBatteryConsumer = this.mBatteryUsageStats.getAggregateBatteryConsumer(0);
        BatteryConsumer aggregateBatteryConsumer2 = this.mBatteryUsageStats.getAggregateBatteryConsumer(1);
        for (int i3 = 0; i3 < 18; i3++) {
            if (z || !this.mBatteryUtils.shouldHideDevicePowerComponent(aggregateBatteryConsumer, i3)) {
                BatteryEntry batteryEntry = r8;
                BatteryEntry batteryEntry2 = new BatteryEntry(this.mContext, i3, aggregateBatteryConsumer.getConsumedPower(i3), aggregateBatteryConsumer2.getConsumedPower(i3), aggregateBatteryConsumer.getUsageDurationMillis(i3));
                arrayList.add(batteryEntry);
            }
        }
        for (int i4 = 1000; i4 < aggregateBatteryConsumer.getCustomPowerComponentCount() + 1000; i4++) {
            if (z || !this.mBatteryUtils.shouldHideCustomDevicePowerComponent(aggregateBatteryConsumer, i4)) {
                arrayList.add(new BatteryEntry(this.mContext, i4, aggregateBatteryConsumer.getCustomPowerComponentName(i4), aggregateBatteryConsumer.getConsumedPowerForCustomComponent(i4), aggregateBatteryConsumer2.getConsumedPowerForCustomComponent(i4)));
            }
        }
        if (z) {
            List userBatteryConsumers = this.mBatteryUsageStats.getUserBatteryConsumers();
            int size2 = userBatteryConsumers.size();
            for (int i5 = 0; i5 < size2; i5++) {
                arrayList.add(new BatteryEntry(this.mContext, this.mHandler, this.mUserManager, (UserBatteryConsumer) userBatteryConsumers.get(i5), true, (String[]) null, (String) null, z2));
            }
        }
        int size3 = sparseArray.size();
        for (int i6 = 0; i6 < size3; i6++) {
            arrayList.add((BatteryEntry) sparseArray.valueAt(i6));
        }
        arrayList.sort(BatteryEntry.COMPARATOR);
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public void setUsageSummary(Preference preference, BatteryEntry batteryEntry) {
        long timeInForegroundMs = batteryEntry.getTimeInForegroundMs();
        if (shouldShowSummary(batteryEntry) && timeInForegroundMs >= 60000) {
            CharSequence formatElapsedTime = StringUtil.formatElapsedTime(this.mContext, (double) timeInForegroundMs, USE_FAKE_DATA, USE_FAKE_DATA);
            if (!batteryEntry.isHidden()) {
                formatElapsedTime = TextUtils.expandTemplate(this.mContext.getText(R.string.battery_used_for), new CharSequence[]{formatElapsedTime});
            }
            preference.setSummary(formatElapsedTime);
        }
    }

    private void cacheRemoveAllPrefs(PreferenceGroup preferenceGroup) {
        this.mPreferenceCache = new ArrayMap<>();
        int preferenceCount = preferenceGroup.getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            Preference preference = preferenceGroup.getPreference(i);
            if (!TextUtils.isEmpty(preference.getKey())) {
                this.mPreferenceCache.put(preference.getKey(), preference);
            }
        }
    }

    private boolean shouldShowSummary(BatteryEntry batteryEntry) {
        CharSequence[] textArray = this.mContext.getResources().getTextArray(R.array.allowlist_hide_summary_in_battery_usage);
        String defaultPackageName = batteryEntry.getDefaultPackageName();
        for (CharSequence equals : textArray) {
            if (TextUtils.equals(defaultPackageName, equals)) {
                return USE_FAKE_DATA;
            }
        }
        return true;
    }

    private static boolean isSharedGid(int i) {
        if (UserHandle.getAppIdFromSharedAppGid(i) > 0) {
            return true;
        }
        return USE_FAKE_DATA;
    }

    private static boolean isSystemUid(int i) {
        int appId = UserHandle.getAppId(i);
        if (appId < 1000 || appId >= 10000) {
            return USE_FAKE_DATA;
        }
        return true;
    }

    private Preference getCachedPreference(String str) {
        ArrayMap<String, Preference> arrayMap = this.mPreferenceCache;
        if (arrayMap != null) {
            return arrayMap.remove(str);
        }
        return null;
    }

    private void removeCachedPrefs(PreferenceGroup preferenceGroup) {
        for (Preference removePreference : this.mPreferenceCache.values()) {
            preferenceGroup.removePreference(removePreference);
        }
        this.mPreferenceCache = null;
    }

    private int getCachedCount() {
        ArrayMap<String, Preference> arrayMap = this.mPreferenceCache;
        if (arrayMap != null) {
            return arrayMap.size();
        }
        return 0;
    }

    private void addNotAvailableMessage() {
        if (getCachedPreference("not_available") == null) {
            Preference preference = new Preference(this.mPrefContext);
            preference.setKey("not_available");
            preference.setTitle((int) R.string.power_usage_not_available);
            preference.setSelectable(USE_FAKE_DATA);
            this.mAppListGroup.addPreference(preference);
        }
    }
}
