package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import com.android.settings.R;
import com.android.settings.password.PasswordUtils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.List;

public final class DevicePickerFragment extends DeviceListPreferenceFragment {
    BluetoothProgressCategory mAvailableDevicesCategory;
    String mCallingAppPackageName;
    Context mContext;
    String mLaunchClass;
    String mLaunchPackage;
    private boolean mNeedAuth;
    private boolean mScanAllowed;

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return null;
    }

    public String getDeviceListKey() {
        return "bt_device_list";
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "DevicePickerFragment";
    }

    public int getMetricsCategory() {
        return 25;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.device_picker;
    }

    public DevicePickerFragment() {
        super((String) null);
    }

    /* access modifiers changed from: package-private */
    public void initPreferencesFromPreferenceScreen() {
        Intent intent = getActivity().getIntent();
        this.mNeedAuth = intent.getBooleanExtra("android.bluetooth.devicepicker.extra.NEED_AUTH", false);
        setFilter(intent.getIntExtra("android.bluetooth.devicepicker.extra.FILTER_TYPE", 0));
        this.mLaunchPackage = intent.getStringExtra("android.bluetooth.devicepicker.extra.LAUNCH_PACKAGE");
        this.mLaunchClass = intent.getStringExtra("android.bluetooth.devicepicker.extra.DEVICE_PICKER_LAUNCH_CLASS");
        this.mAvailableDevicesCategory = (BluetoothProgressCategory) findPreference("bt_device_list");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getActivity().setTitle(getString(R.string.device_picker));
        this.mScanAllowed = !((UserManager) getSystemService("user")).hasUserRestriction("no_config_bluetooth");
        String callingAppPackageName = PasswordUtils.getCallingAppPackageName(getActivity().getActivityToken());
        this.mCallingAppPackageName = callingAppPackageName;
        if (!TextUtils.equals(callingAppPackageName, this.mLaunchPackage)) {
            Log.w("DevicePickerFragment", "sendDevicePickedIntent() launch package name is not equivalent to calling package name!");
        }
        this.mContext = getContext();
        setHasOptionsMenu(true);
    }

    public void onStart() {
        super.onStart();
        addCachedDevices();
        this.mSelectedDevice = null;
        if (this.mScanAllowed) {
            enableScanning();
            this.mAvailableDevicesCategory.setProgress(this.mBluetoothAdapter.isDiscovering());
        }
    }

    public void onStop() {
        disableScanning();
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mSelectedDevice == null) {
            sendDevicePickedIntent((BluetoothDevice) null);
        }
    }

    /* access modifiers changed from: package-private */
    public void onDevicePreferenceClick(BluetoothDevicePreference bluetoothDevicePreference) {
        disableScanning();
        LocalBluetoothPreferences.persistSelectedDeviceInPicker(getActivity(), this.mSelectedDevice.getAddress());
        if (bluetoothDevicePreference.getCachedDevice().getBondState() == 12 || !this.mNeedAuth) {
            sendDevicePickedIntent(this.mSelectedDevice);
            finish();
            return;
        }
        super.onDevicePreferenceClick(bluetoothDevicePreference);
    }

    public void onScanningStateChanged(boolean z) {
        super.onScanningStateChanged(z);
        this.mAvailableDevicesCategory.setProgress(z | this.mScanEnabled);
    }

    public void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        if (device.equals(this.mSelectedDevice)) {
            if (i == 12) {
                sendDevicePickedIntent(device);
                finish();
            } else if (i == 10) {
                enableScanning();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void initDevicePreference(BluetoothDevicePreference bluetoothDevicePreference) {
        super.initDevicePreference(bluetoothDevicePreference);
        bluetoothDevicePreference.setNeedNotifyHierarchyChanged(true);
    }

    public void onBluetoothStateChanged(int i) {
        super.onBluetoothStateChanged(i);
        if (i == 12) {
            enableScanning();
        }
    }

    private void sendDevicePickedIntent(BluetoothDevice bluetoothDevice) {
        Intent intent = new Intent("android.bluetooth.devicepicker.action.DEVICE_SELECTED");
        intent.putExtra("android.bluetooth.device.extra.DEVICE", bluetoothDevice);
        String str = this.mLaunchPackage;
        if (!(str == null || this.mLaunchClass == null || !TextUtils.equals(this.mCallingAppPackageName, str))) {
            intent.setClassName(this.mLaunchPackage, this.mLaunchClass);
        }
        this.mContext.sendBroadcast(intent, "android.permission.BLUETOOTH_ADMIN");
    }
}
