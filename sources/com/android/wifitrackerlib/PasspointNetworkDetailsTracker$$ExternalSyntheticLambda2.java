package com.android.wifitrackerlib;

import android.net.wifi.WifiConfiguration;
import java.util.function.Predicate;

public final /* synthetic */ class PasspointNetworkDetailsTracker$$ExternalSyntheticLambda2 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ PasspointNetworkDetailsTracker$$ExternalSyntheticLambda2(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return PasspointNetworkDetailsTracker.lambda$new$1(this.f$0, (WifiConfiguration) obj);
    }
}
