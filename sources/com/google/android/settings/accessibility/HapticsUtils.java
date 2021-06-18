package com.google.android.settings.accessibility;

import android.content.Context;
import android.provider.Settings;

public class HapticsUtils {
    public static boolean isVibrationPrimarySwitchOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "vibrate_on", 1) == 1;
    }
}
