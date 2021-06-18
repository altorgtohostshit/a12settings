package com.google.android.settings.biometrics.face.anim.curve;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.android.settings.R;
import com.google.android.settings.biometrics.face.anim.FaceEnrollAnimationMultiAngleDrawable;

public class CellState {
    private final boolean mAlternateCursor;
    /* access modifiers changed from: private */
    public final FaceEnrollAnimationMultiAngleDrawable.BucketListener mBucketListener;
    private CellConfig mCellConfig;
    private ValueAnimator mCursorAnimator;
    private ValueAnimator.AnimatorUpdateListener mCursorAnimatorListener;
    private final int mCursorColorAcquired;
    private final int mCursorColorGone;
    private Paint mCursorEdgePaint;
    /* access modifiers changed from: private */
    public int mCursorState;
    private final boolean mDisableCursor;
    private boolean mDone;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                CellState.this.handleFadeCursor();
            }
        }
    };
    /* access modifiers changed from: private */
    public final int mIndex;
    /* access modifiers changed from: private */
    public ValueAnimator mNoActivityAnimator;
    private ValueAnimator.AnimatorUpdateListener mNoActivityAnimatorListener;
    private Paint mNoActivityPaint;
    /* access modifiers changed from: private */
    public boolean mNoActivityPulseShouldRepeat;
    /* access modifiers changed from: private */
    public int mScrimAnimationState;
    private ValueAnimator mScrimAnimator;
    private ValueAnimator.AnimatorUpdateListener mScrimAnimatorListener;
    private final int mScrimColorEnrolled;
    private final int mScrimColorNoActivityEnd;
    private final int mScrimColorNoActivityStart;
    private int mScrimColorNotEnrolled;
    private Paint mScrimPaint;

    public CellState(Context context, int i, FaceEnrollAnimationMultiAngleDrawable.BucketListener bucketListener, int i2) {
        int i3;
        this.mIndex = i;
        this.mBucketListener = bucketListener;
        this.mScrimColorNotEnrolled = i2;
        this.mScrimColorEnrolled = context.getColor(R.color.face_enroll_cell_enrolled);
        this.mCursorColorAcquired = context.getColor(R.color.face_enroll_cursor_acquired);
        int color = context.getColor(R.color.face_enroll_cursor_gone);
        this.mCursorColorGone = color;
        int color2 = context.getColor(R.color.face_enroll_cell_no_activity_start);
        this.mScrimColorNoActivityStart = color2;
        this.mScrimColorNoActivityEnd = context.getColor(R.color.face_enroll_cell_no_activity_end);
        boolean z = false;
        this.mScrimAnimationState = 0;
        this.mCursorState = 0;
        Paint paint = new Paint();
        this.mScrimPaint = paint;
        paint.setAntiAlias(true);
        this.mScrimPaint.setAlpha(0);
        boolean z2 = Settings.Secure.getInt(context.getContentResolver(), "com.google.android.settings.future.biometrics.face.anim.curve.alternate_cursor", 0) != 0;
        this.mAlternateCursor = z2;
        this.mDisableCursor = Settings.Secure.getInt(context.getContentResolver(), "com.google.android.settings.future.biometrics.face.anim.curve.disable_cursor", 0) != 0 ? true : z;
        if (z2) {
            i3 = -65536;
        } else {
            i3 = context.getColor(R.color.face_enroll_cursor_shadow);
        }
        Paint paint2 = new Paint();
        this.mCursorEdgePaint = paint2;
        paint2.setColor(color);
        this.mCursorEdgePaint.setAntiAlias(true);
        this.mCursorEdgePaint.setShadowLayer(6.0f, 0.0f, 0.0f, i3);
        this.mCursorEdgePaint.setStrokeCap(Paint.Cap.ROUND);
        this.mCursorEdgePaint.setStyle(Paint.Style.STROKE);
        this.mCursorEdgePaint.setStrokeWidth(12.0f);
        Paint paint3 = new Paint();
        this.mNoActivityPaint = paint3;
        paint3.setAntiAlias(true);
        this.mNoActivityPaint.setColor(color2);
        this.mScrimAnimatorListener = new CellState$$ExternalSyntheticLambda0(this);
        this.mCursorAnimatorListener = new CellState$$ExternalSyntheticLambda1(this);
        this.mNoActivityAnimatorListener = new CellState$$ExternalSyntheticLambda2(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.mScrimPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(ValueAnimator valueAnimator) {
        this.mCursorEdgePaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(ValueAnimator valueAnimator) {
        this.mNoActivityPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public void updateConfig(CellConfig cellConfig) {
        this.mCellConfig = cellConfig;
    }

    public void draw(Canvas canvas) {
        canvas.save();
        CellConfig cellConfig = this.mCellConfig;
        if (cellConfig != null) {
            if (cellConfig.mFlipVertical) {
                canvas.scale(1.0f, -1.0f, 0.0f, 0.0f);
            }
            canvas.rotate((float) this.mCellConfig.mRotation);
            canvas.drawPath(this.mCellConfig.mPath, this.mScrimPaint);
            canvas.drawPath(this.mCellConfig.mPath, this.mNoActivityPaint);
            canvas.restore();
        }
    }

    public void drawCursor(Canvas canvas) {
        canvas.save();
        CellConfig cellConfig = this.mCellConfig;
        if (cellConfig != null) {
            if (cellConfig.mFlipVertical) {
                canvas.scale(1.0f, -1.0f, 0.0f, 0.0f);
            }
            canvas.rotate((float) this.mCellConfig.mRotation);
            if (!this.mDisableCursor) {
                canvas.drawPath(this.mCellConfig.mPath, this.mCursorEdgePaint);
            }
            canvas.restore();
        }
    }

    public boolean isDone() {
        return this.mDone;
    }

    public void setEarlyDone() {
        this.mDone = true;
    }

    public void stopPulseForNoActivity() {
        this.mNoActivityPulseShouldRepeat = false;
    }

    public void pulseForNoActivity(int i) {
        this.mNoActivityPulseShouldRepeat = true;
        if (!isAnimating(this.mNoActivityAnimator)) {
            int i2 = this.mScrimColorNoActivityStart;
            int i3 = this.mScrimColorNoActivityEnd;
            ValueAnimator ofArgb = ValueAnimator.ofArgb(new int[]{i2, i3, i3, i2});
            this.mNoActivityAnimator = ofArgb;
            ofArgb.setInterpolator(new AccelerateDecelerateInterpolator());
            this.mNoActivityAnimator.addUpdateListener(this.mNoActivityAnimatorListener);
            this.mNoActivityAnimator.setDuration(1233);
            this.mNoActivityAnimator.addListener(new AnimatorListenerAdapter(i) {
                int curPulses = 1;
                final int numPulses;
                final /* synthetic */ int val$times;

                {
                    this.val$times = r2;
                    this.numPulses = r2;
                }

                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (!CellState.this.mNoActivityPulseShouldRepeat || this.curPulses >= this.numPulses) {
                        CellState.this.mBucketListener.onNoActivityAnimationFinished();
                        return;
                    }
                    CellState.this.mNoActivityAnimator.start();
                    this.curPulses++;
                }
            });
            this.mNoActivityAnimator.start();
        }
    }

    public void fadeScrimOut(int i) {
        animateScrimColor(i == 2 ? 0 : this.mDone ? this.mScrimColorEnrolled : this.mScrimColorNotEnrolled, 200, 1);
    }

    public void fadeScrimIn() {
        fadeScrimIn(200);
    }

    private void fadeScrimIn(long j) {
        animateScrimColor(this.mDone ? this.mScrimColorEnrolled : this.mScrimColorNotEnrolled, j, 2);
    }

    public void onAcquired() {
        if (this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, 300);
        }
        if (this.mCursorState == 0 || !this.mDone) {
            this.mCursorState = 1;
            if (!this.mDone) {
                this.mBucketListener.onStartFinishing();
            }
            this.mDone = true;
            ValueAnimator ofArgb = ValueAnimator.ofArgb(new int[]{this.mCursorEdgePaint.getColor(), this.mCursorColorAcquired});
            this.mCursorAnimator = ofArgb;
            ofArgb.setDuration(300);
            this.mCursorAnimator.addUpdateListener(this.mCursorAnimatorListener);
            this.mCursorAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    int unused = CellState.this.mCursorState = 3;
                    if (CellState.this.mScrimAnimationState != 1) {
                        CellState.this.fadeScrimOut(1);
                    } else {
                        Log.w("FaceEnroll/CellState", "Index " + CellState.this.mIndex + " intentionally not going to SCRIM_FADE_REASON_DONE");
                    }
                    CellState.this.mHandler.sendEmptyMessageDelayed(1, 300);
                }
            });
            this.mCursorAnimator.start();
        }
    }

    public void updateScrimNotEnrolledColor(int i, boolean z) {
        this.mScrimColorNotEnrolled = i;
        if (z) {
            int i2 = this.mScrimAnimationState;
            if (i2 == 0) {
                animateScrimNotEnrolledColor(200);
            } else if (i2 == 2) {
                fadeScrimIn(getRemainingAnimationTime(this.mScrimAnimator));
            } else if (i2 == 3) {
                animateScrimNotEnrolledColor(getRemainingAnimationTime(this.mScrimAnimator));
            }
        }
    }

    private void animateScrimNotEnrolledColor(long j) {
        int i;
        if (!this.mDone && this.mScrimPaint.getColor() != (i = this.mScrimColorNotEnrolled)) {
            animateScrimColor(i, j, 3);
        }
    }

    private void animateScrimColor(int i, long j, int i2) {
        if (j > 0) {
            if (isAnimating(this.mScrimAnimator)) {
                this.mScrimAnimator.cancel();
            }
            this.mScrimAnimationState = i2;
            ValueAnimator ofArgb = ValueAnimator.ofArgb(new int[]{this.mScrimPaint.getColor(), i});
            this.mScrimAnimator = ofArgb;
            ofArgb.addUpdateListener(this.mScrimAnimatorListener);
            this.mScrimAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationCancel(Animator animator) {
                    int unused = CellState.this.mScrimAnimationState = 0;
                }

                public void onAnimationEnd(Animator animator) {
                    int unused = CellState.this.mScrimAnimationState = 0;
                }
            });
            this.mScrimAnimator.setDuration(j);
            this.mScrimAnimator.start();
        }
    }

    public void fadeCursorNow() {
        handleFadeCursor();
    }

    /* access modifiers changed from: private */
    public void handleFadeCursor() {
        this.mCursorState = 2;
        if (isAnimating(this.mCursorAnimator)) {
            this.mCursorAnimator.cancel();
        }
        ValueAnimator ofArgb = ValueAnimator.ofArgb(new int[]{this.mCursorEdgePaint.getColor(), this.mCursorColorGone});
        this.mCursorAnimator = ofArgb;
        ofArgb.setDuration(200);
        this.mCursorAnimator.addUpdateListener(this.mCursorAnimatorListener);
        this.mCursorAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                int unused = CellState.this.mCursorState = 0;
            }
        });
        this.mCursorAnimator.start();
    }

    private static boolean isAnimating(ValueAnimator valueAnimator) {
        return valueAnimator != null && valueAnimator.isRunning();
    }

    private static long getRemainingAnimationTime(ValueAnimator valueAnimator) {
        return (long) Math.round((1.0f - valueAnimator.getAnimatedFraction()) * ((float) valueAnimator.getDuration()));
    }
}
