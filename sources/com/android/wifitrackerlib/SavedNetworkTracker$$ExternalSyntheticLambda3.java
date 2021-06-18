package com.android.wifitrackerlib;

import android.net.wifi.hotspot2.PasspointConfiguration;
import java.util.function.Function;

public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda3 implements Function {
    public static final /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda3 INSTANCE = new SavedNetworkTracker$$ExternalSyntheticLambda3();

    private /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda3() {
    }

    public final Object apply(Object obj) {
        return PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(((PasspointConfiguration) obj).getUniqueId());
    }
}
