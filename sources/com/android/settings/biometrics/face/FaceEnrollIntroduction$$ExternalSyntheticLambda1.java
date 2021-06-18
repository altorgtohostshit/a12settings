package com.android.settings.biometrics.face;

import android.hardware.face.FaceManager;

public final /* synthetic */ class FaceEnrollIntroduction$$ExternalSyntheticLambda1 implements FaceManager.GenerateChallengeCallback {
    public final /* synthetic */ FaceEnrollIntroduction f$0;

    public /* synthetic */ FaceEnrollIntroduction$$ExternalSyntheticLambda1(FaceEnrollIntroduction faceEnrollIntroduction) {
        this.f$0 = faceEnrollIntroduction;
    }

    public final void onGenerateChallengeResult(int i, int i2, long j) {
        this.f$0.lambda$onCreate$0(i, i2, j);
    }
}
