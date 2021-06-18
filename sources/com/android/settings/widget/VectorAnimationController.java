package com.android.settings.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.TextureView;
import android.view.View;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat$AnimationCallback;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import com.android.settings.widget.VideoPreference;

class VectorAnimationController implements VideoPreference.AnimationController {
    /* access modifiers changed from: private */
    public AnimatedVectorDrawableCompat mAnimatedVectorDrawableCompat;
    private Animatable2Compat$AnimationCallback mAnimationCallback = new Animatable2Compat$AnimationCallback() {
        public void onAnimationEnd(Drawable drawable) {
            VectorAnimationController.this.mAnimatedVectorDrawableCompat.start();
        }
    };
    private Drawable mPreviewDrawable;

    public int getDuration() {
        return 5000;
    }

    VectorAnimationController(Context context, int i) {
        this.mAnimatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(context, i);
    }

    public int getVideoWidth() {
        return this.mAnimatedVectorDrawableCompat.getIntrinsicWidth();
    }

    public int getVideoHeight() {
        return this.mAnimatedVectorDrawableCompat.getIntrinsicHeight();
    }

    public void attachView(TextureView textureView, View view, View view2) {
        this.mPreviewDrawable = view.getForeground();
        textureView.setVisibility(8);
        updateViewStates(view, view2);
        view.setOnClickListener(new VectorAnimationController$$ExternalSyntheticLambda0(this, view, view2));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attachView$0(View view, View view2, View view3) {
        updateViewStates(view, view2);
    }

    public void release() {
        this.mAnimatedVectorDrawableCompat.stop();
        this.mAnimatedVectorDrawableCompat.clearAnimationCallbacks();
    }

    private void updateViewStates(View view, View view2) {
        if (this.mAnimatedVectorDrawableCompat.isRunning()) {
            this.mAnimatedVectorDrawableCompat.stop();
            this.mAnimatedVectorDrawableCompat.clearAnimationCallbacks();
            view2.setVisibility(0);
            view.setForeground(this.mPreviewDrawable);
            return;
        }
        view2.setVisibility(8);
        view.setForeground(this.mAnimatedVectorDrawableCompat);
        this.mAnimatedVectorDrawableCompat.start();
        this.mAnimatedVectorDrawableCompat.registerAnimationCallback(this.mAnimationCallback);
    }
}
