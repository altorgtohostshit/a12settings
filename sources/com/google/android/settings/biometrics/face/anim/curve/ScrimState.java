package com.google.android.settings.biometrics.face.anim.curve;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ScrimState {
    private final int mGoneColor;
    private final Paint mPaint;
    private ValueAnimator mScrimAnimator;
    private ValueAnimator.AnimatorUpdateListener mScrimAnimatorListener;
    private final int mShowingColor;
    /* access modifiers changed from: private */
    public int mState = 0;

    public ScrimState(int i, int i2) {
        this.mGoneColor = i;
        this.mShowingColor = i2;
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setColor(i);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        this.mScrimAnimatorListener = new ScrimState$$ExternalSyntheticLambda0(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.mPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public boolean isShowing() {
        return this.mState != 0;
    }

    public void fadeOut() {
        int i = this.mState;
        if (i != 0 && i != 2) {
            this.mState = 2;
            ValueAnimator ofArgb = ValueAnimator.ofArgb(new int[]{this.mPaint.getColor(), this.mGoneColor});
            this.mScrimAnimator = ofArgb;
            ofArgb.addUpdateListener(this.mScrimAnimatorListener);
            this.mScrimAnimator.setDuration(200);
            this.mScrimAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    int unused = ScrimState.this.mState = 0;
                }
            });
            this.mScrimAnimator.start();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(0.0f, 0.0f, (float) (canvas.getWidth() / 2), this.mPaint);
    }
}
