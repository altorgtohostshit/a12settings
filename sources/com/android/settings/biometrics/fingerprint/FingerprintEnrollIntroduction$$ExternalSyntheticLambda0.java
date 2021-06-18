package com.android.settings.biometrics.fingerprint;

import android.hardware.fingerprint.FingerprintManager;
import com.android.settings.biometrics.BiometricEnrollIntroduction;

public final /* synthetic */ class FingerprintEnrollIntroduction$$ExternalSyntheticLambda0 implements FingerprintManager.GenerateChallengeCallback {
    public final /* synthetic */ BiometricEnrollIntroduction.GenerateChallengeCallback f$0;

    public /* synthetic */ FingerprintEnrollIntroduction$$ExternalSyntheticLambda0(BiometricEnrollIntroduction.GenerateChallengeCallback generateChallengeCallback) {
        this.f$0 = generateChallengeCallback;
    }

    public final void onChallengeGenerated(int i, int i2, long j) {
        this.f$0.onChallengeGenerated(i, i2, j);
    }
}
