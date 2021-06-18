package com.android.wifitrackerlib;

import com.android.wifitrackerlib.WifiEntry;

public final /* synthetic */ class PasspointWifiEntry$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PasspointWifiEntry f$0;
    public final /* synthetic */ WifiEntry.DisconnectCallback f$1;

    public /* synthetic */ PasspointWifiEntry$$ExternalSyntheticLambda0(PasspointWifiEntry passpointWifiEntry, WifiEntry.DisconnectCallback disconnectCallback) {
        this.f$0 = passpointWifiEntry;
        this.f$1 = disconnectCallback;
    }

    public final void run() {
        this.f$0.lambda$disconnect$0(this.f$1);
    }
}
