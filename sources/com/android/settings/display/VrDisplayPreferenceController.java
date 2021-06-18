package com.android.settings.display;

import android.app.ActivityManager;
import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class VrDisplayPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "vr_display_pref";
    }

    public VrDisplayPreferenceController(Context context) {
        super(context);
    }

    public boolean isAvailable() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.vr.high_performance");
    }

    public void updateState(Preference preference) {
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "vr_display_mode", 0, ActivityManager.getCurrentUser()) == 0) {
            preference.setSummary((int) R.string.display_vr_pref_low_persistence);
        } else {
            preference.setSummary((int) R.string.display_vr_pref_off);
        }
    }
}
