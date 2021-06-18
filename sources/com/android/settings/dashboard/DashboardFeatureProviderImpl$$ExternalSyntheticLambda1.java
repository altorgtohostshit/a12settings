package com.android.settings.dashboard;

import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import com.android.settingslib.drawer.Tile;

public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda1 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ DashboardFeatureProviderImpl f$0;
    public final /* synthetic */ FragmentActivity f$1;
    public final /* synthetic */ Tile f$2;
    public final /* synthetic */ Intent f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda1(DashboardFeatureProviderImpl dashboardFeatureProviderImpl, FragmentActivity fragmentActivity, Tile tile, Intent intent, int i) {
        this.f$0 = dashboardFeatureProviderImpl;
        this.f$1 = fragmentActivity;
        this.f$2 = tile;
        this.f$3 = intent;
        this.f$4 = i;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$bindPreferenceToTileAndGetObservers$0(this.f$1, this.f$2, this.f$3, this.f$4, preference);
    }
}
