package com.android.settings.network;

import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

public final /* synthetic */ class NetworkProviderSettings$$ExternalSyntheticLambda7 implements Predicate {
    public static final /* synthetic */ NetworkProviderSettings$$ExternalSyntheticLambda7 INSTANCE = new NetworkProviderSettings$$ExternalSyntheticLambda7();

    private /* synthetic */ NetworkProviderSettings$$ExternalSyntheticLambda7() {
    }

    public final boolean test(Object obj) {
        return NetworkProviderSettings.lambda$onWifiEntriesChanged$4((WifiEntry) obj);
    }
}
