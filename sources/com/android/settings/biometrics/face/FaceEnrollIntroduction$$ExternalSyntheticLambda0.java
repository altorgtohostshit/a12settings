package com.android.settings.biometrics.face;

import android.hardware.face.FaceManager;
import com.android.settings.biometrics.BiometricEnrollIntroduction;

public final /* synthetic */ class FaceEnrollIntroduction$$ExternalSyntheticLambda0 implements FaceManager.GenerateChallengeCallback {
    public final /* synthetic */ BiometricEnrollIntroduction.GenerateChallengeCallback f$0;

    public /* synthetic */ FaceEnrollIntroduction$$ExternalSyntheticLambda0(BiometricEnrollIntroduction.GenerateChallengeCallback generateChallengeCallback) {
        this.f$0 = generateChallengeCallback;
    }

    public final void onGenerateChallengeResult(int i, int i2, long j) {
        this.f$0.onChallengeGenerated(i, i2, j);
    }
}
