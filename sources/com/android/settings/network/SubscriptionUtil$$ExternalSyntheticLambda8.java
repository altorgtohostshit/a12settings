package com.android.settings.network;

import android.os.ParcelUuid;
import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda8 implements Predicate {
    public final /* synthetic */ ParcelUuid f$0;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda8(ParcelUuid parcelUuid) {
        this.f$0 = parcelUuid;
    }

    public final boolean test(Object obj) {
        return SubscriptionUtil.lambda$findAllSubscriptionsInGroup$13(this.f$0, (SubscriptionInfo) obj);
    }
}
