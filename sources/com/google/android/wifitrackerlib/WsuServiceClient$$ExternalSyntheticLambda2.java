package com.google.android.wifitrackerlib;

import com.android.wsuinterface.NetworkGroupSubscription;
import com.google.android.wifitrackerlib.WsuManager;

public final /* synthetic */ class WsuServiceClient$$ExternalSyntheticLambda2 implements WsuManager.WsuSignupAction {
    public final /* synthetic */ WsuServiceClient f$0;
    public final /* synthetic */ NetworkGroupSubscription f$1;

    public /* synthetic */ WsuServiceClient$$ExternalSyntheticLambda2(WsuServiceClient wsuServiceClient, NetworkGroupSubscription networkGroupSubscription) {
        this.f$0 = wsuServiceClient;
        this.f$1 = networkGroupSubscription;
    }

    public final void onExecute() {
        this.f$0.lambda$createSignupAction$2(this.f$1);
    }
}
