package com.google.android.setupcompat.internal;

import java.util.concurrent.ThreadFactory;

public final /* synthetic */ class ExecutorProvider$$ExternalSyntheticLambda0 implements ThreadFactory {
    public final /* synthetic */ String f$0;

    public /* synthetic */ ExecutorProvider$$ExternalSyntheticLambda0(String str) {
        this.f$0 = str;
    }

    public final Thread newThread(Runnable runnable) {
        return ExecutorProvider.lambda$createSizeBoundedExecutor$0(this.f$0, runnable);
    }
}
