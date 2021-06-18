package com.android.settings.network;

import com.android.settings.network.SubscriptionUtil;
import java.util.Set;
import java.util.function.Predicate;

public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda10 implements Predicate {
    public final /* synthetic */ Set f$0;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda10(Set set) {
        this.f$0 = set;
    }

    public final boolean test(Object obj) {
        return SubscriptionUtil.lambda$getUniqueSubscriptionDisplayNames$7(this.f$0, (SubscriptionUtil.AnonymousClass1DisplayInfo) obj);
    }
}
