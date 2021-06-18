package com.android.settings.wifi.p2p;

import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;

public final /* synthetic */ class WifiP2pSettings$$ExternalSyntheticLambda0 implements WifiP2pManager.ConnectionInfoListener {
    public final /* synthetic */ WifiP2pSettings f$0;
    public final /* synthetic */ NetworkInfo f$1;

    public /* synthetic */ WifiP2pSettings$$ExternalSyntheticLambda0(WifiP2pSettings wifiP2pSettings, NetworkInfo networkInfo) {
        this.f$0 = wifiP2pSettings;
        this.f$1 = networkInfo;
    }

    public final void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
        this.f$0.lambda$onResume$0(this.f$1, wifiP2pInfo);
    }
}
