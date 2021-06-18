package com.android.settings.accessibility;

import android.os.Vibrator;
import com.android.settings.R;

public class TouchVibrationPreferenceFragment extends VibrationPreferenceFragment {
    public int getMetricsCategory() {
        return 1294;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.accessibility_touch_vibration_settings;
    }

    /* access modifiers changed from: protected */
    public int getPreviewVibrationAudioAttributesUsage() {
        return 13;
    }

    /* access modifiers changed from: protected */
    public String getVibrationEnabledSetting() {
        return "haptic_feedback_enabled";
    }

    /* access modifiers changed from: protected */
    public String getVibrationIntensitySetting() {
        return "haptic_feedback_intensity";
    }

    /* access modifiers changed from: protected */
    public int getDefaultVibrationIntensity() {
        return ((Vibrator) getContext().getSystemService(Vibrator.class)).getDefaultHapticFeedbackIntensity();
    }
}
