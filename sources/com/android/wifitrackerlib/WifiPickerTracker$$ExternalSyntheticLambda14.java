package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.Predicate;

public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda14 implements Predicate {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda14(String str, int i) {
        this.f$0 = str;
        this.f$1 = i;
    }

    public final boolean test(Object obj) {
        return WifiPickerTracker.lambda$updateNetworkRequestEntryScans$18(this.f$0, this.f$1, (ScanResult) obj);
    }
}
