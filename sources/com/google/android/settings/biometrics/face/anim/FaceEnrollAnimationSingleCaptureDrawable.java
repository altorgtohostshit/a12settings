package com.google.android.settings.biometrics.face.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.os.Handler;
import android.widget.ImageView;
import com.google.android.settings.biometrics.face.anim.FaceEnrollAnimationBase;
import com.google.android.settings.biometrics.face.anim.single.ArcCollection;

public class FaceEnrollAnimationSingleCaptureDrawable extends FaceEnrollAnimationBase {
    private final Handler mHandler;
    private final ArcCollection mRotatingArcs;

    public int getOpacity() {
        return -3;
    }

    public void onEnrollmentError(int i, CharSequence charSequence) {
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public FaceEnrollAnimationSingleCaptureDrawable(Context context, FaceEnrollAnimationBase.AnimationListener animationListener, ImageView imageView, boolean z) {
        super(context, animationListener, imageView, z);
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mRotatingArcs = new ArcCollection(context, handler);
    }

    /* access modifiers changed from: protected */
    public void startFinishing() {
        super.startFinishing();
        this.mRotatingArcs.startFinishing(new C1820x4787e6ea(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startFinishing$0() {
        getListener().onEnrollAnimationFinished();
    }

    /* access modifiers changed from: protected */
    public void update(long j, long j2) {
        this.mRotatingArcs.update(j, j2);
    }

    /* access modifiers changed from: protected */
    public void onUserLeaveGood(CharSequence charSequence) {
        super.onUserLeaveGood(charSequence);
        this.mRotatingArcs.stopRotating();
    }

    /* access modifiers changed from: protected */
    public void onUserEnterGood() {
        super.onUserEnterGood();
        this.mRotatingArcs.startRotating();
    }

    public void onEnrollmentProgressChange(int i, int i2) {
        super.onEnrollmentProgressChange(i, i2);
        if (i2 == 0) {
            vibrate();
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mRotatingArcs.draw(canvas);
    }
}
