package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.ToIntFunction;

public final /* synthetic */ class Utils$$ExternalSyntheticLambda1 implements ToIntFunction {
    public static final /* synthetic */ Utils$$ExternalSyntheticLambda1 INSTANCE = new Utils$$ExternalSyntheticLambda1();

    private /* synthetic */ Utils$$ExternalSyntheticLambda1() {
    }

    public final int applyAsInt(Object obj) {
        return ((ScanResult) obj).level;
    }
}
