package com.android.settings.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.android.settings.R;
import com.android.settings.R$styleable;

public class ChartSweepView extends View {
    private ChartAxis mAxis;
    private View.OnClickListener mClickListener;
    private Rect mContentOffset;
    private long mDragInterval;
    private int mFollowAxis;
    private int mLabelColor;
    private DynamicLayout mLabelLayout;
    private int mLabelMinSize;
    private float mLabelOffset;
    private float mLabelSize;
    private SpannableStringBuilder mLabelTemplate;
    private int mLabelTemplateRes;
    private long mLabelValue;
    private OnSweepListener mListener;
    private Rect mMargins;
    private float mNeighborMargin;
    private ChartSweepView[] mNeighbors;
    private Paint mOutlinePaint;
    private int mSafeRegion;
    private Drawable mSweep;
    private Point mSweepOffset;
    private Rect mSweepPadding;
    private int mTouchMode;
    private MotionEvent mTracking;
    private float mTrackingStart;
    private long mValidAfter;
    private ChartSweepView mValidAfterDynamic;
    private long mValidBefore;
    private ChartSweepView mValidBeforeDynamic;
    private long mValue;

    public interface OnSweepListener {
        void onSweep(ChartSweepView chartSweepView, boolean z);

        void requestEdit(ChartSweepView chartSweepView);
    }

    public void addOnLayoutChangeListener(View.OnLayoutChangeListener onLayoutChangeListener) {
    }

    public void removeOnLayoutChangeListener(View.OnLayoutChangeListener onLayoutChangeListener) {
    }

