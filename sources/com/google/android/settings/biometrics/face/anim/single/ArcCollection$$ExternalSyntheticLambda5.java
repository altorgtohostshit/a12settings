package com.google.android.settings.biometrics.face.anim.single;

import android.animation.ValueAnimator;

public final /* synthetic */ class ArcCollection$$ExternalSyntheticLambda5 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ArcCollection f$0;

    public /* synthetic */ ArcCollection$$ExternalSyntheticLambda5(ArcCollection arcCollection) {
        this.f$0 = arcCollection;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$stopRotating$1(valueAnimator);
    }
}
