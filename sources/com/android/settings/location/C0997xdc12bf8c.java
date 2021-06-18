package com.android.settings.location;

import android.permission.PermissionControllerManager;

/* renamed from: com.android.settings.location.AppLocationPermissionPreferenceController$$ExternalSyntheticLambda1 */
public final /* synthetic */ class C0997xdc12bf8c implements PermissionControllerManager.OnCountPermissionAppsResultCallback {
    public final /* synthetic */ AppLocationPermissionPreferenceController f$0;

    public /* synthetic */ C0997xdc12bf8c(AppLocationPermissionPreferenceController appLocationPermissionPreferenceController) {
        this.f$0 = appLocationPermissionPreferenceController;
    }

    public final void onCountPermissionApps(int i) {
        this.f$0.lambda$updateState$1(i);
    }
}
