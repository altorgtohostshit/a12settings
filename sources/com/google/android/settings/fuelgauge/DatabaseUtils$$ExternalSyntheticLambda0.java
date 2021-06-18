package com.google.android.settings.fuelgauge;

import android.os.BatteryUsageStats;
import com.android.settings.fuelgauge.BatteryEntry;
import java.util.List;
import java.util.function.Consumer;

public final /* synthetic */ class DatabaseUtils$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ List f$0;
    public final /* synthetic */ BatteryUsageStats f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ long f$5;
    public final /* synthetic */ long f$6;

    public /* synthetic */ DatabaseUtils$$ExternalSyntheticLambda0(List list, BatteryUsageStats batteryUsageStats, int i, int i2, int i3, long j, long j2) {
        this.f$0 = list;
        this.f$1 = batteryUsageStats;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = i3;
        this.f$5 = j;
        this.f$6 = j2;
    }

    public final void accept(Object obj) {
        DatabaseUtils.lambda$sendBatteryEntryData$1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, (BatteryEntry) obj);
    }
}
