package com.google.android.settings.gestures.assist.bubble;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Bubble {
    private int mAlpha;
    private float mAmplitude;
    private int mBubbleState = 0;
    private int mColor;
    private float mFrequency;
    private PointF mOriginalPoint = new PointF();
    private int mOriginalSize;
    private PointF mPoint = new PointF();
    private Random mRandom = new Random();
    private int mSize;
    private float mVelocityY;

    public Bubble(Rect rect) {
        int nextInt = ThreadLocalRandom.current().nextInt(40, 140);
        this.mOriginalSize = nextInt;
        this.mSize = nextInt;
        this.mOriginalPoint.x = ((float) rect.width()) * ((this.mRandom.nextFloat() * 0.6f) + 0.2f);
        this.mOriginalPoint.y = (float) (rect.height() + this.mOriginalSize);
        this.mPoint = this.mOriginalPoint;
        this.mFrequency = this.mRandom.nextFloat() * 2.0f;
        this.mAmplitude = this.mRandom.nextFloat() * 10.0f;
        this.mVelocityY = ((this.mRandom.nextFloat() * 0.4f) + 0.8f) * 600.0f;
        int nextFloat = (int) (((this.mRandom.nextFloat() * 0.2f) + 0.6f) * 255.0f);
        this.mAlpha = nextFloat;
        this.mColor = Color.argb(nextFloat, this.mRandom.nextInt(255), this.mRandom.nextInt(255), this.mRandom.nextInt(255));
    }

    public int getOriginalSize() {
        return this.mOriginalSize;
    }

    public int getState() {
        return this.mBubbleState;
    }

    public void setState(int i) {
        this.mBubbleState = i;
    }

    public int getSize() {
        return this.mSize;
    }

    public void setSize(int i) {
        this.mSize = i;
    }

    public int getColor() {
        return this.mColor;
    }

    public boolean isBubbleDead() {
        return this.mBubbleState == 2;
    }

    public boolean isBubbleTouchingTop() {
        return this.mPoint.y - ((float) this.mSize) <= 0.0f;
    }

    public PointF getPoint() {
        return this.mPoint;
    }

    private void updateDying(long j, long j2) {
        int i = this.mSize - ((int) (((float) j2) * 0.1f));
        this.mSize = i;
        if (i < 0) {
            this.mSize = 0;
            this.mBubbleState = 2;
        }
    }

    public void update(long j, long j2) {
        PointF pointF = this.mPoint;
        pointF.y -= (((float) j2) * 0.001f) * this.mVelocityY;
        pointF.x = this.mOriginalPoint.x + (this.mAmplitude * ((float) Math.sin(((double) this.mFrequency) * 6.283185307179586d * ((double) (((float) j) * 0.001f)))));
        if (this.mBubbleState == 1) {
            updateDying(j, j2);
        }
    }
}
