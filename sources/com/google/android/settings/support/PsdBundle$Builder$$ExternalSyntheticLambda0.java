package com.google.android.settings.support;

import android.os.UidBatteryConsumer;
import java.util.Comparator;

public final /* synthetic */ class PsdBundle$Builder$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ PsdBundle$Builder$$ExternalSyntheticLambda0 INSTANCE = new PsdBundle$Builder$$ExternalSyntheticLambda0();

    private /* synthetic */ PsdBundle$Builder$$ExternalSyntheticLambda0() {
    }

    public final int compare(Object obj, Object obj2) {
        return Double.compare(((UidBatteryConsumer) obj2).getConsumedPower(), ((UidBatteryConsumer) obj).getConsumedPower());
    }
}
