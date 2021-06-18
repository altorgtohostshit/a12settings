package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.Predicate;

public final /* synthetic */ class StandardNetworkDetailsTracker$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ StandardNetworkDetailsTracker f$0;

    public /* synthetic */ StandardNetworkDetailsTracker$$ExternalSyntheticLambda0(StandardNetworkDetailsTracker standardNetworkDetailsTracker) {
        this.f$0 = standardNetworkDetailsTracker;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$cacheNewScanResults$0((ScanResult) obj);
    }
}
