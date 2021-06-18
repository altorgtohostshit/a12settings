package com.google.android.settings.biometrics.face.anim.curve;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageView;
import com.android.settings.R;

public class DirectionIndicatorController {
    private static final AudioAttributes SONIFICATION_AUDIO_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    private Rect mBounds;
    private final Context mContext;
    private final ImageView mImageView;
    private final Paint mLargeAnglePaint;
    /* access modifiers changed from: private */
    public boolean mShouldRepeat;
    private ValueAnimator mStrokeAnimator;
    private final VibrationEffect mVibrationEffect = VibrationEffect.get(1);
    private final Vibrator mVibrator;

    public void draw(Canvas canvas) {
    }

    public DirectionIndicatorController(Context context, ImageView imageView) {
        this.mContext = context;
        this.mImageView = imageView;
        this.mVibrator = (Vibrator) context.getSystemService(Vibrator.class);
        Paint paint = new Paint();
        this.mLargeAnglePaint = paint;
        paint.setAntiAlias(true);
        paint.setColor(context.getColor(R.color.blue_500));
        paint.setStrokeWidth(0.0f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 20.0f, 0.0f});
        this.mStrokeAnimator = ofFloat;
        ofFloat.setDuration(1233);
        this.mStrokeAnimator.addUpdateListener(new DirectionIndicatorController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.mLargeAnglePaint.setStrokeWidth(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public void stopCurrentIndication() {
        this.mShouldRepeat = false;
    }

    public void pulseForNoActivity(int i, int i2) {
        pulseAnimation(i, i2, false);
    }

    private void pulseAnimation(int i, int i2, boolean z) {
        if (this.mBounds != null) {
            this.mShouldRepeat = true;
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) this.mImageView.getDrawable();
            if (animatedVectorDrawable == null || !animatedVectorDrawable.isRunning()) {
                AnimatedVectorDrawable animatedVectorDrawable2 = (AnimatedVectorDrawable) this.mContext.getDrawable(R.drawable.face_indicator_triangle);
                this.mImageView.setImageDrawable(animatedVectorDrawable2);
                double radians = Math.toRadians((double) i);
                int centerX = (int) (((double) (((float) this.mBounds.centerX()) + ((((float) this.mImageView.getMeasuredWidth()) * 0.15f) / 2.0f))) * Math.sin(radians));
                int centerY = (int) (((double) (((float) this.mBounds.centerY()) + ((((float) this.mImageView.getMeasuredWidth()) * 0.15f) / 2.0f))) * Math.cos(radians));
                this.mImageView.setScaleX(0.15f);
                this.mImageView.setScaleY(0.15f);
                if (z) {
                    this.mImageView.setRotation((float) (i - 180));
                } else {
                    this.mImageView.setRotation((float) i);
                }
                this.mImageView.setTranslationX((float) centerX);
                this.mImageView.setTranslationY((float) (-centerY));
                animatedVectorDrawable2.registerAnimationCallback(new Animatable2.AnimationCallback(i2, animatedVectorDrawable2) {
                    int curPulses = 1;
                    final int numPulses;
                    final /* synthetic */ AnimatedVectorDrawable val$animation;
                    final /* synthetic */ int val$times;

                    {
                        this.val$times = r2;
                        this.val$animation = r3;
                        this.numPulses = r2;
                    }

                    public void onAnimationEnd(Drawable drawable) {
                        super.onAnimationEnd(drawable);
                        if (DirectionIndicatorController.this.mShouldRepeat && this.curPulses < this.numPulses) {
                            this.val$animation.start();
                            this.curPulses++;
                        }
                    }
                });
                animatedVectorDrawable2.start();
            }
        }
    }

    public void onBoundsChange(Rect rect) {
        this.mBounds = rect;
    }
}
