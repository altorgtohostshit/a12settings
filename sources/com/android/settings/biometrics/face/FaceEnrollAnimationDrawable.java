package com.android.settings.biometrics.face;

import android.animation.TimeAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.android.settings.biometrics.BiometricEnrollSidecar;
import com.android.settings.biometrics.face.ParticleCollection;

public class FaceEnrollAnimationDrawable extends Drawable implements BiometricEnrollSidecar.Listener {
    private final ParticleCollection.Listener mAnimationListener = new ParticleCollection.Listener() {
        public void onEnrolled() {
            if (FaceEnrollAnimationDrawable.this.mTimeAnimator != null && FaceEnrollAnimationDrawable.this.mTimeAnimator.isStarted()) {
                FaceEnrollAnimationDrawable.this.mTimeAnimator.end();
                FaceEnrollAnimationDrawable.this.mListener.onEnrolled();
            }
        }
    };
    private Rect mBounds;
    private final Paint mCircleCutoutPaint;
    private final Context mContext;
    /* access modifiers changed from: private */
    public final ParticleCollection.Listener mListener;
    private ParticleCollection mParticleCollection;
    private final Paint mSquarePaint;
    /* access modifiers changed from: private */
    public TimeAnimator mTimeAnimator;

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public FaceEnrollAnimationDrawable(Context context, ParticleCollection.Listener listener) {
        this.mContext = context;
        this.mListener = listener;
        Paint paint = new Paint();
        this.mSquarePaint = paint;
        paint.setColor(-1);
        paint.setAntiAlias(true);
        Paint paint2 = new Paint();
        this.mCircleCutoutPaint = paint2;
        paint2.setColor(0);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint2.setAntiAlias(true);
    }

    public void onEnrollmentHelp(int i, CharSequence charSequence) {
        this.mParticleCollection.onEnrollmentHelp(i, charSequence);
    }

    public void onEnrollmentError(int i, CharSequence charSequence) {
        this.mParticleCollection.onEnrollmentError(i, charSequence);
    }

    public void onEnrollmentProgressChange(int i, int i2) {
        this.mParticleCollection.onEnrollmentProgressChange(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        this.mBounds = rect;
        this.mParticleCollection = new ParticleCollection(this.mContext, this.mAnimationListener, rect, 20);
        if (this.mTimeAnimator == null) {
            TimeAnimator timeAnimator = new TimeAnimator();
            this.mTimeAnimator = timeAnimator;
            timeAnimator.setTimeListener(new FaceEnrollAnimationDrawable$$ExternalSyntheticLambda0(this));
            this.mTimeAnimator.start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBoundsChange$0(TimeAnimator timeAnimator, long j, long j2) {
        this.mParticleCollection.update(j, j2);
        invalidateSelf();
    }

    public void draw(Canvas canvas) {
        if (this.mBounds != null) {
            canvas.save();
            canvas.drawRect(0.0f, 0.0f, (float) this.mBounds.width(), (float) this.mBounds.height(), this.mSquarePaint);
            canvas.drawCircle(this.mBounds.exactCenterX(), this.mBounds.exactCenterY(), (float) ((this.mBounds.height() / 2) - 20), this.mCircleCutoutPaint);
            this.mParticleCollection.draw(canvas);
            canvas.restore();
        }
    }
}
