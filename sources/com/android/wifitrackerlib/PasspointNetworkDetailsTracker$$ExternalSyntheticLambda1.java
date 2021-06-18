package com.android.wifitrackerlib;

import android.net.wifi.hotspot2.PasspointConfiguration;
import java.util.function.Predicate;

public final /* synthetic */ class PasspointNetworkDetailsTracker$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ PasspointNetworkDetailsTracker f$0;

    public /* synthetic */ PasspointNetworkDetailsTracker$$ExternalSyntheticLambda1(PasspointNetworkDetailsTracker passpointNetworkDetailsTracker) {
        this.f$0 = passpointNetworkDetailsTracker;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$conditionallyUpdateConfig$2((PasspointConfiguration) obj);
    }
}
