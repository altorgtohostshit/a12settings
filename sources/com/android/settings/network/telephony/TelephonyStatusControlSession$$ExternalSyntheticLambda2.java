package com.android.settings.network.telephony;

import java.util.function.Consumer;

public final /* synthetic */ class TelephonyStatusControlSession$$ExternalSyntheticLambda2 implements Consumer {
    public static final /* synthetic */ TelephonyStatusControlSession$$ExternalSyntheticLambda2 INSTANCE = new TelephonyStatusControlSession$$ExternalSyntheticLambda2();

    private /* synthetic */ TelephonyStatusControlSession$$ExternalSyntheticLambda2() {
    }

    public final void accept(Object obj) {
        ((TelephonyAvailabilityHandler) obj).unsetAvailabilityStatus();
    }
}
