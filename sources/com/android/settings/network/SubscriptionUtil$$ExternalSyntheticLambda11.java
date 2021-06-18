package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda11 implements Predicate {
    public static final /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda11 INSTANCE = new SubscriptionUtil$$ExternalSyntheticLambda11();

    private /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda11() {
    }

    public final boolean test(Object obj) {
        return SubscriptionUtil.lambda$getUniqueSubscriptionDisplayNames$0((SubscriptionInfo) obj);
    }
}
