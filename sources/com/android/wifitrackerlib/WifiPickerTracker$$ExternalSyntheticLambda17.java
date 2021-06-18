package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.Predicate;

public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda17 implements Predicate {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda17 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda17();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda17() {
    }

    public final boolean test(Object obj) {
        return WifiPickerTracker.lambda$updateSuggestedWifiEntryScans$12((ScanResult) obj);
    }
}
