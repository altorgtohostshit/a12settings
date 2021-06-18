package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.util.function.Function;

public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda4 implements Function {
    public static final /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda4 INSTANCE = new SavedNetworkTracker$$ExternalSyntheticLambda4();

    private /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda4() {
    }

    public final Object apply(Object obj) {
        return new StandardWifiEntry.ScanResultKey((ScanResult) obj);
    }
}
