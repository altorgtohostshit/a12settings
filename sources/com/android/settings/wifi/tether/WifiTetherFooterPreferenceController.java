package com.android.settings.wifi.tether;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.wifi.tether.WifiTetherBasePreferenceController;

public class WifiTetherFooterPreferenceController extends WifiTetherBasePreferenceController {
    public String getPreferenceKey() {
        return "tether_prefs_footer_2";
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        return true;
    }

    public WifiTetherFooterPreferenceController(Context context) {
        super(context, (WifiTetherBasePreferenceController.OnTetherConfigUpdateListener) null);
    }

    public void updateDisplay() {
        if (this.mWifiManager.isStaApConcurrencySupported()) {
            this.mPreference.setTitle((int) R.string.tethering_footer_info_sta_ap_concurrency);
        } else {
            this.mPreference.setTitle((int) R.string.tethering_footer_info);
        }
    }
}
