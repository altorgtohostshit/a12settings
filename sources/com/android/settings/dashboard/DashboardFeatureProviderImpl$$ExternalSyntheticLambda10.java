package com.android.settings.dashboard;

import androidx.preference.Preference;

public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda10 implements Runnable {
    public final /* synthetic */ DashboardFeatureProviderImpl f$0;
    public final /* synthetic */ Preference f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda10(DashboardFeatureProviderImpl dashboardFeatureProviderImpl, Preference preference, boolean z) {
        this.f$0 = dashboardFeatureProviderImpl;
        this.f$1 = preference;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.lambda$refreshSwitch$8(this.f$1, this.f$2);
    }
}
