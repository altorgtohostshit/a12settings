package com.android.settings.network.telephony;

import android.widget.Switch;
import com.android.settings.widget.SettingsMainSwitchBar;

public final /* synthetic */ class MobileNetworkSwitchController$$ExternalSyntheticLambda0 implements SettingsMainSwitchBar.OnBeforeCheckedChangeListener {
    public final /* synthetic */ MobileNetworkSwitchController f$0;

    public /* synthetic */ MobileNetworkSwitchController$$ExternalSyntheticLambda0(MobileNetworkSwitchController mobileNetworkSwitchController) {
        this.f$0 = mobileNetworkSwitchController;
    }

    public final boolean onBeforeCheckedChanged(Switch switchR, boolean z) {
        return this.f$0.lambda$displayPreference$0(switchR, z);
    }
}
