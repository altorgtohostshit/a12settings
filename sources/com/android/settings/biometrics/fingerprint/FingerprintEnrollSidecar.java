package com.android.settings.biometrics.fingerprint;

import android.app.Activity;
import android.hardware.fingerprint.FingerprintManager;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricEnrollSidecar;

public class FingerprintEnrollSidecar extends BiometricEnrollSidecar {
    private int mEnrollReason;
    private FingerprintManager.EnrollmentCallback mEnrollmentCallback = new FingerprintManager.EnrollmentCallback() {
        public void onEnrollmentProgress(int i) {
            FingerprintEnrollSidecar.super.onEnrollmentProgress(i);
        }

        public void onEnrollmentHelp(int i, CharSequence charSequence) {
            FingerprintEnrollSidecar.super.onEnrollmentHelp(i, charSequence);
        }

        public void onEnrollmentError(int i, CharSequence charSequence) {
            FingerprintEnrollSidecar.super.onEnrollmentError(i, charSequence);
        }
    };
    private FingerprintManager mFingerprintManager;

    public int getMetricsCategory() {
        return 245;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mFingerprintManager = Utils.getFingerprintManagerOrNull(activity);
    }

    /* access modifiers changed from: protected */
    public void startEnrollment() {
        super.startEnrollment();
        this.mFingerprintManager.enroll(this.mToken, this.mEnrollmentCancel, this.mUserId, this.mEnrollmentCallback, this.mEnrollReason);
    }

    public void setEnrollReason(int i) {
        this.mEnrollReason = i;
    }
}
