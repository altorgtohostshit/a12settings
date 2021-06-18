package com.android.wifitrackerlib;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda15 implements Predicate {
    public final /* synthetic */ Set f$0;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda15(Set set) {
        this.f$0 = set;
    }

    public final boolean test(Object obj) {
        return WifiPickerTracker.lambda$updatePasspointWifiEntryScans$15(this.f$0, (Map.Entry) obj);
    }
}
