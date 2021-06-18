package com.android.wifitrackerlib;

import com.android.wifitrackerlib.BaseWifiTracker;

public final /* synthetic */ class BaseWifiTracker$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BaseWifiTracker.BaseWifiTrackerCallback f$0;

    public /* synthetic */ BaseWifiTracker$$ExternalSyntheticLambda0(BaseWifiTracker.BaseWifiTrackerCallback baseWifiTrackerCallback) {
        this.f$0 = baseWifiTrackerCallback;
    }

    public final void run() {
        this.f$0.onWifiStateChanged();
    }
}
