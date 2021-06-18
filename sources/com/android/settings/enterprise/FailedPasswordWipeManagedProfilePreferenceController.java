package com.android.settings.enterprise;

import android.content.Context;

public class FailedPasswordWipeManagedProfilePreferenceController extends FailedPasswordWipePreferenceControllerBase {
    public String getPreferenceKey() {
        return "failed_password_wipe_managed_profile";
    }

    public FailedPasswordWipeManagedProfilePreferenceController(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public int getMaximumFailedPasswordsBeforeWipe() {
        return this.mFeatureProvider.getMaximumFailedPasswordsBeforeWipeInManagedProfile();
    }
}
