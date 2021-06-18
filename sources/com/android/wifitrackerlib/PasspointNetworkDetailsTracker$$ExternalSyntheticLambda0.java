package com.android.wifitrackerlib;

import android.net.wifi.hotspot2.PasspointConfiguration;
import java.util.function.Consumer;

public final /* synthetic */ class PasspointNetworkDetailsTracker$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ PasspointNetworkDetailsTracker f$0;

    public /* synthetic */ PasspointNetworkDetailsTracker$$ExternalSyntheticLambda0(PasspointNetworkDetailsTracker passpointNetworkDetailsTracker) {
        this.f$0 = passpointNetworkDetailsTracker;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$conditionallyUpdateConfig$3((PasspointConfiguration) obj);
    }
}
