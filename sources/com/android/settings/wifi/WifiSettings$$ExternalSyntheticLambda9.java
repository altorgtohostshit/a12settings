package com.android.settings.wifi;

import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

public final /* synthetic */ class WifiSettings$$ExternalSyntheticLambda9 implements Predicate {
    public static final /* synthetic */ WifiSettings$$ExternalSyntheticLambda9 INSTANCE = new WifiSettings$$ExternalSyntheticLambda9();

    private /* synthetic */ WifiSettings$$ExternalSyntheticLambda9() {
    }

    public final boolean test(Object obj) {
        return WifiSettings.lambda$onWifiEntriesChanged$5((WifiEntry) obj);
    }
}
