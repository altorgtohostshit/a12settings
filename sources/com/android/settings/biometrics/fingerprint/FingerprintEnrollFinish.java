package com.android.settings.biometrics.fingerprint;

import android.content.ComponentName;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricEnrollBase;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;

public class FingerprintEnrollFinish extends BiometricEnrollBase {
    static final String FINGERPRINT_SUGGESTION_ACTIVITY = "com.android.settings.SetupFingerprintSuggestionActivity";
    static final int REQUEST_ADD_ANOTHER = 1;

    public int getMetricsCategory() {
        return 242;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.fingerprint_enroll_finish);
        setHeaderText((int) R.string.security_settings_fingerprint_enroll_finish_title);
        setDescriptionText((int) R.string.security_settings_fingerprint_enroll_finish_message);
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.fingerprint_enroll_button_add).setButtonType(7).setTheme(2131952137).build());
        this.mFooterBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R.string.security_settings_fingerprint_enroll_done).setListener(new FingerprintEnrollFinish$$ExternalSyntheticLambda0(this)).setButtonType(5).setTheme(2131952136).build());
    }

    public void onBackPressed() {
        super.onBackPressed();
        updateFingerprintSuggestionEnableState();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        FooterButton secondaryButton = this.mFooterBarMixin.getSecondaryButton();
        FingerprintManager fingerprintManagerOrNull = Utils.getFingerprintManagerOrNull(this);
        boolean z = false;
        if (fingerprintManagerOrNull != null && fingerprintManagerOrNull.getEnrolledFingerprints(this.mUserId).size() >= getResources().getInteger(17694816)) {
            z = true;
        }
        if (z) {
            secondaryButton.setVisibility(4);
        } else {
            secondaryButton.setOnClickListener(new FingerprintEnrollFinish$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: protected */
    public void onNextButtonClick(View view) {
        updateFingerprintSuggestionEnableState();
        setResult(1);
        if (WizardManagerHelper.isAnySetupWizard(getIntent())) {
            postEnroll();
        } else if (this.mFromSettingsSummary) {
            launchFingerprintSettings();
        }
        finish();
    }

    private void updateFingerprintSuggestionEnableState() {
        FingerprintManager fingerprintManagerOrNull = Utils.getFingerprintManagerOrNull(this);
        if (fingerprintManagerOrNull != null) {
            int size = fingerprintManagerOrNull.getEnrolledFingerprints(this.mUserId).size();
            boolean z = true;
            getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext(), FINGERPRINT_SUGGESTION_ACTIVITY), size == 1 ? 1 : 2, 1);
            StringBuilder sb = new StringBuilder();
            sb.append("com.android.settings.SetupFingerprintSuggestionActivity enabled state = ");
            if (size != 1) {
                z = false;
            }
            sb.append(z);
            Log.d("FingerprintEnrollFinish", sb.toString());
        }
    }

    private void postEnroll() {
        FingerprintManager fingerprintManagerOrNull = Utils.getFingerprintManagerOrNull(this);
        if (fingerprintManagerOrNull != null) {
            fingerprintManagerOrNull.revokeChallenge(this.mUserId, this.mChallenge);
        }
    }

    private void launchFingerprintSettings() {
        Intent intent = new Intent("android.settings.FINGERPRINT_SETTINGS");
        intent.setPackage("com.android.settings");
        intent.putExtra("hw_auth_token", this.mToken);
        intent.setFlags(603979776);
        intent.putExtra("android.intent.extra.USER_ID", this.mUserId);
        intent.putExtra("challenge", this.mChallenge);
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void onAddAnotherButtonClick(View view) {
        startActivityForResult(getFingerprintEnrollingIntent(), 1);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        updateFingerprintSuggestionEnableState();
        if (i != 1 || i2 == 0) {
            super.onActivityResult(i, i2, intent);
            return;
        }
        setResult(i2, intent);
        finish();
    }
}
