package com.android.settings.network;

import android.content.Context;
import java.util.function.Supplier;

public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda12 implements Supplier {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda12(Context context) {
        this.f$0 = context;
    }

    public final Object get() {
        return SubscriptionUtil.getAvailableSubscriptions(this.f$0).stream().filter(SubscriptionUtil$$ExternalSyntheticLambda11.INSTANCE).map(SubscriptionUtil$$ExternalSyntheticLambda2.INSTANCE);
    }
}
