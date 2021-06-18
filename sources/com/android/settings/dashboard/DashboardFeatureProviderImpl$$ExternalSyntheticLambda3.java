package com.android.settings.dashboard;

import androidx.preference.Preference;

public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ Preference f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda3(Preference preference, String str) {
        this.f$0 = preference;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.setTitle((CharSequence) this.f$1);
    }
}
