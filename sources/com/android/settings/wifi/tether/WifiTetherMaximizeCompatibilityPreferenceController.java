package com.android.settings.wifi.tether;

import android.content.Context;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.wifi.tether.WifiTetherBasePreferenceController;

public class WifiTetherMaximizeCompatibilityPreferenceController extends WifiTetherBasePreferenceController {
    private boolean mIsChecked = isMaximizeCompatibilityEnabled();

    public String getPreferenceKey() {
        return "wifi_tether_maximize_compatibility";
    }

    public WifiTetherMaximizeCompatibilityPreferenceController(Context context, WifiTetherBasePreferenceController.OnTetherConfigUpdateListener onTetherConfigUpdateListener) {
        super(context, onTetherConfigUpdateListener);
    }

    public void updateDisplay() {
        Preference preference = this.mPreference;
        if (preference != null) {
            preference.setEnabled(is5GhzBandSupported());
            ((SwitchPreference) this.mPreference).setChecked(this.mIsChecked);
            this.mPreference.setSummary(this.mWifiManager.isBridgedApConcurrencySupported() ? R.string.wifi_hotspot_maximize_compatibility_dual_ap_summary : R.string.wifi_hotspot_maximize_compatibility_single_ap_summary);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        this.mIsChecked = ((Boolean) obj).booleanValue();
        WifiTetherBasePreferenceController.OnTetherConfigUpdateListener onTetherConfigUpdateListener = this.mListener;
        if (onTetherConfigUpdateListener == null) {
            return true;
        }
        onTetherConfigUpdateListener.onTetherConfigUpdated(this);
        return true;
    }

    private boolean is5GhzBandSupported() {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager == null || !wifiManager.is5GHzBandSupported() || this.mWifiManager.getCountryCode() == null) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isMaximizeCompatibilityEnabled() {
        SoftApConfiguration softApConfiguration;
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager == null || (softApConfiguration = wifiManager.getSoftApConfiguration()) == null) {
            return false;
        }
        if (this.mWifiManager.isBridgedApConcurrencySupported()) {
            boolean isBridgedModeOpportunisticShutdownEnabled = softApConfiguration.isBridgedModeOpportunisticShutdownEnabled();
            Log.d("WifiTetherMaximizeCompatibilityPref", "isBridgedModeOpportunisticShutdownEnabled:" + isBridgedModeOpportunisticShutdownEnabled);
            return !isBridgedModeOpportunisticShutdownEnabled;
        }
        int band = softApConfiguration.getBand();
        Log.d("WifiTetherMaximizeCompatibilityPref", "getBand:" + band);
        if (band == 1) {
            return true;
        }
        return false;
    }

    public void setupMaximizeCompatibility(SoftApConfiguration.Builder builder) {
        if (builder != null) {
            boolean z = this.mIsChecked;
            int i = 1;
            if (this.mWifiManager.isBridgedApConcurrencySupported()) {
                builder.setBands(new int[]{1, 3});
                Log.d("WifiTetherMaximizeCompatibilityPref", "setBridgedModeOpportunisticShutdownEnabled:" + z);
                builder.setBridgedModeOpportunisticShutdownEnabled(z ^ true);
                return;
            }
            if (!z) {
                i = 3;
            }
            Log.d("WifiTetherMaximizeCompatibilityPref", "setBand:" + i);
            builder.setBand(i);
        }
    }
}
