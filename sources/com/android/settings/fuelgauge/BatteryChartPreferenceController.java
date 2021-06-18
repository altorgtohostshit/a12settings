package com.android.settings.fuelgauge;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.fuelgauge.BatteryChartView;
import com.android.settings.fuelgauge.ExpandDividerPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnCreate;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnSaveInstanceState;
import com.android.settingslib.utils.StringUtil;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatteryChartPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnCreate, OnDestroy, OnSaveInstanceState, BatteryChartView.OnSelectListener, OnResume, ExpandDividerPreference.OnExpandListener {
    private static int sUiMode;
    private final SettingsActivity mActivity;
    PreferenceGroup mAppListPrefGroup;
    BatteryChartView mBatteryChartView;
    long[] mBatteryHistoryKeys;
    int[] mBatteryHistoryLevels;
    Map<Integer, List<BatteryDiffEntry>> mBatteryIndexedMap;
    BatteryUtils mBatteryUtils;
    ExpandDividerPreference mExpandDividerPreference;
    private final InstrumentedPreferenceFragment mFragment;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    boolean mIsExpanded = false;
    private final CharSequence[] mNotAllowShowEntryPackages;
    private final CharSequence[] mNotAllowShowSummaryPackages;
    Context mPrefContext;
    final Map<String, Preference> mPreferenceCache = new HashMap();
    private final String mPreferenceKey;
    final List<BatteryDiffEntry> mSystemEntries = new ArrayList();
    int mTrapezoidIndex = -2;

    public boolean isAvailable() {
        return true;
    }

    public BatteryChartPreferenceController(Context context, String str, Lifecycle lifecycle, SettingsActivity settingsActivity, InstrumentedPreferenceFragment instrumentedPreferenceFragment) {
        super(context);
        this.mActivity = settingsActivity;
        this.mFragment = instrumentedPreferenceFragment;
        this.mPreferenceKey = str;
        this.mNotAllowShowSummaryPackages = context.getResources().getTextArray(R.array.allowlist_hide_summary_in_battery_usage);
        this.mNotAllowShowEntryPackages = context.getResources().getTextArray(R.array.allowlist_hide_entry_in_battery_usage);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    public void onCreate(Bundle bundle) {
        if (bundle != null) {
            this.mTrapezoidIndex = bundle.getInt("current_time_slot", this.mTrapezoidIndex);
            this.mIsExpanded = bundle.getBoolean("expand_system_info", this.mIsExpanded);
            Log.d("BatteryChartPreferenceController", String.format("onCreate() slotIndex=%d isExpanded=%b", new Object[]{Integer.valueOf(this.mTrapezoidIndex), Boolean.valueOf(this.mIsExpanded)}));
        }
    }

    public void onResume() {
        int i = this.mContext.getResources().getConfiguration().uiMode & 48;
        if (sUiMode != i) {
            sUiMode = i;
            BatteryDiffEntry.clearCache();
            Log.d("BatteryChartPreferenceController", "clear icon and label cache since uiMode is changed");
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (bundle != null) {
            bundle.putInt("current_time_slot", this.mTrapezoidIndex);
            bundle.putBoolean("expand_system_info", this.mIsExpanded);
            Log.d("BatteryChartPreferenceController", String.format("onSaveInstanceState() slotIndex=%d isExpanded=%b", new Object[]{Integer.valueOf(this.mTrapezoidIndex), Boolean.valueOf(this.mIsExpanded)}));
        }
    }

    public void onDestroy() {
        if (this.mActivity.isChangingConfigurations()) {
            BatteryDiffEntry.clearCache();
        }
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mPreferenceCache.clear();
        PreferenceGroup preferenceGroup = this.mAppListPrefGroup;
        if (preferenceGroup != null) {
            preferenceGroup.removeAll();
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPrefContext = preferenceScreen.getContext();
        PreferenceGroup preferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(this.mPreferenceKey);
        this.mAppListPrefGroup = preferenceGroup;
        preferenceGroup.setOrderingAsAdded(false);
    }

    public String getPreferenceKey() {
        return this.mPreferenceKey;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x006b A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean handlePreferenceTreeClick(androidx.preference.Preference r10) {
        /*
            r9 = this;
            boolean r0 = r10 instanceof com.android.settings.fuelgauge.PowerGaugePreference
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            com.android.settings.fuelgauge.PowerGaugePreference r10 = (com.android.settings.fuelgauge.PowerGaugePreference) r10
            com.android.settings.fuelgauge.BatteryDiffEntry r4 = r10.getBatteryDiffEntry()
            com.android.settings.fuelgauge.BatteryHistEntry r0 = r4.mBatteryHistEntry
            java.lang.String r2 = r0.mPackageName
            boolean r3 = r0.isAppEntry()
            r8 = 1
            if (r3 == 0) goto L_0x002f
            com.android.settings.fuelgauge.BatteryUtils r3 = r9.mBatteryUtils
            if (r3 != 0) goto L_0x0023
            android.content.Context r3 = r9.mPrefContext
            com.android.settings.fuelgauge.BatteryUtils r3 = com.android.settings.fuelgauge.BatteryUtils.getInstance(r3)
            r9.mBatteryUtils = r3
        L_0x0023:
            com.android.settings.fuelgauge.BatteryUtils r3 = r9.mBatteryUtils
            int r3 = r3.getPackageUid(r2)
            r5 = -1
            if (r3 == r5) goto L_0x002d
            goto L_0x002f
        L_0x002d:
            r3 = r1
            goto L_0x0030
        L_0x002f:
            r3 = r8
        L_0x0030:
            r5 = 4
            java.lang.Object[] r5 = new java.lang.Object[r5]
            java.lang.String r6 = r4.getAppLabel()
            r5[r1] = r6
            java.lang.String r6 = r0.getKey()
            r5[r8] = r6
            r6 = 2
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r3)
            r5[r6] = r7
            r6 = 3
            r5[r6] = r0
            java.lang.String r0 = "handleClick() label=%s key=%s isValid:%b\n%s"
            java.lang.String r0 = java.lang.String.format(r0, r5)
            java.lang.String r5 = "BatteryChartPreferenceController"
            android.util.Log.d(r5, r0)
            if (r3 == 0) goto L_0x006b
            com.android.settings.SettingsActivity r0 = r9.mActivity
            com.android.settings.core.InstrumentedPreferenceFragment r3 = r9.mFragment
            java.lang.String r5 = r10.getPercent()
            boolean r6 = r9.isValidToShowSummary(r2)
            java.lang.String r7 = r9.getSlotInformation()
            r2 = r0
            com.android.settings.fuelgauge.AdvancedPowerUsageDetail.startBatteryDetailPage(r2, r3, r4, r5, r6, r7)
            return r8
        L_0x006b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.fuelgauge.BatteryChartPreferenceController.handlePreferenceTreeClick(androidx.preference.Preference):boolean");
    }

    public void onSelect(int i) {
        Log.d("BatteryChartPreferenceController", "onChartSelect:" + i);
        refreshUi(i, false);
    }

    public void onExpand(boolean z) {
        this.mIsExpanded = z;
        refreshExpandUi();
    }

    /* access modifiers changed from: package-private */
    public void setBatteryHistoryMap(Map<Long, Map<String, BatteryHistEntry>> map) {
        this.mHandler.post(new BatteryChartPreferenceController$$ExternalSyntheticLambda2(this, map));
    }

    /* access modifiers changed from: private */
    /* renamed from: setBatteryHistoryMapInner */
    public void lambda$setBatteryHistoryMap$0(Map<Long, Map<String, BatteryHistEntry>> map) {
        if (map == null || map.isEmpty()) {
            this.mBatteryIndexedMap = null;
            this.mBatteryHistoryKeys = null;
            this.mBatteryHistoryLevels = null;
            return;
        }
        ArrayList arrayList = new ArrayList(map.keySet());
        Collections.sort(arrayList);
        this.mBatteryHistoryKeys = new long[25];
        for (int i = 0; i < 25; i++) {
            this.mBatteryHistoryKeys[i] = ((Long) arrayList.get(i)).longValue();
        }
        this.mBatteryHistoryLevels = new int[13];
        for (int i2 = 0; i2 < 13; i2++) {
            long j = this.mBatteryHistoryKeys[i2 * 2];
            Map map2 = map.get(Long.valueOf(j));
            if (map2 == null || map2.isEmpty()) {
                Log.e("BatteryChartPreferenceController", "abnormal entry list in the timestamp:" + ConvertUtils.utcToLocalTime(j));
            } else {
                float f = 0.0f;
                for (BatteryHistEntry batteryHistEntry : map2.values()) {
                    f += (float) batteryHistEntry.mBatteryLevel;
                }
                this.mBatteryHistoryLevels[i2] = Math.round(f / ((float) map2.size()));
            }
        }
        this.mBatteryIndexedMap = ConvertUtils.getIndexedUsageMap(this.mPrefContext, 12, this.mBatteryHistoryKeys, map, true);
        forceRefreshUi();
        Log.d("BatteryChartPreferenceController", String.format("setBatteryHistoryMap() size=%d\nkeys=%s\nlevels=%s", new Object[]{Integer.valueOf(arrayList.size()), utcToLocalTime(this.mBatteryHistoryKeys), Arrays.toString(this.mBatteryHistoryLevels)}));
    }

    /* access modifiers changed from: package-private */
    public void setBatteryChartView(BatteryChartView batteryChartView) {
        if (this.mBatteryChartView != batteryChartView) {
            this.mHandler.post(new BatteryChartPreferenceController$$ExternalSyntheticLambda1(this, batteryChartView));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: setBatteryChartViewInner */
    public void lambda$setBatteryChartView$1(BatteryChartView batteryChartView) {
        this.mBatteryChartView = batteryChartView;
        batteryChartView.setOnSelectListener(this);
        forceRefreshUi();
    }

    private void forceRefreshUi() {
        int i = this.mTrapezoidIndex;
        if (i == -2) {
            i = -1;
        }
        BatteryChartView batteryChartView = this.mBatteryChartView;
        if (batteryChartView != null) {
            batteryChartView.setLevels(this.mBatteryHistoryLevels);
            this.mBatteryChartView.setSelectedIndex(i);
            setTimestampLabel();
        }
        refreshUi(i, true);
    }

    /* access modifiers changed from: package-private */
    public boolean refreshUi(int i, boolean z) {
        if (this.mBatteryIndexedMap == null || this.mBatteryChartView == null || (this.mTrapezoidIndex == i && !z)) {
            return false;
        }
        Log.d("BatteryChartPreferenceController", String.format("refreshUi: index=%d size=%d isForce:%b", new Object[]{Integer.valueOf(i), Integer.valueOf(this.mBatteryIndexedMap.size()), Boolean.valueOf(z)}));
        this.mTrapezoidIndex = i;
        this.mHandler.post(new BatteryChartPreferenceController$$ExternalSyntheticLambda0(this));
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshUi$2() {
        removeAndCacheAllPrefs();
        addAllPreferences();
        refreshCategoryTitle();
    }

    private void addAllPreferences() {
        List list = this.mBatteryIndexedMap.get(Integer.valueOf(this.mTrapezoidIndex));
        if (list == null) {
            Log.w("BatteryChartPreferenceController", "cannot find BatteryDiffEntry for:" + this.mTrapezoidIndex);
            return;
        }
        ArrayList arrayList = new ArrayList();
        this.mSystemEntries.clear();
        list.forEach(new BatteryChartPreferenceController$$ExternalSyntheticLambda3(this, arrayList));
        Comparator<BatteryDiffEntry> comparator = BatteryDiffEntry.COMPARATOR;
        Collections.sort(arrayList, comparator);
        Collections.sort(this.mSystemEntries, comparator);
        Log.d("BatteryChartPreferenceController", String.format("addAllPreferences() app=%d system=%d", new Object[]{Integer.valueOf(arrayList.size()), Integer.valueOf(this.mSystemEntries.size())}));
        if (!arrayList.isEmpty()) {
            addPreferenceToScreen(arrayList);
        }
        if (!this.mSystemEntries.isEmpty()) {
            if (this.mExpandDividerPreference == null) {
                ExpandDividerPreference expandDividerPreference = new ExpandDividerPreference(this.mPrefContext);
                this.mExpandDividerPreference = expandDividerPreference;
                expandDividerPreference.setOnExpandListener(this);
                this.mExpandDividerPreference.setIsExpanded(this.mIsExpanded);
            }
            this.mExpandDividerPreference.setOrder(this.mAppListPrefGroup.getPreferenceCount());
            this.mAppListPrefGroup.addPreference(this.mExpandDividerPreference);
        }
        refreshExpandUi();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addAllPreferences$3(List list, BatteryDiffEntry batteryDiffEntry) {
        String packageName = batteryDiffEntry.getPackageName();
        if (!isValidToShowEntry(packageName)) {
            Log.w("BatteryChartPreferenceController", "ignore showing item:" + packageName);
            return;
        }
        if (batteryDiffEntry.isSystemEntry()) {
            this.mSystemEntries.add(batteryDiffEntry);
        } else {
            list.add(batteryDiffEntry);
        }
        if (this.mTrapezoidIndex >= 0) {
            validateUsageTime(batteryDiffEntry);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v6, resolved type: com.android.settings.fuelgauge.PowerGaugePreference} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addPreferenceToScreen(java.util.List<com.android.settings.fuelgauge.BatteryDiffEntry> r11) {
        /*
            r10 = this;
            androidx.preference.PreferenceGroup r0 = r10.mAppListPrefGroup
            if (r0 == 0) goto L_0x00b1
            boolean r0 = r11.isEmpty()
            if (r0 == 0) goto L_0x000c
            goto L_0x00b1
        L_0x000c:
            androidx.preference.PreferenceGroup r0 = r10.mAppListPrefGroup
            int r0 = r0.getPreferenceCount()
            java.util.Iterator r11 = r11.iterator()
        L_0x0016:
            boolean r1 = r11.hasNext()
            if (r1 == 0) goto L_0x00b1
            java.lang.Object r1 = r11.next()
            com.android.settings.fuelgauge.BatteryDiffEntry r1 = (com.android.settings.fuelgauge.BatteryDiffEntry) r1
            r2 = 0
            java.lang.String r3 = r1.getAppLabel()
            android.graphics.drawable.Drawable r4 = r1.getAppIcon()
            boolean r5 = android.text.TextUtils.isEmpty(r3)
            java.lang.String r6 = "BatteryChartPreferenceController"
            if (r5 != 0) goto L_0x009b
            if (r4 != 0) goto L_0x0036
            goto L_0x009b
        L_0x0036:
            com.android.settings.fuelgauge.BatteryHistEntry r5 = r1.mBatteryHistEntry
            java.lang.String r5 = r5.getKey()
            androidx.preference.PreferenceGroup r7 = r10.mAppListPrefGroup
            androidx.preference.Preference r7 = r7.findPreference(r5)
            com.android.settings.fuelgauge.PowerGaugePreference r7 = (com.android.settings.fuelgauge.PowerGaugePreference) r7
            r8 = 1
            if (r7 == 0) goto L_0x005d
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r9 = "preference should be removed for\n"
            r2.append(r9)
            r2.append(r1)
            java.lang.String r2 = r2.toString()
            android.util.Log.w(r6, r2)
            r2 = r8
            goto L_0x0066
        L_0x005d:
            java.util.Map<java.lang.String, androidx.preference.Preference> r6 = r10.mPreferenceCache
            java.lang.Object r6 = r6.get(r5)
            r7 = r6
            com.android.settings.fuelgauge.PowerGaugePreference r7 = (com.android.settings.fuelgauge.PowerGaugePreference) r7
        L_0x0066:
            if (r7 != 0) goto L_0x0077
            com.android.settings.fuelgauge.PowerGaugePreference r7 = new com.android.settings.fuelgauge.PowerGaugePreference
            android.content.Context r6 = r10.mPrefContext
            r7.<init>(r6)
            r7.setKey(r5)
            java.util.Map<java.lang.String, androidx.preference.Preference> r6 = r10.mPreferenceCache
            r6.put(r5, r7)
        L_0x0077:
            r7.setIcon((android.graphics.drawable.Drawable) r4)
            r7.setTitle((java.lang.CharSequence) r3)
            r7.setOrder(r0)
            double r3 = r1.getPercentOfTotal()
            r7.setPercent(r3)
            r7.setSingleLineTitle(r8)
            r7.setBatteryDiffEntry(r1)
            r10.setPreferenceSummary(r7, r1)
            if (r2 != 0) goto L_0x0097
            androidx.preference.PreferenceGroup r1 = r10.mAppListPrefGroup
            r1.addPreference(r7)
        L_0x0097:
            int r0 = r0 + 1
            goto L_0x0016
        L_0x009b:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "cannot find app resource for\n"
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            android.util.Log.w(r6, r1)
            goto L_0x0016
        L_0x00b1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.fuelgauge.BatteryChartPreferenceController.addPreferenceToScreen(java.util.List):void");
    }

    private void removeAndCacheAllPrefs() {
        PreferenceGroup preferenceGroup = this.mAppListPrefGroup;
        if (preferenceGroup != null && preferenceGroup.getPreferenceCount() != 0) {
            int preferenceCount = this.mAppListPrefGroup.getPreferenceCount();
            for (int i = 0; i < preferenceCount; i++) {
                Preference preference = this.mAppListPrefGroup.getPreference(i);
                if (!TextUtils.isEmpty(preference.getKey())) {
                    this.mPreferenceCache.put(preference.getKey(), preference);
                }
            }
            this.mAppListPrefGroup.removeAll();
        }
    }

    private void refreshExpandUi() {
        if (this.mIsExpanded) {
            addPreferenceToScreen(this.mSystemEntries);
            return;
        }
        for (BatteryDiffEntry batteryDiffEntry : this.mSystemEntries) {
            Preference findPreference = this.mAppListPrefGroup.findPreference(batteryDiffEntry.mBatteryHistEntry.getKey());
            if (findPreference != null) {
                this.mAppListPrefGroup.removePreference(findPreference);
                this.mPreferenceCache.put(findPreference.getKey(), findPreference);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void refreshCategoryTitle() {
        String slotInformation = getSlotInformation();
        Log.d("BatteryChartPreferenceController", String.format("refreshCategoryTitle:%s", new Object[]{slotInformation}));
        PreferenceGroup preferenceGroup = this.mAppListPrefGroup;
        if (preferenceGroup != null) {
            preferenceGroup.setTitle((CharSequence) getSlotInformation(true, slotInformation));
        }
        ExpandDividerPreference expandDividerPreference = this.mExpandDividerPreference;
        if (expandDividerPreference != null) {
            expandDividerPreference.setTitle(getSlotInformation(false, slotInformation));
        }
    }

    private String getSlotInformation(boolean z, String str) {
        if (str == null) {
            if (z) {
                return this.mPrefContext.getString(R.string.battery_app_usage_for_past_24);
            }
            return this.mPrefContext.getString(R.string.battery_system_usage_for_past_24);
        } else if (z) {
            return this.mPrefContext.getString(R.string.battery_app_usage_for, new Object[]{str});
        } else {
            return this.mPrefContext.getString(R.string.battery_system_usage_for, new Object[]{str});
        }
    }

    private String getSlotInformation() {
        int i = this.mTrapezoidIndex;
        if (i < 0) {
            return null;
        }
        return String.format("%s-%s", new Object[]{ConvertUtils.utcToLocalTimeHour(this.mBatteryHistoryKeys[i * 2]), ConvertUtils.utcToLocalTimeHour(this.mBatteryHistoryKeys[(this.mTrapezoidIndex + 1) * 2])});
    }

    /* access modifiers changed from: package-private */
    public void setPreferenceSummary(PowerGaugePreference powerGaugePreference, BatteryDiffEntry batteryDiffEntry) {
        long j = batteryDiffEntry.mForegroundUsageTimeInMs;
        long j2 = batteryDiffEntry.mBackgroundUsageTimeInMs;
        long j3 = j + j2;
        String str = null;
        if (!isValidToShowSummary(batteryDiffEntry.getPackageName())) {
            powerGaugePreference.setSummary((CharSequence) null);
            return;
        }
        if (j3 == 0) {
            powerGaugePreference.setSummary((CharSequence) null);
        } else if (j == 0 && j2 != 0) {
            str = buildUsageTimeInfo(j2, true);
        } else if (j3 < 60000) {
            str = buildUsageTimeInfo(j3, false);
        } else {
            str = buildUsageTimeInfo(j3, false);
            if (j2 > 0) {
                str = str + "\n" + buildUsageTimeInfo(j2, true);
            }
        }
        powerGaugePreference.setSummary((CharSequence) str);
    }

    private String buildUsageTimeInfo(long j, boolean z) {
        if (j < 60000) {
            return this.mPrefContext.getString(z ? R.string.battery_usage_background_less_than_one_minute : R.string.battery_usage_total_less_than_one_minute);
        }
        return this.mPrefContext.getString(z ? R.string.battery_usage_for_background_time : R.string.battery_usage_for_total_time, new Object[]{StringUtil.formatElapsedTime(this.mPrefContext, (double) j, false, false)});
    }

    /* access modifiers changed from: package-private */
    public boolean isValidToShowSummary(String str) {
        return !contains(str, this.mNotAllowShowSummaryPackages);
    }

    /* access modifiers changed from: package-private */
    public boolean isValidToShowEntry(String str) {
        return !contains(str, this.mNotAllowShowEntryPackages);
    }

    /* access modifiers changed from: package-private */
    public void setTimestampLabel() {
        long[] jArr;
        if (this.mBatteryChartView != null && (jArr = this.mBatteryHistoryKeys) != null) {
            long j = jArr[jArr.length - 1];
            if (j == 0) {
                j = Clock.systemUTC().millis();
            }
            String[] strArr = new String[4];
            for (int i = 0; i < 4; i++) {
                strArr[i] = ConvertUtils.utcToLocalTimeHour(j - (((long) (3 - i)) * 28800000));
            }
            this.mBatteryChartView.setTimestamps(strArr);
        }
    }

    private static String utcToLocalTime(long[] jArr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jArr.length; i++) {
            sb.append(String.format("%s| ", new Object[]{ConvertUtils.utcToLocalTime(jArr[i])}));
        }
        return sb.toString();
    }

    private static boolean contains(String str, CharSequence[] charSequenceArr) {
        if (!(str == null || charSequenceArr == null)) {
            for (CharSequence equals : charSequenceArr) {
                if (TextUtils.equals(str, equals)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean validateUsageTime(BatteryDiffEntry batteryDiffEntry) {
        long j = batteryDiffEntry.mForegroundUsageTimeInMs;
        long j2 = batteryDiffEntry.mBackgroundUsageTimeInMs;
        long j3 = j + j2;
        if (j <= 7200000 && j2 <= 7200000 && j3 <= 7200000) {
            return true;
        }
        Log.e("BatteryChartPreferenceController", "validateUsageTime() fail for\n" + batteryDiffEntry);
        return false;
    }
}
