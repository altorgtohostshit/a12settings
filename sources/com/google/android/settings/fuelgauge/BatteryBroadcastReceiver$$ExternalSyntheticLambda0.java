package com.google.android.settings.fuelgauge;

import android.content.BroadcastReceiver;
import android.content.Context;

public final /* synthetic */ class BatteryBroadcastReceiver$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BatteryBroadcastReceiver f$0;
    public final /* synthetic */ Context f$1;
    public final /* synthetic */ BroadcastReceiver.PendingResult f$2;

    public /* synthetic */ BatteryBroadcastReceiver$$ExternalSyntheticLambda0(BatteryBroadcastReceiver batteryBroadcastReceiver, Context context, BroadcastReceiver.PendingResult pendingResult) {
        this.f$0 = batteryBroadcastReceiver;
        this.f$1 = context;
        this.f$2 = pendingResult;
    }

    public final void run() {
        this.f$0.lambda$onReceive$0(this.f$1, this.f$2);
    }
}
