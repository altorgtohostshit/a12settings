package com.android.settings.notification;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class RedactNotificationPreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    static final String KEY_LOCKSCREEN_REDACT = "lock_screen_redact";
    static final String KEY_LOCKSCREEN_WORK_PROFILE_REDACT = "lock_screen_work_redact";
    private static final String TAG = "LockScreenNotifPref";
    private ContentObserver mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean z) {
            if (RedactNotificationPreferenceController.this.mPreference != null) {
                RedactNotificationPreferenceController.this.mPreference.setEnabled(RedactNotificationPreferenceController.this.getAvailabilityStatus() != 5);
            }
        }
    };
    private KeyguardManager mKm;
    /* access modifiers changed from: private */
    public RestrictedSwitchPreference mPreference;
    int mProfileUserId;
    private UserManager mUm;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public RedactNotificationPreferenceController(Context context, String str) {
        super(context, str);
        this.mUm = (UserManager) context.getSystemService(UserManager.class);
        this.mKm = (KeyguardManager) context.getSystemService(KeyguardManager.class);
        this.mProfileUserId = UserHandle.myUserId();
        for (int i : this.mUm.getProfileIdsWithDisabled(UserHandle.myUserId())) {
            if (i != UserHandle.myUserId()) {
                this.mProfileUserId = i;
            }
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (RestrictedSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        int myUserId = KEY_LOCKSCREEN_REDACT.equals(getPreferenceKey()) ? UserHandle.myUserId() : this.mProfileUserId;
        if (myUserId != -10000) {
            this.mPreference.setDisabledByAdmin(getEnforcedAdmin(myUserId));
        }
    }

    public boolean isChecked() {
        return getAllowPrivateNotifications(KEY_LOCKSCREEN_REDACT.equals(getPreferenceKey()) ? UserHandle.myUserId() : this.mProfileUserId);
    }

    public boolean setChecked(boolean z) {
        Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "lock_screen_allow_private_notifications", z ? 1 : 0, KEY_LOCKSCREEN_REDACT.equals(getPreferenceKey()) ? UserHandle.myUserId() : this.mProfileUserId);
        return true;
    }

    public int getAvailabilityStatus() {
        if (KEY_LOCKSCREEN_WORK_PROFILE_REDACT.equals(getPreferenceKey()) && this.mProfileUserId == UserHandle.myUserId()) {
            return 2;
        }
        int myUserId = KEY_LOCKSCREEN_REDACT.equals(getPreferenceKey()) ? UserHandle.myUserId() : this.mProfileUserId;
        if (!FeatureFactory.getFactory(this.mContext).getSecurityFeatureProvider().getLockPatternUtils(this.mContext).isSecure(myUserId)) {
            return 2;
        }
        if (!getLockscreenNotificationsEnabled(myUserId)) {
            return 5;
        }
        if (!KEY_LOCKSCREEN_WORK_PROFILE_REDACT.equals(getPreferenceKey()) || !this.mKm.isDeviceLocked(this.mProfileUserId)) {
            return 0;
        }
        return 5;
    }

    public void onStart() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("lock_screen_show_notifications"), false, this.mContentObserver);
    }

    public void onStop() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
    }

    private RestrictedLockUtils.EnforcedAdmin getEnforcedAdmin(int i) {
        RestrictedLockUtils.EnforcedAdmin checkIfKeyguardFeaturesDisabled = RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(this.mContext, 4, i);
        if (checkIfKeyguardFeaturesDisabled != null) {
            return checkIfKeyguardFeaturesDisabled;
        }
        return RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(this.mContext, 8, i);
    }

    private boolean getAllowPrivateNotifications(int i) {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "lock_screen_allow_private_notifications", 1, i) != 0;
    }

    private boolean getLockscreenNotificationsEnabled(int i) {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "lock_screen_show_notifications", 1, i) != 0;
    }
}
