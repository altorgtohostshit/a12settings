package com.android.settingslib.wifi;

import com.android.settingslib.wifi.WifiTracker;

public final /* synthetic */ class WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WifiTracker.WifiListener f$0;

    public /* synthetic */ WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda0(WifiTracker.WifiListener wifiListener) {
        this.f$0 = wifiListener;
    }

    public final void run() {
        this.f$0.onAccessPointsChanged();
    }
}
