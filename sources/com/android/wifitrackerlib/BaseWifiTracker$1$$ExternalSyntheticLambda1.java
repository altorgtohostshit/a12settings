package com.android.wifitrackerlib;

import android.net.NetworkKey;
import java.util.Set;
import java.util.function.Predicate;

public final /* synthetic */ class BaseWifiTracker$1$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ Set f$0;

    public /* synthetic */ BaseWifiTracker$1$$ExternalSyntheticLambda1(Set set) {
        this.f$0 = set;
    }

    public final boolean test(Object obj) {
        return this.f$0.add((NetworkKey) obj);
    }
}
