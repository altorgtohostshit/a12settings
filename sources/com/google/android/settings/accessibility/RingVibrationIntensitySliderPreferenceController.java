package com.google.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.provider.Settings;
import com.android.settings.slices.SliceBackgroundWorker;
import com.google.android.settings.accessibility.VibrationIntensitySliderPreferenceController;

public class RingVibrationIntensitySliderPreferenceController extends VibrationIntensitySliderPreferenceController {
    static final String FAKE_RING_VIBRATION_INTENSITY_KEY = "ring_vibration_intensity_FAKE";
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f119ON = 1;
    static final String VIBRATE_WHEN_RINGING_KEY = "vibrate_when_ringing";
    private final VibrationIntensitySliderPreferenceController.SettingObserver mVibrateWhenRingingSettingsContentObserver = new VibrationIntensitySliderPreferenceController.SettingObserver(VIBRATE_WHEN_RINGING_KEY) {
        public void onChange(boolean z, Uri uri) {
            RingVibrationIntensitySliderPreferenceController.this.updateSliderPositionOnVibrateWhenRingingMode();
            RingVibrationIntensitySliderPreferenceController ringVibrationIntensitySliderPreferenceController = RingVibrationIntensitySliderPreferenceController.this;
            ringVibrationIntensitySliderPreferenceController.updateState(ringVibrationIntensitySliderPreferenceController.getPreference());
        }
    };

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

    /* access modifiers changed from: protected */
    public int getPreviewVibrationAudioAttributesUsage() {
        return 6;
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

    public RingVibrationIntensitySliderPreferenceController(Context context, String str) {
        super(context, str, "ring_vibration_intensity", VIBRATE_WHEN_RINGING_KEY, true);
    }

    public void onStart() {
        updateSliderPositionOnVibrateWhenRingingMode();
        registerSettingsObservers();
        this.mReceiver.register(true);
        this.mVibrateWhenRingingSettingsContentObserver.register(this.mContext.getContentResolver());
        updatePreferenceVisibility();
    }

    public void onStop() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mVibrateWhenRingingSettingsContentObserver);
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public int getDefaultIntensity() {
        return this.mVibrator.getDefaultRingVibrationIntensity();
    }

    /* access modifiers changed from: protected */
    public String getVibrationEnabledSetting() {
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "apply_ramping_ringer", 0) == 1) {
            return "apply_ramping_ringer";
        }
        return VIBRATE_WHEN_RINGING_KEY;
    }

    /* access modifiers changed from: protected */
    public boolean updateSetting(int i) {
        return Settings.System.putInt(this.mContext.getContentResolver(), "ring_vibration_intensity", i);
    }

    /* access modifiers changed from: protected */
    public void setSliderPositionFromPrimarySwitchTriggerReason() {
        if (!this.mSharedPrefs.isSwitchOffTriggerReasonDependencies()) {
            this.mPosition = 0;
            setSliderPosition(restorePreference());
        } else if (this.mSharedPrefs.isTriggerReasonAcknowledged("ring_vibration_trigger_reason_acknowledged")) {
            setSliderPosition(restorePreference());
        } else {
            setSliderPosition(getDefaultIntensity());
            this.mSharedPrefs.setAckFlag("ring_vibration_trigger_reason_acknowledged", true);
        }
    }

    /* access modifiers changed from: protected */
    public void toggleEnable() {
        int i;
        boolean isVibrationPrimarySwitchOn = HapticsUtils.isVibrationPrimarySwitchOn(this.mContext);
        if (!isVibrationPrimarySwitchOn) {
            if (getSliderPosition() == 0) {
                i = 0;
            } else {
                i = savePreference();
            }
            Settings.System.putInt(this.mContext.getContentResolver(), "ring_vibration_intensity", 0);
            setFakeSliderPosition(i);
            saveFakePreference(i);
        } else if (getSliderPosition() != 0) {
            setSliderPositionFromPrimarySwitchTriggerReason();
        }
        this.mPreference.setEnabled(isVibrationPrimarySwitchOn);
    }

    private void saveFakePreference(int i) {
        this.mSharedPrefs.getSharedPreferences().edit().putInt(FAKE_RING_VIBRATION_INTENSITY_KEY, i).apply();
    }

    private int restoreFakePreference() {
        return this.mSharedPrefs.getSharedPreferences().getInt(FAKE_RING_VIBRATION_INTENSITY_KEY, getSettingValue());
    }

    /* access modifiers changed from: private */
    public void updateSliderPositionOnVibrateWhenRingingMode() {
        if (!HapticsUtils.isVibrationPrimarySwitchOn(this.mContext)) {
            setFakeSliderPosition(restoreFakePreference());
        } else if (Settings.System.getInt(this.mContext.getContentResolver(), VIBRATE_WHEN_RINGING_KEY, 0) == 0 && Settings.Global.getInt(this.mContext.getContentResolver(), "apply_ramping_ringer", 0) == 0) {
            if (getSliderPosition() != 0) {
                savePreference();
            }
            setSliderPosition(0);
        } else {
            setSliderPositionFromPrimarySwitchTriggerReason();
        }
    }
}
