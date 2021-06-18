package com.google.android.settings.aware;

import android.content.ContentResolver;
import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.aware.AwareFeatureProviderImpl;

public class AwareFeatureProviderGoogleImpl extends AwareFeatureProviderImpl {
    public boolean isSupported(Context context) {
        return hasAwareSensor() && isAllowed(context);
    }

    public boolean isEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "aware_enabled", 0) == 1;
    }

    public void showRestrictionDialog(Fragment fragment) {
        AwareEnabledDialogFragment.show(fragment, Boolean.FALSE);
    }

    public CharSequence getGestureSummary(Context context, boolean z, boolean z2, boolean z3) {
        ContentResolver contentResolver = context.getContentResolver();
        boolean z4 = true;
        boolean z5 = Settings.Secure.getInt(contentResolver, "silence_gesture", 1) == 1;
        boolean z6 = Settings.Secure.getInt(contentResolver, "skip_gesture", 1) == 1;
        boolean z7 = Settings.Secure.getInt(contentResolver, "tap_gesture", 1) == 1;
        if (!z5 && !z6 && !z7) {
            z4 = false;
        }
        if (!isSupported(context) || !isEnabled(context) || !z4) {
            return null;
        }
        if (z && z2) {
            return context.getText(R.string.aware_gesture_summary_on_with_assist);
        }
        if (z3) {
            return context.getText(R.string.aware_gesture_summary_on_non_assist);
        }
        return context.getText(R.string.aware_gesture_summary_off);
    }

    private static boolean isAllowed(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "aware_allowed", 0) == 1;
    }

    private static boolean hasAwareSensor() {
        return SystemProperties.getBoolean("ro.vendor.aware_available", false);
    }
}
