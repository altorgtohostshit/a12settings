package com.android.settings.fuelgauge;

import java.util.Comparator;

public final /* synthetic */ class BatteryEntry$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ BatteryEntry$$ExternalSyntheticLambda0 INSTANCE = new BatteryEntry$$ExternalSyntheticLambda0();

    private /* synthetic */ BatteryEntry$$ExternalSyntheticLambda0() {
    }

    public final int compare(Object obj, Object obj2) {
        return Double.compare(((BatteryEntry) obj2).getConsumedPower(), ((BatteryEntry) obj).getConsumedPower());
    }
}
