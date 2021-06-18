package com.android.settings.bluetooth;

import android.widget.CompoundButton;

public final /* synthetic */ class BluetoothPairingDialogFragment$$ExternalSyntheticLambda1 implements CompoundButton.OnCheckedChangeListener {
    public final /* synthetic */ BluetoothPairingDialogFragment f$0;

    public /* synthetic */ BluetoothPairingDialogFragment$$ExternalSyntheticLambda1(BluetoothPairingDialogFragment bluetoothPairingDialogFragment) {
        this.f$0 = bluetoothPairingDialogFragment;
    }

    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        this.f$0.lambda$createPinEntryView$1(compoundButton, z);
    }
}
