package com.android.wifitrackerlib;

import com.android.wifitrackerlib.WifiEntry;

public final /* synthetic */ class StandardWifiEntry$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WifiEntry.ConnectCallback f$0;

    public /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda1(WifiEntry.ConnectCallback connectCallback) {
        this.f$0 = connectCallback;
    }

    public final void run() {
        this.f$0.onConnectResult(3);
    }
}
