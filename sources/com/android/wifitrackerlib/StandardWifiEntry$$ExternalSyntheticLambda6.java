package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.ToIntFunction;

public final /* synthetic */ class StandardWifiEntry$$ExternalSyntheticLambda6 implements ToIntFunction {
    public static final /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda6 INSTANCE = new StandardWifiEntry$$ExternalSyntheticLambda6();

    private /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda6() {
    }

    public final int applyAsInt(Object obj) {
        return StandardWifiEntry.lambda$getScanResultDescription$4((ScanResult) obj);
    }
}
