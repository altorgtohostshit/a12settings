package com.android.wifitrackerlib;

import com.android.wifitrackerlib.BaseWifiTracker;

public final /* synthetic */ class BaseWifiTracker$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ BaseWifiTracker.Scanner f$0;

    public /* synthetic */ BaseWifiTracker$$ExternalSyntheticLambda1(BaseWifiTracker.Scanner scanner) {
        this.f$0 = scanner;
    }

    public final void run() {
        this.f$0.stop();
    }
}
