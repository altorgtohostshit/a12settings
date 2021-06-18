package com.android.settings.notification;

import android.content.Context;
import android.os.Vibrator;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class VibrateOnTouchPreferenceController extends SettingPrefController {
    public VibrateOnTouchPreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, Lifecycle lifecycle) {
        super(context, settingsPreferenceFragment, lifecycle);
        this.mPreference = new SettingPref(2, "vibrate_on_touch", "haptic_feedback_enabled", 0, new int[0]) {
            public boolean isApplicable(Context context) {
                return VibrateOnTouchPreferenceController.hasHaptic(context);
            }
        };
    }

    /* access modifiers changed from: private */
    public static boolean hasHaptic(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        return vibrator != null && vibrator.hasVibrator();
    }
}
