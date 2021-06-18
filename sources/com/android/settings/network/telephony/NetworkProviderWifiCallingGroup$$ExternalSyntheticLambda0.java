package com.android.settings.network.telephony;

import android.telephony.SubscriptionInfo;
import androidx.preference.Preference;

public final /* synthetic */ class NetworkProviderWifiCallingGroup$$ExternalSyntheticLambda0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ NetworkProviderWifiCallingGroup f$0;
    public final /* synthetic */ SubscriptionInfo f$1;

    public /* synthetic */ NetworkProviderWifiCallingGroup$$ExternalSyntheticLambda0(NetworkProviderWifiCallingGroup networkProviderWifiCallingGroup, SubscriptionInfo subscriptionInfo) {
        this.f$0 = networkProviderWifiCallingGroup;
        this.f$1 = subscriptionInfo;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$setSubscriptionInfoForPreference$0(this.f$1, preference);
    }
}
