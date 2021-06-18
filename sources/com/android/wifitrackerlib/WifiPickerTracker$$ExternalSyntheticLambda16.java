package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.Predicate;

public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda16 implements Predicate {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda16 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda16();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda16() {
    }

    public final boolean test(Object obj) {
        return WifiPickerTracker.lambda$updateStandardWifiEntryScans$9((ScanResult) obj);
    }
}
