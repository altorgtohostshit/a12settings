package com.android.settings.fuelgauge.batterytip;

import android.app.job.JobParameters;

public final /* synthetic */ class AnomalyDetectionJobService$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AnomalyDetectionJobService f$0;
    public final /* synthetic */ JobParameters f$1;

    public /* synthetic */ AnomalyDetectionJobService$$ExternalSyntheticLambda0(AnomalyDetectionJobService anomalyDetectionJobService, JobParameters jobParameters) {
        this.f$0 = anomalyDetectionJobService;
        this.f$1 = jobParameters;
    }

    public final void run() {
        this.f$0.lambda$onStartJob$0(this.f$1);
    }
}
