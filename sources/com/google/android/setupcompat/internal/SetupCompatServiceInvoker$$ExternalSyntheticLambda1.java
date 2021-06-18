package com.google.android.setupcompat.internal;

import android.os.Bundle;

public final /* synthetic */ class SetupCompatServiceInvoker$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ SetupCompatServiceInvoker f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ Bundle f$2;

    public /* synthetic */ SetupCompatServiceInvoker$$ExternalSyntheticLambda1(SetupCompatServiceInvoker setupCompatServiceInvoker, String str, Bundle bundle) {
        this.f$0 = setupCompatServiceInvoker;
        this.f$1 = str;
        this.f$2 = bundle;
    }

    public final void run() {
        this.f$0.lambda$bindBack$1(this.f$1, this.f$2);
    }
}
