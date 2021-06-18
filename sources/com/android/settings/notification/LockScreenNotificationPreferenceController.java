package com.android.settings.notification;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.RestrictedListPreference;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import java.util.ArrayList;

public class LockScreenNotificationPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener, LifecycleObserver, OnResume, OnPause {
    private RestrictedListPreference mLockscreen;
    private RestrictedListPreference mLockscreenProfile;
    private int mLockscreenSelectedValue;
    private int mLockscreenSelectedValueProfile;
    /* access modifiers changed from: private */
    public final int mProfileUserId;
    private final boolean mSecure;
    private final boolean mSecureProfile;
    private final String mSettingKey;
    private SettingObserver mSettingObserver;
    private final String mWorkSettingCategoryKey;
    private final String mWorkSettingKey;

    public String getPreferenceKey() {
        return null;
    }

    public boolean isAvailable() {
        return false;
    }

    public LockScreenNotificationPreferenceController(Context context) {
        this(context, (String) null, (String) null, (String) null);
    }

    public LockScreenNotificationPreferenceController(Context context, String str, String str2, String str3) {
        super(context);
        this.mSettingKey = str;
        this.mWorkSettingCategoryKey = str2;
        this.mWorkSettingKey = str3;
        int managedProfileId = Utils.getManagedProfileId(UserManager.get(context), UserHandle.myUserId());
        this.mProfileUserId = managedProfileId;
        LockPatternUtils lockPatternUtils = FeatureFactory.getFactory(context).getSecurityFeatureProvider().getLockPatternUtils(context);
        this.mSecure = lockPatternUtils.isSecure(UserHandle.myUserId());
        this.mSecureProfile = managedProfileId != -10000 && lockPatternUtils.isSecure(managedProfileId);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        RestrictedListPreference restrictedListPreference = (RestrictedListPreference) preferenceScreen.findPreference(this.mSettingKey);
        this.mLockscreen = restrictedListPreference;
        if (restrictedListPreference == null) {
            Log.i("LockScreenNotifPref", "Preference not found: " + this.mSettingKey);
            return;
        }
        if (this.mProfileUserId != -10000) {
            RestrictedListPreference restrictedListPreference2 = (RestrictedListPreference) preferenceScreen.findPreference(this.mWorkSettingKey);
            this.mLockscreenProfile = restrictedListPreference2;
            restrictedListPreference2.setRequiresActiveUnlockedProfile(true);
            this.mLockscreenProfile.setProfileUserId(this.mProfileUserId);
        } else {
            setVisible(preferenceScreen, this.mWorkSettingKey, false);
            setVisible(preferenceScreen, this.mWorkSettingCategoryKey, false);
        }
        this.mSettingObserver = new SettingObserver();
        initLockScreenNotificationPrefDisplay();
        initLockscreenNotificationPrefForProfile();
    }

