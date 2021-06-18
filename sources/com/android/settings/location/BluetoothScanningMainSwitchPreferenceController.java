package com.android.settings.location;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.widget.Switch;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class BluetoothScanningMainSwitchPreferenceController extends TogglePreferenceController implements OnMainSwitchChangeListener {
    private static final String KEY_BLUETOOTH_SCANNING_SWITCH = "bluetooth_always_scanning_switch";

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

    public BluetoothScanningMainSwitchPreferenceController(Context context) {
        super(context, KEY_BLUETOOTH_SCANNING_SWITCH);
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
        return Settings.Global.getInt(this.mContext.getContentResolver(), "ble_scan_always_enabled", 0) == 1;
    }

    public boolean setChecked(boolean z) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "ble_scan_always_enabled", z ? 1 : 0);
        return true;
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        if (z != isChecked()) {
            setChecked(z);
        }
    }
}
