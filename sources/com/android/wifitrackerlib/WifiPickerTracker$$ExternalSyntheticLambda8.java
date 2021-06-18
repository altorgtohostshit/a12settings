package com.android.wifitrackerlib;

import android.net.wifi.hotspot2.PasspointConfiguration;
import java.util.function.Function;

public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda8 implements Function {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda8 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda8();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda8() {
    }

    public final Object apply(Object obj) {
        return PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(((PasspointConfiguration) obj).getUniqueId());
    }
}
