package com.android.settings.homepage.contextualcards.slices;

import android.content.Context;
import java.util.concurrent.Callable;

public final /* synthetic */ class BatteryFixSlice$$ExternalSyntheticLambda0 implements Callable {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ BatteryFixSlice$$ExternalSyntheticLambda0(Context context) {
        this.f$0 = context;
    }

    public final Object call() {
        return BatteryFixSlice.refreshBatteryTips(this.f$0);
    }
}
