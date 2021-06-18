package com.android.settings.biometrics.face;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import com.android.settings.R;
import java.util.List;

public class AnimationParticle {
    private int mAnimationState;
    private final int mAssignedColor;
    private final int mBorderWidth;
    private final Rect mBounds;
    private float mCurrentAngle;
    private float mCurrentSize = 10.0f;
    private final int mErrorColor;
    private final ArgbEvaluator mEvaluator;
    private final int mIndex;
    private int mLastAnimationState;
    private final Listener mListener;
    private final float mOffsetTimeSec;
    private final Paint mPaint;
    private float mRingAdjustRate;
    private float mRingCompletionTime;
    private float mRotationSpeed = 0.8f;
    private float mSweepAngle = 0.0f;
    private float mSweepRate = 240.0f;

    public interface Listener {
        void onRingCompleted(int i);
    }

    public AnimationParticle(Context context, Listener listener, Rect rect, int i, int i2, int i3, List<Integer> list) {
        this.mBounds = rect;
        this.mBorderWidth = i;
        this.mEvaluator = new ArgbEvaluator();
        this.mErrorColor = context.getResources().getColor(R.color.face_anim_particle_error, context.getTheme());
        this.mIndex = i2;
        this.mListener = listener;
        float f = ((float) i2) / ((float) i3);
        this.mCurrentAngle = f * 2.0f * 3.1415927f;
        this.mOffsetTimeSec = f * 1.25f * 2.0f * 3.1415927f;
        Paint paint = new Paint();
        this.mPaint = paint;
        int intValue = list.get(i2 % list.size()).intValue();
        this.mAssignedColor = intValue;
        paint.setColor(intValue);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(this.mCurrentSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void updateState(int i) {
        if (this.mAnimationState == i) {
            Log.w("AnimationParticle", "Already in state " + i);
            return;
        }
        if (i == 4) {
            this.mPaint.setStyle(Paint.Style.STROKE);
        }
        this.mLastAnimationState = this.mAnimationState;
        this.mAnimationState = i;
    }

    public void setAsPrimary() {
        this.mSweepRate = 480.0f;
    }

    public void update(long j, long j2) {
        if (this.mAnimationState != 4) {
            updateDot(j, j2);
        } else {
            updateRing(j, j2);
        }
    }

    private void updateDot(long j, long j2) {
        float f = ((float) j2) * 0.001f;
        float f2 = ((float) j) * 0.001f;
        float f3 = this.mRotationSpeed;
        float f4 = f3 / 0.8f;
        int i = this.mAnimationState;
        if ((i == 2 || i == 3) && f3 > 0.0f) {
            this.mRotationSpeed = Math.max(f3 - (2.0f * f), 0.0f);
        } else if (i == 1 && f3 < 0.8f) {
            this.mRotationSpeed = f3 + (2.0f * f);
        }
        this.mCurrentAngle += f * this.mRotationSpeed;
        float sin = (((float) Math.sin((double) ((f2 * 6.2831855f) + this.mOffsetTimeSec))) * 5.0f) + 15.0f;
        this.mCurrentSize = sin;
        this.mCurrentSize = ((sin - 10.0f) * f4) + 10.0f;
        int i2 = this.mAssignedColor;
        if (this.mAnimationState == 3) {
            i2 = ((Integer) this.mEvaluator.evaluate(1.0f - f4, Integer.valueOf(i2), Integer.valueOf(this.mErrorColor))).intValue();
        } else if (this.mLastAnimationState == 3) {
            i2 = ((Integer) this.mEvaluator.evaluate(1.0f - f4, Integer.valueOf(i2), Integer.valueOf(this.mErrorColor))).intValue();
        }
        this.mPaint.setColor(i2);
        this.mPaint.setStrokeWidth(this.mCurrentSize);
    }

    private void updateRing(long j, long j2) {
        float f = ((float) j2) * 0.001f;
        float f2 = ((float) j) * 0.001f;
        if (this.mRingAdjustRate == 0.0f) {
            this.mRingAdjustRate = (15.0f - this.mCurrentSize) / 0.1f;
            if (this.mRingCompletionTime == 0.0f) {
                this.mRingCompletionTime = f2 + 0.1f;
            }
        }
        float f3 = this.mRotationSpeed;
        if (f3 < 0.8f) {
            this.mRotationSpeed = f3 + (2.0f * f);
        }
        this.mCurrentAngle += this.mRotationSpeed * f;
        float f4 = this.mSweepAngle;
        if (f4 < 360.0f) {
            float f5 = this.mSweepRate;
            float f6 = f5 * f;
            this.mSweepAngle = f4 + f6;
            this.mSweepRate = f5 + f6;
        }
        if (this.mSweepAngle > 360.0f) {
            this.mSweepAngle = 360.0f;
            this.mListener.onRingCompleted(this.mIndex);
        }
        if (f2 < 0.1f) {
            float f7 = this.mCurrentSize + (this.mRingAdjustRate * f);
            this.mCurrentSize = f7;
            this.mPaint.setStrokeWidth(f7);
            return;
        }
        this.mCurrentSize = 15.0f;
        this.mPaint.setStrokeWidth(15.0f);
    }

    public void draw(Canvas canvas) {
        if (this.mAnimationState != 4) {
            drawDot(canvas);
        } else {
            drawRing(canvas);
        }
    }

    private void drawDot(Canvas canvas) {
        Rect rect = this.mBounds;
        float exactCenterX = (((float) rect.right) - rect.exactCenterX()) - ((float) this.mBorderWidth);
        Rect rect2 = this.mBounds;
        canvas.drawCircle(this.mBounds.exactCenterX() + (exactCenterX * ((float) Math.cos((double) this.mCurrentAngle))), this.mBounds.exactCenterY() + (((((float) rect2.bottom) - rect2.exactCenterY()) - ((float) this.mBorderWidth)) * ((float) Math.sin((double) this.mCurrentAngle))), this.mCurrentSize, this.mPaint);
    }

    private void drawRing(Canvas canvas) {
        int i = this.mBorderWidth;
        RectF rectF = new RectF((float) i, (float) i, (float) (this.mBounds.width() - this.mBorderWidth), (float) (this.mBounds.height() - this.mBorderWidth));
        Path path = new Path();
        path.arcTo(rectF, (float) Math.toDegrees((double) this.mCurrentAngle), this.mSweepAngle);
        canvas.drawPath(path, this.mPaint);
    }
}
