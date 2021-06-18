package com.android.settings.network;

import androidx.preference.Preference;
import com.android.settings.wifi.ConnectedWifiEntryPreference;
import com.android.wifitrackerlib.WifiEntry;

public final /* synthetic */ class NetworkProviderSettings$$ExternalSyntheticLambda0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ NetworkProviderSettings f$0;
    public final /* synthetic */ WifiEntry f$1;
    public final /* synthetic */ ConnectedWifiEntryPreference f$2;

    public /* synthetic */ NetworkProviderSettings$$ExternalSyntheticLambda0(NetworkProviderSettings networkProviderSettings, WifiEntry wifiEntry, ConnectedWifiEntryPreference connectedWifiEntryPreference) {
        this.f$0 = networkProviderSettings;
        this.f$1 = wifiEntry;
        this.f$2 = connectedWifiEntryPreference;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$updateWifiEntryPreferences$6(this.f$1, this.f$2, preference);
    }
}
