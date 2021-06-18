package com.google.android.settings.sound;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.google.android.settings.accessibility.HapticsRingReceiverHelper;
import com.google.android.settings.accessibility.HapticsSharedPreferences;
import com.google.android.settings.accessibility.HapticsUtils;

public class VibrateForCallsPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop {
    static final String RING_VIBRATION_INTENSITY = "ring_vibration_intensity";
    /* access modifiers changed from: private */
    public Preference mPreference;
    private final SettingObserver mPrimaryVibrationSettingsContentObserver = new SettingObserver("vibrate_on") {
        public void onChange(boolean z, Uri uri) {
            VibrateForCallsPreferenceController.this.toggleEnable();
            VibrateForCallsPreferenceController vibrateForCallsPreferenceController = VibrateForCallsPreferenceController.this;
            vibrateForCallsPreferenceController.updateState(vibrateForCallsPreferenceController.mPreference);
        }
    };
    private HapticsRingReceiverHelper mReceiver = new HapticsRingReceiverHelper(this.mContext) {
        public void onChange() {
            VibrateForCallsPreferenceController.this.updatePreferenceVisibility();
        }
    };
    private final SettingObserver mSettingsContentObserver = new SettingObserver(RING_VIBRATION_INTENSITY) {
        public void onChange(boolean z, Uri uri) {
            VibrateForCallsPreferenceController vibrateForCallsPreferenceController = VibrateForCallsPreferenceController.this;
            vibrateForCallsPreferenceController.updateState(vibrateForCallsPreferenceController.mPreference);
        }
    };
    private HapticsSharedPreferences mSharedPrefs = new HapticsSharedPreferences(this.mContext);

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

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public VibrateForCallsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return VibrateForCallsCategoryPreferenceController.isVibrateForCallsAvailable(this.mContext) ? 0 : 3;
    }

    public CharSequence getSummary() {
        if (isRampingRingerEnabled()) {
            return this.mContext.getText(R.string.vibrate_when_ringing_option_ramping_ringer);
        }
        if (Settings.System.getInt(this.mContext.getContentResolver(), "vibrate_when_ringing", 0) == 1) {
            return this.mContext.getText(R.string.vibrate_when_ringing_option_always_vibrate);
        }
        return this.mContext.getText(R.string.vibrate_when_ringing_option_never_vibrate);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onStart() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        SettingObserver settingObserver = this.mSettingsContentObserver;
        contentResolver.registerContentObserver(settingObserver.uri, false, settingObserver);
        ContentResolver contentResolver2 = this.mContext.getContentResolver();
        SettingObserver settingObserver2 = this.mPrimaryVibrationSettingsContentObserver;
        contentResolver2.registerContentObserver(settingObserver2.uri, false, settingObserver2);
        this.mReceiver.register(true);
        if (!this.mSharedPrefs.isTriggerReasonAcknowledged("vibrate_for_calls_trigger_reason_acknowledged") && this.mSharedPrefs.isSwitchOffTriggerReasonDependencies() && HapticsUtils.isVibrationPrimarySwitchOn(this.mContext)) {
            setDefaults();
            this.mSharedPrefs.setAckFlag("vibrate_for_calls_trigger_reason_acknowledged", true);
        }
        if (!HapticsUtils.isVibrationPrimarySwitchOn(this.mContext)) {
            this.mPreference.setEnabled(false);
        }
        updateState(this.mPreference);
        updatePreferenceVisibility();
    }

    public void onStop() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsContentObserver);
        this.mContext.getContentResolver().unregisterContentObserver(this.mPrimaryVibrationSettingsContentObserver);
        this.mReceiver.register(false);
    }

    private int getApplyRampingRinger() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "apply_ramping_ringer", 0);
    }

    private boolean isRampingRingerEnabled() {
        return getApplyRampingRinger() == 1;
    }

    private void setDefaults() {
        Settings.System.putInt(this.mContext.getContentResolver(), "vibrate_when_ringing", 1);
        Settings.Global.putInt(this.mContext.getContentResolver(), "apply_ramping_ringer", 0);
    }

    /* access modifiers changed from: protected */
    public void toggleEnable() {
        boolean isVibrationPrimarySwitchOn = HapticsUtils.isVibrationPrimarySwitchOn(this.mContext);
        if (!isVibrationPrimarySwitchOn) {
            savePreference();
            Settings.System.putInt(this.mContext.getContentResolver(), "vibrate_when_ringing", 0);
            Settings.Global.putInt(this.mContext.getContentResolver(), "apply_ramping_ringer", 0);
        } else if (this.mSharedPrefs.isSwitchOffTriggerReasonDependencies()) {
            setDefaults();
        } else {
            restorePreference();
        }
        this.mPreference.setEnabled(isVibrationPrimarySwitchOn);
    }

    /* access modifiers changed from: protected */
    public void savePreference() {
        SharedPreferences sharedPreferences = this.mSharedPrefs.getSharedPreferences();
        sharedPreferences.edit().putInt("vibrate_when_ringing", Settings.System.getInt(this.mContext.getContentResolver(), "vibrate_when_ringing", 1)).apply();
        sharedPreferences.edit().putInt("apply_ramping_ringer", getApplyRampingRinger()).apply();
    }

    /* access modifiers changed from: protected */
    public void restorePreference() {
        SharedPreferences sharedPreferences = this.mSharedPrefs.getSharedPreferences();
        Settings.System.putInt(this.mContext.getContentResolver(), "vibrate_when_ringing", sharedPreferences.getInt("vibrate_when_ringing", 1));
        Settings.Global.putInt(this.mContext.getContentResolver(), "apply_ramping_ringer", sharedPreferences.getInt("apply_ramping_ringer", 0));
    }

    /* access modifiers changed from: private */
    public void updatePreferenceVisibility() {
        this.mPreference.setVisible(!this.mReceiver.isRingerModeSilent());
    }

    private static class SettingObserver extends ContentObserver {
        public final Uri uri;

        public SettingObserver(String str) {
            super(new Handler(Looper.getMainLooper()));
            this.uri = Settings.System.getUriFor(str);
        }
    }
}
