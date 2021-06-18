package com.android.settings.fuelgauge.batterytip;

import android.app.job.JobParameters;

public final /* synthetic */ class AnomalyCleanupJobService$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AnomalyCleanupJobService f$0;
    public final /* synthetic */ BatteryDatabaseManager f$1;
    public final /* synthetic */ BatteryTipPolicy f$2;
    public final /* synthetic */ JobParameters f$3;

    public /* synthetic */ AnomalyCleanupJobService$$ExternalSyntheticLambda0(AnomalyCleanupJobService anomalyCleanupJobService, BatteryDatabaseManager batteryDatabaseManager, BatteryTipPolicy batteryTipPolicy, JobParameters jobParameters) {
        this.f$0 = anomalyCleanupJobService;
        this.f$1 = batteryDatabaseManager;
        this.f$2 = batteryTipPolicy;
        this.f$3 = jobParameters;
    }

    public final void run() {
        this.f$0.lambda$onStartJob$0(this.f$1, this.f$2, this.f$3);
    }
}
