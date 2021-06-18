package com.android.wifitrackerlib;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ Map f$0;

    public /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda2(Map map) {
        this.f$0 = map;
    }

    public final void accept(Object obj) {
        ((StandardWifiEntry) obj).updateScanResultInfo((List) this.f$0.get(((StandardWifiEntry) obj).getStandardWifiEntryKey().getScanResultKey()));
    }
}
