package com.google.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.settings.slices.SliceBackgroundWorker;

public class RingVibrationTogglePreferenceController extends VibrationTogglePreferenceController {
    static final String APPLY_RAMPING_RINGER_KEY = "apply_ramping_ringer";
    static final String VIBRATE_WHEN_RINGING_KEY = "vibrate_when_ringing";

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public RingVibrationTogglePreferenceController(Context context, String str) {
        super(context, str, "ring_vibration_intensity");
    }

    public void onStart() {
        super.onStart();
        if (HapticsUtils.isVibrationPrimarySwitchOn(this.mContext)) {
            setIntensityFromPrimarySwitchTriggerReason();
        }
    }

    /* access modifiers changed from: protected */
    public String getVibrationEnabledSetting() {
        return isRampingRingerEnabled() ? APPLY_RAMPING_RINGER_KEY : VIBRATE_WHEN_RINGING_KEY;
    }

    /* access modifiers changed from: protected */
    public boolean isVibrationEnabled() {
        if (TextUtils.equals(getVibrationEnabledSetting(), APPLY_RAMPING_RINGER_KEY)) {
            if (Settings.Global.getInt(this.mContext.getContentResolver(), APPLY_RAMPING_RINGER_KEY, 0) == 1) {
                return true;
            }
            return false;
        } else if (!TextUtils.equals(getVibrationEnabledSetting(), VIBRATE_WHEN_RINGING_KEY) || Settings.System.getInt(this.mContext.getContentResolver(), VIBRATE_WHEN_RINGING_KEY, 1) != 1) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public int getDefaultIntensity() {
        return this.mVibrator.getDefaultRingVibrationIntensity();
    }

    /* access modifiers changed from: protected */
    public boolean updateSetting(int i) {
        return Settings.System.putInt(this.mContext.getContentResolver(), "ring_vibration_intensity", i);
    }

    /* access modifiers changed from: protected */
    public void setSettingValue() {
        if (isVibrateWhenRingingOn() || isRampingRingerEnabled()) {
            Settings.System.putInt(this.mContext.getContentResolver(), "ring_vibration_intensity", getDefaultIntensity());
        } else {
            Settings.System.putInt(this.mContext.getContentResolver(), "ring_vibration_intensity", 0);
        }
    }

    private boolean isVibrateWhenRingingOn() {
        return Settings.System.getInt(this.mContext.getContentResolver(), VIBRATE_WHEN_RINGING_KEY, 1) == 1;
    }

    private boolean isRampingRingerEnabled() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), APPLY_RAMPING_RINGER_KEY, 0) == 1;
    }
}
