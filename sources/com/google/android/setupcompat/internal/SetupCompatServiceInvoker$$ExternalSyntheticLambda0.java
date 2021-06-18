package com.google.android.setupcompat.internal;

import android.os.Bundle;

public final /* synthetic */ class SetupCompatServiceInvoker$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SetupCompatServiceInvoker f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ Bundle f$2;

    public /* synthetic */ SetupCompatServiceInvoker$$ExternalSyntheticLambda0(SetupCompatServiceInvoker setupCompatServiceInvoker, int i, Bundle bundle) {
        this.f$0 = setupCompatServiceInvoker;
        this.f$1 = i;
        this.f$2 = bundle;
    }

    public final void run() {
        this.f$0.lambda$logMetricEvent$0(this.f$1, this.f$2);
    }
}
