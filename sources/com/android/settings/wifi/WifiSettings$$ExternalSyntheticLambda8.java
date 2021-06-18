package com.android.settings.wifi;

import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

public final /* synthetic */ class WifiSettings$$ExternalSyntheticLambda8 implements Predicate {
    public static final /* synthetic */ WifiSettings$$ExternalSyntheticLambda8 INSTANCE = new WifiSettings$$ExternalSyntheticLambda8();

    private /* synthetic */ WifiSettings$$ExternalSyntheticLambda8() {
    }

    public final boolean test(Object obj) {
        return WifiSettings.lambda$onWifiEntriesChanged$4((WifiEntry) obj);
    }
}
