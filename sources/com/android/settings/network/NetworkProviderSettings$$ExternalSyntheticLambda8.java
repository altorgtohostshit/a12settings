package com.android.settings.network;

import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

public final /* synthetic */ class NetworkProviderSettings$$ExternalSyntheticLambda8 implements Predicate {
    public static final /* synthetic */ NetworkProviderSettings$$ExternalSyntheticLambda8 INSTANCE = new NetworkProviderSettings$$ExternalSyntheticLambda8();

    private /* synthetic */ NetworkProviderSettings$$ExternalSyntheticLambda8() {
    }

    public final boolean test(Object obj) {
        return NetworkProviderSettings.lambda$onWifiEntriesChanged$5((WifiEntry) obj);
    }
}
