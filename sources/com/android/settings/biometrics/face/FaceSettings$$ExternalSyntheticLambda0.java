package com.android.settings.biometrics.face;

import android.content.Intent;
import android.hardware.face.FaceManager;

public final /* synthetic */ class FaceSettings$$ExternalSyntheticLambda0 implements FaceManager.GenerateChallengeCallback {
    public final /* synthetic */ FaceSettings f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ FaceSettings$$ExternalSyntheticLambda0(FaceSettings faceSettings, Intent intent) {
        this.f$0 = faceSettings;
        this.f$1 = intent;
    }

    public final void onGenerateChallengeResult(int i, int i2, long j) {
        this.f$0.lambda$onActivityResult$2(this.f$1, i, i2, j);
    }
}
