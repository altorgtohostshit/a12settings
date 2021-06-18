package com.android.wifitrackerlib;

import java.util.Map;
import java.util.function.Predicate;

public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda7 implements Predicate {
    public final /* synthetic */ Map f$0;

    public /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda7(Map map) {
        this.f$0 = map;
    }

    public final boolean test(Object obj) {
        return SavedNetworkTracker.lambda$updatePasspointWifiEntryConfigs$4(this.f$0, (Map.Entry) obj);
    }
}
