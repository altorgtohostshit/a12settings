package com.android.settings.fuelgauge.batterytip.detectors;

import android.os.UidBatteryConsumer;
import java.util.Comparator;

public final /* synthetic */ class HighUsageDetector$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ HighUsageDetector$$ExternalSyntheticLambda0 INSTANCE = new HighUsageDetector$$ExternalSyntheticLambda0();

    private /* synthetic */ HighUsageDetector$$ExternalSyntheticLambda0() {
    }

    public final int compare(Object obj, Object obj2) {
        return Double.compare(((UidBatteryConsumer) obj2).getConsumedPower(), ((UidBatteryConsumer) obj).getConsumedPower());
    }
}
