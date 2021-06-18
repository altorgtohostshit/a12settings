package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.ToIntFunction;

public final /* synthetic */ class StandardWifiEntry$$ExternalSyntheticLambda5 implements ToIntFunction {
    public static final /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda5 INSTANCE = new StandardWifiEntry$$ExternalSyntheticLambda5();

    private /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda5() {
    }

    public final int applyAsInt(Object obj) {
        return ((ScanResult) obj).level;
    }
}
