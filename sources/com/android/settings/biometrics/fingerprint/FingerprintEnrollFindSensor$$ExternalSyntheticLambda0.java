package com.android.settings.biometrics.fingerprint;

import android.hardware.fingerprint.FingerprintManager;

public final /* synthetic */ class FingerprintEnrollFindSensor$$ExternalSyntheticLambda0 implements FingerprintManager.GenerateChallengeCallback {
    public final /* synthetic */ FingerprintEnrollFindSensor f$0;

    public /* synthetic */ FingerprintEnrollFindSensor$$ExternalSyntheticLambda0(FingerprintEnrollFindSensor fingerprintEnrollFindSensor) {
        this.f$0 = fingerprintEnrollFindSensor;
    }

    public final void onChallengeGenerated(int i, int i2, long j) {
        this.f$0.lambda$onCreate$0(i, i2, j);
    }
}
