package com.google.android.wifitrackerlib;

import com.android.wifitrackerlib.WifiEntry;
import com.android.wsuinterface.NetworkGroupSubscription;

public final /* synthetic */ class WsuServiceClient$$ExternalSyntheticLambda0 implements WifiEntry.ManageSubscriptionAction {
    public final /* synthetic */ WsuServiceClient f$0;
    public final /* synthetic */ NetworkGroupSubscription f$1;

    public /* synthetic */ WsuServiceClient$$ExternalSyntheticLambda0(WsuServiceClient wsuServiceClient, NetworkGroupSubscription networkGroupSubscription) {
        this.f$0 = wsuServiceClient;
        this.f$1 = networkGroupSubscription;
    }

    public final void onExecute() {
        this.f$0.lambda$createManageSubscriptionAction$0(this.f$1);
    }
}
