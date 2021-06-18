package com.android.settings.network;

import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

public final /* synthetic */ class NetworkProviderSettings$$ExternalSyntheticLambda6 implements Predicate {
    public final /* synthetic */ NetworkProviderSettings f$0;

    public /* synthetic */ NetworkProviderSettings$$ExternalSyntheticLambda6(NetworkProviderSettings networkProviderSettings) {
        this.f$0 = networkProviderSettings;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$onWifiEntriesChanged$3((WifiEntry) obj);
    }
}
