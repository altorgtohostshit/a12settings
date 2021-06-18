package com.android.wifitrackerlib;

import java.util.function.Function;

public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda9 implements Function {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda9 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda9();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda9() {
    }

    public final Object apply(Object obj) {
        return ((StandardWifiEntry) obj).getStandardWifiEntryKey().getScanResultKey();
    }
}
