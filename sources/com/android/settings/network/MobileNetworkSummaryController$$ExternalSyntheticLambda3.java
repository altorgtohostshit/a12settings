package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import java.util.function.Function;

public final /* synthetic */ class MobileNetworkSummaryController$$ExternalSyntheticLambda3 implements Function {
    public final /* synthetic */ MobileNetworkSummaryController f$0;

    public /* synthetic */ MobileNetworkSummaryController$$ExternalSyntheticLambda3(MobileNetworkSummaryController mobileNetworkSummaryController) {
        this.f$0 = mobileNetworkSummaryController;
    }

    public final Object apply(Object obj) {
        return this.f$0.lambda$getSummaryForProviderModel$0((SubscriptionInfo) obj);
    }
}
