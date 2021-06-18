package com.android.settings.wifi.dpp;

import androidx.lifecycle.Observer;

public final /* synthetic */ class WifiDppAddDeviceFragment$$ExternalSyntheticLambda6 implements Observer {
    public final /* synthetic */ WifiDppAddDeviceFragment f$0;
    public final /* synthetic */ WifiDppInitiatorViewModel f$1;

    public /* synthetic */ WifiDppAddDeviceFragment$$ExternalSyntheticLambda6(WifiDppAddDeviceFragment wifiDppAddDeviceFragment, WifiDppInitiatorViewModel wifiDppInitiatorViewModel) {
        this.f$0 = wifiDppAddDeviceFragment;
        this.f$1 = wifiDppInitiatorViewModel;
    }

    public final void onChanged(Object obj) {
        this.f$0.lambda$onCreate$3(this.f$1, (Integer) obj);
    }
}
