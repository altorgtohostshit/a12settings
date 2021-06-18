package com.android.settings.bluetooth;

import android.graphics.Bitmap;
import android.widget.ImageView;

/* renamed from: com.android.settings.bluetooth.AdvancedBluetoothDetailsHeaderController$$ExternalSyntheticLambda2 */
public final /* synthetic */ class C0775xd65cca50 implements Runnable {
    public final /* synthetic */ AdvancedBluetoothDetailsHeaderController f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ Bitmap f$2;
    public final /* synthetic */ ImageView f$3;

    public /* synthetic */ C0775xd65cca50(AdvancedBluetoothDetailsHeaderController advancedBluetoothDetailsHeaderController, String str, Bitmap bitmap, ImageView imageView) {
        this.f$0 = advancedBluetoothDetailsHeaderController;
        this.f$1 = str;
        this.f$2 = bitmap;
        this.f$3 = imageView;
    }

    public final void run() {
        this.f$0.lambda$updateIcon$2(this.f$1, this.f$2, this.f$3);
    }
}
