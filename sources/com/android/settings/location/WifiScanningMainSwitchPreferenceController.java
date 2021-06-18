package com.android.settings.location;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.widget.Switch;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class WifiScanningMainSwitchPreferenceController extends TogglePreferenceController implements OnMainSwitchChangeListener {
    private static final String KEY_WIFI_SCANNING_SWITCH = "wifi_always_scanning_switch";
    private final WifiManager mWifiManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public WifiScanningMainSwitchPreferenceController(Context context) {
        super(context, KEY_WIFI_SCANNING_SWITCH);
        this.mWifiManager = (WifiManager) context.getSystemService(WifiManager.class);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        mainSwitchPreference.addOnSwitchChangeListener(this);
        mainSwitchPreference.updateStatus(isChecked());
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_location_scanning) ? 0 : 3;
    }

    public boolean isChecked() {
        return this.mWifiManager.isScanAlwaysAvailable();
    }

    public boolean setChecked(boolean z) {
        this.mWifiManager.setScanAlwaysAvailable(z);
        return true;
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        if (z != isChecked()) {
            setChecked(z);
        }
    }
}
