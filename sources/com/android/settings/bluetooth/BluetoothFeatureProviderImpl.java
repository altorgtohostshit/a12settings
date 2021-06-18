package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.Uri;

public class BluetoothFeatureProviderImpl implements BluetoothFeatureProvider {
    private Context mContext;

    public BluetoothFeatureProviderImpl(Context context) {
        this.mContext = context;
    }

    public Uri getBluetoothDeviceSettingsUri(BluetoothDevice bluetoothDevice) {
        byte[] metadata = bluetoothDevice.getMetadata(16);
        if (metadata == null) {
            return null;
        }
        return Uri.parse(new String(metadata));
    }
}
