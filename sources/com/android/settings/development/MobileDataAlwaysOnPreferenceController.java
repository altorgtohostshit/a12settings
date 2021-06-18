package com.android.settings.development;

import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class MobileDataAlwaysOnPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    static final int SETTING_VALUE_OFF = 0;
    static final int SETTING_VALUE_ON = 1;

    public String getPreferenceKey() {
        return "mobile_data_always_on";
    }

    public MobileDataAlwaysOnPreferenceController(Context context) {
        super(context);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "mobile_data_always_on", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }

    public void updateState(Preference preference) {
        boolean z = true;
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "mobile_data_always_on", 1);
        SwitchPreference switchPreference = (SwitchPreference) this.mPreference;
        if (i == 0) {
            z = false;
        }
        switchPreference.setChecked(z);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        Settings.Global.putInt(this.mContext.getContentResolver(), "mobile_data_always_on", 1);
        ((SwitchPreference) this.mPreference).setChecked(true);
    }
}
