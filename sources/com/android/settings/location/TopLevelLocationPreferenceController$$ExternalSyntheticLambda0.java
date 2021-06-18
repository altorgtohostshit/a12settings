package com.android.settings.location;

import android.permission.PermissionControllerManager;

public final /* synthetic */ class TopLevelLocationPreferenceController$$ExternalSyntheticLambda0 implements PermissionControllerManager.OnCountPermissionAppsResultCallback {
    public final /* synthetic */ TopLevelLocationPreferenceController f$0;

    public /* synthetic */ TopLevelLocationPreferenceController$$ExternalSyntheticLambda0(TopLevelLocationPreferenceController topLevelLocationPreferenceController) {
        this.f$0 = topLevelLocationPreferenceController;
    }

    public final void onCountPermissionApps(int i) {
        this.f$0.lambda$updateState$0(i);
    }
}
