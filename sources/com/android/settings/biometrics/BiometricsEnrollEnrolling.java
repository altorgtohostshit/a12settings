package com.android.settings.biometrics;

import android.content.Intent;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.biometrics.BiometricEnrollSidecar;
import com.google.android.setupcompat.util.WizardManagerHelper;

public abstract class BiometricsEnrollEnrolling extends BiometricEnrollBase implements BiometricEnrollSidecar.Listener {
    protected BiometricEnrollSidecar mSidecar;

    /* access modifiers changed from: protected */
    public abstract Intent getFinishIntent();

    /* access modifiers changed from: protected */
    public abstract BiometricEnrollSidecar getSidecar();

    /* access modifiers changed from: protected */
    public boolean shouldFinishWhenBackgrounded() {
        return false;
    }

    /* access modifiers changed from: protected */
    public abstract boolean shouldStartAutomatically();

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        if (shouldStartAutomatically()) {
            startEnrollment();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        if (biometricEnrollSidecar != null) {
            biometricEnrollSidecar.setListener((BiometricEnrollSidecar.Listener) null);
        }
        if (!isChangingConfigurations()) {
            BiometricEnrollSidecar biometricEnrollSidecar2 = this.mSidecar;
            if (biometricEnrollSidecar2 != null) {
                biometricEnrollSidecar2.cancelEnrollment();
                getSupportFragmentManager().beginTransaction().remove(this.mSidecar).commitAllowingStateLoss();
            }
            if (!WizardManagerHelper.isAnySetupWizard(getIntent())) {
                setResult(3);
            }
            finish();
        }
    }

    public void onBackPressed() {
        cancelEnrollment();
        super.onBackPressed();
    }

    /* access modifiers changed from: protected */
    public void onSkipButtonClick(View view) {
        cancelEnrollment();
        setResult(2);
        finish();
    }

    public void cancelEnrollment() {
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        if (biometricEnrollSidecar != null) {
            biometricEnrollSidecar.setListener((BiometricEnrollSidecar.Listener) null);
            this.mSidecar.cancelEnrollment();
            getSupportFragmentManager().beginTransaction().remove(this.mSidecar).commitAllowingStateLoss();
            this.mSidecar = null;
        }
    }

    public void startEnrollment() {
        BiometricEnrollSidecar biometricEnrollSidecar = (BiometricEnrollSidecar) getSupportFragmentManager().findFragmentByTag("sidecar");
        this.mSidecar = biometricEnrollSidecar;
        if (biometricEnrollSidecar == null) {
            this.mSidecar = getSidecar();
            getSupportFragmentManager().beginTransaction().add((Fragment) this.mSidecar, "sidecar").commitAllowingStateLoss();
        }
        this.mSidecar.setListener(this);
    }

    /* access modifiers changed from: protected */
    public void launchFinish(byte[] bArr) {
        Intent finishIntent = getFinishIntent();
        finishIntent.addFlags(637534208);
        finishIntent.putExtra("hw_auth_token", bArr);
        finishIntent.putExtra("sensor_id", this.mSensorId);
        finishIntent.putExtra("challenge", this.mChallenge);
        finishIntent.putExtra("from_settings_summary", this.mFromSettingsSummary);
        int i = this.mUserId;
        if (i != -10000) {
            finishIntent.putExtra("android.intent.extra.USER_ID", i);
        }
        startActivity(finishIntent);
        overridePendingTransition(R.anim.sud_slide_next_in, R.anim.sud_slide_next_out);
        finish();
    }
}
