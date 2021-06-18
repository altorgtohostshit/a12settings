package com.android.settings.biometrics.face;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.face.FaceManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricEnrollBase;
import com.android.settings.biometrics.BiometricUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.view.IllustrationVideoView;

public class FaceEnrollEducation extends BiometricEnrollBase {
    private boolean mAccessibilityEnabled;
    /* access modifiers changed from: private */
    public TextView mDescriptionText;
    private FaceManager mFaceManager;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public View mIllustrationAccessibility;
    /* access modifiers changed from: private */
    public IllustrationVideoView mIllustrationNormal;
    private boolean mNextClicked;
    private Intent mResultIntent;
    private FaceEnrollAccessibilityToggle mSwitchDiversity;
    private CompoundButton.OnCheckedChangeListener mSwitchDiversityListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            int i = z ? R.string.security_settings_face_enroll_education_title_accessibility : R.string.security_settings_face_enroll_education_title;
            FaceEnrollEducation.this.getLayout().setHeaderText(i);
            FaceEnrollEducation.this.setTitle(i);
            if (z) {
                FaceEnrollEducation.this.mIllustrationNormal.stop();
                FaceEnrollEducation.this.mIllustrationNormal.setVisibility(4);
                FaceEnrollEducation.this.mIllustrationAccessibility.setVisibility(0);
                FaceEnrollEducation.this.mDescriptionText.setVisibility(4);
                return;
            }
            FaceEnrollEducation.this.mIllustrationNormal.setVisibility(0);
            FaceEnrollEducation.this.mIllustrationNormal.start();
            FaceEnrollEducation.this.mIllustrationAccessibility.setVisibility(4);
            FaceEnrollEducation.this.mDescriptionText.setVisibility(0);
        }
    };

    public int getMetricsCategory() {
        return 1506;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.face_enroll_education);
        getLayout().setHeaderText((int) R.string.security_settings_face_enroll_education_title);
        setTitle(R.string.security_settings_face_enroll_education_title);
        this.mHandler = new Handler();
        this.mFaceManager = Utils.getFaceManagerOrNull(this);
        this.mIllustrationNormal = (IllustrationVideoView) findViewById(R.id.illustration_normal);
        this.mIllustrationAccessibility = findViewById(R.id.illustration_accessibility);
        this.mDescriptionText = (TextView) findViewById(R.id.sud_layout_description);
        this.mFooterBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        if (WizardManagerHelper.isAnySetupWizard(getIntent())) {
            this.mFooterBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.skip_label).setListener(new FaceEnrollEducation$$ExternalSyntheticLambda2(this)).setButtonType(7).setTheme(2131952137).build());
        } else {
            this.mFooterBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.security_settings_face_enroll_introduction_cancel).setListener(new FaceEnrollEducation$$ExternalSyntheticLambda2(this)).setButtonType(2).setTheme(2131952137).build());
        }
        FooterButton build = new FooterButton.Builder(this).setText(R.string.security_settings_face_enroll_education_start).setListener(new FaceEnrollEducation$$ExternalSyntheticLambda1(this)).setButtonType(5).setTheme(2131952136).build();
        AccessibilityManager accessibilityManager = (AccessibilityManager) getApplicationContext().getSystemService(AccessibilityManager.class);
        if (accessibilityManager != null) {
            this.mAccessibilityEnabled = accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled();
        }
        this.mFooterBarMixin.setPrimaryButton(build);
        Button button = (Button) findViewById(R.id.accessibility_button);
        button.setOnClickListener(new FaceEnrollEducation$$ExternalSyntheticLambda4(this, button));
        FaceEnrollAccessibilityToggle faceEnrollAccessibilityToggle = (FaceEnrollAccessibilityToggle) findViewById(R.id.toggle_diversity);
        this.mSwitchDiversity = faceEnrollAccessibilityToggle;
        faceEnrollAccessibilityToggle.setListener(this.mSwitchDiversityListener);
        this.mSwitchDiversity.setOnClickListener(new FaceEnrollEducation$$ExternalSyntheticLambda3(this));
        if (this.mAccessibilityEnabled) {
            button.callOnClick();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(Button button, View view) {
        this.mSwitchDiversity.setChecked(true);
        button.setVisibility(8);
        this.mSwitchDiversity.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        this.mSwitchDiversity.getSwitch().toggle();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mSwitchDiversityListener.onCheckedChanged(this.mSwitchDiversity.getSwitch(), this.mSwitchDiversity.isChecked());
        if (this.mFaceManager.getEnrolledFaces(this.mUserId).size() >= getResources().getInteger(17694815)) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldFinishWhenBackgrounded() {
        return super.shouldFinishWhenBackgrounded() && !this.mNextClicked;
    }

    /* access modifiers changed from: protected */
    public void onNextButtonClick(View view) {
        Intent intent = new Intent();
        byte[] bArr = this.mToken;
        if (bArr != null) {
            intent.putExtra("hw_auth_token", bArr);
        }
        int i = this.mUserId;
        if (i != -10000) {
            intent.putExtra("android.intent.extra.USER_ID", i);
        }
        intent.putExtra("challenge", this.mChallenge);
        intent.putExtra("sensor_id", this.mSensorId);
        intent.putExtra("from_settings_summary", this.mFromSettingsSummary);
        BiometricUtils.copyMultiBiometricExtras(getIntent(), intent);
        String string = getString(R.string.config_face_enroll);
        if (!TextUtils.isEmpty(string)) {
            intent.setComponent(ComponentName.unflattenFromString(string));
        } else {
            intent.setClass(this, FaceEnrollEnrolling.class);
        }
        WizardManagerHelper.copyWizardManagerExtras(getIntent(), intent);
        Intent intent2 = this.mResultIntent;
        if (intent2 != null) {
            intent.putExtras(intent2);
        }
        intent.putExtra("accessibility_diversity", !this.mSwitchDiversity.isChecked());
        if (this.mSwitchDiversity.isChecked() || !this.mAccessibilityEnabled) {
            startActivityForResult(intent, 2);
            this.mNextClicked = true;
            return;
        }
        FaceEnrollAccessibilityDialog newInstance = FaceEnrollAccessibilityDialog.newInstance();
        newInstance.setPositiveButtonListener(new FaceEnrollEducation$$ExternalSyntheticLambda0(this, intent));
        newInstance.show(getSupportFragmentManager(), FaceEnrollAccessibilityDialog.class.getName());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNextButtonClick$2(Intent intent, DialogInterface dialogInterface, int i) {
        startActivityForResult(intent, 2);
        this.mNextClicked = true;
    }

    /* access modifiers changed from: protected */
    public void onSkipButtonClick(View view) {
        setResult(2);
        finish();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mResultIntent = intent;
        if (i != 2) {
            return;
        }
        if (i2 == 1 || i2 == 2 || i2 == 3) {
            setResult(i2, intent);
            finish();
        }
    }
}
