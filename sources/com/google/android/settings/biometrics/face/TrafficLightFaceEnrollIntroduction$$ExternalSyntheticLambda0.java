package com.google.android.settings.biometrics.face;

import android.hardware.face.FaceManager;

public final /* synthetic */ class TrafficLightFaceEnrollIntroduction$$ExternalSyntheticLambda0 implements FaceManager.GenerateChallengeCallback {
    public final /* synthetic */ TrafficLightFaceEnrollIntroduction f$0;

    public /* synthetic */ TrafficLightFaceEnrollIntroduction$$ExternalSyntheticLambda0(TrafficLightFaceEnrollIntroduction trafficLightFaceEnrollIntroduction) {
        this.f$0 = trafficLightFaceEnrollIntroduction;
    }

    public final void onGenerateChallengeResult(int i, int i2, long j) {
        this.f$0.lambda$onCreate$0(i, i2, j);
    }
}
