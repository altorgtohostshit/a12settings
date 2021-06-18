package com.android.settingslib;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import com.android.settingslib.RestrictedLockUtilsInternal;

public final /* synthetic */ class RestrictedLockUtilsInternal$$ExternalSyntheticLambda0 implements RestrictedLockUtilsInternal.LockSettingCheck {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ RestrictedLockUtilsInternal$$ExternalSyntheticLambda0(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final boolean isEnforcing(DevicePolicyManager devicePolicyManager, ComponentName componentName, int i) {
        return RestrictedLockUtilsInternal.lambda$checkIfKeyguardFeaturesDisabled$0(this.f$0, this.f$1, devicePolicyManager, componentName, i);
    }
}