    public ChartSweepView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ChartSweepView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ChartSweepView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSweepPadding = new Rect();
        this.mContentOffset = new Rect();
        this.mSweepOffset = new Point();
        this.mMargins = new Rect();
        this.mOutlinePaint = new Paint();
        this.mTouchMode = 0;
        this.mDragInterval = 1;
        this.mNeighbors = new ChartSweepView[0];
        this.mClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                ChartSweepView.this.dispatchRequestEdit();
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ChartSweepView, i, 0);
        int color = obtainStyledAttributes.getColor(1, -16776961);
        setSweepDrawable(obtainStyledAttributes.getDrawable(6), color);
        setFollowAxis(obtainStyledAttributes.getInt(0, -1));
        setNeighborMargin((float) obtainStyledAttributes.getDimensionPixelSize(4, 0));
        setSafeRegion(obtainStyledAttributes.getDimensionPixelSize(5, 0));
        setLabelMinSize(obtainStyledAttributes.getDimensionPixelSize(2, 0));
        setLabelTemplate(obtainStyledAttributes.getResourceId(3, 0));
        setLabelColor(color);
        setBackgroundResource(R.drawable.data_usage_sweep_background);
        this.mOutlinePaint.setColor(-65536);
        this.mOutlinePaint.setStrokeWidth(1.0f);
        this.mOutlinePaint.setStyle(Paint.Style.STROKE);
        obtainStyledAttributes.recycle();
        setClickable(true);
        setOnClickListener(this.mClickListener);
        setWillNotDraw(false);
    }

    public void setNeighbors(ChartSweepView... chartSweepViewArr) {
        this.mNeighbors = chartSweepViewArr;
    }

    public int getFollowAxis() {
        return this.mFollowAxis;
    }

    public Rect getMargins() {
        return this.mMargins;
    }

    public void setDragInterval(long j) {
        this.mDragInterval = j;
    }

    private float getTargetInset() {
        float f;
        int i;
        if (this.mFollowAxis == 1) {
            int intrinsicHeight = this.mSweep.getIntrinsicHeight();
            Rect rect = this.mSweepPadding;
            int i2 = rect.top;
            f = ((float) i2) + (((float) ((intrinsicHeight - i2) - rect.bottom)) / 2.0f);
            i = this.mSweepOffset.y;
        } else {
            int intrinsicWidth = this.mSweep.getIntrinsicWidth();
            Rect rect2 = this.mSweepPadding;
            int i3 = rect2.left;
            f = ((float) i3) + (((float) ((intrinsicWidth - i3) - rect2.right)) / 2.0f);
            i = this.mSweepOffset.x;
        }
        return f + ((float) i);
    }

    private void dispatchOnSweep(boolean z) {
        OnSweepListener onSweepListener = this.mListener;
        if (onSweepListener != null) {
            onSweepListener.onSweep(this, z);
        }
    }

    /* access modifiers changed from: private */
    public void dispatchRequestEdit() {
        OnSweepListener onSweepListener = this.mListener;
        if (onSweepListener != null) {
            onSweepListener.requestEdit(this);
        }
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        setFocusable(z);
        requestLayout();
    }

    public void setSweepDrawable(Drawable drawable, int i) {
        Drawable drawable2 = this.mSweep;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback) null);
            unscheduleDrawable(this.mSweep);
        }
        if (drawable != null) {
            drawable.setCallback(this);
            if (drawable.isStateful()) {
                drawable.setState(getDrawableState());
            }
            drawable.setVisible(getVisibility() == 0, false);
            this.mSweep = drawable;
            drawable.setTint(i);
            drawable.getPadding(this.mSweepPadding);
        } else {
            this.mSweep = null;
        }
        invalidate();
    }

    public void setFollowAxis(int i) {
        this.mFollowAxis = i;
    }

    public void setLabelMinSize(int i) {
        this.mLabelMinSize = i;
        invalidateLabelTemplate();
    }

    public void setLabelTemplate(int i) {
        this.mLabelTemplateRes = i;
        invalidateLabelTemplate();
    }

    public void setLabelColor(int i) {
        this.mLabelColor = i;
        invalidateLabelTemplate();
    }

    private void invalidateLabelTemplate() {
        if (this.mLabelTemplateRes != 0) {
            CharSequence text = getResources().getText(this.mLabelTemplateRes);
            TextPaint textPaint = new TextPaint(1);
            textPaint.density = getResources().getDisplayMetrics().density;
            textPaint.setCompatibilityScaling(getResources().getCompatibilityInfo().applicationScale);
            textPaint.setColor(this.mLabelColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
            this.mLabelTemplate = spannableStringBuilder;
            this.mLabelLayout = DynamicLayout.Builder.obtain(spannableStringBuilder, textPaint, 1024).setAlignment(Layout.Alignment.ALIGN_RIGHT).setIncludePad(false).setUseLineSpacingFromFallbacks(true).build();
            invalidateLabel();
        } else {
            this.mLabelTemplate = null;
            this.mLabelLayout = null;
        }
        invalidate();
        requestLayout();
    }

    private void invalidateLabel() {
        ChartAxis chartAxis;
        if (this.mLabelTemplate == null || (chartAxis = this.mAxis) == null) {
            this.mLabelValue = this.mValue;
            return;
        }
        this.mLabelValue = chartAxis.buildLabel(getResources(), this.mLabelTemplate, this.mValue);
        setContentDescription(this.mLabelTemplate);
        invalidateLabelOffset();
        invalidate();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0029, code lost:
        if (r0 < 0.0f) goto L_0x002b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void invalidateLabelOffset() {
        /*
            r4 = this;
            int r0 = r4.mFollowAxis
            r1 = 0
            r2 = 1
            if (r0 != r2) goto L_0x0059
            com.android.settings.widget.ChartSweepView r0 = r4.mValidAfterDynamic
            r2 = 1073741824(0x40000000, float:2.0)
            if (r0 == 0) goto L_0x002e
            float r0 = getLabelWidth(r4)
            com.android.settings.widget.ChartSweepView r3 = r4.mValidAfterDynamic
            float r3 = getLabelWidth(r3)
            float r0 = java.lang.Math.max(r0, r3)
            r4.mLabelSize = r0
            com.android.settings.widget.ChartSweepView r0 = r4.mValidAfterDynamic
            float r0 = getLabelTop(r0)
            float r3 = getLabelBottom(r4)
            float r0 = r0 - r3
            int r3 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r3 >= 0) goto L_0x0059
        L_0x002b:
            float r1 = r0 / r2
            goto L_0x0059
        L_0x002e:
            com.android.settings.widget.ChartSweepView r0 = r4.mValidBeforeDynamic
            if (r0 == 0) goto L_0x0053
            float r0 = getLabelWidth(r4)
            com.android.settings.widget.ChartSweepView r3 = r4.mValidBeforeDynamic
            float r3 = getLabelWidth(r3)
            float r0 = java.lang.Math.max(r0, r3)
            r4.mLabelSize = r0
            float r0 = getLabelTop(r4)
            com.android.settings.widget.ChartSweepView r3 = r4.mValidBeforeDynamic
            float r3 = getLabelBottom(r3)
            float r0 = r0 - r3
            int r3 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r3 >= 0) goto L_0x0059
            float r0 = -r0
            goto L_0x002b
        L_0x0053:
            float r0 = getLabelWidth(r4)
            r4.mLabelSize = r0
        L_0x0059:
            float r0 = r4.mLabelSize
            int r2 = r4.mLabelMinSize
            float r2 = (float) r2
            float r0 = java.lang.Math.max(r0, r2)
            r4.mLabelSize = r0
            float r0 = r4.mLabelOffset
            int r0 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1))
            if (r0 == 0) goto L_0x007d
            r4.mLabelOffset = r1
            r4.invalidate()
            com.android.settings.widget.ChartSweepView r0 = r4.mValidAfterDynamic
            if (r0 == 0) goto L_0x0076
            r0.invalidateLabelOffset()
        L_0x0076:
            com.android.settings.widget.ChartSweepView r4 = r4.mValidBeforeDynamic
            if (r4 == 0) goto L_0x007d
            r4.invalidateLabelOffset()
        L_0x007d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.widget.ChartSweepView.invalidateLabelOffset():void");
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mSweep;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        Drawable drawable = this.mSweep;
        if (drawable != null) {
            drawable.setVisible(i == 0, false);
        }
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable drawable) {
        return drawable == this.mSweep || super.verifyDrawable(drawable);
    }

    public ChartAxis getAxis() {
        return this.mAxis;
    }

    public void setValue(long j) {
        this.mValue = j;
        invalidateLabel();
    }

    public long getValue() {
        return this.mValue;
    }

    public long getLabelValue() {
        return this.mLabelValue;
    }

    public float getPoint() {
        if (isEnabled()) {
            return this.mAxis.convertToPoint(this.mValue);
        }
        return 0.0f;
    }

    public void setNeighborMargin(float f) {
        this.mNeighborMargin = f;
    }

    public void setSafeRegion(int i) {
        this.mSafeRegion = i;
    }

    public boolean isTouchCloserTo(MotionEvent motionEvent, ChartSweepView chartSweepView) {
        return chartSweepView.getTouchDistanceFromTarget(motionEvent) < getTouchDistanceFromTarget(motionEvent);
    }

    private float getTouchDistanceFromTarget(MotionEvent motionEvent) {
        if (this.mFollowAxis == 0) {
            return Math.abs(motionEvent.getX() - (getX() + getTargetInset()));
        }
        return Math.abs(motionEvent.getY() - (getY() + getTargetInset()));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00f8, code lost:
        if (r12.getX() < ((float) r11.mLabelLayout.getWidth())) goto L_0x00fa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0126, code lost:
        if (r12.getY() < ((float) r11.mLabelLayout.getHeight())) goto L_0x00fa;
     */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0140  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x014e  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x017d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r12) {
        /*
            r11 = this;
            boolean r0 = r11.isEnabled()
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            android.view.ViewParent r0 = r11.getParent()
            android.view.View r0 = (android.view.View) r0
            int r2 = r12.getAction()
            r3 = 2
            r4 = 1
            if (r2 == 0) goto L_0x00cc
            if (r2 == r4) goto L_0x00a9
            if (r2 == r3) goto L_0x001b
            return r1
        L_0x001b:
            int r0 = r11.mTouchMode
            if (r0 != r3) goto L_0x0020
            return r4
        L_0x0020:
            android.view.ViewParent r0 = r11.getParent()
            r0.requestDisallowInterceptTouchEvent(r4)
            android.graphics.Rect r0 = r11.getParentContentRect()
            android.graphics.Rect r2 = r11.computeClampRect(r0)
            boolean r3 = r2.isEmpty()
            if (r3 == 0) goto L_0x0036
            return r4
        L_0x0036:
            int r3 = r11.mFollowAxis
            if (r3 != r4) goto L_0x006c
            int r3 = r11.getTop()
            android.graphics.Rect r5 = r11.mMargins
            int r5 = r5.top
            int r3 = r3 - r5
            float r3 = (float) r3
            float r5 = r11.mTrackingStart
            float r12 = r12.getRawY()
            android.view.MotionEvent r6 = r11.mTracking
            float r6 = r6.getRawY()
            float r12 = r12 - r6
            float r5 = r5 + r12
            int r12 = r2.top
            float r12 = (float) r12
            int r2 = r2.bottom
            float r2 = (float) r2
            float r12 = android.util.MathUtils.constrain(r5, r12, r2)
            float r2 = r12 - r3
            r11.setTranslationY(r2)
            com.android.settings.widget.ChartAxis r2 = r11.mAxis
            int r0 = r0.top
            float r0 = (float) r0
            float r12 = r12 - r0
            long r2 = r2.convertToValue(r12)
            goto L_0x009d
        L_0x006c:
            int r3 = r11.getLeft()
            android.graphics.Rect r5 = r11.mMargins
            int r5 = r5.left
            int r3 = r3 - r5
            float r3 = (float) r3
            float r5 = r11.mTrackingStart
            float r12 = r12.getRawX()
            android.view.MotionEvent r6 = r11.mTracking
            float r6 = r6.getRawX()
            float r12 = r12 - r6
            float r5 = r5 + r12
            int r12 = r2.left
            float r12 = (float) r12
            int r2 = r2.right
            float r2 = (float) r2
            float r12 = android.util.MathUtils.constrain(r5, r12, r2)
            float r2 = r12 - r3
            r11.setTranslationX(r2)
            com.android.settings.widget.ChartAxis r2 = r11.mAxis
            int r0 = r0.left
            float r0 = (float) r0
            float r12 = r12 - r0
            long r2 = r2.convertToValue(r12)
        L_0x009d:
            long r5 = r11.mDragInterval
            long r5 = r2 % r5
            long r2 = r2 - r5
            r11.setValue(r2)
            r11.dispatchOnSweep(r1)
            return r4
        L_0x00a9:
            int r12 = r11.mTouchMode
            if (r12 != r3) goto L_0x00b1
            r11.performClick()
            goto L_0x00c9
        L_0x00b1:
            if (r12 != r4) goto L_0x00c9
            r12 = 0
            r11.mTrackingStart = r12
            r0 = 0
            r11.mTracking = r0
            long r2 = r11.mLabelValue
            r11.mValue = r2
            r11.dispatchOnSweep(r4)
            r11.setTranslationX(r12)
            r11.setTranslationY(r12)
            r11.requestLayout()
        L_0x00c9:
            r11.mTouchMode = r1
            return r4
        L_0x00cc:
            int r2 = r11.mFollowAxis
            if (r2 != r4) goto L_0x00fe
            float r2 = r12.getX()
            int r5 = r11.getWidth()
            android.graphics.Rect r6 = r11.mSweepPadding
            int r6 = r6.right
            int r6 = r6 * 8
            int r5 = r5 - r6
            float r5 = (float) r5
            int r2 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r2 <= 0) goto L_0x00e6
            r2 = r4
            goto L_0x00e7
        L_0x00e6:
            r2 = r1
        L_0x00e7:
            android.text.DynamicLayout r5 = r11.mLabelLayout
            if (r5 == 0) goto L_0x00fc
            float r5 = r12.getX()
            android.text.DynamicLayout r6 = r11.mLabelLayout
            int r6 = r6.getWidth()
            float r6 = (float) r6
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 >= 0) goto L_0x00fc
        L_0x00fa:
            r5 = r4
            goto L_0x0129
        L_0x00fc:
            r5 = r1
            goto L_0x0129
        L_0x00fe:
            float r2 = r12.getY()
            int r5 = r11.getHeight()
            android.graphics.Rect r6 = r11.mSweepPadding
            int r6 = r6.bottom
            int r6 = r6 * 8
            int r5 = r5 - r6
            float r5 = (float) r5
            int r2 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r2 <= 0) goto L_0x0114
            r2 = r4
            goto L_0x0115
        L_0x0114:
            r2 = r1
        L_0x0115:
            android.text.DynamicLayout r5 = r11.mLabelLayout
            if (r5 == 0) goto L_0x00fc
            float r5 = r12.getY()
            android.text.DynamicLayout r6 = r11.mLabelLayout
            int r6 = r6.getHeight()
            float r6 = (float) r6
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 >= 0) goto L_0x00fc
            goto L_0x00fa
        L_0x0129:
            android.view.MotionEvent r6 = r12.copy()
            int r7 = r11.getLeft()
            float r7 = (float) r7
            int r8 = r11.getTop()
            float r8 = (float) r8
            r6.offsetLocation(r7, r8)
            com.android.settings.widget.ChartSweepView[] r7 = r11.mNeighbors
            int r8 = r7.length
            r9 = r1
        L_0x013e:
            if (r9 >= r8) goto L_0x014c
            r10 = r7[r9]
            boolean r10 = r11.isTouchCloserTo(r6, r10)
            if (r10 == 0) goto L_0x0149
            return r1
        L_0x0149:
            int r9 = r9 + 1
            goto L_0x013e
        L_0x014c:
            if (r2 == 0) goto L_0x017d
            int r1 = r11.mFollowAxis
            if (r1 != r4) goto L_0x015f
            int r1 = r11.getTop()
            android.graphics.Rect r2 = r11.mMargins
            int r2 = r2.top
            int r1 = r1 - r2
            float r1 = (float) r1
            r11.mTrackingStart = r1
            goto L_0x016b
        L_0x015f:
            int r1 = r11.getLeft()
            android.graphics.Rect r2 = r11.mMargins
            int r2 = r2.left
            int r1 = r1 - r2
            float r1 = (float) r1
            r11.mTrackingStart = r1
        L_0x016b:
            android.view.MotionEvent r12 = r12.copy()
            r11.mTracking = r12
            r11.mTouchMode = r4
            boolean r11 = r0.isActivated()
            if (r11 != 0) goto L_0x017c
            r0.setActivated(r4)
        L_0x017c:
            return r4
        L_0x017d:
            if (r5 == 0) goto L_0x0182
            r11.mTouchMode = r3
            return r4
        L_0x0182:
            r11.mTouchMode = r1
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.widget.ChartSweepView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private Rect getParentContentRect() {
        View view = (View) getParent();
        return new Rect(view.getPaddingLeft(), view.getPaddingTop(), view.getWidth() - view.getPaddingRight(), view.getHeight() - view.getPaddingBottom());
    }

    private long getValidAfterDynamic() {
        ChartSweepView chartSweepView = this.mValidAfterDynamic;
        if (chartSweepView == null || !chartSweepView.isEnabled()) {
            return Long.MIN_VALUE;
        }
        return chartSweepView.getValue();
    }

    private long getValidBeforeDynamic() {
        ChartSweepView chartSweepView = this.mValidBeforeDynamic;
        if (chartSweepView == null || !chartSweepView.isEnabled()) {
            return Long.MAX_VALUE;
        }
        return chartSweepView.getValue();
    }

    private Rect computeClampRect(Rect rect) {
        Rect buildClampRect = buildClampRect(rect, this.mValidAfter, this.mValidBefore, 0.0f);
        if (!buildClampRect.intersect(buildClampRect(rect, getValidAfterDynamic(), getValidBeforeDynamic(), this.mNeighborMargin))) {
            buildClampRect.setEmpty();
        }
        return buildClampRect;
    }

    private Rect buildClampRect(Rect rect, long j, long j2, float f) {
        ChartAxis chartAxis = this.mAxis;
        boolean z = false;
        boolean z2 = (j == Long.MIN_VALUE || j == Long.MAX_VALUE) ? false : true;
        if (!(j2 == Long.MIN_VALUE || j2 == Long.MAX_VALUE)) {
            z = true;
        }
        float convertToPoint = chartAxis.convertToPoint(j) + f;
        float convertToPoint2 = this.mAxis.convertToPoint(j2) - f;
        Rect rect2 = new Rect(rect);
        if (this.mFollowAxis == 1) {
            if (z) {
                rect2.bottom = rect2.top + ((int) convertToPoint2);
            }
            if (z2) {
                rect2.top = (int) (((float) rect2.top) + convertToPoint);
            }
        } else {
            if (z) {
                rect2.right = rect2.left + ((int) convertToPoint2);
            }
            if (z2) {
                rect2.left = (int) (((float) rect2.left) + convertToPoint);
            }
        }
        return rect2;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mSweep.isStateful()) {
            this.mSweep.setState(getDrawableState());
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (!isEnabled() || this.mLabelLayout == null) {
            Point point = this.mSweepOffset;
            point.x = 0;
            point.y = 0;
            setMeasuredDimension(this.mSweep.getIntrinsicWidth(), this.mSweep.getIntrinsicHeight());
        } else {
            int intrinsicHeight = this.mSweep.getIntrinsicHeight();
            int height = this.mLabelLayout.getHeight();
            Point point2 = this.mSweepOffset;
            point2.x = 0;
            point2.y = 0;
            point2.y = (int) (((float) (height / 2)) - getTargetInset());
            setMeasuredDimension(this.mSweep.getIntrinsicWidth(), Math.max(intrinsicHeight, height));
        }
        if (this.mFollowAxis == 1) {
            int intrinsicHeight2 = this.mSweep.getIntrinsicHeight();
            Rect rect = this.mSweepPadding;
            int i3 = rect.top;
            int i4 = (intrinsicHeight2 - i3) - rect.bottom;
            Rect rect2 = this.mMargins;
            rect2.top = -(i3 + (i4 / 2));
            rect2.bottom = 0;
            rect2.left = -rect.left;
            rect2.right = rect.right;
        } else {
            int intrinsicWidth = this.mSweep.getIntrinsicWidth();
            Rect rect3 = this.mSweepPadding;
            int i5 = rect3.left;
            int i6 = (intrinsicWidth - i5) - rect3.right;
            Rect rect4 = this.mMargins;
            rect4.left = -(i5 + (i6 / 2));
            rect4.right = 0;
            rect4.top = -rect3.top;
            rect4.bottom = rect3.bottom;
        }
        this.mContentOffset.set(0, 0, 0, 0);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (this.mFollowAxis == 0) {
            int i7 = measuredWidth * 3;
            setMeasuredDimension(i7, measuredHeight);
            Rect rect5 = this.mContentOffset;
            rect5.left = (i7 - measuredWidth) / 2;
            int i8 = this.mSweepPadding.bottom * 2;
            rect5.bottom -= i8;
            this.mMargins.bottom += i8;
        } else {
            int i9 = measuredHeight * 2;
            setMeasuredDimension(measuredWidth, i9);
            this.mContentOffset.offset(0, (i9 - measuredHeight) / 2);
            int i10 = this.mSweepPadding.right * 2;
            this.mContentOffset.right -= i10;
            this.mMargins.right += i10;
        }
        Point point3 = this.mSweepOffset;
        Rect rect6 = this.mContentOffset;
        point3.offset(rect6.left, rect6.top);
        Rect rect7 = this.mMargins;
        Point point4 = this.mSweepOffset;
        rect7.offset(-point4.x, -point4.y);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        invalidateLabelOffset();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (!isEnabled() || this.mLabelLayout == null) {
            i = 0;
        } else {
            int save = canvas.save();
            Rect rect = this.mContentOffset;
            canvas.translate(((float) rect.left) + (this.mLabelSize - 1024.0f), ((float) rect.top) + this.mLabelOffset);
            this.mLabelLayout.draw(canvas);
            canvas.restoreToCount(save);
            i = ((int) this.mLabelSize) + this.mSafeRegion;
        }
        if (this.mFollowAxis == 1) {
            Drawable drawable = this.mSweep;
            int i2 = this.mSweepOffset.y;
            drawable.setBounds(i, i2, width + this.mContentOffset.right, drawable.getIntrinsicHeight() + i2);
        } else {
            Drawable drawable2 = this.mSweep;
            int i3 = this.mSweepOffset.x;
            drawable2.setBounds(i3, i, drawable2.getIntrinsicWidth() + i3, height + this.mContentOffset.bottom);
        }
        this.mSweep.draw(canvas);
    }

    public static float getLabelTop(ChartSweepView chartSweepView) {
        return chartSweepView.getY() + ((float) chartSweepView.mContentOffset.top);
    }

    public static float getLabelBottom(ChartSweepView chartSweepView) {
        return getLabelTop(chartSweepView) + ((float) chartSweepView.mLabelLayout.getHeight());
    }

    public static float getLabelWidth(ChartSweepView chartSweepView) {
        return Layout.getDesiredWidth(chartSweepView.mLabelLayout.getText(), chartSweepView.mLabelLayout.getPaint());
    }
}
