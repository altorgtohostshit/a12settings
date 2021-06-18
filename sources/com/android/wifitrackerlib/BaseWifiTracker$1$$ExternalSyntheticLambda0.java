package com.android.wifitrackerlib;

import android.net.NetworkKey;
import android.net.wifi.ScanResult;
import java.util.function.Function;

public final /* synthetic */ class BaseWifiTracker$1$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ BaseWifiTracker$1$$ExternalSyntheticLambda0 INSTANCE = new BaseWifiTracker$1$$ExternalSyntheticLambda0();

    private /* synthetic */ BaseWifiTracker$1$$ExternalSyntheticLambda0() {
    }

    public final Object apply(Object obj) {
        return NetworkKey.createFromScanResult((ScanResult) obj);
    }
}
