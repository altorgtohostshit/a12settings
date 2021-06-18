package com.android.settings.wifi.p2p;

import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;

public final /* synthetic */ class WifiP2pSettings$$ExternalSyntheticLambda1 implements WifiP2pManager.NetworkInfoListener {
    public final /* synthetic */ WifiP2pSettings f$0;

    public /* synthetic */ WifiP2pSettings$$ExternalSyntheticLambda1(WifiP2pSettings wifiP2pSettings) {
        this.f$0 = wifiP2pSettings;
    }

    public final void onNetworkInfoAvailable(NetworkInfo networkInfo) {
        this.f$0.lambda$onResume$1(networkInfo);
    }
}
