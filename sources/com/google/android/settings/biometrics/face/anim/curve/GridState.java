package com.google.android.settings.biometrics.face.anim.curve;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import com.android.settings.R;

public class GridState {
    private ValueAnimator mAnimator;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new GridState$$ExternalSyntheticLambda0(this);
    private final Paint mEdgePaint;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    /* access modifiers changed from: private */
    public int mState = 0;

    public GridState(Context context, Handler handler) {
        this.mHandler = handler;
        Paint paint = new Paint();
        this.mEdgePaint = paint;
        paint.setColor(context.getColor(R.color.face_enroll_grid));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3.0f);
        paint.setAlpha(0);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.mEdgePaint.setAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public void fadeIn() {
        if (this.mState != 1) {
            this.mState = 2;
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.mEdgePaint.getAlpha(), 64});
            this.mAnimator = ofInt;
            ofInt.removeAllUpdateListeners();
            this.mAnimator.addUpdateListener(this.mAnimatorUpdateListener);
            this.mAnimator.removeAllListeners();
            this.mAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    int unused = GridState.this.mState = 1;
                }
            });
            this.mAnimator.start();
        }
    }

    public void fadeOut(final Runnable runnable) {
        if (this.mState == 0) {
            this.mHandler.post(runnable);
            return;
        }
        this.mState = 2;
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.mEdgePaint.getAlpha(), 0});
        this.mAnimator = ofInt;
        ofInt.addUpdateListener(this.mAnimatorUpdateListener);
        this.mAnimator.removeAllListeners();
        this.mAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                int unused = GridState.this.mState = 0;
                GridState.this.mHandler.post(runnable);
            }
        });
        this.mAnimator.start();
    }

    public void draw(Canvas canvas) {
        int width = canvas.getWidth() / 2;
        int height = canvas.getHeight() / 2;
        float f = (float) width;
        canvas.drawCircle(0.0f, 0.0f, f - (this.mEdgePaint.getStrokeWidth() / 2.0f), this.mEdgePaint);
        float width2 = ((float) canvas.getWidth()) * 0.32f;
        float width3 = ((float) canvas.getWidth()) * 0.78f;
        float f2 = (-width2) / 2.0f;
        float f3 = (float) (-height);
        float f4 = width2 / 2.0f;
        float f5 = (float) height;
        Canvas canvas2 = canvas;
        canvas2.drawArc(new RectF(f2, f3, f4, f5), 0.0f, 360.0f, false, this.mEdgePaint);
        float f6 = (float) (-width);
        canvas2.drawArc(new RectF(f6, f2, f, f4), 0.0f, 360.0f, false, this.mEdgePaint);
        float f7 = (-width3) / 2.0f;
        float f8 = width3 / 2.0f;
        canvas2.drawArc(new RectF(f7, f3, f8, f5), 0.0f, 360.0f, false, this.mEdgePaint);
        canvas2.drawArc(new RectF(f6, f7, f, f8), 0.0f, 360.0f, false, this.mEdgePaint);
    }
}
