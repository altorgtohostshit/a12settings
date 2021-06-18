package com.android.settings.fuelgauge;

public final /* synthetic */ class BatteryUtils$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BatteryUtils f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ BatteryUtils$$ExternalSyntheticLambda0(BatteryUtils batteryUtils, int i, int i2, String str) {
        this.f$0 = batteryUtils;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = str;
    }

    public final void run() {
        this.f$0.lambda$setForceAppStandby$0(this.f$1, this.f$2, this.f$3);
    }
}
