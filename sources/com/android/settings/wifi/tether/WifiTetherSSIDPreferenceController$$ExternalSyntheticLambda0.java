package com.android.settings.wifi.tether;

import android.content.Intent;
import android.view.View;

public final /* synthetic */ class WifiTetherSSIDPreferenceController$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ WifiTetherSSIDPreferenceController f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ WifiTetherSSIDPreferenceController$$ExternalSyntheticLambda0(WifiTetherSSIDPreferenceController wifiTetherSSIDPreferenceController, Intent intent) {
        this.f$0 = wifiTetherSSIDPreferenceController;
        this.f$1 = intent;
    }

    public final void onClick(View view) {
        this.f$0.lambda$updateDisplay$0(this.f$1, view);
    }
}
