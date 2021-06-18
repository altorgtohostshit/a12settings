package com.android.settings.sim.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;

public final /* synthetic */ class SimSlotChangeReceiver$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ SimSlotChangeReceiver f$0;
    public final /* synthetic */ Context f$1;
    public final /* synthetic */ BroadcastReceiver.PendingResult f$2;

    public /* synthetic */ SimSlotChangeReceiver$$ExternalSyntheticLambda1(SimSlotChangeReceiver simSlotChangeReceiver, Context context, BroadcastReceiver.PendingResult pendingResult) {
        this.f$0 = simSlotChangeReceiver;
        this.f$1 = context;
        this.f$2 = pendingResult;
    }

    public final void run() {
        this.f$0.lambda$onReceive$0(this.f$1, this.f$2);
    }
}
