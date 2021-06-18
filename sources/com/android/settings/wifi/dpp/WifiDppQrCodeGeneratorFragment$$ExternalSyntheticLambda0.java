package com.android.settings.wifi.dpp;

import android.content.Intent;
import android.view.View;

public final /* synthetic */ class WifiDppQrCodeGeneratorFragment$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ WifiDppQrCodeGeneratorFragment f$0;
    public final /* synthetic */ Intent f$1;
    public final /* synthetic */ WifiNetworkConfig f$2;

    public /* synthetic */ WifiDppQrCodeGeneratorFragment$$ExternalSyntheticLambda0(WifiDppQrCodeGeneratorFragment wifiDppQrCodeGeneratorFragment, Intent intent, WifiNetworkConfig wifiNetworkConfig) {
        this.f$0 = wifiDppQrCodeGeneratorFragment;
        this.f$1 = intent;
        this.f$2 = wifiNetworkConfig;
    }

    public final void onClick(View view) {
        this.f$0.lambda$onViewCreated$0(this.f$1, this.f$2, view);
    }
}
