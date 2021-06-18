package com.android.settings.fuelgauge.batterytip;

import android.app.job.JobParameters;

public final /* synthetic */ class AnomalyConfigJobService$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AnomalyConfigJobService f$0;
    public final /* synthetic */ JobParameters f$1;

    public /* synthetic */ AnomalyConfigJobService$$ExternalSyntheticLambda0(AnomalyConfigJobService anomalyConfigJobService, JobParameters jobParameters) {
        this.f$0 = anomalyConfigJobService;
        this.f$1 = jobParameters;
    }

    public final void run() {
        this.f$0.lambda$onStartJob$0(this.f$1);
    }
}
