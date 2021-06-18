package com.android.launcher3.icons;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import java.nio.ByteBuffer;

public class IconNormalizer {
    private final RectF mAdaptiveIconBounds = new RectF();
    private float mAdaptiveIconScale;
    private final Bitmap mBitmap;
    private final Rect mBounds = new Rect();
    private final Canvas mCanvas;
    private boolean mEnableShapeDetection;
    private final float[] mLeftBorder;
    private final Matrix mMatrix;
    private final int mMaxSize;
    private final Paint mPaintMaskShape;
    private final Paint mPaintMaskShapeOutline;
    private final byte[] mPixels;
    private final float[] mRightBorder;
    private final Path mShapePath;

    IconNormalizer(Context context, int i, boolean z) {
        int i2 = i * 2;
        this.mMaxSize = i2;
        Bitmap createBitmap = Bitmap.createBitmap(i2, i2, Bitmap.Config.ALPHA_8);
        this.mBitmap = createBitmap;
        this.mCanvas = new Canvas(createBitmap);
        this.mPixels = new byte[(i2 * i2)];
        this.mLeftBorder = new float[i2];
        this.mRightBorder = new float[i2];
        Paint paint = new Paint();
        this.mPaintMaskShape = paint;
        paint.setColor(-65536);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        Paint paint2 = new Paint();
        this.mPaintMaskShapeOutline = paint2;
        paint2.setStrokeWidth(context.getResources().getDisplayMetrics().density * 2.0f);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setColor(-16777216);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mShapePath = new Path();
        this.mMatrix = new Matrix();
        this.mAdaptiveIconScale = 0.0f;
        this.mEnableShapeDetection = z;
    }

    private static float getScale(float f, float f2, float f3) {
        float f4 = f / f2;
        float f5 = f4 < 0.7853982f ? 0.6597222f : ((1.0f - f4) * 0.040449437f) + 0.6510417f;
        float f6 = f / f3;
        if (f6 > f5) {
            return (float) Math.sqrt((double) (f5 / f6));
        }
        return 1.0f;
    }

    @TargetApi(26)
    public static float normalizeAdaptiveIcon(Drawable drawable, int i, RectF rectF) {
        Rect rect = new Rect(drawable.getBounds());
        drawable.setBounds(0, 0, i, i);
        Path iconMask = ((AdaptiveIconDrawable) drawable).getIconMask();
        Region region = new Region();
        region.setPath(iconMask, new Region(0, 0, i, i));
        Rect bounds = region.getBounds();
        int area = GraphicsUtils.getArea(region);
        if (rectF != null) {
            float f = (float) i;
            rectF.set(((float) bounds.left) / f, ((float) bounds.top) / f, 1.0f - (((float) bounds.right) / f), 1.0f - (((float) bounds.bottom) / f));
        }
        drawable.setBounds(rect);
        float f2 = (float) area;
        return getScale(f2, f2, (float) (i * i));
    }

    private boolean isShape(Path path) {
        if (Math.abs((((float) this.mBounds.width()) / ((float) this.mBounds.height())) - 1.0f) > 0.05f) {
            return false;
        }
        this.mMatrix.reset();
        this.mMatrix.setScale((float) this.mBounds.width(), (float) this.mBounds.height());
        Matrix matrix = this.mMatrix;
        Rect rect = this.mBounds;
        matrix.postTranslate((float) rect.left, (float) rect.top);
        path.transform(this.mMatrix, this.mShapePath);
        this.mCanvas.drawPath(this.mShapePath, this.mPaintMaskShape);
        this.mCanvas.drawPath(this.mShapePath, this.mPaintMaskShapeOutline);
        return isTransparentBitmap();
    }

