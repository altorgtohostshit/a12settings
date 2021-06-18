package com.android.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BluetoothDeviceUpdater implements BluetoothCallback, LocalBluetoothProfileManager.ServiceListener {
    private static final boolean DBG = Log.isLoggable("BluetoothDeviceUpdater", 3);
    protected final DevicePreferenceCallback mDevicePreferenceCallback;
    final GearPreference.OnGearClickListener mDeviceProfilesListener;
    protected DashboardFragment mFragment;
    protected LocalBluetoothManager mLocalManager;
    protected final MetricsFeatureProvider mMetricsFeatureProvider;
    protected Context mPrefContext;
    protected final Map<BluetoothDevice, Preference> mPreferenceMap;

    /* access modifiers changed from: protected */
    public abstract String getPreferenceKey();

    public abstract boolean isFilterMatched(CachedBluetoothDevice cachedBluetoothDevice);

    public void onServiceDisconnected() {
    }

    public BluetoothDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        this(context, dashboardFragment, devicePreferenceCallback, Utils.getLocalBtManager(context));
    }

    BluetoothDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback, LocalBluetoothManager localBluetoothManager) {
        this.mDeviceProfilesListener = new BluetoothDeviceUpdater$$ExternalSyntheticLambda0(this);
        this.mFragment = dashboardFragment;
        this.mDevicePreferenceCallback = devicePreferenceCallback;
        this.mPreferenceMap = new HashMap();
        this.mLocalManager = localBluetoothManager;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public void registerCallback() {
        LocalBluetoothManager localBluetoothManager = this.mLocalManager;
        if (localBluetoothManager == null) {
            Log.e("BluetoothDeviceUpdater", "registerCallback() Bluetooth is not supported on this device");
            return;
        }
        localBluetoothManager.setForegroundActivity(this.mFragment.getContext());
        this.mLocalManager.getEventManager().registerCallback(this);
        this.mLocalManager.getProfileManager().addServiceListener(this);
        forceUpdate();
    }

    public void unregisterCallback() {
        LocalBluetoothManager localBluetoothManager = this.mLocalManager;
        if (localBluetoothManager == null) {
            Log.e("BluetoothDeviceUpdater", "unregisterCallback() Bluetooth is not supported on this device");
            return;
        }
        localBluetoothManager.setForegroundActivity((Context) null);
        this.mLocalManager.getEventManager().unregisterCallback(this);
        this.mLocalManager.getProfileManager().removeServiceListener(this);
    }

    public void forceUpdate() {
        if (this.mLocalManager == null) {
            Log.e("BluetoothDeviceUpdater", "forceUpdate() Bluetooth is not supported on this device");
        } else if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            for (CachedBluetoothDevice update : this.mLocalManager.getCachedDeviceManager().getCachedDevicesCopy()) {
                update(update);
            }
        } else {
            removeAllDevicesFromPreference();
        }
    }

    public void removeAllDevicesFromPreference() {
        LocalBluetoothManager localBluetoothManager = this.mLocalManager;
        if (localBluetoothManager == null) {
            Log.e("BluetoothDeviceUpdater", "removeAllDevicesFromPreference() BT is not supported on this device");
            return;
        }
        for (CachedBluetoothDevice removePreference : localBluetoothManager.getCachedDeviceManager().getCachedDevicesCopy()) {
            removePreference(removePreference);
        }
    }

    public void onBluetoothStateChanged(int i) {
        if (12 == i) {
            forceUpdate();
        } else if (10 == i) {
            removeAllDevicesFromPreference();
        }
    }

    public void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
        update(cachedBluetoothDevice);
    }

    public void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
        removePreference(cachedBluetoothDevice);
    }

    public void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        update(cachedBluetoothDevice);
    }

    public void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        if (DBG) {
            Log.d("BluetoothDeviceUpdater", "onProfileConnectionStateChanged() device: " + cachedBluetoothDevice.getName() + ", state: " + i + ", bluetoothProfile: " + i2);
        }
        update(cachedBluetoothDevice);
    }

    public void onAclConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (DBG) {
            Log.d("BluetoothDeviceUpdater", "onAclConnectionStateChanged() device: " + cachedBluetoothDevice.getName() + ", state: " + i);
        }
        update(cachedBluetoothDevice);
    }

    public void onServiceConnected() {
        forceUpdate();
    }

    public void setPrefContext(Context context) {
        this.mPrefContext = context;
    }

    /* access modifiers changed from: protected */
    public void update(CachedBluetoothDevice cachedBluetoothDevice) {
        if (isFilterMatched(cachedBluetoothDevice)) {
            addPreference(cachedBluetoothDevice);
        } else {
            removePreference(cachedBluetoothDevice);
        }
    }

    /* access modifiers changed from: protected */
    public void addPreference(CachedBluetoothDevice cachedBluetoothDevice) {
        addPreference(cachedBluetoothDevice, 1);
    }

    /* access modifiers changed from: protected */
    public void addPreference(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        if (!this.mPreferenceMap.containsKey(device)) {
            BluetoothDevicePreference bluetoothDevicePreference = new BluetoothDevicePreference(this.mPrefContext, cachedBluetoothDevice, true, i);
            bluetoothDevicePreference.setKey(getPreferenceKey());
            bluetoothDevicePreference.setOnGearClickListener(this.mDeviceProfilesListener);
            if (this instanceof Preference.OnPreferenceClickListener) {
                bluetoothDevicePreference.setOnPreferenceClickListener((Preference.OnPreferenceClickListener) this);
            }
            this.mPreferenceMap.put(device, bluetoothDevicePreference);
            this.mDevicePreferenceCallback.onDeviceAdded(bluetoothDevicePreference);
        }
    }

    /* access modifiers changed from: protected */
    public void removePreference(CachedBluetoothDevice cachedBluetoothDevice) {
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        CachedBluetoothDevice subDevice = cachedBluetoothDevice.getSubDevice();
        if (this.mPreferenceMap.containsKey(device)) {
            this.mDevicePreferenceCallback.onDeviceRemoved(this.mPreferenceMap.get(device));
            this.mPreferenceMap.remove(device);
        } else if (subDevice != null) {
            BluetoothDevice device2 = subDevice.getDevice();
            if (this.mPreferenceMap.containsKey(device2)) {
                this.mDevicePreferenceCallback.onDeviceRemoved(this.mPreferenceMap.get(device2));
                this.mPreferenceMap.remove(device2);
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: launchDeviceDetails */
    public void lambda$new$0(Preference preference) {
        this.mMetricsFeatureProvider.logClickedPreference(preference, this.mFragment.getMetricsCategory());
        CachedBluetoothDevice bluetoothDevice = ((BluetoothDevicePreference) preference).getBluetoothDevice();
        if (bluetoothDevice != null) {
            Bundle bundle = new Bundle();
            bundle.putString("device_address", bluetoothDevice.getDevice().getAddress());
            new SubSettingLauncher(this.mFragment.getContext()).setDestination(BluetoothDeviceDetailsFragment.class.getName()).setArguments(bundle).setTitleRes(R.string.device_details_title).setSourceMetricsCategory(this.mFragment.getMetricsCategory()).launch();
        }
    }

    public boolean isDeviceConnected(CachedBluetoothDevice cachedBluetoothDevice) {
        if (cachedBluetoothDevice == null) {
            return false;
        }
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        if (DBG) {
            Log.d("BluetoothDeviceUpdater", "isDeviceConnected() device name : " + cachedBluetoothDevice.getName() + ", is connected : " + device.isConnected() + " , is profile connected : " + cachedBluetoothDevice.isConnected());
        }
        if (device.getBondState() != 12 || !device.isConnected()) {
            return false;
        }
        return true;
    }

    public void refreshPreference() {
        Iterator<Preference> it = this.mPreferenceMap.values().iterator();
        while (it.hasNext()) {
            ((BluetoothDevicePreference) it.next()).onPreferenceAttributesChanged();
        }
    }
}
