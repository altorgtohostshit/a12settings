package com.google.android.wifitrackerlib;

import android.net.wifi.WifiManager;
import java.util.concurrent.Executor;

public final /* synthetic */ class WsuPostProvisioningReceiver$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WsuPostProvisioningReceiver f$0;
    public final /* synthetic */ Executor f$1;
    public final /* synthetic */ WifiManager.ScanResultsCallback f$2;

    public /* synthetic */ WsuPostProvisioningReceiver$$ExternalSyntheticLambda0(WsuPostProvisioningReceiver wsuPostProvisioningReceiver, Executor executor, WifiManager.ScanResultsCallback scanResultsCallback) {
        this.f$0 = wsuPostProvisioningReceiver;
        this.f$1 = executor;
        this.f$2 = scanResultsCallback;
    }

    public final void run() {
        this.f$0.lambda$onReceive$0(this.f$1, this.f$2);
    }
}
