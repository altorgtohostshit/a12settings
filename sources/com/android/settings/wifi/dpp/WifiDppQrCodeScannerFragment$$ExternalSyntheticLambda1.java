package com.android.settings.wifi.dpp;

import androidx.lifecycle.Observer;

public final /* synthetic */ class WifiDppQrCodeScannerFragment$$ExternalSyntheticLambda1 implements Observer {
    public final /* synthetic */ WifiDppQrCodeScannerFragment f$0;
    public final /* synthetic */ WifiDppInitiatorViewModel f$1;

    public /* synthetic */ WifiDppQrCodeScannerFragment$$ExternalSyntheticLambda1(WifiDppQrCodeScannerFragment wifiDppQrCodeScannerFragment, WifiDppInitiatorViewModel wifiDppInitiatorViewModel) {
        this.f$0 = wifiDppQrCodeScannerFragment;
        this.f$1 = wifiDppInitiatorViewModel;
    }

    public final void onChanged(Object obj) {
        this.f$0.lambda$onCreate$0(this.f$1, (Integer) obj);
    }
}
