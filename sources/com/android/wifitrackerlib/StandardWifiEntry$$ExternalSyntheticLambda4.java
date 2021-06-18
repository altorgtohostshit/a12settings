package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.Predicate;

public final /* synthetic */ class StandardWifiEntry$$ExternalSyntheticLambda4 implements Predicate {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda4(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final boolean test(Object obj) {
        return StandardWifiEntry.lambda$getScanResultDescription$3(this.f$0, this.f$1, (ScanResult) obj);
    }
}
