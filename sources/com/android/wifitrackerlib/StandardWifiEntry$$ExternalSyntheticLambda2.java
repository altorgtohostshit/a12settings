package com.android.wifitrackerlib;

import com.android.wifitrackerlib.WifiEntry;

public final /* synthetic */ class StandardWifiEntry$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ WifiEntry.ConnectCallback f$0;

    public /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda2(WifiEntry.ConnectCallback connectCallback) {
        this.f$0 = connectCallback;
    }

    public final void run() {
        this.f$0.onConnectResult(1);
    }
}
