package com.android.settings.development;

import java.util.Map;
import java.util.function.Predicate;

public final /* synthetic */ class WirelessDebuggingFragment$$ExternalSyntheticLambda3 implements Predicate {
    public final /* synthetic */ WirelessDebuggingFragment f$0;
    public final /* synthetic */ Map f$1;

    public /* synthetic */ WirelessDebuggingFragment$$ExternalSyntheticLambda3(WirelessDebuggingFragment wirelessDebuggingFragment, Map map) {
        this.f$0 = wirelessDebuggingFragment;
        this.f$1 = map;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$updatePairedDevicePreferences$2(this.f$1, (Map.Entry) obj);
    }
}
