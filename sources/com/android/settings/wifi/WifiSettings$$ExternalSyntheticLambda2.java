package com.android.settings.wifi;

import com.android.settings.wifi.ConnectedWifiEntryPreference;

public final /* synthetic */ class WifiSettings$$ExternalSyntheticLambda2 implements ConnectedWifiEntryPreference.OnGearClickListener {
    public final /* synthetic */ WifiSettings f$0;
    public final /* synthetic */ ConnectedWifiEntryPreference f$1;

    public /* synthetic */ WifiSettings$$ExternalSyntheticLambda2(WifiSettings wifiSettings, ConnectedWifiEntryPreference connectedWifiEntryPreference) {
        this.f$0 = wifiSettings;
        this.f$1 = connectedWifiEntryPreference;
    }

    public final void onGearClick(ConnectedWifiEntryPreference connectedWifiEntryPreference) {
        this.f$0.lambda$updateWifiEntryPreferences$7(this.f$1, connectedWifiEntryPreference);
    }
}
