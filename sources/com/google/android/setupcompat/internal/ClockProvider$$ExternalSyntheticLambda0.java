package com.google.android.setupcompat.internal;

import com.google.android.setupcompat.internal.ClockProvider;

public final /* synthetic */ class ClockProvider$$ExternalSyntheticLambda0 implements Ticker {
    public final /* synthetic */ ClockProvider.Supplier f$0;

    public /* synthetic */ ClockProvider$$ExternalSyntheticLambda0(ClockProvider.Supplier supplier) {
        this.f$0 = supplier;
    }

    public final long read() {
        return ((Long) this.f$0.get()).longValue();
    }
}
