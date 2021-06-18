package com.android.settings.enterprise;

import com.android.settings.applications.ApplicationFeatureProvider;

/* renamed from: com.android.settings.enterprise.EnterpriseInstalledPackagesPreferenceController$$ExternalSyntheticLambda1 */
public final /* synthetic */ class C0913xc1662b8d implements ApplicationFeatureProvider.NumberOfAppsCallback {
    public final /* synthetic */ Boolean[] f$0;

    public /* synthetic */ C0913xc1662b8d(Boolean[] boolArr) {
        this.f$0 = boolArr;
    }

    public final void onNumberOfAppsResult(int i) {
        EnterpriseInstalledPackagesPreferenceController.lambda$isAvailable$1(this.f$0, i);
    }
}
