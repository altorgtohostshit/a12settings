package com.android.settings.network;

import com.android.settingslib.wifi.WifiEntryPreference;
import com.android.wifitrackerlib.WifiEntry;

public final /* synthetic */ class NetworkProviderSettings$$ExternalSyntheticLambda2 implements WifiEntryPreference.OnButtonClickListener {
    public final /* synthetic */ NetworkProviderSettings f$0;
    public final /* synthetic */ WifiEntry f$1;

    public /* synthetic */ NetworkProviderSettings$$ExternalSyntheticLambda2(NetworkProviderSettings networkProviderSettings, WifiEntry wifiEntry) {
        this.f$0 = networkProviderSettings;
        this.f$1 = wifiEntry;
    }

    public final void onButtonClick(WifiEntryPreference wifiEntryPreference) {
        this.f$0.lambda$updateWifiEntryPreferences$8(this.f$1, wifiEntryPreference);
    }
}
