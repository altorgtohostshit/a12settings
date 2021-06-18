package com.android.settings.connecteddevice.usb;

import com.android.settings.connecteddevice.usb.UsbConnectionBroadcastReceiver;

public final /* synthetic */ class UsbDefaultFragment$$ExternalSyntheticLambda0 implements UsbConnectionBroadcastReceiver.UsbConnectionListener {
    public final /* synthetic */ UsbDefaultFragment f$0;

    public /* synthetic */ UsbDefaultFragment$$ExternalSyntheticLambda0(UsbDefaultFragment usbDefaultFragment) {
        this.f$0 = usbDefaultFragment;
    }

    public final void onUsbConnectionChanged(boolean z, long j, int i, int i2) {
        this.f$0.lambda$new$0(z, j, i, i2);
    }
}
