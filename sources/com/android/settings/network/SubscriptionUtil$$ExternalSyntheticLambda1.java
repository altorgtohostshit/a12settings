package com.android.settings.network;

import android.content.Context;
import com.android.settings.network.SubscriptionUtil;
import java.util.Set;
import java.util.function.Function;

public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda1 implements Function {
    public final /* synthetic */ Set f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda1(Set set, Context context) {
        this.f$0 = set;
        this.f$1 = context;
    }

    public final Object apply(Object obj) {
        return SubscriptionUtil.lambda$getUniqueSubscriptionDisplayNames$5(this.f$0, this.f$1, (SubscriptionUtil.AnonymousClass1DisplayInfo) obj);
    }
}
