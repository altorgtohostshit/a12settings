package com.android.settings.bluetooth;

import android.widget.LinearLayout;

/* renamed from: com.android.settings.bluetooth.AdvancedBluetoothDetailsHeaderController$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C0773xd65cca4e implements Runnable {
    public final /* synthetic */ AdvancedBluetoothDetailsHeaderController f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ LinearLayout f$3;

    public /* synthetic */ C0773xd65cca4e(AdvancedBluetoothDetailsHeaderController advancedBluetoothDetailsHeaderController, int i, int i2, LinearLayout linearLayout) {
        this.f$0 = advancedBluetoothDetailsHeaderController;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = linearLayout;
    }

    public final void run() {
        this.f$0.lambda$showBatteryPredictionIfNecessary$0(this.f$1, this.f$2, this.f$3);
    }
}
