package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import com.android.settings.widget.GearPreference;

public final /* synthetic */ class SubscriptionsPreferenceController$$ExternalSyntheticLambda2 implements GearPreference.OnGearClickListener {
    public final /* synthetic */ SubscriptionsPreferenceController f$0;
    public final /* synthetic */ SubscriptionInfo f$1;

    public /* synthetic */ SubscriptionsPreferenceController$$ExternalSyntheticLambda2(SubscriptionsPreferenceController subscriptionsPreferenceController, SubscriptionInfo subscriptionInfo) {
        this.f$0 = subscriptionsPreferenceController;
        this.f$1 = subscriptionInfo;
    }

    public final void onGearClick(GearPreference gearPreference) {
        this.f$0.lambda$updateForProvider$1(this.f$1, gearPreference);
    }
}
