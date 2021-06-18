package com.android.settingslib.wifi;

import android.net.wifi.WifiConfiguration;
import java.util.function.Predicate;

public final /* synthetic */ class WifiTracker$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ AccessPoint f$0;

    public /* synthetic */ WifiTracker$$ExternalSyntheticLambda0(AccessPoint accessPoint) {
        this.f$0 = accessPoint;
    }

    public final boolean test(Object obj) {
        return this.f$0.matches((WifiConfiguration) obj);
    }
}
