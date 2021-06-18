package com.android.settings.connecteddevice.usb;

import com.android.settings.connecteddevice.usb.UsbConnectionBroadcastReceiver;

public final /* synthetic */ class UsbDetailsFragment$$ExternalSyntheticLambda0 implements UsbConnectionBroadcastReceiver.UsbConnectionListener {
    public final /* synthetic */ UsbDetailsFragment f$0;

    public /* synthetic */ UsbDetailsFragment$$ExternalSyntheticLambda0(UsbDetailsFragment usbDetailsFragment) {
        this.f$0 = usbDetailsFragment;
    }

    public final void onUsbConnectionChanged(boolean z, long j, int i, int i2) {
        this.f$0.lambda$new$0(z, j, i, i2);
    }
}
