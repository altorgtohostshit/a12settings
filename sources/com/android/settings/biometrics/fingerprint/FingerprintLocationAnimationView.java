package com.android.settings.biometrics.fingerprint;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.android.settings.R;
import com.android.settingslib.Utils;

public class FingerprintLocationAnimationView extends View implements FingerprintFindSensorAnimation {
    /* access modifiers changed from: private */
    public ValueAnimator mAlphaAnimator;
    private final Paint mDotPaint;
    private final int mDotRadius = getResources().getDimensionPixelSize(R.dimen.fingerprint_dot_radius);
    private final Interpolator mFastOutSlowInInterpolator;
    private final float mFractionCenterX = getResources().getFraction(R.fraction.fingerprint_sensor_location_fraction_x, 1, 1);
    private final float mFractionCenterY = getResources().getFraction(R.fraction.fingerprint_sensor_location_fraction_y, 1, 1);
    private final Interpolator mLinearOutSlowInInterpolator;
    private final int mMaxPulseRadius = getResources().getDimensionPixelSize(R.dimen.fingerprint_pulse_radius);
    /* access modifiers changed from: private */
    public final Paint mPulsePaint;
    /* access modifiers changed from: private */
    public float mPulseRadius;
    /* access modifiers changed from: private */
    public ValueAnimator mRadiusAnimator;
    /* access modifiers changed from: private */
    public final Runnable mStartPhaseRunnable = new Runnable() {
        public void run() {
            FingerprintLocationAnimationView.this.startPhase();
        }
    };

    public FingerprintLocationAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mDotPaint = paint;
        Paint paint2 = new Paint();
        this.mPulsePaint = paint2;
        int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(context);
        paint.setAntiAlias(true);
        paint2.setAntiAlias(true);
        paint.setColor(colorAccentDefaultColor);
        paint2.setColor(colorAccentDefaultColor);
        this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, 17563662);
        this.mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, 17563662);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        drawPulse(canvas);
        drawDot(canvas);
    }

    private void drawDot(Canvas canvas) {
        canvas.drawCircle(getCenterX(), getCenterY(), (float) this.mDotRadius, this.mDotPaint);
    }

    private void drawPulse(Canvas canvas) {
        canvas.drawCircle(getCenterX(), getCenterY(), this.mPulseRadius, this.mPulsePaint);
    }

    private float getCenterX() {
        return ((float) getWidth()) * this.mFractionCenterX;
    }

    private float getCenterY() {
        return ((float) getHeight()) * this.mFractionCenterY;
    }

    public void startAnimation() {
        startPhase();
    }

    public void stopAnimation() {
        removeCallbacks(this.mStartPhaseRunnable);
        ValueAnimator valueAnimator = this.mRadiusAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator valueAnimator2 = this.mAlphaAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.cancel();
        }
    }

    public void pauseAnimation() {
        stopAnimation();
    }

    /* access modifiers changed from: private */
    public void startPhase() {
        startRadiusAnimation();
        startAlphaAnimation();
    }

    private void startRadiusAnimation() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, (float) this.mMaxPulseRadius});
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float unused = FingerprintLocationAnimationView.this.mPulseRadius = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                FingerprintLocationAnimationView.this.invalidate();
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() {
            boolean mCancelled;

            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            public void onAnimationEnd(Animator animator) {
                ValueAnimator unused = FingerprintLocationAnimationView.this.mRadiusAnimator = null;
                if (!this.mCancelled) {
                    FingerprintLocationAnimationView fingerprintLocationAnimationView = FingerprintLocationAnimationView.this;
                    fingerprintLocationAnimationView.postDelayed(fingerprintLocationAnimationView.mStartPhaseRunnable, 1000);
                }
            }
        });
        ofFloat.setDuration(1000);
        ofFloat.setInterpolator(this.mLinearOutSlowInInterpolator);
        ofFloat.start();
        this.mRadiusAnimator = ofFloat;
    }

    private void startAlphaAnimation() {
        this.mPulsePaint.setAlpha(38);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.15f, 0.0f});
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FingerprintLocationAnimationView.this.mPulsePaint.setAlpha((int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 255.0f));
                FingerprintLocationAnimationView.this.invalidate();
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ValueAnimator unused = FingerprintLocationAnimationView.this.mAlphaAnimator = null;
            }
        });
        ofFloat.setDuration(750);
        ofFloat.setInterpolator(this.mFastOutSlowInInterpolator);
        ofFloat.setStartDelay(250);
        ofFloat.start();
        this.mAlphaAnimator = ofFloat;
    }
}
