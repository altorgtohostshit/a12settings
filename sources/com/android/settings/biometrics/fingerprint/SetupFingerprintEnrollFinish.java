package com.android.settings.biometrics.fingerprint;

import android.content.Intent;
import com.android.settings.R;
import com.android.settings.SetupWizardUtils;

public class SetupFingerprintEnrollFinish extends FingerprintEnrollFinish {
    public int getMetricsCategory() {
        return 248;
    }

    /* access modifiers changed from: protected */
    public Intent getFingerprintEnrollingIntent() {
        Intent intent = new Intent(this, SetupFingerprintEnrollEnrolling.class);
        intent.putExtra("hw_auth_token", this.mToken);
        int i = this.mUserId;
        if (i != -10000) {
            intent.putExtra("android.intent.extra.USER_ID", i);
        }
        SetupWizardUtils.copySetupExtras(getIntent(), intent);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void initViews() {
        super.initViews();
        getNextButton().setText(this, R.string.next_label);
    }
}
