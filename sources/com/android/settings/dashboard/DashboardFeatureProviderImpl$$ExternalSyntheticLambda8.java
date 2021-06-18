package com.android.settings.dashboard;

import android.os.Bundle;
import androidx.preference.Preference;

public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ DashboardFeatureProviderImpl f$0;
    public final /* synthetic */ Preference f$1;
    public final /* synthetic */ Bundle f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda8(DashboardFeatureProviderImpl dashboardFeatureProviderImpl, Preference preference, Bundle bundle, boolean z) {
        this.f$0 = dashboardFeatureProviderImpl;
        this.f$1 = preference;
        this.f$2 = bundle;
        this.f$3 = z;
    }

    public final void run() {
        this.f$0.lambda$onCheckedChanged$6(this.f$1, this.f$2, this.f$3);
    }
}
