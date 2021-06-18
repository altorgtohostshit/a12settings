package com.android.settings.connecteddevice.usb;

import com.android.settings.connecteddevice.usb.UsbConnectionBroadcastReceiver;

public final /* synthetic */ class ConnectedUsbDeviceUpdater$$ExternalSyntheticLambda1 implements UsbConnectionBroadcastReceiver.UsbConnectionListener {
    public final /* synthetic */ ConnectedUsbDeviceUpdater f$0;

    public /* synthetic */ ConnectedUsbDeviceUpdater$$ExternalSyntheticLambda1(ConnectedUsbDeviceUpdater connectedUsbDeviceUpdater) {
        this.f$0 = connectedUsbDeviceUpdater;
    }

    public final void onUsbConnectionChanged(boolean z, long j, int i, int i2) {
        this.f$0.lambda$new$0(z, j, i, i2);
    }
}