    private void initLockScreenNotificationPrefDisplay() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        String string = this.mContext.getString(R.string.lock_screen_notifications_summary_show);
        String num = Integer.toString(R.string.lock_screen_notifications_summary_show);
        arrayList.add(string);
        arrayList2.add(num);
        setRestrictedIfNotificationFeaturesDisabled(string, num, 12);
        if (this.mSecure) {
            String string2 = this.mContext.getString(R.string.lock_screen_notifications_summary_hide);
            String num2 = Integer.toString(R.string.lock_screen_notifications_summary_hide);
            arrayList.add(string2);
            arrayList2.add(num2);
            setRestrictedIfNotificationFeaturesDisabled(string2, num2, 4);
        }
        arrayList.add(this.mContext.getString(R.string.lock_screen_notifications_summary_disable));
        arrayList2.add(Integer.toString(R.string.lock_screen_notifications_summary_disable));
        this.mLockscreen.setEntries((CharSequence[]) arrayList.toArray(new CharSequence[arrayList.size()]));
        this.mLockscreen.setEntryValues((CharSequence[]) arrayList2.toArray(new CharSequence[arrayList2.size()]));
        updateLockscreenNotifications();
        if (this.mLockscreen.getEntries().length > 1) {
            this.mLockscreen.setOnPreferenceChangeListener(this);
        } else {
            this.mLockscreen.setEnabled(false);
        }
    }

    private void initLockscreenNotificationPrefForProfile() {
        if (this.mLockscreenProfile == null) {
            Log.i("LockScreenNotifPref", "Preference not found: " + this.mWorkSettingKey);
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        String string = this.mContext.getString(R.string.lock_screen_notifications_summary_show_profile);
        String num = Integer.toString(R.string.lock_screen_notifications_summary_show_profile);
        arrayList.add(string);
        arrayList2.add(num);
        setRestrictedIfNotificationFeaturesDisabled(string, num, 12);
        if (this.mSecureProfile) {
            String string2 = this.mContext.getString(R.string.lock_screen_notifications_summary_hide_profile);
            String num2 = Integer.toString(R.string.lock_screen_notifications_summary_hide_profile);
            arrayList.add(string2);
            arrayList2.add(num2);
            setRestrictedIfNotificationFeaturesDisabled(string2, num2, 4);
        }
        this.mLockscreenProfile.setEntries((CharSequence[]) arrayList.toArray(new CharSequence[arrayList.size()]));
        this.mLockscreenProfile.setEntryValues((CharSequence[]) arrayList2.toArray(new CharSequence[arrayList2.size()]));
        updateLockscreenNotificationsForProfile();
        if (this.mLockscreenProfile.getEntries().length > 1) {
            this.mLockscreenProfile.setOnPreferenceChangeListener(this);
        } else {
            this.mLockscreenProfile.setEnabled(false);
        }
    }

    public void onResume() {
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.register(this.mContext.getContentResolver(), true);
        }
    }

    public void onPause() {
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.register(this.mContext.getContentResolver(), false);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        int parseInt;
        String key = preference.getKey();
        int i = 0;
        if (TextUtils.equals(this.mWorkSettingKey, key)) {
            int parseInt2 = Integer.parseInt((String) obj);
            if (parseInt2 == this.mLockscreenSelectedValueProfile) {
                return false;
            }
            if (parseInt2 == R.string.lock_screen_notifications_summary_show_profile) {
                i = 1;
            }
            Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "lock_screen_allow_private_notifications", i, this.mProfileUserId);
            this.mLockscreenSelectedValueProfile = parseInt2;
            return true;
        } else if (!TextUtils.equals(this.mSettingKey, key) || (parseInt = Integer.parseInt((String) obj)) == this.mLockscreenSelectedValue) {
            return false;
        } else {
            int i2 = parseInt != R.string.lock_screen_notifications_summary_disable ? 1 : 0;
            if (parseInt == R.string.lock_screen_notifications_summary_show) {
                i = 1;
            }
            Settings.Secure.putInt(this.mContext.getContentResolver(), "lock_screen_allow_private_notifications", i);
            Settings.Secure.putInt(this.mContext.getContentResolver(), "lock_screen_show_notifications", i2);
            this.mLockscreenSelectedValue = parseInt;
            return true;
        }
    }

    private void setRestrictedIfNotificationFeaturesDisabled(CharSequence charSequence, CharSequence charSequence2, int i) {
        RestrictedLockUtils.EnforcedAdmin checkIfKeyguardFeaturesDisabled;
        RestrictedLockUtils.EnforcedAdmin checkIfKeyguardFeaturesDisabled2 = RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(this.mContext, i, UserHandle.myUserId());
        if (!(checkIfKeyguardFeaturesDisabled2 == null || this.mLockscreen == null)) {
            this.mLockscreen.addRestrictedItem(new RestrictedListPreference.RestrictedItem(charSequence, charSequence2, checkIfKeyguardFeaturesDisabled2));
        }
        int i2 = this.mProfileUserId;
        if (i2 != -10000 && (checkIfKeyguardFeaturesDisabled = RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(this.mContext, i, i2)) != null && this.mLockscreenProfile != null) {
            this.mLockscreenProfile.addRestrictedItem(new RestrictedListPreference.RestrictedItem(charSequence, charSequence2, checkIfKeyguardFeaturesDisabled));
        }
    }

    public static int getSummaryResource(Context context) {
        boolean lockscreenNotificationsEnabled = getLockscreenNotificationsEnabled(context);
        boolean z = !FeatureFactory.getFactory(context).getSecurityFeatureProvider().getLockPatternUtils(context).isSecure(UserHandle.myUserId()) || getAllowPrivateNotifications(context, UserHandle.myUserId());
        if (!lockscreenNotificationsEnabled) {
            return R.string.lock_screen_notifications_summary_disable;
        }
        return z ? R.string.lock_screen_notifications_summary_show : R.string.lock_screen_notifications_summary_hide;
    }

    /* access modifiers changed from: private */
    public void updateLockscreenNotifications() {
        if (this.mLockscreen != null) {
            this.mLockscreenSelectedValue = getSummaryResource(this.mContext);
            this.mLockscreen.setSummary("%s");
            this.mLockscreen.setValue(Integer.toString(this.mLockscreenSelectedValue));
        }
    }

    private boolean adminAllowsUnredactedNotifications(int i) {
        return (((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)).getKeyguardDisabledFeatures((ComponentName) null, i) & 8) == 0;
    }

    /* access modifiers changed from: private */
    public void updateLockscreenNotificationsForProfile() {
        int i = this.mProfileUserId;
        if (i != -10000 && this.mLockscreenProfile != null) {
            boolean z = adminAllowsUnredactedNotifications(i) && (!this.mSecureProfile || getAllowPrivateNotifications(this.mContext, this.mProfileUserId));
            this.mLockscreenProfile.setSummary("%s");
            int i2 = z ? R.string.lock_screen_notifications_summary_show_profile : R.string.lock_screen_notifications_summary_hide_profile;
            this.mLockscreenSelectedValueProfile = i2;
            this.mLockscreenProfile.setValue(Integer.toString(i2));
        }
    }

    private static boolean getLockscreenNotificationsEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "lock_screen_show_notifications", 0) != 0;
    }

    private static boolean getAllowPrivateNotifications(Context context, int i) {
        return Settings.Secure.getIntForUser(context.getContentResolver(), "lock_screen_allow_private_notifications", 0, i) != 0;
    }

    class SettingObserver extends ContentObserver {
        private final Uri LOCK_SCREEN_PRIVATE_URI = Settings.Secure.getUriFor("lock_screen_allow_private_notifications");
        private final Uri LOCK_SCREEN_SHOW_URI = Settings.Secure.getUriFor("lock_screen_show_notifications");

        public SettingObserver() {
            super(new Handler());
        }

        public void register(ContentResolver contentResolver, boolean z) {
            if (z) {
                contentResolver.registerContentObserver(this.LOCK_SCREEN_PRIVATE_URI, false, this);
                contentResolver.registerContentObserver(this.LOCK_SCREEN_SHOW_URI, false, this);
                return;
            }
            contentResolver.unregisterContentObserver(this);
        }

        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            if (this.LOCK_SCREEN_PRIVATE_URI.equals(uri) || this.LOCK_SCREEN_SHOW_URI.equals(uri)) {
                LockScreenNotificationPreferenceController.this.updateLockscreenNotifications();
                if (LockScreenNotificationPreferenceController.this.mProfileUserId != -10000) {
                    LockScreenNotificationPreferenceController.this.updateLockscreenNotificationsForProfile();
                }
            }
        }
    }
}
