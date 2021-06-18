package com.android.settings.wifi;

public final /* synthetic */ class WifiSummaryUpdater$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WifiSummaryUpdater f$0;

    public /* synthetic */ WifiSummaryUpdater$$ExternalSyntheticLambda0(WifiSummaryUpdater wifiSummaryUpdater) {
        this.f$0 = wifiSummaryUpdater;
    }

    public final void run() {
        this.f$0.notifyChangeIfNeeded();
    }
}
