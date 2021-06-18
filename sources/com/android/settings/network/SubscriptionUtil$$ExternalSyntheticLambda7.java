package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda7 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda7(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return SubscriptionUtil.lambda$getSubById$12(this.f$0, (SubscriptionInfo) obj);
    }
}
