package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class VibrationPreferenceController extends BasePreferenceController {
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
        int i = Settings.System.getInt(this.mContext.getContentResolver(), "ring_vibration_intensity", this.mVibrator.getDefaultRingVibrationIntensity());
        if (Settings.System.getInt(this.mContext.getContentResolver(), "vibrate_when_ringing", 0) == 0 && !AccessibilitySettings.isRampingRingerEnabled(this.mContext)) {
            i = 0;
        }
        CharSequence intensityString = VibrationIntensityPreferenceController.getIntensityString(this.mContext, i);
        int i2 = Settings.System.getInt(this.mContext.getContentResolver(), "notification_vibration_intensity", this.mVibrator.getDefaultNotificationVibrationIntensity());
        CharSequence intensityString2 = VibrationIntensityPreferenceController.getIntensityString(this.mContext, i2);
        int i3 = Settings.System.getInt(this.mContext.getContentResolver(), "haptic_feedback_intensity", this.mVibrator.getDefaultHapticFeedbackIntensity());
        if (Settings.System.getInt(this.mContext.getContentResolver(), "haptic_feedback_enabled", 0) == 0) {
            i3 = 0;
        }
        CharSequence intensityString3 = VibrationIntensityPreferenceController.getIntensityString(this.mContext, i3);
        if (i == i3 && i == i2) {
            return intensityString;
        }
        return this.mContext.getString(R.string.accessibility_vibration_summary, new Object[]{intensityString, intensityString2, intensityString3});
    }
}
