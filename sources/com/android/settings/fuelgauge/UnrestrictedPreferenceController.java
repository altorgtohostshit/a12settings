package com.android.settings.fuelgauge;

import android.content.Context;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.fuelgauge.BatteryOptimizeUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.widget.RadioButtonPreference;

public class UnrestrictedPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    String KEY_UNRESTRICTED_PREF = "unrestricted_pref";
    BatteryOptimizeUtils mBatteryOptimizeUtils;

    public boolean isAvailable() {
        return true;
    }

    public UnrestrictedPreferenceController(Context context, int i, String str) {
        super(context);
        this.mBatteryOptimizeUtils = new BatteryOptimizeUtils(context, i, str);
    }

    public void updateState(Preference preference) {
        if (!this.mBatteryOptimizeUtils.isValidPackageName()) {
            Log.d("UNRESTRICTED_PREF", "invalid package name, disable pref");
            preference.setEnabled(false);
            return;
        }
        preference.setEnabled(true);
        if (this.mBatteryOptimizeUtils.isSystemOrDefaultApp()) {
            Log.d("UNRESTRICTED_PREF", "is system or default app, unrestricted states only");
            ((RadioButtonPreference) preference).setChecked(true);
        } else if (this.mBatteryOptimizeUtils.getAppUsageState() == BatteryOptimizeUtils.AppUsageState.UNRESTRICTED) {
            Log.d("UNRESTRICTED_PREF", "is unrestricted states");
            ((RadioButtonPreference) preference).setChecked(true);
        } else {
            ((RadioButtonPreference) preference).setChecked(false);
        }
    }

    public String getPreferenceKey() {
        return this.KEY_UNRESTRICTED_PREF;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!this.KEY_UNRESTRICTED_PREF.equals(preference.getKey())) {
            return false;
        }
        this.mBatteryOptimizeUtils.setAppUsageState(BatteryOptimizeUtils.AppUsageState.UNRESTRICTED);
        Log.d("UNRESTRICTED_PREF", "Set unrestricted");
        return true;
    }
}
