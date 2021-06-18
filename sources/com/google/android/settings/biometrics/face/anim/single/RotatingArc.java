package com.google.android.settings.biometrics.face.anim.single;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;

public class RotatingArc {
    private float mAngle;
    private ValueAnimator mColorAnimator;
    private final int[] mColors;
    private final int mIndex;
    private final Paint mPaint;
    private float mRotateSpeed;
    private float mSweepAngle;

    public RotatingArc(int i, int i2, int[] iArr) {
        this.mIndex = i;
        this.mColors = iArr;
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(20.0f);
        paint.setColor(getColorForIndex(i));
        this.mAngle = (float) ((360 / i2) * i);
    }

    public void setSweepAngle(float f) {
        this.mSweepAngle = f;
    }

    public void setRotateSpeed(float f) {
        this.mRotateSpeed = f;
    }

    public int getColorForIndex(int i) {
        int[] iArr = this.mColors;
        return iArr[i % iArr.length];
    }

    public void update(long j, long j2) {
        this.mAngle += (this.mRotateSpeed * ((float) j2)) / 1000.0f;
    }

    public void draw(Canvas canvas) {
        float width = ((float) (canvas.getWidth() / 2)) - (this.mPaint.getStrokeWidth() / 2.0f);
        canvas.drawArc(((float) (canvas.getWidth() / 2)) - width, ((float) (canvas.getHeight() / 2)) - width, ((float) (canvas.getWidth() / 2)) + width, ((float) (canvas.getWidth() / 2)) + width, this.mAngle, this.mSweepAngle, false, this.mPaint);
    }

    public void stopCurrentAnimation() {
        ValueAnimator valueAnimator = this.mColorAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public void stopRotating(long j) {
        ValueAnimator ofArgb = ValueAnimator.ofArgb(new int[]{this.mPaint.getColor(), 0});
        this.mColorAnimator = ofArgb;
        ofArgb.setDuration(j);
        this.mColorAnimator.addUpdateListener(new RotatingArc$$ExternalSyntheticLambda0(this));
        this.mColorAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$stopRotating$0(ValueAnimator valueAnimator) {
        this.mPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public void startRotating(long j) {
        ValueAnimator ofArgb = ValueAnimator.ofArgb(new int[]{this.mPaint.getColor(), getColorForIndex(this.mIndex)});
        this.mColorAnimator = ofArgb;
        ofArgb.setDuration(j);
        this.mColorAnimator.addUpdateListener(new RotatingArc$$ExternalSyntheticLambda1(this));
        this.mColorAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startRotating$1(ValueAnimator valueAnimator) {
        this.mPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public void startFinishing(long j) {
        startRotating(j);
    }
}
