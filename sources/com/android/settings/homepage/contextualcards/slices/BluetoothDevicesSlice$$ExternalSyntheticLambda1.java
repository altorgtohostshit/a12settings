package com.android.settings.homepage.contextualcards.slices;

import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import java.util.function.Predicate;

public final /* synthetic */ class BluetoothDevicesSlice$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ BluetoothDevicesSlice$$ExternalSyntheticLambda1 INSTANCE = new BluetoothDevicesSlice$$ExternalSyntheticLambda1();

    private /* synthetic */ BluetoothDevicesSlice$$ExternalSyntheticLambda1() {
    }

    public final boolean test(Object obj) {
        return BluetoothDevicesSlice.lambda$getPairedBluetoothDevices$1((CachedBluetoothDevice) obj);
    }
}
