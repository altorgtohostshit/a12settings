package com.android.settings.biometrics;

import android.hardware.fingerprint.FingerprintManager;

public final /* synthetic */ class MultiBiometricEnrollHelper$$ExternalSyntheticLambda1 implements FingerprintManager.GenerateChallengeCallback {
    public final /* synthetic */ MultiBiometricEnrollHelper f$0;

    public /* synthetic */ MultiBiometricEnrollHelper$$ExternalSyntheticLambda1(MultiBiometricEnrollHelper multiBiometricEnrollHelper) {
        this.f$0 = multiBiometricEnrollHelper;
    }

    public final void onChallengeGenerated(int i, int i2, long j) {
        this.f$0.lambda$launchFingerprintEnroll$1(i, i2, j);
    }
}
