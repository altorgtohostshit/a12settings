package com.android.wifitrackerlib;

import android.net.wifi.WifiConfiguration;
import java.util.function.Predicate;

public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda18 implements Predicate {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda18 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda18();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda18() {
    }

    public final boolean test(Object obj) {
        return WifiPickerTracker.lambda$updateWifiConfigurations$19((WifiConfiguration) obj);
    }
}
