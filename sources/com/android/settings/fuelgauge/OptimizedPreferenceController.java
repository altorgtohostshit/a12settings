package com.android.settings.fuelgauge;

import android.content.Context;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.fuelgauge.BatteryOptimizeUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.widget.RadioButtonPreference;

public class OptimizedPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    String KEY_OPTIMIZED_PREF = "optimized_pref";
    BatteryOptimizeUtils mBatteryOptimizeUtils;

    public boolean isAvailable() {
        return true;
    }

    public OptimizedPreferenceController(Context context, int i, String str) {
        super(context);
        this.mBatteryOptimizeUtils = new BatteryOptimizeUtils(context, i, str);
    }

    public void updateState(Preference preference) {
        if (!this.mBatteryOptimizeUtils.isValidPackageName()) {
            Log.d("OPTIMIZED_PREF", "invalid package name, optimized states only");
            preference.setEnabled(true);
            ((RadioButtonPreference) preference).setChecked(true);
        } else if (this.mBatteryOptimizeUtils.isSystemOrDefaultApp()) {
            Log.d("OPTIMIZED_PREF", "is system or default app, disable pref");
            ((RadioButtonPreference) preference).setChecked(false);
            preference.setEnabled(false);
        } else if (this.mBatteryOptimizeUtils.getAppUsageState() == BatteryOptimizeUtils.AppUsageState.OPTIMIZED) {
            Log.d("OPTIMIZED_PREF", "is optimized states");
            ((RadioButtonPreference) preference).setChecked(true);
        } else {
            ((RadioButtonPreference) preference).setChecked(false);
        }
    }

    public String getPreferenceKey() {
        return this.KEY_OPTIMIZED_PREF;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!this.KEY_OPTIMIZED_PREF.equals(preference.getKey())) {
            return false;
        }
        this.mBatteryOptimizeUtils.setAppUsageState(BatteryOptimizeUtils.AppUsageState.OPTIMIZED);
        Log.d("OPTIMIZED_PREF", "Set optimized");
        return true;
    }
}
