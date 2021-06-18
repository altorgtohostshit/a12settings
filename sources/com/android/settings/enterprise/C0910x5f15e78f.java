package com.android.settings.enterprise;

import androidx.preference.Preference;
import com.android.settings.applications.ApplicationFeatureProvider;

/* renamed from: com.android.settings.enterprise.AdminGrantedPermissionsPreferenceControllerBase$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C0910x5f15e78f implements ApplicationFeatureProvider.NumberOfAppsCallback {
    public final /* synthetic */ AdminGrantedPermissionsPreferenceControllerBase f$0;
    public final /* synthetic */ Preference f$1;

    public /* synthetic */ C0910x5f15e78f(AdminGrantedPermissionsPreferenceControllerBase adminGrantedPermissionsPreferenceControllerBase, Preference preference) {
        this.f$0 = adminGrantedPermissionsPreferenceControllerBase;
        this.f$1 = preference;
    }

    public final void onNumberOfAppsResult(int i) {
        this.f$0.lambda$updateState$0(this.f$1, i);
    }
}
