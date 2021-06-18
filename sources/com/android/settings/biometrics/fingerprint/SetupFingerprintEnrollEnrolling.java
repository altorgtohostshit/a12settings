package com.android.settings.biometrics.fingerprint;

import android.content.Intent;
import com.android.settings.SetupWizardUtils;

public class SetupFingerprintEnrollEnrolling extends FingerprintEnrollEnrolling {
    public int getMetricsCategory() {
        return 246;
    }

    /* access modifiers changed from: protected */
    public Intent getFinishIntent() {
        Intent intent = new Intent(this, SetupFingerprintEnrollFinish.class);
        SetupWizardUtils.copySetupExtras(getIntent(), intent);
        return intent;
    }
}
