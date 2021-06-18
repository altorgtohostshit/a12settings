package com.android.settings.biometrics;

import android.hardware.face.FaceManager;

public final /* synthetic */ class MultiBiometricEnrollHelper$$ExternalSyntheticLambda0 implements FaceManager.GenerateChallengeCallback {
    public final /* synthetic */ MultiBiometricEnrollHelper f$0;

    public /* synthetic */ MultiBiometricEnrollHelper$$ExternalSyntheticLambda0(MultiBiometricEnrollHelper multiBiometricEnrollHelper) {
        this.f$0 = multiBiometricEnrollHelper;
    }

    public final void onGenerateChallengeResult(int i, int i2, long j) {
        this.f$0.lambda$launchFaceEnroll$0(i, i2, j);
    }
}
