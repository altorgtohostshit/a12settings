package com.android.settings.wifi;

import androidx.preference.Preference;
import com.android.wifitrackerlib.WifiEntry;

public final /* synthetic */ class WifiConnectionPreferenceController$$ExternalSyntheticLambda0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ WifiConnectionPreferenceController f$0;
    public final /* synthetic */ WifiEntry f$1;

    public /* synthetic */ WifiConnectionPreferenceController$$ExternalSyntheticLambda0(WifiConnectionPreferenceController wifiConnectionPreferenceController, WifiEntry wifiEntry) {
        this.f$0 = wifiConnectionPreferenceController;
        this.f$1 = wifiEntry;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$updatePreference$0(this.f$1, preference);
    }
}
