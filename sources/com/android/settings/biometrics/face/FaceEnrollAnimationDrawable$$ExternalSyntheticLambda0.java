package com.android.settings.biometrics.face;

import android.animation.TimeAnimator;

public final /* synthetic */ class FaceEnrollAnimationDrawable$$ExternalSyntheticLambda0 implements TimeAnimator.TimeListener {
    public final /* synthetic */ FaceEnrollAnimationDrawable f$0;

    public /* synthetic */ FaceEnrollAnimationDrawable$$ExternalSyntheticLambda0(FaceEnrollAnimationDrawable faceEnrollAnimationDrawable) {
        this.f$0 = faceEnrollAnimationDrawable;
    }

    public final void onTimeUpdate(TimeAnimator timeAnimator, long j, long j2) {
        this.f$0.lambda$onBoundsChange$0(timeAnimator, j, j2);
    }
}
