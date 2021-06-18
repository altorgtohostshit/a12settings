package com.android.settings.wifi.dpp;

import android.content.Intent;
import android.view.View;

public final /* synthetic */ class WifiDppAddDeviceFragment$$ExternalSyntheticLambda5 implements View.OnClickListener {
    public final /* synthetic */ WifiDppAddDeviceFragment f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ WifiDppAddDeviceFragment$$ExternalSyntheticLambda5(WifiDppAddDeviceFragment wifiDppAddDeviceFragment, Intent intent) {
        this.f$0 = wifiDppAddDeviceFragment;
        this.f$1 = intent;
    }

    public final void onClick(View view) {
        this.f$0.lambda$showErrorUi$2(this.f$1, view);
    }
}
