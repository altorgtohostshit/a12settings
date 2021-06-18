package com.android.settings.dashboard;

import androidx.preference.Preference;

public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ Preference f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda2(Preference preference, String str) {
        this.f$0 = preference;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.setSummary((CharSequence) this.f$1);
    }
}
