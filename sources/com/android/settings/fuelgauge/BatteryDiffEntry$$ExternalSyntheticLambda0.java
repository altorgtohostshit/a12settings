package com.android.settings.fuelgauge;

import java.util.Comparator;

public final /* synthetic */ class BatteryDiffEntry$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ BatteryDiffEntry$$ExternalSyntheticLambda0 INSTANCE = new BatteryDiffEntry$$ExternalSyntheticLambda0();

    private /* synthetic */ BatteryDiffEntry$$ExternalSyntheticLambda0() {
    }

    public final int compare(Object obj, Object obj2) {
        return Double.compare(((BatteryDiffEntry) obj2).getPercentOfTotal(), ((BatteryDiffEntry) obj).getPercentOfTotal());
    }
}
