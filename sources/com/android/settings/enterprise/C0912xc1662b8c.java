package com.android.settings.enterprise;

import androidx.preference.Preference;
import com.android.settings.applications.ApplicationFeatureProvider;

/* renamed from: com.android.settings.enterprise.EnterpriseInstalledPackagesPreferenceController$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C0912xc1662b8c implements ApplicationFeatureProvider.NumberOfAppsCallback {
    public final /* synthetic */ EnterpriseInstalledPackagesPreferenceController f$0;
    public final /* synthetic */ Preference f$1;

    public /* synthetic */ C0912xc1662b8c(EnterpriseInstalledPackagesPreferenceController enterpriseInstalledPackagesPreferenceController, Preference preference) {
        this.f$0 = enterpriseInstalledPackagesPreferenceController;
        this.f$1 = preference;
    }

    public final void onNumberOfAppsResult(int i) {
        this.f$0.lambda$updateState$0(this.f$1, i);
    }
}
