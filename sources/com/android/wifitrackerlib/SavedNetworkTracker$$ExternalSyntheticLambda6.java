package com.android.wifitrackerlib;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda6 implements Predicate {
    public final /* synthetic */ Map f$0;

    public /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda6(Map map) {
        this.f$0 = map;
    }

    public final boolean test(Object obj) {
        return ((StandardWifiEntry) obj).updateConfig((List) this.f$0.remove(((StandardWifiEntry) obj).getStandardWifiEntryKey()));
    }
}
