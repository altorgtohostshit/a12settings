package com.google.android.settings.biometrics.face.anim.curve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.ArraySet;
import com.android.internal.graphics.ColorUtils;
import com.android.settings.R;
import com.google.android.settings.biometrics.face.anim.FaceEnrollAnimationMultiAngleDrawable;

public class GridController {
    private static final int[] SCRIM_OPACITY_THRESHOLDS = {0, 5, 10, 15, 20};
    private static final float[] SCRIM_OPACITY_VALUES = {0.55f, 0.6f, 0.65f, 0.7f, 0.75f};
    private final FaceEnrollAnimationMultiAngleDrawable.BucketListener mBucketListener;
    private CellConfig[] mCellConfigs;
    private final CellState[] mCellStates;
    private int mEnrolledCount = 0;
    private final GridState mGridState;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final ScrimState mNoActivityScrimState;
    private final ArraySet<Integer> mPrimaryCellIndices;
    private final int mScrimNotEnrolledDefaultColor;
    private final int mScrimNotEnrolledPrimaryColor;
    private final int mScrimNotEnrolledSecondaryColor;

    public void onBoundsChange(Rect rect) {
    }

    public GridController(Context context, FaceEnrollAnimationMultiAngleDrawable.BucketListener bucketListener) {
        this.mBucketListener = bucketListener;
        this.mScrimNotEnrolledDefaultColor = context.getColor(R.color.face_enroll_cell_not_enrolled);
        this.mScrimNotEnrolledPrimaryColor = context.getColor(R.color.face_enroll_cell_not_enrolled_primary);
        this.mScrimNotEnrolledSecondaryColor = context.getColor(R.color.face_enroll_cell_not_enrolled_secondary);
        int[] intArray = context.getResources().getIntArray(R.array.face_enroll_primary_buckets);
        this.mPrimaryCellIndices = new ArraySet<>(intArray.length);
        for (int valueOf : intArray) {
            this.mPrimaryCellIndices.add(Integer.valueOf(valueOf));
        }
        this.mNoActivityScrimState = new ScrimState(context.getColor(R.color.face_enroll_no_activity_gone), context.getColor(R.color.face_enroll_no_activity_showing));
        this.mGridState = new GridState(context, this.mHandler);
        this.mCellStates = new CellState[25];
        for (int i = 0; i < this.mCellStates.length; i++) {
            this.mCellStates[i] = new CellState(context, i, this.mBucketListener, getScrimNotEnrolledColor(0, i));
        }
    }

    public void stopPulseForNoActivity() {
        int i = 0;
        while (true) {
            CellState[] cellStateArr = this.mCellStates;
            if (i < cellStateArr.length) {
                cellStateArr[i].stopPulseForNoActivity();
                i++;
            } else {
                return;
            }
        }
    }

    public void pulseForNoActivity(int i, int i2) {
        this.mCellStates[i].pulseForNoActivity(i2);
    }

    public void onUserLeaveGood() {
        int i = 0;
        while (true) {
            CellState[] cellStateArr = this.mCellStates;
            if (i < cellStateArr.length) {
                cellStateArr[i].fadeScrimOut(2);
                this.mCellStates[i].fadeCursorNow();
                i++;
            } else {
                this.mGridState.fadeOut((Runnable) null);
                return;
            }
        }
    }

    public void onUserEnterGood() {
        this.mGridState.fadeIn();
        int i = 0;
        while (true) {
            CellState[] cellStateArr = this.mCellStates;
            if (i < cellStateArr.length) {
                cellStateArr[i].fadeScrimIn();
                i++;
            } else {
                return;
            }
        }
    }

    public void onAcquired(int i) {
        boolean isDone = this.mCellStates[i].isDone();
        if (this.mNoActivityScrimState.isShowing() && !isDone) {
            this.mNoActivityScrimState.fadeOut();
        }
        this.mCellStates[i].onAcquired();
        if (!isDone) {
            this.mEnrolledCount++;
            updateColor(true);
        }
    }

    public void restoreState(int i, boolean z) {
        if (z) {
            this.mCellStates[i].setEarlyDone();
            this.mEnrolledCount++;
        }
    }

    private void updateColor(boolean z) {
        int i = 0;
        while (true) {
            CellState[] cellStateArr = this.mCellStates;
            if (i < cellStateArr.length) {
                CellState cellState = cellStateArr[i];
                if (!cellState.isDone()) {
                    cellState.updateScrimNotEnrolledColor(getScrimNotEnrolledColor(this.mEnrolledCount, i), z);
                }
                i++;
            } else {
                return;
            }
        }
    }

    public void setEarlyDone(int i) {
        if (!this.mCellStates[i].isDone()) {
            this.mCellStates[i].setEarlyDone();
            this.mEnrolledCount++;
            updateColor(false);
        }
    }

    public void draw(Canvas canvas) {
        canvas.save();
        int i = 0;
        if (this.mCellConfigs == null) {
            initializeCells(canvas.getWidth(), canvas.getHeight());
            int i2 = 0;
            while (true) {
                CellState[] cellStateArr = this.mCellStates;
                if (i2 >= cellStateArr.length) {
                    break;
                }
                cellStateArr[i2].updateConfig(this.mCellConfigs[i2]);
                i2++;
            }
        }
        canvas.translate((float) (canvas.getWidth() / 2), (float) (canvas.getHeight() / 2));
        this.mNoActivityScrimState.draw(canvas);
        int i3 = 0;
        while (true) {
            CellState[] cellStateArr2 = this.mCellStates;
            if (i3 >= cellStateArr2.length) {
                break;
            }
            cellStateArr2[i3].draw(canvas);
            i3++;
        }
        this.mGridState.draw(canvas);
        while (true) {
            CellState[] cellStateArr3 = this.mCellStates;
            if (i < cellStateArr3.length) {
                cellStateArr3[i].drawCursor(canvas);
                i++;
            } else {
                canvas.restore();
                return;
            }
        }
    }

