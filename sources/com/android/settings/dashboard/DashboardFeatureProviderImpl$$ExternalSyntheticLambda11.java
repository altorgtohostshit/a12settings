package com.android.settings.dashboard;

import androidx.preference.Preference;
import com.android.settingslib.drawer.Tile;

public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda11 implements Runnable {
    public final /* synthetic */ DashboardFeatureProviderImpl f$0;
    public final /* synthetic */ Tile f$1;
    public final /* synthetic */ Preference f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda11(DashboardFeatureProviderImpl dashboardFeatureProviderImpl, Tile tile, Preference preference, boolean z) {
        this.f$0 = dashboardFeatureProviderImpl;
        this.f$1 = tile;
        this.f$2 = preference;
        this.f$3 = z;
    }

    public final void run() {
        this.f$0.lambda$bindIcon$11(this.f$1, this.f$2, this.f$3);
    }
}
