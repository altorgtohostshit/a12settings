package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import androidx.preference.Preference;

public final /* synthetic */ class MobileNetworkListController$$ExternalSyntheticLambda0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ MobileNetworkListController f$0;
    public final /* synthetic */ SubscriptionInfo f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ MobileNetworkListController$$ExternalSyntheticLambda0(MobileNetworkListController mobileNetworkListController, SubscriptionInfo subscriptionInfo, int i) {
        this.f$0 = mobileNetworkListController;
        this.f$1 = subscriptionInfo;
        this.f$2 = i;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$update$0(this.f$1, this.f$2, preference);
    }
}
