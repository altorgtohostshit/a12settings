package com.google.android.settings.gestures.assist.bubble;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.android.settings.R;
import java.util.Random;

public class SpiralingAndroid {
    private Drawable mAndroid;
    private float mCurrentRotation;
    private Random mRandom;
    private final float mRotationSpeed = (((this.mRandom.nextFloat() * 0.8f) + 0.8f) * 360.0f);
    private final float mVelocityY;

    public SpiralingAndroid(Context context, Rect rect) {
        Random random = new Random();
        this.mRandom = random;
        this.mVelocityY = ((random.nextFloat() * 0.8f) + 0.8f) * 400.0f;
        int width = (rect.width() / 10) + this.mRandom.nextInt(rect.width() / 5);
        Drawable newDrawable = context.getResources().getDrawable(R.drawable.ic_android_black_24dp, (Resources.Theme) null).getConstantState().newDrawable();
        this.mAndroid = newDrawable;
        newDrawable.mutate();
        this.mAndroid.setColorFilter(this.mRandom.nextInt(), PorterDuff.Mode.SRC_ATOP);
        int nextInt = this.mRandom.nextInt(rect.width() - width);
        int i = (-this.mRandom.nextInt(rect.height() / 2)) - width;
        this.mAndroid.setBounds(new Rect(nextInt, i, nextInt + width, width + i));
    }

    public Drawable getAndroid() {
        return this.mAndroid;
    }

    public float getCurrentRotation() {
        return this.mCurrentRotation;
    }

    public void update(long j, long j2) {
        float f = ((float) j2) * 0.001f;
        Rect copyBounds = this.mAndroid.copyBounds();
        copyBounds.offset((int) 0.0f, (int) (this.mVelocityY * f));
        this.mAndroid.setBounds(copyBounds);
        this.mCurrentRotation += this.mRotationSpeed * f;
    }
}
