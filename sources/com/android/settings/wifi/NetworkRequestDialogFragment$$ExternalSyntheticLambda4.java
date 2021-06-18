package com.android.settings.wifi;

import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

public final /* synthetic */ class NetworkRequestDialogFragment$$ExternalSyntheticLambda4 implements Predicate {
    public final /* synthetic */ NetworkRequestDialogFragment f$0;

    public /* synthetic */ NetworkRequestDialogFragment$$ExternalSyntheticLambda4(NetworkRequestDialogFragment networkRequestDialogFragment) {
        this.f$0 = networkRequestDialogFragment;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$updateWifiEntries$4((WifiEntry) obj);
    }
}
