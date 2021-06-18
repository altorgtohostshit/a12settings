package com.google.android.settings.fuelgauge;

import com.android.settings.fuelgauge.BatteryEntry;
import java.util.function.Predicate;

public final /* synthetic */ class DatabaseUtils$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ DatabaseUtils$$ExternalSyntheticLambda1 INSTANCE = new DatabaseUtils$$ExternalSyntheticLambda1();

    private /* synthetic */ DatabaseUtils$$ExternalSyntheticLambda1() {
    }

    public final boolean test(Object obj) {
        return DatabaseUtils.lambda$sendBatteryEntryData$0((BatteryEntry) obj);
    }
}
