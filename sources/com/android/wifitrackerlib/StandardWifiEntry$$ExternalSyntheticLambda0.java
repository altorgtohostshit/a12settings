package com.android.wifitrackerlib;

import com.android.wifitrackerlib.WifiEntry;

public final /* synthetic */ class StandardWifiEntry$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ StandardWifiEntry f$0;
    public final /* synthetic */ WifiEntry.DisconnectCallback f$1;

    public /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda0(StandardWifiEntry standardWifiEntry, WifiEntry.DisconnectCallback disconnectCallback) {
        this.f$0 = standardWifiEntry;
        this.f$1 = disconnectCallback;
    }

    public final void run() {
        this.f$0.lambda$disconnect$2(this.f$1);
    }
}
