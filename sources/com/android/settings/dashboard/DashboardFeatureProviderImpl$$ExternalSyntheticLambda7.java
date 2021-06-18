package com.android.settings.dashboard;

import android.net.Uri;
import androidx.preference.Preference;

public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ DashboardFeatureProviderImpl f$0;
    public final /* synthetic */ Uri f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ Preference f$3;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda7(DashboardFeatureProviderImpl dashboardFeatureProviderImpl, Uri uri, boolean z, Preference preference) {
        this.f$0 = dashboardFeatureProviderImpl;
        this.f$1 = uri;
        this.f$2 = z;
        this.f$3 = preference;
    }

    public final void run() {
        this.f$0.lambda$onCheckedChanged$7(this.f$1, this.f$2, this.f$3);
    }
}
