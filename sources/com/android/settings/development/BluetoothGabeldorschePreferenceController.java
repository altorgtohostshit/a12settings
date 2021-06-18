package com.android.settings.development;

import android.content.Context;
import android.provider.DeviceConfig;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class BluetoothGabeldorschePreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    static final String CURRENT_GD_FLAG = "INIT_gd_scanning";

    public String getPreferenceKey() {
        return "bluetooth_gabeldorsche_enable";
    }

    public BluetoothGabeldorschePreferenceController(Context context) {
        super(context);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        DeviceConfig.setProperty("bluetooth", CURRENT_GD_FLAG, ((Boolean) obj).booleanValue() ? "true" : "false", false);
        return true;
    }

    public void updateState(Preference preference) {
        ((SwitchPreference) this.mPreference).setChecked(DeviceConfig.getBoolean("bluetooth", CURRENT_GD_FLAG, false));
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        DeviceConfig.setProperty("bluetooth", CURRENT_GD_FLAG, (String) null, false);
        ((SwitchPreference) this.mPreference).setChecked(false);
    }
}
