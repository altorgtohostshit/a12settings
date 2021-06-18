package com.android.settings.enterprise;

import android.content.Context;

public class FailedPasswordWipeCurrentUserPreferenceController extends FailedPasswordWipePreferenceControllerBase {
    public String getPreferenceKey() {
        return "failed_password_wipe_current_user";
    }

    public FailedPasswordWipeCurrentUserPreferenceController(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public int getMaximumFailedPasswordsBeforeWipe() {
        return this.mFeatureProvider.getMaximumFailedPasswordsBeforeWipeInCurrentUser();
    }
}
