package com.google.android.settings.gestures.assist.bubble;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.appcompat.R$styleable;

public class AssistGesturePlayButtonDrawable extends Drawable {
    private Rect mBounds;
    private PointF mCircleCenter = new PointF();
    private float mCircleRadius;
    private Paint mPaint;

    public int getOpacity() {
        return -3;
    }

    public AssistGesturePlayButtonDrawable() {
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
    }

    private void drawTriangle(Canvas canvas, float f, float f2, float f3, Paint paint) {
        float cos = ((float) Math.cos(0.5235987901687622d)) * f3;
        float sin = ((float) Math.sin(0.5235987901687622d)) * f3;
        PointF pointF = new PointF(f, f2 - f3);
        float f4 = sin + f2;
        PointF pointF2 = new PointF(f + cos, f4);
        PointF pointF3 = new PointF(f - cos, f4);
        canvas.save();
        canvas.rotate(90.0f, f, f2);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(pointF.x, pointF.y);
        path.lineTo(pointF2.x, pointF2.y);
        path.lineTo(pointF3.x, pointF3.y);
        path.lineTo(pointF.x, pointF.y);
        path.close();
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    public void draw(Canvas canvas) {
        canvas.save();
        Rect rect = this.mBounds;
        if (rect != null) {
            this.mCircleCenter.x = (float) (rect.width() / 2);
            this.mCircleCenter.y = (float) (this.mBounds.height() / 2);
            this.mCircleRadius = (float) (this.mBounds.width() / 6);
            this.mPaint.setColor(Color.rgb(90, R$styleable.AppCompatTheme_windowFixedHeightMajor, 160));
            PointF pointF = this.mCircleCenter;
            canvas.drawCircle(pointF.x, pointF.y, this.mCircleRadius, this.mPaint);
            this.mPaint.setColor(-1);
            drawTriangle(canvas, (float) (this.mBounds.width() / 2), (float) (this.mBounds.height() / 2), (float) (this.mBounds.width() / 12), this.mPaint);
            canvas.restore();
        }
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public void onBoundsChange(Rect rect) {
        this.mBounds = rect;
        invalidateSelf();
    }

    private double distance(PointF pointF, PointF pointF2) {
        return Math.sqrt(Math.pow((double) (pointF2.x - pointF.x), 2.0d) + Math.pow((double) (pointF2.y - pointF.y), 2.0d));
    }

    public boolean hitTest(float f, float f2) {
        return distance(new PointF(f, f2), this.mCircleCenter) <= ((double) this.mCircleRadius);
    }
}
