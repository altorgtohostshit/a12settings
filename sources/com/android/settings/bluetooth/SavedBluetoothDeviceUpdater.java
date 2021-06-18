package com.android.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.connecteddevice.PreviouslyConnectedDeviceDashboardFragment;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.CachedBluetoothDeviceManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SavedBluetoothDeviceUpdater extends BluetoothDeviceUpdater implements Preference.OnPreferenceClickListener {
    private static final boolean DBG = Log.isLoggable("SavedBluetoothDeviceUpdater", 3);
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final boolean mDisplayConnected;

    /* access modifiers changed from: protected */
    public String getPreferenceKey() {
        return "saved_bt";
    }

    public SavedBluetoothDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        super(context, dashboardFragment, devicePreferenceCallback);
        this.mDisplayConnected = dashboardFragment instanceof PreviouslyConnectedDeviceDashboardFragment;
    }

    public void forceUpdate() {
        if (this.mBluetoothAdapter.isEnabled()) {
            CachedBluetoothDeviceManager cachedDeviceManager = this.mLocalManager.getCachedDeviceManager();
            List<BluetoothDevice> mostRecentlyConnectedDevices = this.mBluetoothAdapter.getMostRecentlyConnectedDevices();
            removePreferenceIfNecessary(mostRecentlyConnectedDevices, cachedDeviceManager);
            for (BluetoothDevice findDevice : mostRecentlyConnectedDevices) {
                CachedBluetoothDevice findDevice2 = cachedDeviceManager.findDevice(findDevice);
                if (findDevice2 != null) {
                    update(findDevice2);
                }
            }
            return;
        }
        removeAllDevicesFromPreference();
    }

    private void removePreferenceIfNecessary(List<BluetoothDevice> list, CachedBluetoothDeviceManager cachedBluetoothDeviceManager) {
        CachedBluetoothDevice findDevice;
        Iterator it = new ArrayList(this.mPreferenceMap.keySet()).iterator();
        while (it.hasNext()) {
            BluetoothDevice bluetoothDevice = (BluetoothDevice) it.next();
            if (!list.contains(bluetoothDevice) && (findDevice = cachedBluetoothDeviceManager.findDevice(bluetoothDevice)) != null) {
                removePreference(findDevice);
            }
        }
    }

    public void update(CachedBluetoothDevice cachedBluetoothDevice) {
        if (isFilterMatched(cachedBluetoothDevice)) {
            addPreference(cachedBluetoothDevice, 3);
        } else {
            removePreference(cachedBluetoothDevice);
        }
    }

    public boolean isFilterMatched(CachedBluetoothDevice cachedBluetoothDevice) {
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        if (DBG) {
            Log.d("SavedBluetoothDeviceUpdater", "isFilterMatched() device name : " + cachedBluetoothDevice.getName() + ", is connected : " + device.isConnected() + ", is profile connected : " + cachedBluetoothDevice.isConnected());
        }
        return device.getBondState() == 12 && (this.mDisplayConnected || !device.isConnected());
    }

    public boolean onPreferenceClick(Preference preference) {
        this.mMetricsFeatureProvider.logClickedPreference(preference, this.mFragment.getMetricsCategory());
        CachedBluetoothDevice bluetoothDevice = ((BluetoothDevicePreference) preference).getBluetoothDevice();
        if (bluetoothDevice.isConnected()) {
            return bluetoothDevice.setActive();
        }
        bluetoothDevice.connect();
        return true;
    }
}
