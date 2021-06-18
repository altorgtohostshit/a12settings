package com.android.settings.deviceinfo.simstatus;

import java.util.concurrent.atomic.AtomicReference;

public final /* synthetic */ class SimStatusDialogController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ SimStatusDialogController f$0;
    public final /* synthetic */ AtomicReference f$1;

    public /* synthetic */ SimStatusDialogController$$ExternalSyntheticLambda1(SimStatusDialogController simStatusDialogController, AtomicReference atomicReference) {
        this.f$0 = simStatusDialogController;
        this.f$1 = atomicReference;
    }

    public final void run() {
        this.f$0.lambda$requestForUpdateEid$0(this.f$1);
    }
}
