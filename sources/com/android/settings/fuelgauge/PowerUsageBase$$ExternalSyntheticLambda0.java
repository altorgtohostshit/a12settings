package com.android.settings.fuelgauge;

import com.android.settings.fuelgauge.BatteryBroadcastReceiver;

public final /* synthetic */ class PowerUsageBase$$ExternalSyntheticLambda0 implements BatteryBroadcastReceiver.OnBatteryChangedListener {
    public final /* synthetic */ PowerUsageBase f$0;

    public /* synthetic */ PowerUsageBase$$ExternalSyntheticLambda0(PowerUsageBase powerUsageBase) {
        this.f$0 = powerUsageBase;
    }

    public final void onBatteryChanged(int i) {
        this.f$0.lambda$onCreate$0(i);
    }
}
