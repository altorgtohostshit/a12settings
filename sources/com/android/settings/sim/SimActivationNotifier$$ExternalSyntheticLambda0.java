package com.android.settings.sim;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

public final /* synthetic */ class SimActivationNotifier$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ SimActivationNotifier$$ExternalSyntheticLambda0 INSTANCE = new SimActivationNotifier$$ExternalSyntheticLambda0();

    private /* synthetic */ SimActivationNotifier$$ExternalSyntheticLambda0() {
    }

    public final boolean test(Object obj) {
        return SimActivationNotifier.lambda$getActiveRemovableSub$0((SubscriptionInfo) obj);
    }
}
