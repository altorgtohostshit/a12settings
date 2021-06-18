package com.google.android.settings.biometrics.face;

import android.content.Context;
import android.content.Intent;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricEnrollIntroduction;
import com.android.settings.biometrics.BiometricUtils;
import com.android.settings.biometrics.face.FaceEnrollIntroduction$$ExternalSyntheticLambda0;
import com.android.settings.biometrics.face.FaceFeatureProvider;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.span.LinkSpan;
import com.google.android.setupdesign.template.RequireScrollMixin;
import java.util.Objects;

public class TrafficLightFaceEnrollIntroduction extends BiometricEnrollIntroduction {
    private FaceFeatureProvider mFaceFeatureProvider;
    private FaceManager mFaceManager;

    /* access modifiers changed from: protected */
    public int getConfirmLockTitleResId() {
        return R.string.security_settings_face_preference_title;
    }

    /* access modifiers changed from: protected */
    public int getDescriptionResDisabledByAdmin() {
        return R.string.security_settings_face_enroll_introduction_message_unlock_disabled;
    }

    /* access modifiers changed from: protected */
    public String getExtraKeyForBiometric() {
        return "for_face";
    }

    /* access modifiers changed from: protected */
    public int getHeaderResDefault() {
        return R.string.security_settings_face_enroll_introduction_title;
    }

    /* access modifiers changed from: protected */
    public int getHeaderResDisabledByAdmin() {
        return R.string.security_settings_face_enroll_introduction_title_unlock_disabled;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResource() {
        return R.layout.traffic_light_face_enroll_introduction;
    }

    public int getMetricsCategory() {
        return 1506;
    }

    public void onClick(LinkSpan linkSpan) {
    }

    /* access modifiers changed from: protected */
    public void onSkipButtonClick(View view) {
        if (!BiometricUtils.tryStartingNextBiometricEnroll(this, 6)) {
            super.onSkipButtonClick(view);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getLayout().setDescriptionText((CharSequence) getString(R.string.security_settings_traffic_light_face_enroll_introduction_message));
        this.mFaceManager = Utils.getFaceManagerOrNull(this);
        this.mFaceFeatureProvider = FeatureFactory.getFactory(getApplicationContext()).getFaceFeatureProvider();
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.security_settings_face_enroll_introduction_no_thanks).setListener(new TrafficLightFaceEnrollIntroduction$$ExternalSyntheticLambda1(this)).setButtonType(7).setTheme(2131952137).build());
        FooterButton.Builder theme = new FooterButton.Builder(this).setText(R.string.security_settings_traffic_light_face_enroll_introduction_agree).setButtonType(5).setTheme(2131952136);
        if (maxFacesEnrolled()) {
            theme.setListener(new TrafficLightFaceEnrollIntroduction$$ExternalSyntheticLambda2(this));
            this.mFooterBarMixin.setPrimaryButton(theme.build());
        } else {
            FooterButton build = theme.build();
            this.mFooterBarMixin.setPrimaryButton(build);
            ((RequireScrollMixin) getLayout().getMixin(RequireScrollMixin.class)).requireScrollWithButton(this, build, R.string.security_settings_face_enroll_introduction_more, new TrafficLightFaceEnrollIntroduction$$ExternalSyntheticLambda2(this));
        }
        if (this.mToken == null && BiometricUtils.containsGatekeeperPasswordHandle(getIntent())) {
            this.mFooterBarMixin.getPrimaryButton().setEnabled(false);
            this.mFaceManager.generateChallenge(this.mUserId, new TrafficLightFaceEnrollIntroduction$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(int i, int i2, long j) {
        this.mToken = BiometricUtils.requestGatekeeperHat((Context) this, getIntent(), this.mUserId, j);
        this.mSensorId = i;
        this.mChallenge = j;
        if (BiometricUtils.isMultiBiometricEnrollmentFlow(this)) {
            BiometricUtils.removeGatekeeperPasswordHandle((Context) this, getIntent());
        }
        this.mFooterBarMixin.getPrimaryButton().setEnabled(true);
    }

    /* access modifiers changed from: protected */
    public boolean isDisabledByAdmin() {
        return RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(this, 128, this.mUserId) != null;
    }

    /* access modifiers changed from: protected */
    public FooterButton getNextButton() {
        FooterBarMixin footerBarMixin = this.mFooterBarMixin;
        if (footerBarMixin != null) {
            return footerBarMixin.getPrimaryButton();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public TextView getErrorTextView() {
        return (TextView) findViewById(R.id.error_text);
    }

    private boolean maxFacesEnrolled() {
        FaceManager faceManager = this.mFaceManager;
        if (faceManager == null || this.mFaceManager.getEnrolledFaces(this.mUserId).size() < ((FaceSensorPropertiesInternal) faceManager.getSensorPropertiesInternal().get(0)).maxEnrollmentsPerUser) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public int checkMaxEnrolled() {
        if (this.mFaceManager == null) {
            return R.string.face_intro_error_unknown;
        }
        if (maxFacesEnrolled()) {
            return R.string.face_intro_error_max;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void getChallenge(BiometricEnrollIntroduction.GenerateChallengeCallback generateChallengeCallback) {
        FaceManager faceManagerOrNull = Utils.getFaceManagerOrNull(this);
        this.mFaceManager = faceManagerOrNull;
        if (faceManagerOrNull == null) {
            generateChallengeCallback.onChallengeGenerated(0, 0, 0);
            return;
        }
        int i = this.mUserId;
        Objects.requireNonNull(generateChallengeCallback);
        faceManagerOrNull.generateChallenge(i, new FaceEnrollIntroduction$$ExternalSyntheticLambda0(generateChallengeCallback));
    }

    /* access modifiers changed from: protected */
    public Intent getEnrollingIntent() {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.biometrics.face.FaceEnrollEducation");
        WizardManagerHelper.copyWizardManagerExtras(getIntent(), intent);
        return intent;
    }
}
