package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

public final /* synthetic */ class SubscriptionsPreferenceController$$ExternalSyntheticLambda3 implements Predicate {
    public final /* synthetic */ SubscriptionsPreferenceController f$0;

    public /* synthetic */ SubscriptionsPreferenceController$$ExternalSyntheticLambda3(SubscriptionsPreferenceController subscriptionsPreferenceController) {
        this.f$0 = subscriptionsPreferenceController;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$isAvailable$3((SubscriptionInfo) obj);
    }
}
