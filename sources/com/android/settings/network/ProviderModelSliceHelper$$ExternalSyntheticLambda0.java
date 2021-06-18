package com.android.settings.network;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public final /* synthetic */ class ProviderModelSliceHelper$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ProviderModelSliceHelper f$0;
    public final /* synthetic */ AtomicReference f$1;
    public final /* synthetic */ Semaphore f$2;

    public /* synthetic */ ProviderModelSliceHelper$$ExternalSyntheticLambda0(ProviderModelSliceHelper providerModelSliceHelper, AtomicReference atomicReference, Semaphore semaphore) {
        this.f$0 = providerModelSliceHelper;
        this.f$1 = atomicReference;
        this.f$2 = semaphore;
    }

    public final void run() {
        this.f$0.lambda$getMobileDrawable$1(this.f$1, this.f$2);
    }
}
