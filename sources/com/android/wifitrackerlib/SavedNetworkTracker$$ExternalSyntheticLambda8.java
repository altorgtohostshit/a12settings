package com.android.wifitrackerlib;

import android.net.wifi.WifiConfiguration;
import java.util.function.Predicate;

public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda8 implements Predicate {
    public static final /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda8 INSTANCE = new SavedNetworkTracker$$ExternalSyntheticLambda8();

    private /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda8() {
    }

    public final boolean test(Object obj) {
        return SavedNetworkTracker.lambda$updateStandardWifiEntryConfigs$1((WifiConfiguration) obj);
    }
}
