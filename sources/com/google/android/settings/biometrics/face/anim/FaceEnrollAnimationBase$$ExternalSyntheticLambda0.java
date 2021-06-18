package com.google.android.settings.biometrics.face.anim;

import android.animation.TimeAnimator;

public final /* synthetic */ class FaceEnrollAnimationBase$$ExternalSyntheticLambda0 implements TimeAnimator.TimeListener {
    public final /* synthetic */ FaceEnrollAnimationBase f$0;

    public /* synthetic */ FaceEnrollAnimationBase$$ExternalSyntheticLambda0(FaceEnrollAnimationBase faceEnrollAnimationBase) {
        this.f$0 = faceEnrollAnimationBase;
    }

    public final void onTimeUpdate(TimeAnimator timeAnimator, long j, long j2) {
        this.f$0.lambda$onBoundsChange$0(timeAnimator, j, j2);
    }
}
