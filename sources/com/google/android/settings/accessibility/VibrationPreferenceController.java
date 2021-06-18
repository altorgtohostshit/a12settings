package com.google.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class VibrationPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop {
    public static final int OFF = 0;

    /* renamed from: ON */
    public static final int f120ON = 1;
    boolean mIsAvailable;
    private Preference mPreference;
    HapticsRingReceiverHelper mReceiver = new HapticsRingReceiverHelper(this.mContext) {
        public void onChange() {
            VibrationPreferenceController.this.updatePreference();
        }
    };
    private HapticsSharedPreferences mSharedPrefs = new HapticsSharedPreferences(this.mContext);
    private int mSwitchToggledOffTrigger = 0;
    private final Vibrator mVibrator = ((Vibrator) this.mContext.getSystemService(Vibrator.class));

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

    public VibrationPreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        if (this.mReceiver.isRingerModeSilent() || !HapticsUtils.isVibrationPrimarySwitchOn(this.mContext)) {
            return this.mContext.getText(R.string.switch_off_text);
        }
        return this.mContext.getText(R.string.switch_on_text);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(this.mPreferenceKey);
    }

    public void onStart() {
        this.mReceiver.register(true);
        if (HapticsUtils.isVibrationPrimarySwitchOn(this.mContext)) {
            if (HapticsSwitchPreferenceController.isAllSettingDependenciesOff(this.mContext)) {
                Settings.System.putInt(this.mContext.getContentResolver(), "vibrate_on", 0);
                this.mSwitchToggledOffTrigger = 2;
                this.mSharedPrefs.setAckFlag("ring_vibration_trigger_reason_acknowledged", false);
            } else {
                this.mSwitchToggledOffTrigger = 0;
            }
            this.mSharedPrefs.savePrimarySwitchOffTriggerReason(this.mSwitchToggledOffTrigger);
        }
    }

    public void onStop() {
        this.mReceiver.register(false);
    }

    private void enablePreferenceSetting(boolean z) {
        Settings.System.putInt(this.mContext.getContentResolver(), "vibrate_on", z ? 1 : 0);
    }

    /* access modifiers changed from: protected */
    public void updatePreference() {
        boolean z = true;
        boolean z2 = !this.mReceiver.isRingerModeSilent();
        if (!z2) {
            this.mSwitchToggledOffTrigger = 3;
            this.mSharedPrefs.savePrimarySwitchOffTriggerReason(3);
            this.mSharedPrefs.setAckFlag("vibrate_for_calls_trigger_reason_acknowledged", false);
            this.mSharedPrefs.setAckFlag("ring_vibration_trigger_reason_acknowledged", false);
            this.mIsAvailable = z2;
            updateState(this.mPreference);
        } else if (this.mIsAvailable ^ z2) {
            if (z2) {
                if (this.mSharedPrefs.getPrimarySwitchOffTriggerReason() == 3) {
                    enablePreferenceSetting(true);
                } else {
                    if (!z2 || !HapticsUtils.isVibrationPrimarySwitchOn(this.mContext)) {
                        z = false;
                    }
                    enablePreferenceSetting(z);
                }
            }
            this.mIsAvailable = z2;
            updateState(this.mPreference);
        }
    }
}
