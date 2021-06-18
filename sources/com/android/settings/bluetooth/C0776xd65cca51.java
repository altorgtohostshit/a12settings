package com.android.settings.bluetooth;

import android.widget.ImageView;

/* renamed from: com.android.settings.bluetooth.AdvancedBluetoothDetailsHeaderController$$ExternalSyntheticLambda3 */
public final /* synthetic */ class C0776xd65cca51 implements Runnable {
    public final /* synthetic */ AdvancedBluetoothDetailsHeaderController f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ImageView f$2;

    public /* synthetic */ C0776xd65cca51(AdvancedBluetoothDetailsHeaderController advancedBluetoothDetailsHeaderController, String str, ImageView imageView) {
        this.f$0 = advancedBluetoothDetailsHeaderController;
        this.f$1 = str;
        this.f$2 = imageView;
    }

    public final void run() {
        this.f$0.lambda$updateIcon$3(this.f$1, this.f$2);
    }
}
