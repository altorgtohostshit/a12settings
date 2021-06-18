package com.android.settingslib.wifi;

import com.android.settingslib.wifi.WifiTracker;

public final /* synthetic */ class WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ WifiTracker.WifiListenerExecutor f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda3(WifiTracker.WifiListenerExecutor wifiListenerExecutor, String str, Runnable runnable) {
        this.f$0 = wifiListenerExecutor;
        this.f$1 = str;
        this.f$2 = runnable;
    }

    public final void run() {
        this.f$0.lambda$runAndLog$1(this.f$1, this.f$2);
    }
}