    private void initializeCells(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        float f = ((float) i3) * 0.32f;
        float f2 = ((float) i4) * 0.78f;
        float f3 = (-f) / 2.0f;
        float f4 = (float) ((-i4) / 2);
        float f5 = f / 2.0f;
        float f6 = (float) (i4 / 2);
        RectF rectF = new RectF(f3, f4, f5, f6);
        float f7 = (float) ((-i3) / 2);
        float f8 = (float) (i3 / 2);
        RectF rectF2 = new RectF(f7, f3, f8, f5);
        float f9 = (-f2) / 2.0f;
        float f10 = f2 / 2.0f;
        RectF rectF3 = new RectF(f9, f4, f10, f6);
        RectF rectF4 = new RectF(f7, f9, f8, f10);
        RectF rectF5 = new RectF(f7, f4, f8, f6);
        float[][] fArr = {new float[]{72.26f, 165.41f, 252.3f, 342.3f}, new float[]{78.0f, 131.2f, 107.5f, 17.8f}, new float[]{52.0f, 48.8f, 72.0f, 12.0f}, new float[]{50.0f, 102.05f}, new float[]{0.0f, 38.0f, 40.0f}, new float[]{0.0f, 90.0f, 52.0f}};
        float[][] fArr2 = {new float[]{35.45f, 30.07f, 35.0f, 35.4f}, new float[]{24.0f, 31.0f, -35.0f, 31.6f}, new float[]{26.0f, -31.0f, -31.0f, 26.0f}, new float[]{81.0f, -23.85f}, new float[]{52.0f, -26.0f, -40.0f}, new float[]{90.0f, -52.0f, -52.0f}};
        Path path = new Path();
        path.arcTo(rectF2, fArr[0][0], fArr2[0][0]);
        path.arcTo(rectF, fArr[0][1], fArr2[0][1]);
        path.arcTo(rectF2, fArr[0][2], fArr2[0][2]);
        path.arcTo(rectF, fArr[0][3], fArr2[0][3]);
        Path path2 = new Path();
        path2.arcTo(rectF4, fArr[1][0], fArr2[1][0]);
        path2.arcTo(rectF, fArr[1][1], fArr2[1][1]);
        path2.arcTo(rectF2, fArr[1][2], fArr2[1][2]);
        path2.arcTo(rectF, fArr[1][3], fArr2[1][3]);
        Path path3 = new Path();
        path3.arcTo(rectF4, fArr[2][0], fArr2[2][0]);
        path3.arcTo(rectF, fArr[2][1], fArr2[2][1]);
        path3.arcTo(rectF2, fArr[2][2], fArr2[2][2]);
        path3.arcTo(rectF3, fArr[2][3], fArr2[2][3]);
        Path path4 = new Path();
        path4.arcTo(rectF, fArr[3][0], fArr2[3][0]);
        path4.arcTo(rectF4, fArr[3][1], fArr2[3][1]);
        Path path5 = new Path();
        path5.arcTo(rectF4, fArr[4][0], fArr2[4][0]);
        path5.arcTo(rectF3, fArr[4][1], fArr2[4][1]);
        path5.arcTo(rectF2, fArr[4][2], fArr2[4][2]);
        Path path6 = new Path();
        path6.arcTo(rectF5, fArr[5][0], fArr2[5][0]);
        path6.arcTo(rectF3, fArr[5][1], fArr2[5][1]);
        path6.arcTo(rectF4, fArr[5][2], fArr2[5][2]);
        this.mCellConfigs = new CellConfig[]{new CellConfig(path6, 180), new CellConfig(path5, 90, true), new CellConfig(path4, 180), new CellConfig(path5, 270), new CellConfig(path6, 270), new CellConfig(path5, 180), new CellConfig(path3, 180), new CellConfig(path2, 180), new CellConfig(path3, 270), new CellConfig(path5, 0, true), new CellConfig(path4, 90), new CellConfig(path2, 90), new CellConfig(path, 0), new CellConfig(path2, 270), new CellConfig(path4, 270), new CellConfig(path5, 180, true), new CellConfig(path3, 90), new CellConfig(path2, 0), new CellConfig(path3, 0), new CellConfig(path5, 0), new CellConfig(path6, 90), new CellConfig(path5, 90), new CellConfig(path4, 0), new CellConfig(path5, 270, true), new CellConfig(path6, 0)};
    }

    private int getScrimNotEnrolledColor(int i, int i2) {
        if (this.mPrimaryCellIndices.isEmpty()) {
            return getScrimNotEnrolledColorWithoutPrimaryCells(i);
        }
        return getScrimNotEnrolledColorWithPrimaryCells(i2);
    }

    private int getScrimNotEnrolledColorWithoutPrimaryCells(int i) {
        return ColorUtils.setAlphaComponent(this.mScrimNotEnrolledDefaultColor, Math.round(getScrimNotEnrolledOpacity(i) * 255.0f));
    }

    private int getScrimNotEnrolledColorWithPrimaryCells(int i) {
        if (this.mPrimaryCellIndices.contains(Integer.valueOf(i))) {
            return this.mScrimNotEnrolledPrimaryColor;
        }
        return this.mScrimNotEnrolledSecondaryColor;
    }

    private static float getScrimNotEnrolledOpacity(int i) {
        for (int length = SCRIM_OPACITY_THRESHOLDS.length - 1; length >= 0; length--) {
            if (i >= SCRIM_OPACITY_THRESHOLDS[length]) {
                return SCRIM_OPACITY_VALUES[length];
            }
        }
        return SCRIM_OPACITY_VALUES[0];
    }
}
