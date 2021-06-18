package com.android.settings.inputmethod;

import android.content.Context;

public final /* synthetic */ class PhysicalKeyboardFragment$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ PhysicalKeyboardFragment f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ PhysicalKeyboardFragment$$ExternalSyntheticLambda2(PhysicalKeyboardFragment physicalKeyboardFragment, Context context) {
        this.f$0 = physicalKeyboardFragment;
        this.f$1 = context;
    }

    public final void run() {
        this.f$0.lambda$scheduleUpdateHardKeyboards$1(this.f$1);
    }
}
