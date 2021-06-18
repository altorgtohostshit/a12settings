package com.android.settings.biometrics.fingerprint;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricEnrollBase;
import com.android.settings.biometrics.BiometricEnrollSidecar;
import com.android.settings.biometrics.BiometricUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import java.util.List;

public class FingerprintEnrollFindSensor extends BiometricEnrollBase implements BiometricEnrollSidecar.Listener {
    private FingerprintFindSensorAnimation mAnimation;
    private boolean mCanAssumeUdfps;
    private boolean mNextClicked;
    private FingerprintEnrollSidecar mSidecar;

    public int getMetricsCategory() {
        return 241;
    }

    public void onEnrollmentHelp(int i, CharSequence charSequence) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        List sensorPropertiesInternal = ((FingerprintManager) getSystemService(FingerprintManager.class)).getSensorPropertiesInternal();
        boolean z = true;
        if (sensorPropertiesInternal == null || sensorPropertiesInternal.size() != 1 || !((FingerprintSensorPropertiesInternal) sensorPropertiesInternal.get(0)).isAnyUdfpsType()) {
            z = false;
        }
        this.mCanAssumeUdfps = z;
        setContentView(getContentView());
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.skip_label).setListener(new FingerprintEnrollFindSensor$$ExternalSyntheticLambda1(this)).setButtonType(7).setTheme(2131952137).build());
        if (this.mCanAssumeUdfps) {
            setHeaderText((int) R.string.security_settings_udfps_enroll_find_sensor_title);
            setDescriptionText((int) R.string.security_settings_udfps_enroll_find_sensor_message);
        } else {
            setHeaderText((int) R.string.security_settings_fingerprint_enroll_find_sensor_title);
            setDescriptionText((int) R.string.security_settings_fingerprint_enroll_find_sensor_message);
        }
        if (this.mToken == null && BiometricUtils.containsGatekeeperPasswordHandle(getIntent())) {
            ((FingerprintManager) getSystemService(FingerprintManager.class)).generateChallenge(this.mUserId, new FingerprintEnrollFindSensor$$ExternalSyntheticLambda0(this));
        } else if (this.mToken != null) {
            startLookingForFingerprint();
        } else {
            throw new IllegalStateException("HAT and GkPwHandle both missing...");
        }
        this.mAnimation = null;
        if (!this.mCanAssumeUdfps) {
            View findViewById = findViewById(R.id.fingerprint_sensor_location_animation);
            if (findViewById instanceof FingerprintFindSensorAnimation) {
                this.mAnimation = (FingerprintFindSensorAnimation) findViewById;
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(int i, int i2, long j) {
        this.mChallenge = j;
        this.mSensorId = i;
        this.mToken = BiometricUtils.requestGatekeeperHat((Context) this, getIntent(), this.mUserId, j);
        getIntent().putExtra("hw_auth_token", this.mToken);
        startLookingForFingerprint();
    }

    public void onBackPressed() {
        stopLookingForFingerprint();
        super.onBackPressed();
    }

    /* access modifiers changed from: protected */
    public int getContentView() {
        if (this.mCanAssumeUdfps) {
            return BiometricUtils.isReverseLandscape(getApplicationContext()) ? R.layout.udfps_enroll_find_sensor_land : R.layout.udfps_enroll_find_sensor_layout;
        }
        return R.layout.fingerprint_enroll_find_sensor;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        FingerprintFindSensorAnimation fingerprintFindSensorAnimation = this.mAnimation;
        if (fingerprintFindSensorAnimation != null) {
            fingerprintFindSensorAnimation.startAnimation();
        }
    }

    private void stopLookingForFingerprint() {
        FingerprintEnrollSidecar fingerprintEnrollSidecar = this.mSidecar;
        if (fingerprintEnrollSidecar != null) {
            fingerprintEnrollSidecar.setListener((BiometricEnrollSidecar.Listener) null);
            this.mSidecar.cancelEnrollment();
            getSupportFragmentManager().beginTransaction().remove(this.mSidecar).commitAllowingStateLoss();
            this.mSidecar = null;
        }
    }

    private void startLookingForFingerprint() {
        FingerprintEnrollSidecar fingerprintEnrollSidecar = (FingerprintEnrollSidecar) getSupportFragmentManager().findFragmentByTag("sidecar");
        this.mSidecar = fingerprintEnrollSidecar;
        if (fingerprintEnrollSidecar == null) {
            FingerprintEnrollSidecar fingerprintEnrollSidecar2 = new FingerprintEnrollSidecar();
            this.mSidecar = fingerprintEnrollSidecar2;
            fingerprintEnrollSidecar2.setEnrollReason(1);
            getSupportFragmentManager().beginTransaction().add((Fragment) this.mSidecar, "sidecar").commitAllowingStateLoss();
        }
        this.mSidecar.setListener(this);
    }

    public void onEnrollmentProgressChange(int i, int i2) {
        this.mNextClicked = true;
        proceedToEnrolling(true);
    }

    public void onEnrollmentError(int i, CharSequence charSequence) {
        if (!this.mNextClicked || i != 5) {
            FingerprintErrorDialog.showErrorDialog(this, i);
            return;
        }
        this.mNextClicked = false;
        proceedToEnrolling(false);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        FingerprintFindSensorAnimation fingerprintFindSensorAnimation = this.mAnimation;
        if (fingerprintFindSensorAnimation != null) {
            fingerprintFindSensorAnimation.pauseAnimation();
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldFinishWhenBackgrounded() {
        return super.shouldFinishWhenBackgrounded() && !this.mNextClicked;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        FingerprintFindSensorAnimation fingerprintFindSensorAnimation = this.mAnimation;
        if (fingerprintFindSensorAnimation != null) {
            fingerprintFindSensorAnimation.stopAnimation();
        }
    }

    /* access modifiers changed from: protected */
    public void onSkipButtonClick(View view) {
        stopLookingForFingerprint();
        setResult(2);
        finish();
    }

    private void proceedToEnrolling(boolean z) {
        FingerprintEnrollSidecar fingerprintEnrollSidecar = this.mSidecar;
        if (fingerprintEnrollSidecar == null) {
            return;
        }
        if (!z || !fingerprintEnrollSidecar.cancelEnrollment()) {
            getSupportFragmentManager().beginTransaction().remove(this.mSidecar).commitAllowingStateLoss();
            this.mSidecar = null;
            startActivityForResult(getFingerprintEnrollingIntent(), 5);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 4) {
            if (i2 != -1 || intent == null) {
                finish();
                return;
            }
            throw new IllegalStateException("Pretty sure this is dead code");
        } else if (i != 5) {
            super.onActivityResult(i, i2, intent);
        } else if (i2 == 1 || i2 == 2 || i2 == 3) {
            setResult(i2);
            finish();
        } else if (Utils.getFingerprintManagerOrNull(this).getEnrolledFingerprints().size() >= getResources().getInteger(17694816)) {
            finish();
        } else {
            startLookingForFingerprint();
        }
    }
}
