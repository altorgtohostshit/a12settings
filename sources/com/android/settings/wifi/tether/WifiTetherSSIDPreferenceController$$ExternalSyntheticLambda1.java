package com.android.settings.wifi.tether;

import android.content.Intent;

public final /* synthetic */ class WifiTetherSSIDPreferenceController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WifiTetherSSIDPreferenceController f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ WifiTetherSSIDPreferenceController$$ExternalSyntheticLambda1(WifiTetherSSIDPreferenceController wifiTetherSSIDPreferenceController, Intent intent) {
        this.f$0 = wifiTetherSSIDPreferenceController;
        this.f$1 = intent;
    }

    public final void run() {
        this.f$0.lambda$shareHotspotNetwork$1(this.f$1);
    }
}
