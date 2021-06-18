package com.android.settingslib.applications;

import android.permission.PermissionControllerManager;
import com.android.settingslib.applications.PermissionsSummaryHelper;
import java.util.List;

public final /* synthetic */ class PermissionsSummaryHelper$$ExternalSyntheticLambda0 implements PermissionControllerManager.OnGetAppPermissionResultCallback {
    public final /* synthetic */ PermissionsSummaryHelper.PermissionsResultCallback f$0;

    public /* synthetic */ PermissionsSummaryHelper$$ExternalSyntheticLambda0(PermissionsSummaryHelper.PermissionsResultCallback permissionsResultCallback) {
        this.f$0 = permissionsResultCallback;
    }

    public final void onGetAppPermissions(List list) {
        PermissionsSummaryHelper.lambda$getPermissionSummary$0(this.f$0, list);
    }
}
