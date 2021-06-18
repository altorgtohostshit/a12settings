package com.android.settings.network;

import com.android.settings.network.SubscriptionUtil;
import java.util.function.Function;

public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda4 implements Function {
    public static final /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda4 INSTANCE = new SubscriptionUtil$$ExternalSyntheticLambda4();

    private /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda4() {
    }

    public final Object apply(Object obj) {
        return Integer.valueOf(((SubscriptionUtil.AnonymousClass1DisplayInfo) obj).subscriptionInfo.getSubscriptionId());
    }
}
