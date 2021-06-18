package com.android.settings.dashboard;

import android.graphics.drawable.Icon;
import android.util.Pair;
import androidx.preference.Preference;
import com.android.settingslib.drawer.Tile;

public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda9 implements Runnable {
    public final /* synthetic */ DashboardFeatureProviderImpl f$0;
    public final /* synthetic */ Preference f$1;
    public final /* synthetic */ Tile f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ Pair f$4;
    public final /* synthetic */ Icon f$5;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda9(DashboardFeatureProviderImpl dashboardFeatureProviderImpl, Preference preference, Tile tile, boolean z, Pair pair, Icon icon) {
        this.f$0 = dashboardFeatureProviderImpl;
        this.f$1 = preference;
        this.f$2 = tile;
        this.f$3 = z;
        this.f$4 = pair;
        this.f$5 = icon;
    }

    public final void run() {
        this.f$0.lambda$bindIcon$10(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
