package com.google.android.settings.biometrics.face;

import android.content.Intent;
import android.content.res.Resources;
import android.hardware.face.FaceManager;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.biometrics.BiometricUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeResolver;

public class FaceEnrollConfirmation extends FragmentActivity {
    private FooterBarMixin mFooterBarMixin;
    private boolean mNextClicked;
    protected byte[] mToken;
    protected int mUserId;

    public void onCreate(Bundle bundle) {
        ThemeResolver.getDefault().applyTheme(this);
        super.onCreate(bundle);
        setContentView((int) R.layout.face_enroll_confirmation);
        this.mToken = getIntent().getByteArrayExtra("hw_auth_token");
        this.mUserId = getIntent().getIntExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
        if (bundle != null) {
            if (this.mToken == null) {
                this.mToken = bundle.getByteArray("hw_auth_token");
            }
            this.mUserId = bundle.getInt("android.intent.extra.USER_ID", this.mUserId);
        }
        setHeaderText(R.string.security_settings_face_enroll_finish_title);
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R.string.security_settings_face_enroll_done).setListener(new FaceEnrollConfirmation$$ExternalSyntheticLambda0(this)).setButtonType(5).setTheme(2131952136).build());
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (!isChangingConfigurations() && !WizardManagerHelper.isAnySetupWizard(getIntent()) && !this.mNextClicked) {
            setResult(3);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onApplyThemeResource(Resources.Theme theme, int i, boolean z) {
        theme.applyStyle(R.style.SetupWizardPartnerResource, true);
        super.onApplyThemeResource(theme, i, z);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putByteArray("hw_auth_token", this.mToken);
        bundle.putInt("android.intent.extra.USER_ID", this.mUserId);
    }

    private GlifLayout getLayout() {
        return (GlifLayout) findViewById(R.id.face_enroll_confirmation);
    }

    /* access modifiers changed from: private */
    public void onButtonPositive(View view) {
        this.mNextClicked = true;
        boolean z = false;
        if (getIntent().getBooleanExtra("from_settings_summary", false)) {
            launchFaceSettings();
            return;
        }
        if (WizardManagerHelper.isAnySetupWizard(getIntent())) {
            revokeChallenge();
        }
        if (BiometricUtils.isMultiBiometricEnrollmentFlow(this)) {
            Log.d("FaceEnrollConfirmation", "Multi-biometric flow");
            z = BiometricUtils.tryStartingNextBiometricEnroll(this, 2);
        }
        if (!z) {
            setResult(1);
            finish();
        }
    }

    private void setHeaderText(int i) {
        TextView headerTextView = getLayout().getHeaderTextView();
        CharSequence text = headerTextView.getText();
        CharSequence text2 = getText(i);
        if (text != text2) {
            if (!TextUtils.isEmpty(text2)) {
                headerTextView.setAccessibilityLiveRegion(1);
            }
            getLayout().setHeaderText(text2);
            setTitle(text2);
        }
    }

    private void revokeChallenge() {
        FaceManager faceManager = (FaceManager) getSystemService(FaceManager.class);
        if (faceManager != null) {
            faceManager.revokeChallenge(getIntent().getIntExtra("sensor_id", -1), this.mUserId, getIntent().getLongExtra("challenge", 0));
        }
    }

    private void launchFaceSettings() {
        Intent intent = new Intent("android.settings.FACE_SETTINGS");
        intent.setPackage("com.android.settings");
        intent.putExtra("hw_auth_token", this.mToken);
        intent.putExtra("android.intent.extra.USER_ID", this.mUserId);
        intent.setFlags(67239936);
        intent.putExtra("challenge", getIntent().getLongExtra("challenge", 0));
        intent.putExtra("sensor_id", getIntent().getIntExtra("sensor_id", 0));
        startActivityForResult(intent, 1);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            setResult(1);
            finish();
        } else if (i == 2) {
            Log.d("FaceEnrollConfirmation", "Next biometric's result: " + i2);
            setResult(1);
            finish();
        }
    }
}