    private boolean isTransparentBitmap() {
        Rect rect;
        ByteBuffer wrap = ByteBuffer.wrap(this.mPixels);
        wrap.rewind();
        this.mBitmap.copyPixelsToBuffer(wrap);
        Rect rect2 = this.mBounds;
        int i = rect2.top;
        int i2 = this.mMaxSize;
        int i3 = i * i2;
        int i4 = i2 - rect2.right;
        int i5 = 0;
        while (true) {
            rect = this.mBounds;
            if (i >= rect.bottom) {
                break;
            }
            int i6 = rect.left;
            int i7 = i3 + i6;
            while (i6 < this.mBounds.right) {
                if ((this.mPixels[i7] & 255) > 40) {
                    i5++;
                }
                i7++;
                i6++;
            }
            i3 = i7 + i4;
            i++;
        }
        if (((float) i5) / ((float) (rect.width() * this.mBounds.height())) < 0.005f) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:74:0x012e, code lost:
        return 1.0f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00c6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized float getScale(android.graphics.drawable.Drawable r17, android.graphics.RectF r18, android.graphics.Path r19, boolean[] r20) {
        /*
            r16 = this;
            r1 = r16
            r0 = r17
            r2 = r18
            r3 = r20
            monitor-enter(r16)
            boolean r4 = com.android.launcher3.icons.BaseIconFactory.ATLEAST_OREO     // Catch:{ all -> 0x012f }
            r5 = 0
            if (r4 == 0) goto L_0x002d
            boolean r4 = r0 instanceof android.graphics.drawable.AdaptiveIconDrawable     // Catch:{ all -> 0x012f }
            if (r4 == 0) goto L_0x002d
            float r3 = r1.mAdaptiveIconScale     // Catch:{ all -> 0x012f }
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 != 0) goto L_0x0022
            int r3 = r1.mMaxSize     // Catch:{ all -> 0x012f }
            android.graphics.RectF r4 = r1.mAdaptiveIconBounds     // Catch:{ all -> 0x012f }
            float r0 = normalizeAdaptiveIcon(r0, r3, r4)     // Catch:{ all -> 0x012f }
            r1.mAdaptiveIconScale = r0     // Catch:{ all -> 0x012f }
        L_0x0022:
            if (r2 == 0) goto L_0x0029
            android.graphics.RectF r0 = r1.mAdaptiveIconBounds     // Catch:{ all -> 0x012f }
            r2.set(r0)     // Catch:{ all -> 0x012f }
        L_0x0029:
            float r0 = r1.mAdaptiveIconScale     // Catch:{ all -> 0x012f }
            monitor-exit(r16)
            return r0
        L_0x002d:
            int r4 = r17.getIntrinsicWidth()     // Catch:{ all -> 0x012f }
            int r6 = r17.getIntrinsicHeight()     // Catch:{ all -> 0x012f }
            if (r4 <= 0) goto L_0x004c
            if (r6 > 0) goto L_0x003a
            goto L_0x004c
        L_0x003a:
            int r7 = r1.mMaxSize     // Catch:{ all -> 0x012f }
            if (r4 > r7) goto L_0x0040
            if (r6 <= r7) goto L_0x005c
        L_0x0040:
            int r7 = java.lang.Math.max(r4, r6)     // Catch:{ all -> 0x012f }
            int r8 = r1.mMaxSize     // Catch:{ all -> 0x012f }
            int r4 = r4 * r8
            int r4 = r4 / r7
            int r8 = r8 * r6
            int r6 = r8 / r7
            goto L_0x005c
        L_0x004c:
            if (r4 <= 0) goto L_0x0052
            int r7 = r1.mMaxSize     // Catch:{ all -> 0x012f }
            if (r4 <= r7) goto L_0x0054
        L_0x0052:
            int r4 = r1.mMaxSize     // Catch:{ all -> 0x012f }
        L_0x0054:
            if (r6 <= 0) goto L_0x005a
            int r7 = r1.mMaxSize     // Catch:{ all -> 0x012f }
            if (r6 <= r7) goto L_0x005c
        L_0x005a:
            int r6 = r1.mMaxSize     // Catch:{ all -> 0x012f }
        L_0x005c:
            android.graphics.Bitmap r7 = r1.mBitmap     // Catch:{ all -> 0x012f }
            r8 = 0
            r7.eraseColor(r8)     // Catch:{ all -> 0x012f }
            r0.setBounds(r8, r8, r4, r6)     // Catch:{ all -> 0x012f }
            android.graphics.Canvas r7 = r1.mCanvas     // Catch:{ all -> 0x012f }
            r0.draw(r7)     // Catch:{ all -> 0x012f }
            byte[] r0 = r1.mPixels     // Catch:{ all -> 0x012f }
            java.nio.ByteBuffer r0 = java.nio.ByteBuffer.wrap(r0)     // Catch:{ all -> 0x012f }
            r0.rewind()     // Catch:{ all -> 0x012f }
            android.graphics.Bitmap r7 = r1.mBitmap     // Catch:{ all -> 0x012f }
            r7.copyPixelsToBuffer(r0)     // Catch:{ all -> 0x012f }
            int r0 = r1.mMaxSize     // Catch:{ all -> 0x012f }
            int r7 = r0 + 1
            int r0 = r0 - r4
            r10 = r8
            r14 = r10
            r11 = -1
            r12 = -1
            r13 = -1
        L_0x0082:
            if (r10 >= r6) goto L_0x00c1
            r15 = r8
            r5 = -1
            r8 = -1
        L_0x0087:
            if (r15 >= r4) goto L_0x009f
            byte[] r9 = r1.mPixels     // Catch:{ all -> 0x012f }
            byte r9 = r9[r14]     // Catch:{ all -> 0x012f }
            r9 = r9 & 255(0xff, float:3.57E-43)
            r3 = 40
            if (r9 <= r3) goto L_0x0098
            r3 = -1
            if (r5 != r3) goto L_0x0097
            r5 = r15
        L_0x0097:
            r8 = r15
        L_0x0098:
            int r14 = r14 + 1
            int r15 = r15 + 1
            r3 = r20
            goto L_0x0087
        L_0x009f:
            int r14 = r14 + r0
            float[] r3 = r1.mLeftBorder     // Catch:{ all -> 0x012f }
            float r9 = (float) r5     // Catch:{ all -> 0x012f }
            r3[r10] = r9     // Catch:{ all -> 0x012f }
            float[] r3 = r1.mRightBorder     // Catch:{ all -> 0x012f }
            float r9 = (float) r8     // Catch:{ all -> 0x012f }
            r3[r10] = r9     // Catch:{ all -> 0x012f }
            r3 = -1
            if (r5 == r3) goto L_0x00ba
            if (r11 != r3) goto L_0x00b0
            r11 = r10
        L_0x00b0:
            int r3 = java.lang.Math.min(r7, r5)     // Catch:{ all -> 0x012f }
            int r12 = java.lang.Math.max(r12, r8)     // Catch:{ all -> 0x012f }
            r7 = r3
            r13 = r10
        L_0x00ba:
            int r10 = r10 + 1
            r3 = r20
            r5 = 0
            r8 = 0
            goto L_0x0082
        L_0x00c1:
            r0 = 1065353216(0x3f800000, float:1.0)
            r3 = -1
            if (r11 == r3) goto L_0x012d
            if (r12 != r3) goto L_0x00c9
            goto L_0x012d
        L_0x00c9:
            float[] r5 = r1.mLeftBorder     // Catch:{ all -> 0x012f }
            r8 = 1
            convertToConvexArray(r5, r8, r11, r13)     // Catch:{ all -> 0x012f }
            float[] r5 = r1.mRightBorder     // Catch:{ all -> 0x012f }
            convertToConvexArray(r5, r3, r11, r13)     // Catch:{ all -> 0x012f }
            r3 = 0
            r5 = 0
        L_0x00d6:
            if (r3 >= r6) goto L_0x00ef
            float[] r9 = r1.mLeftBorder     // Catch:{ all -> 0x012f }
            r10 = r9[r3]     // Catch:{ all -> 0x012f }
            r14 = -1082130432(0xffffffffbf800000, float:-1.0)
            int r10 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
            if (r10 > 0) goto L_0x00e3
            goto L_0x00ec
        L_0x00e3:
            float[] r10 = r1.mRightBorder     // Catch:{ all -> 0x012f }
            r10 = r10[r3]     // Catch:{ all -> 0x012f }
            r9 = r9[r3]     // Catch:{ all -> 0x012f }
            float r10 = r10 - r9
            float r10 = r10 + r0
            float r5 = r5 + r10
        L_0x00ec:
            int r3 = r3 + 1
            goto L_0x00d6
        L_0x00ef:
            android.graphics.Rect r3 = r1.mBounds     // Catch:{ all -> 0x012f }
            r3.left = r7     // Catch:{ all -> 0x012f }
            r3.right = r12     // Catch:{ all -> 0x012f }
            r3.top = r11     // Catch:{ all -> 0x012f }
            r3.bottom = r13     // Catch:{ all -> 0x012f }
            if (r2 == 0) goto L_0x010b
            float r3 = (float) r7     // Catch:{ all -> 0x012f }
            float r9 = (float) r4     // Catch:{ all -> 0x012f }
            float r3 = r3 / r9
            float r10 = (float) r11     // Catch:{ all -> 0x012f }
            float r14 = (float) r6     // Catch:{ all -> 0x012f }
            float r10 = r10 / r14
            float r15 = (float) r12     // Catch:{ all -> 0x012f }
            float r15 = r15 / r9
            float r9 = r0 - r15
            float r15 = (float) r13     // Catch:{ all -> 0x012f }
            float r15 = r15 / r14
            float r0 = r0 - r15
            r2.set(r3, r10, r9, r0)     // Catch:{ all -> 0x012f }
        L_0x010b:
            r0 = r20
            if (r0 == 0) goto L_0x011f
            boolean r2 = r1.mEnableShapeDetection     // Catch:{ all -> 0x012f }
            if (r2 == 0) goto L_0x011f
            int r2 = r0.length     // Catch:{ all -> 0x012f }
            if (r2 <= 0) goto L_0x011f
            r2 = r19
            boolean r2 = r1.isShape(r2)     // Catch:{ all -> 0x012f }
            r3 = 0
            r0[r3] = r2     // Catch:{ all -> 0x012f }
        L_0x011f:
            int r13 = r13 + r8
            int r13 = r13 - r11
            int r12 = r12 + r8
            int r12 = r12 - r7
            int r13 = r13 * r12
            float r0 = (float) r13     // Catch:{ all -> 0x012f }
            int r4 = r4 * r6
            float r2 = (float) r4     // Catch:{ all -> 0x012f }
            float r0 = getScale(r5, r0, r2)     // Catch:{ all -> 0x012f }
            monitor-exit(r16)
            return r0
        L_0x012d:
            monitor-exit(r16)
            return r0
        L_0x012f:
            r0 = move-exception
            monitor-exit(r16)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.launcher3.icons.IconNormalizer.getScale(android.graphics.drawable.Drawable, android.graphics.RectF, android.graphics.Path, boolean[]):float");
    }

    private static void convertToConvexArray(float[] fArr, int i, int i2, int i3) {
        float[] fArr2 = new float[(fArr.length - 1)];
        int i4 = -1;
        float f = Float.MAX_VALUE;
        for (int i5 = i2 + 1; i5 <= i3; i5++) {
            if (fArr[i5] > -1.0f) {
                if (f == Float.MAX_VALUE) {
                    i4 = i2;
                } else {
                    float f2 = ((fArr[i5] - fArr[i4]) / ((float) (i5 - i4))) - f;
                    float f3 = (float) i;
                    if (f2 * f3 < 0.0f) {
                        while (i4 > i2) {
                            i4--;
                            if ((((fArr[i5] - fArr[i4]) / ((float) (i5 - i4))) - fArr2[i4]) * f3 >= 0.0f) {
                                break;
                            }
                        }
                    }
                }
                f = (fArr[i5] - fArr[i4]) / ((float) (i5 - i4));
                for (int i6 = i4; i6 < i5; i6++) {
                    fArr2[i6] = f;
                    fArr[i6] = fArr[i4] + (((float) (i6 - i4)) * f);
                }
                i4 = i5;
            }
        }
    }
}
