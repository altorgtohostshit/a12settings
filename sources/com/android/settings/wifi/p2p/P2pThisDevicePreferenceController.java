package com.android.settings.wifi.p2p;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class P2pThisDevicePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private Preference mPreference;

    public String getPreferenceKey() {
        return "p2p_this_device";
    }

    public boolean isAvailable() {
        return true;
    }

    public P2pThisDevicePreferenceController(Context context) {
        super(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void setEnabled(boolean z) {
        Preference preference = this.mPreference;
        if (preference != null) {
            preference.setEnabled(z);
        }
    }

    public void updateDeviceName(WifiP2pDevice wifiP2pDevice) {
        if (this.mPreference != null && wifiP2pDevice != null) {
            if (TextUtils.isEmpty(wifiP2pDevice.deviceName)) {
                this.mPreference.setTitle((CharSequence) wifiP2pDevice.deviceAddress);
            } else {
                this.mPreference.setTitle((CharSequence) wifiP2pDevice.deviceName);
            }
        }
    }
}
