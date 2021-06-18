package com.android.settingslib.graph;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ThemedBatteryDrawable.kt */
final class ThemedBatteryDrawable$invalidateRunnable$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ ThemedBatteryDrawable this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ThemedBatteryDrawable$invalidateRunnable$1(ThemedBatteryDrawable themedBatteryDrawable) {
        super(0);
        this.this$0 = themedBatteryDrawable;
    }

    public final void invoke() {
        this.this$0.invalidateSelf();
    }
}
