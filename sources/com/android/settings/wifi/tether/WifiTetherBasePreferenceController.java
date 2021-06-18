package com.android.settings.wifi.tether;

import android.content.Context;
import android.net.TetheringManager;
import android.net.wifi.WifiManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public abstract class WifiTetherBasePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    protected final OnTetherConfigUpdateListener mListener;
    protected Preference mPreference;
    protected final TetheringManager mTm;
    protected final WifiManager mWifiManager;
    protected final String[] mWifiRegexs;

    public interface OnTetherConfigUpdateListener {
        void onTetherConfigUpdated(AbstractPreferenceController abstractPreferenceController);
    }

    public abstract void updateDisplay();

    public WifiTetherBasePreferenceController(Context context, OnTetherConfigUpdateListener onTetherConfigUpdateListener) {
        super(context);
        this.mListener = onTetherConfigUpdateListener;
        this.mWifiManager = (WifiManager) context.getSystemService(WifiManager.class);
        TetheringManager tetheringManager = (TetheringManager) context.getSystemService(TetheringManager.class);
        this.mTm = tetheringManager;
        this.mWifiRegexs = tetheringManager.getTetherableWifiRegexs();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r1 = r1.mWifiRegexs;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isAvailable() {
        /*
            r1 = this;
            android.net.wifi.WifiManager r0 = r1.mWifiManager
            if (r0 == 0) goto L_0x000d
            java.lang.String[] r1 = r1.mWifiRegexs
            if (r1 == 0) goto L_0x000d
            int r1 = r1.length
            if (r1 <= 0) goto L_0x000d
            r1 = 1
            goto L_0x000e
        L_0x000d:
            r1 = 0
        L_0x000e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.wifi.tether.WifiTetherBasePreferenceController.isAvailable():boolean");
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
        updateDisplay();
    }
}
