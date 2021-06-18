package com.android.settingslib;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import com.android.settingslib.RestrictedLockUtilsInternal;

public final /* synthetic */ class RestrictedLockUtilsInternal$$ExternalSyntheticLambda2 implements RestrictedLockUtilsInternal.LockSettingCheck {
    public static final /* synthetic */ RestrictedLockUtilsInternal$$ExternalSyntheticLambda2 INSTANCE = new RestrictedLockUtilsInternal$$ExternalSyntheticLambda2();

    private /* synthetic */ RestrictedLockUtilsInternal$$ExternalSyntheticLambda2() {
    }

    public final boolean isEnforcing(DevicePolicyManager devicePolicyManager, ComponentName componentName, int i) {
        return RestrictedLockUtilsInternal.lambda$checkIfMaximumTimeToLockIsSet$2(devicePolicyManager, componentName, i);
    }
}
