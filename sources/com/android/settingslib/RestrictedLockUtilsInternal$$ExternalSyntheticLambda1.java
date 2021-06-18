package com.android.settingslib;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import com.android.settingslib.RestrictedLockUtilsInternal;

public final /* synthetic */ class RestrictedLockUtilsInternal$$ExternalSyntheticLambda1 implements RestrictedLockUtilsInternal.LockSettingCheck {
    public static final /* synthetic */ RestrictedLockUtilsInternal$$ExternalSyntheticLambda1 INSTANCE = new RestrictedLockUtilsInternal$$ExternalSyntheticLambda1();

    private /* synthetic */ RestrictedLockUtilsInternal$$ExternalSyntheticLambda1() {
    }

    public final boolean isEnforcing(DevicePolicyManager devicePolicyManager, ComponentName componentName, int i) {
        return RestrictedLockUtilsInternal.lambda$checkIfPasswordQualityIsSet$1(devicePolicyManager, componentName, i);
    }
}
