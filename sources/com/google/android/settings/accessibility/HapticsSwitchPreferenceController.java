package com.google.android.settings.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class HapticsSwitchPreferenceController extends BasePreferenceController implements PreferenceControllerMixin, OnMainSwitchChangeListener, LifecycleObserver, OnStart, OnStop {
    private final Context mContext;
    private final SettingObserver mDependenciesSettingObserver = new SettingObserver() {
        public void onChange(boolean z, Uri uri) {
            HapticsSwitchPreferenceController.this.updatePreferenceOnDependencyChange();
        }
    };
    boolean mIsAvailable;
    HapticsRingReceiverHelper mReceiver;
    boolean mRingerModeSilencedBefore;
    private HapticsSharedPreferences mSharedPrefs;
    private SettingsMainSwitchPreference mSwitch;

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$displayPreference$0(Preference preference) {
        return true;
    }

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
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

    public HapticsSwitchPreferenceController(Context context, String str) {
        super(context, str);
        this.mContext = context;
        this.mReceiver = new HapticsRingReceiverHelper(context) {
            public void onChange() {
                HapticsSwitchPreferenceController.this.updatePreference();
            }
        };
        this.mSharedPrefs = new HapticsSharedPreferences(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (isAvailable()) {
            SettingsMainSwitchPreference settingsMainSwitchPreference = (SettingsMainSwitchPreference) preferenceScreen.findPreference(this.mPreferenceKey);
            this.mSwitch = settingsMainSwitchPreference;
            if (settingsMainSwitchPreference != null) {
                settingsMainSwitchPreference.setOnPreferenceClickListener(HapticsSwitchPreferenceController$$ExternalSyntheticLambda0.INSTANCE);
                this.mSwitch.addOnSwitchChangeListener(this);
                this.mSwitch.setTitle(this.mContext.getString(R.string.accessibility_vibration_settings_screen_title));
                this.mSwitch.updateStatus(HapticsUtils.isVibrationPrimarySwitchOn(this.mContext));
                showSwitch(!this.mReceiver.isRingerModeSilent());
                if (this.mSharedPrefs.getPrimarySwitchOffTriggerReason() != 3) {
                    updatePreferenceOnDependencyChange();
                }
            }
        }
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        enablePreferenceSetting(z);
        if (!z) {
            int i = isAllSettingDependenciesOff(this.mContext) ? 2 : 1;
            this.mSharedPrefs.savePrimarySwitchOffTriggerReason(i);
            if (i == 2) {
                this.mSharedPrefs.setAckFlag("ring_vibration_trigger_reason_acknowledged", false);
            }
        }
    }

    public void onStart() {
        this.mReceiver.register(true);
        this.mDependenciesSettingObserver.register(this.mContext.getContentResolver());
        if (HapticsUtils.isVibrationPrimarySwitchOn(this.mContext) && this.mSharedPrefs.getPrimarySwitchOffTriggerReason() != 3) {
            this.mSharedPrefs.savePrimarySwitchOffTriggerReason(0);
        }
    }

    public void onStop() {
        this.mReceiver.register(false);
        this.mDependenciesSettingObserver.unregister(this.mContext.getContentResolver());
    }

    private void enablePreferenceSetting(boolean z) {
        Settings.System.putInt(this.mContext.getContentResolver(), "vibrate_on", z ? 1 : 0);
    }

    private void showSwitch(boolean z) {
        if (z) {
            this.mSwitch.show();
        } else {
            this.mSwitch.hide();
        }
    }

    /* access modifiers changed from: protected */
    public void updatePreference() {
        boolean z = true;
        boolean z2 = !this.mReceiver.isRingerModeSilent();
        boolean isVibrationPrimarySwitchOn = HapticsUtils.isVibrationPrimarySwitchOn(this.mContext);
        showSwitch(z2);
        if (this.mIsAvailable ^ z2) {
            if (!z2) {
                this.mRingerModeSilencedBefore = true;
                this.mSharedPrefs.savePrimarySwitchOffTriggerReason(3);
                this.mSharedPrefs.setAckFlag("vibrate_for_calls_trigger_reason_acknowledged", false);
                this.mSharedPrefs.setAckFlag("ring_vibration_trigger_reason_acknowledged", false);
            } else if (this.mRingerModeSilencedBefore) {
                enablePreferenceSetting(true);
                this.mSwitch.setChecked(true);
            } else {
                enablePreferenceSetting(z2 && isVibrationPrimarySwitchOn);
                SettingsMainSwitchPreference settingsMainSwitchPreference = this.mSwitch;
                if (!z2 || !isVibrationPrimarySwitchOn) {
                    z = false;
                }
                settingsMainSwitchPreference.setChecked(z);
            }
            this.mIsAvailable = z2;
        }
    }

    public static boolean isAllSettingDependenciesOff(Context context) {
        boolean z = Settings.System.getInt(context.getContentResolver(), "haptic_feedback_enabled", 1) == 0;
        boolean z2 = Settings.System.getInt(context.getContentResolver(), "haptic_feedback_intensity", 1) == 0;
        boolean z3 = Settings.System.getInt(context.getContentResolver(), "notification_vibration_intensity", 1) == 0;
        boolean z4 = Settings.System.getInt(context.getContentResolver(), "ring_vibration_intensity", 1) == 0;
        if ((z || z2) && z3 && z4) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void updatePreferenceOnDependencyChange() {
        enablePreferenceSetting(!isAllSettingDependenciesOff(this.mContext));
        this.mSwitch.setChecked(!isAllSettingDependenciesOff(this.mContext));
    }

    private static class SettingObserver extends ContentObserver {
        private final Uri HAPTIC_FEEDBACK_ENABLED_URI = Settings.System.getUriFor("haptic_feedback_enabled");
        private final Uri HAPTIC_FEEDBACK_URI = Settings.System.getUriFor("haptic_feedback_intensity");
        private final Uri NOTIFICATION_VIBRATION_URI = Settings.System.getUriFor("notification_vibration_intensity");
        private final Uri RING_VIBRATION_URI = Settings.System.getUriFor("ring_vibration_intensity");

        public SettingObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        public void register(ContentResolver contentResolver) {
            contentResolver.registerContentObserver(this.HAPTIC_FEEDBACK_ENABLED_URI, false, this);
            contentResolver.registerContentObserver(this.HAPTIC_FEEDBACK_URI, false, this);
            contentResolver.registerContentObserver(this.NOTIFICATION_VIBRATION_URI, false, this);
            contentResolver.registerContentObserver(this.RING_VIBRATION_URI, false, this);
        }

        public void unregister(ContentResolver contentResolver) {
            contentResolver.unregisterContentObserver(this);
        }
    }
}
