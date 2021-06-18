package com.android.settings.location;

import android.content.Context;
import android.net.wifi.WifiManager;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class WifiScanningPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final WifiManager mWifiManager;

    public String getPreferenceKey() {
        return "wifi_always_scanning";
    }

    public boolean isAvailable() {
        return true;
    }

    public WifiScanningPreferenceController(Context context) {
        super(context);
        this.mWifiManager = (WifiManager) context.getSystemService(WifiManager.class);
    }

    public void updateState(Preference preference) {
        ((SwitchPreference) preference).setChecked(this.mWifiManager.isScanAlwaysAvailable());
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!"wifi_always_scanning".equals(preference.getKey())) {
            return false;
        }
        this.mWifiManager.setScanAlwaysAvailable(((SwitchPreference) preference).isChecked());
        return true;
    }
}
