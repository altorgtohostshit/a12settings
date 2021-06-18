package com.android.settings.network.telephony;

import android.content.Context;
import java.util.concurrent.Callable;

public final /* synthetic */ class MobileNetworkUtils$$ExternalSyntheticLambda0 implements Callable {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ MobileNetworkUtils$$ExternalSyntheticLambda0(Context context) {
        this.f$0 = context;
    }

    public final Object call() {
        return MobileNetworkUtils.showEuiccSettingsDetecting(this.f$0);
    }
}
