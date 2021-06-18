package com.android.settingslib.graph;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class BatteryMeterDrawableBase extends Drawable {
    public static final String TAG = BatteryMeterDrawableBase.class.getSimpleName();
    protected final Paint mBatteryPaint;
    private final RectF mBoltFrame;
    protected final Paint mBoltPaint;
    private final Path mBoltPath;
    private final float[] mBoltPoints;
    private final RectF mButtonFrame;
    protected float mButtonHeightFraction;
    private int mChargeColor;
    private boolean mCharging;
    private final int[] mColors;
    private final int mCriticalLevel;
    private final RectF mFrame;
    protected final Paint mFramePaint;
    private int mHeight;
    private int mIconTint;
    private final int mIntrinsicHeight;
    private final int mIntrinsicWidth;
    private int mLevel;
    private final Path mOutlinePath;
    private final Rect mPadding;
    private final RectF mPlusFrame;
    protected final Paint mPlusPaint;
    private final Path mPlusPath;
    private final float[] mPlusPoints;
    protected boolean mPowerSaveAsColorError;
    private boolean mPowerSaveEnabled;
    protected final Paint mPowersavePaint;
    private final Path mShapePath;
    private boolean mShowPercent;
    private float mTextHeight;
    protected final Paint mTextPaint;
    private final Path mTextPath;
    private String mWarningString;
    private float mWarningTextHeight;
    protected final Paint mWarningTextPaint;
    private int mWidth;

    /* access modifiers changed from: protected */
    public float getAspectRatio() {
        return 0.58f;
    }

    public int getOpacity() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public float getRadiusRatio() {
        return 0.05882353f;
    }

    public void setAlpha(int i) {
    }

    public int getIntrinsicHeight() {
        return this.mIntrinsicHeight;
    }

    public int getIntrinsicWidth() {
        return this.mIntrinsicWidth;
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        updateSize();
    }

    private void updateSize() {
        Rect bounds = getBounds();
        int i = bounds.bottom;
        Rect rect = this.mPadding;
        int i2 = (i - rect.bottom) - (bounds.top + rect.top);
        this.mHeight = i2;
        this.mWidth = (bounds.right - rect.right) - (bounds.left + rect.left);
        this.mWarningTextPaint.setTextSize(((float) i2) * 0.75f);
        this.mWarningTextHeight = -this.mWarningTextPaint.getFontMetrics().ascent;
    }

    public boolean getPadding(Rect rect) {
        Rect rect2 = this.mPadding;
        if (rect2.left == 0 && rect2.top == 0 && rect2.right == 0 && rect2.bottom == 0) {
            return super.getPadding(rect);
        }
        rect.set(rect2);
        return true;
    }

    private int getColorForLevel(int i) {
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int[] iArr = this.mColors;
            if (i2 >= iArr.length) {
                return i3;
            }
            int i4 = iArr[i2];
            int i5 = iArr[i2 + 1];
            if (i <= i4) {
                return i2 == iArr.length + -2 ? this.mIconTint : i5;
            }
            i2 += 2;
            i3 = i5;
        }
    }

    /* access modifiers changed from: protected */
    public int batteryColorForLevel(int i) {
        if (this.mCharging || (this.mPowerSaveEnabled && this.mPowerSaveAsColorError)) {
            return this.mChargeColor;
        }
        return getColorForLevel(i);
    }

    public void draw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float[] fArr;
        float[] fArr2;
        Canvas canvas2 = canvas;
        int i = this.mLevel;
        Rect bounds = getBounds();
        if (i != -1) {
            float f4 = ((float) i) / 100.0f;
            int i2 = this.mHeight;
            int aspectRatio = (int) (getAspectRatio() * ((float) this.mHeight));
            float f5 = (float) i2;
            int round = Math.round(this.mButtonHeightFraction * f5);
            Rect rect = this.mPadding;
            int i3 = rect.left + bounds.left;
            int i4 = (bounds.bottom - rect.bottom) - i2;
            float f6 = (float) i3;
            float f7 = (float) i4;
            this.mFrame.set(f6, f7, (float) (i3 + aspectRatio), (float) (i2 + i4));
            this.mFrame.offset((float) ((this.mWidth - aspectRatio) / 2), 0.0f);
            RectF rectF = this.mButtonFrame;
            float f8 = ((float) aspectRatio) * 0.28f;
            float round2 = this.mFrame.left + ((float) Math.round(f8));
            RectF rectF2 = this.mFrame;
            float f9 = (float) round;
            rectF.set(round2, rectF2.top, rectF2.right - ((float) Math.round(f8)), this.mFrame.top + f9);
            this.mFrame.top += f9;
            this.mBatteryPaint.setColor(batteryColorForLevel(i));
            if (i >= 96) {
                f4 = 1.0f;
            } else if (i <= this.mCriticalLevel) {
                f4 = 0.0f;
            }
            if (f4 == 1.0f) {
                f = this.mButtonFrame.top;
            } else {
                RectF rectF3 = this.mFrame;
                f = (rectF3.height() * (1.0f - f4)) + rectF3.top;
            }
            this.mShapePath.reset();
            this.mOutlinePath.reset();
            float radiusRatio = getRadiusRatio() * (this.mFrame.height() + f9);
            this.mShapePath.setFillType(Path.FillType.WINDING);
            this.mShapePath.addRoundRect(this.mFrame, radiusRatio, radiusRatio, Path.Direction.CW);
            this.mShapePath.addRect(this.mButtonFrame, Path.Direction.CW);
            this.mOutlinePath.addRoundRect(this.mFrame, radiusRatio, radiusRatio, Path.Direction.CW);
            Path path = new Path();
            path.addRect(this.mButtonFrame, Path.Direction.CW);
            this.mOutlinePath.op(path, Path.Op.XOR);
            boolean z = false;
            if (this.mCharging) {
                RectF rectF4 = this.mFrame;
                float width = rectF4.left + (rectF4.width() / 4.0f) + 1.0f;
                RectF rectF5 = this.mFrame;
                float height = rectF5.top + (rectF5.height() / 6.0f);
                RectF rectF6 = this.mFrame;
                float width2 = (rectF6.right - (rectF6.width() / 4.0f)) + 1.0f;
                RectF rectF7 = this.mFrame;
                float height2 = rectF7.bottom - (rectF7.height() / 10.0f);
                RectF rectF8 = this.mBoltFrame;
                if (!(rectF8.left == width && rectF8.top == height && rectF8.right == width2 && rectF8.bottom == height2)) {
                    rectF8.set(width, height, width2, height2);
                    this.mBoltPath.reset();
                    Path path2 = this.mBoltPath;
                    RectF rectF9 = this.mBoltFrame;
                    RectF rectF10 = this.mBoltFrame;
                    path2.moveTo(rectF9.left + (this.mBoltPoints[0] * rectF9.width()), rectF10.top + (this.mBoltPoints[1] * rectF10.height()));
                    int i5 = 2;
                    while (true) {
                        fArr2 = this.mBoltPoints;
                        if (i5 >= fArr2.length) {
                            break;
                        }
                        Path path3 = this.mBoltPath;
                        RectF rectF11 = this.mBoltFrame;
                        float width3 = rectF11.left + (fArr2[i5] * rectF11.width());
                        RectF rectF12 = this.mBoltFrame;
                        path3.lineTo(width3, rectF12.top + (this.mBoltPoints[i5 + 1] * rectF12.height()));
                        i5 += 2;
                    }
                    Path path4 = this.mBoltPath;
                    RectF rectF13 = this.mBoltFrame;
                    float width4 = rectF13.left + (fArr2[0] * rectF13.width());
                    RectF rectF14 = this.mBoltFrame;
                    path4.lineTo(width4, rectF14.top + (this.mBoltPoints[1] * rectF14.height()));
                }
                RectF rectF15 = this.mBoltFrame;
                float f10 = rectF15.bottom;
                if (Math.min(Math.max((f10 - f) / (f10 - rectF15.top), 0.0f), 1.0f) <= 0.3f) {
                    canvas2.drawPath(this.mBoltPath, this.mBoltPaint);
                } else {
                    this.mShapePath.op(this.mBoltPath, Path.Op.DIFFERENCE);
                }
            } else if (this.mPowerSaveEnabled) {
                float width5 = (this.mFrame.width() * 2.0f) / 3.0f;
                RectF rectF16 = this.mFrame;
                float width6 = rectF16.left + ((rectF16.width() - width5) / 2.0f);
                RectF rectF17 = this.mFrame;
                float height3 = rectF17.top + ((rectF17.height() - width5) / 2.0f);
                RectF rectF18 = this.mFrame;
                float width7 = rectF18.right - ((rectF18.width() - width5) / 2.0f);
                RectF rectF19 = this.mFrame;
                float height4 = rectF19.bottom - ((rectF19.height() - width5) / 2.0f);
                RectF rectF20 = this.mPlusFrame;
                if (!(rectF20.left == width6 && rectF20.top == height3 && rectF20.right == width7 && rectF20.bottom == height4)) {
                    rectF20.set(width6, height3, width7, height4);
                    this.mPlusPath.reset();
                    Path path5 = this.mPlusPath;
                    RectF rectF21 = this.mPlusFrame;
                    RectF rectF22 = this.mPlusFrame;
                    path5.moveTo(rectF21.left + (this.mPlusPoints[0] * rectF21.width()), rectF22.top + (this.mPlusPoints[1] * rectF22.height()));
                    int i6 = 2;
                    while (true) {
                        fArr = this.mPlusPoints;
                        if (i6 >= fArr.length) {
                            break;
                        }
                        Path path6 = this.mPlusPath;
                        RectF rectF23 = this.mPlusFrame;
                        float width8 = rectF23.left + (fArr[i6] * rectF23.width());
                        RectF rectF24 = this.mPlusFrame;
                        path6.lineTo(width8, rectF24.top + (this.mPlusPoints[i6 + 1] * rectF24.height()));
                        i6 += 2;
                    }
                    Path path7 = this.mPlusPath;
                    RectF rectF25 = this.mPlusFrame;
                    float width9 = rectF25.left + (fArr[0] * rectF25.width());
                    RectF rectF26 = this.mPlusFrame;
                    path7.lineTo(width9, rectF26.top + (this.mPlusPoints[1] * rectF26.height()));
                }
                this.mShapePath.op(this.mPlusPath, Path.Op.DIFFERENCE);
                if (this.mPowerSaveAsColorError) {
                    canvas2.drawPath(this.mPlusPath, this.mPlusPaint);
                }
            }
            String str = null;
            if (this.mCharging || this.mPowerSaveEnabled || i <= this.mCriticalLevel || !this.mShowPercent) {
                f3 = 0.0f;
                f2 = 0.0f;
            } else {
                this.mTextPaint.setColor(getColorForLevel(i));
                this.mTextPaint.setTextSize(f5 * (this.mLevel == 100 ? 0.38f : 0.5f));
                this.mTextHeight = -this.mTextPaint.getFontMetrics().ascent;
                str = String.valueOf(i);
                f3 = (((float) this.mWidth) * 0.5f) + f6;
                f2 = ((((float) this.mHeight) + this.mTextHeight) * 0.47f) + f7;
                if (f > f2) {
                    z = true;
                }
                if (!z) {
                    this.mTextPath.reset();
                    this.mTextPaint.getTextPath(str, 0, str.length(), f3, f2, this.mTextPath);
                    this.mShapePath.op(this.mTextPath, Path.Op.DIFFERENCE);
                }
            }
            canvas2.drawPath(this.mShapePath, this.mFramePaint);
            this.mFrame.top = f;
            canvas.save();
            canvas2.clipRect(this.mFrame);
            canvas2.drawPath(this.mShapePath, this.mBatteryPaint);
            canvas.restore();
            if (!this.mCharging && !this.mPowerSaveEnabled) {
                if (i <= this.mCriticalLevel) {
                    canvas2.drawText(this.mWarningString, (((float) this.mWidth) * 0.5f) + f6, ((((float) this.mHeight) + this.mWarningTextHeight) * 0.48f) + f7, this.mWarningTextPaint);
                } else if (z) {
                    canvas2.drawText(str, f3, f2, this.mTextPaint);
                }
            }
            if (!this.mCharging && this.mPowerSaveEnabled && this.mPowerSaveAsColorError) {
                canvas2.drawPath(this.mOutlinePath, this.mPowersavePaint);
            }
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mFramePaint.setColorFilter(colorFilter);
        this.mBatteryPaint.setColorFilter(colorFilter);
        this.mWarningTextPaint.setColorFilter(colorFilter);
        this.mBoltPaint.setColorFilter(colorFilter);
        this.mPlusPaint.setColorFilter(colorFilter);
    }
}
