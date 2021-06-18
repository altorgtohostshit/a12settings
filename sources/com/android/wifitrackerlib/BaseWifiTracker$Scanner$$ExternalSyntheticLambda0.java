package com.android.wifitrackerlib;

import com.android.wifitrackerlib.BaseWifiTracker;

public final /* synthetic */ class BaseWifiTracker$Scanner$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BaseWifiTracker.Scanner f$0;

    public /* synthetic */ BaseWifiTracker$Scanner$$ExternalSyntheticLambda0(BaseWifiTracker.Scanner scanner) {
        this.f$0 = scanner;
    }

    public final void run() {
        this.f$0.postScan();
    }
}
