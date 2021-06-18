package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.android.settings.R;
import com.android.settingslib.bluetooth.BluetoothDeviceFilter;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.widget.FooterPreference;

public class BluetoothPairingDetail extends DeviceListPreferenceFragment {
    static final String KEY_AVAIL_DEVICES = "available_devices";
    static final String KEY_FOOTER_PREF = "footer_preference";
    AlwaysDiscoverable mAlwaysDiscoverable;
    BluetoothProgressCategory mAvailableDevicesCategory;
    FooterPreference mFooterPreference;
    private boolean mInitialScanStarted;

    public String getDeviceListKey() {
        return KEY_AVAIL_DEVICES;
    }

    public int getHelpResource() {
        return R.string.help_url_bluetooth;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "BluetoothPairingDetail";
    }

    public int getMetricsCategory() {
        return 1018;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.bluetooth_pairing_detail;
    }

    public BluetoothPairingDetail() {
        super("no_config_bluetooth");
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mInitialScanStarted = false;
        this.mAlwaysDiscoverable = new AlwaysDiscoverable(getContext());
    }

    public void onStart() {
        super.onStart();
        if (this.mLocalManager == null) {
            Log.e("BluetoothPairingDetail", "Bluetooth is not supported on this device");
            return;
        }
        updateBluetooth();
        this.mAvailableDevicesCategory.setProgress(this.mBluetoothAdapter.isDiscovering());
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((BluetoothDeviceRenamePreferenceController) use(BluetoothDeviceRenamePreferenceController.class)).setFragment(this);
    }

    /* access modifiers changed from: package-private */
    public void updateBluetooth() {
        if (this.mBluetoothAdapter.isEnabled()) {
            updateContent(this.mBluetoothAdapter.getState());
        } else {
            this.mBluetoothAdapter.enable();
        }
    }

    public void onStop() {
        super.onStop();
        if (this.mLocalManager == null) {
            Log.e("BluetoothPairingDetail", "Bluetooth is not supported on this device");
            return;
        }
        this.mAlwaysDiscoverable.stop();
        disableScanning();
    }

    /* access modifiers changed from: package-private */
    public void initPreferencesFromPreferenceScreen() {
        this.mAvailableDevicesCategory = (BluetoothProgressCategory) findPreference(KEY_AVAIL_DEVICES);
        FooterPreference footerPreference = (FooterPreference) findPreference(KEY_FOOTER_PREF);
        this.mFooterPreference = footerPreference;
        footerPreference.setSelectable(false);
    }

    /* access modifiers changed from: package-private */
    public void enableScanning() {
        if (!this.mInitialScanStarted) {
            if (this.mAvailableDevicesCategory != null) {
                removeAllDevices();
            }
            this.mLocalManager.getCachedDeviceManager().clearNonBondedDevices();
            this.mInitialScanStarted = true;
        }
        super.enableScanning();
    }

    /* access modifiers changed from: package-private */
    public void onDevicePreferenceClick(BluetoothDevicePreference bluetoothDevicePreference) {
        disableScanning();
        super.onDevicePreferenceClick(bluetoothDevicePreference);
    }

    public void onScanningStateChanged(boolean z) {
        super.onScanningStateChanged(z);
        this.mAvailableDevicesCategory.setProgress(z | this.mScanEnabled);
    }

    /* access modifiers changed from: package-private */
    public void updateContent(int i) {
        if (i == 10) {
            finish();
        } else if (i == 12) {
            this.mDevicePreferenceMap.clear();
            this.mBluetoothAdapter.enable();
            addDeviceCategory(this.mAvailableDevicesCategory, R.string.bluetooth_preference_found_media_devices, BluetoothDeviceFilter.ALL_FILTER, this.mInitialScanStarted);
            updateFooterPreference(this.mFooterPreference);
            this.mAlwaysDiscoverable.start();
            enableScanning();
        }
    }

    public void onBluetoothStateChanged(int i) {
        super.onBluetoothStateChanged(i);
        updateContent(i);
        if (i == 12) {
            showBluetoothTurnedOnToast();
        }
    }

    public void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        BluetoothDevice device;
        if (i == 12) {
            finish();
        } else if (this.mSelectedDevice != null && cachedBluetoothDevice != null && (device = cachedBluetoothDevice.getDevice()) != null && this.mSelectedDevice.equals(device) && i == 10) {
            enableScanning();
        }
    }

    public void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        if (cachedBluetoothDevice != null && cachedBluetoothDevice.isConnected()) {
            BluetoothDevice device = cachedBluetoothDevice.getDevice();
            if (device != null && this.mSelectedList.contains(device)) {
                finish();
            } else if (this.mDevicePreferenceMap.containsKey(cachedBluetoothDevice)) {
                onDeviceDeleted(cachedBluetoothDevice);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void showBluetoothTurnedOnToast() {
        Toast.makeText(getContext(), R.string.connected_device_bluetooth_turned_on_toast, 0).show();
    }
}
