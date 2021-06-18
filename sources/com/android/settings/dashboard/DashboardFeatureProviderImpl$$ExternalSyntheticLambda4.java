package com.android.settings.dashboard;

import android.net.Uri;
import androidx.preference.Preference;

public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ DashboardFeatureProviderImpl f$0;
    public final /* synthetic */ Uri f$1;
    public final /* synthetic */ Preference f$2;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda4(DashboardFeatureProviderImpl dashboardFeatureProviderImpl, Uri uri, Preference preference) {
        this.f$0 = dashboardFeatureProviderImpl;
        this.f$1 = uri;
        this.f$2 = preference;
    }

    public final void run() {
        this.f$0.lambda$refreshSummary$4(this.f$1, this.f$2);
    }
}
