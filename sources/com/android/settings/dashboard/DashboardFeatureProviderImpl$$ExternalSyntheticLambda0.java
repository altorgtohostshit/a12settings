package com.android.settings.dashboard;

import android.net.Uri;
import androidx.preference.Preference;

public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda0 implements Preference.OnPreferenceChangeListener {
    public final /* synthetic */ DashboardFeatureProviderImpl f$0;
    public final /* synthetic */ Uri f$1;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda0(DashboardFeatureProviderImpl dashboardFeatureProviderImpl, Uri uri) {
        this.f$0 = dashboardFeatureProviderImpl;
        this.f$1 = uri;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        return this.f$0.lambda$bindSwitchAndGetObserver$5(this.f$1, preference, obj);
    }
}
