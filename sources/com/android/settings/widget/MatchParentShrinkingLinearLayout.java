package com.android.settings.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewHierarchyEncoder;
import com.android.internal.R;

public class MatchParentShrinkingLinearLayout extends ViewGroup {
    @ViewDebug.ExportedProperty(category = "layout")
    private boolean mBaselineAligned;
    @ViewDebug.ExportedProperty(category = "layout")
    private int mBaselineAlignedChildIndex;
    @ViewDebug.ExportedProperty(category = "measurement")
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    @ViewDebug.ExportedProperty(category = "measurement", flagMapping = {@ViewDebug.FlagToString(equals = -1, mask = -1, name = "NONE"), @ViewDebug.FlagToString(equals = 0, mask = 0, name = "NONE"), @ViewDebug.FlagToString(equals = 48, mask = 48, name = "TOP"), @ViewDebug.FlagToString(equals = 80, mask = 80, name = "BOTTOM"), @ViewDebug.FlagToString(equals = 3, mask = 3, name = "LEFT"), @ViewDebug.FlagToString(equals = 5, mask = 5, name = "RIGHT"), @ViewDebug.FlagToString(equals = 8388611, mask = 8388611, name = "START"), @ViewDebug.FlagToString(equals = 8388613, mask = 8388613, name = "END"), @ViewDebug.FlagToString(equals = 16, mask = 16, name = "CENTER_VERTICAL"), @ViewDebug.FlagToString(equals = 112, mask = 112, name = "FILL_VERTICAL"), @ViewDebug.FlagToString(equals = 1, mask = 1, name = "CENTER_HORIZONTAL"), @ViewDebug.FlagToString(equals = 7, mask = 7, name = "FILL_HORIZONTAL"), @ViewDebug.FlagToString(equals = 17, mask = 17, name = "CENTER"), @ViewDebug.FlagToString(equals = 119, mask = 119, name = "FILL"), @ViewDebug.FlagToString(equals = 8388608, mask = 8388608, name = "RELATIVE")}, formatToHexString = true)
    private int mGravity;
    private int mLayoutDirection;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    @ViewDebug.ExportedProperty(category = "measurement")
    private int mOrientation;
    private int mShowDividers;
    @ViewDebug.ExportedProperty(category = "measurement")
    private int mTotalLength;
    @ViewDebug.ExportedProperty(category = "layout")
    private boolean mUseLargestChild;
    @ViewDebug.ExportedProperty(category = "layout")
    private float mWeightSum;

    /* access modifiers changed from: package-private */
    public int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int getLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int getNextLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int measureNullChild(int i) {
        return 0;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public MatchParentShrinkingLinearLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public MatchParentShrinkingLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MatchParentShrinkingLinearLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public MatchParentShrinkingLinearLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        this.mLayoutDirection = -1;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayout, i, i2);
        int i3 = obtainStyledAttributes.getInt(1, -1);
        if (i3 >= 0) {
            setOrientation(i3);
        }
        int i4 = obtainStyledAttributes.getInt(0, -1);
        if (i4 >= 0) {
            setGravity(i4);
        }
        boolean z = obtainStyledAttributes.getBoolean(2, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(4, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(3, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(6, false);
        setDividerDrawable(obtainStyledAttributes.getDrawable(5));
        this.mShowDividers = obtainStyledAttributes.getInt(7, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(8, 0);
        obtainStyledAttributes.recycle();
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable != this.mDivider) {
            this.mDivider = drawable;
            boolean z = false;
            if (drawable != null) {
                this.mDividerWidth = drawable.getIntrinsicWidth();
                this.mDividerHeight = drawable.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            if (drawable == null) {
                z = true;
            }
            setWillNotDraw(z);
            requestLayout();
        }
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void drawDividersVertical(Canvas canvas) {
        int i;
        int virtualChildCount = getVirtualChildCount();
        for (int i2 = 0; i2 < virtualChildCount; i2++) {
            View virtualChildAt = getVirtualChildAt(i2);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i2))) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((LayoutParams) virtualChildAt.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 == null) {
                i = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                i = virtualChildAt2.getBottom() + ((LayoutParams) virtualChildAt2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: package-private */
    public void drawDividersHorizontal(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        int virtualChildCount = getVirtualChildCount();
        boolean isLayoutRtl = isLayoutRtl();
        for (int i5 = 0; i5 < virtualChildCount; i5++) {
            View virtualChildAt = getVirtualChildAt(i5);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i5))) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (isLayoutRtl) {
                    i4 = virtualChildAt.getRight() + layoutParams.rightMargin;
                } else {
                    i4 = (virtualChildAt.getLeft() - layoutParams.leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, i4);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (isLayoutRtl) {
                    i3 = virtualChildAt2.getLeft() - layoutParams2.leftMargin;
                    i2 = this.mDividerWidth;
                } else {
                    i = virtualChildAt2.getRight() + layoutParams2.rightMargin;
                    drawVerticalDivider(canvas, i);
                }
            } else if (isLayoutRtl) {
                i = getPaddingLeft();
                drawVerticalDivider(canvas, i);
            } else {
                i3 = getWidth() - getPaddingRight();
                i2 = this.mDividerWidth;
            }
            i = i3 - i2;
            drawVerticalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: package-private */
    public void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    /* access modifiers changed from: package-private */
    public void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    @RemotableViewMethod
    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    @RemotableViewMethod
    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    public int getBaseline() {
        int i;
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i2 = this.mBaselineAlignedChildIndex;
        if (childCount > i2) {
            View childAt = getChildAt(i2);
            int baseline = childAt.getBaseline();
            if (baseline != -1) {
                int i3 = this.mBaselineChildTop;
                if (this.mOrientation == 1 && (i = this.mGravity & 112) != 48) {
                    if (i == 16) {
                        i3 += ((((this.mBottom - this.mTop) - this.mPaddingTop) - this.mPaddingBottom) - this.mTotalLength) / 2;
                    } else if (i == 80) {
                        i3 = ((this.mBottom - this.mTop) - this.mPaddingBottom) - this.mTotalLength;
                    }
                }
                return i3 + ((LayoutParams) childAt.getLayoutParams()).topMargin + baseline;
            } else if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            } else {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
        } else {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    @RemotableViewMethod
    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
        }
        this.mBaselineAlignedChildIndex = i;
    }

    /* access modifiers changed from: package-private */
    public View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    /* access modifiers changed from: package-private */
    public int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    @RemotableViewMethod
    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public boolean hasDividerBeforeChildAt(int i) {
        if (i == 0) {
            return (this.mShowDividers & 1) != 0;
        }
        if (i == getChildCount()) {
            if ((this.mShowDividers & 4) != 0) {
                return true;
            }
            return false;
        } else if ((this.mShowDividers & 2) == 0) {
            return false;
        } else {
            for (int i2 = i - 1; i2 >= 0; i2--) {
                if (getChildAt(i2).getVisibility() != 8) {
                    return true;
                }
            }
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x0364  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x0370  */
    /* JADX WARNING: Removed duplicated region for block: B:155:0x0373  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void measureVertical(int r33, int r34) {
        /*
            r32 = this;
            r7 = r32
            r8 = r33
            r9 = r34
            r10 = 0
            r7.mTotalLength = r10
            int r11 = r32.getVirtualChildCount()
            int r12 = android.view.View.MeasureSpec.getMode(r33)
            int r13 = android.view.View.MeasureSpec.getMode(r34)
            int r14 = r7.mBaselineAlignedChildIndex
            boolean r15 = r7.mUseLargestChild
            r16 = 0
            r17 = 1
            r1 = r10
            r3 = r1
            r4 = r3
            r5 = r4
            r18 = r5
            r20 = r18
            r0 = r16
            r19 = r17
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
        L_0x002b:
            r6 = 8
            r22 = r4
            if (r5 >= r11) goto L_0x01a3
            android.view.View r4 = r7.getVirtualChildAt(r5)
            if (r4 != 0) goto L_0x004a
            int r4 = r7.mTotalLength
            int r6 = r7.measureNullChild(r5)
            int r4 = r4 + r6
            r7.mTotalLength = r4
            r23 = r11
            r25 = r13
            r4 = r22
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            goto L_0x0197
        L_0x004a:
            r25 = r1
            int r1 = r4.getVisibility()
            if (r1 != r6) goto L_0x0063
            int r1 = r7.getChildrenSkipCount(r4, r5)
            int r5 = r5 + r1
            r23 = r11
            r4 = r22
            r1 = r25
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            r25 = r13
            goto L_0x0197
        L_0x0063:
            boolean r1 = r7.hasDividerBeforeChildAt(r5)
            if (r1 == 0) goto L_0x0070
            int r1 = r7.mTotalLength
            int r6 = r7.mDividerHeight
            int r1 = r1 + r6
            r7.mTotalLength = r1
        L_0x0070:
            android.view.ViewGroup$LayoutParams r1 = r4.getLayoutParams()
            r6 = r1
            com.android.settings.widget.MatchParentShrinkingLinearLayout$LayoutParams r6 = (com.android.settings.widget.MatchParentShrinkingLinearLayout.LayoutParams) r6
            float r1 = r6.weight
            float r26 = r0 + r1
            r0 = 1073741824(0x40000000, float:2.0)
            if (r13 != r0) goto L_0x00ac
            int r0 = r6.height
            if (r0 != 0) goto L_0x00ac
            int r0 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1))
            if (r0 <= 0) goto L_0x00ac
            int r0 = r7.mTotalLength
            int r1 = r6.topMargin
            int r1 = r1 + r0
            r27 = r2
            int r2 = r6.bottomMargin
            int r1 = r1 + r2
            int r0 = java.lang.Math.max(r0, r1)
            r7.mTotalLength = r0
            r21 = r3
            r3 = r4
            r23 = r11
            r18 = r17
            r28 = r22
            r8 = r25
            r2 = r27
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            r11 = r5
            r25 = r13
            r13 = r6
            goto L_0x011f
        L_0x00ac:
            r27 = r2
            int r0 = r6.height
            if (r0 != 0) goto L_0x00bb
            int r0 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1))
            if (r0 <= 0) goto L_0x00bb
            r0 = -2
            r6.height = r0
            r2 = 0
            goto L_0x00bd
        L_0x00bb:
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
        L_0x00bd:
            r28 = 0
            int r0 = (r26 > r16 ? 1 : (r26 == r16 ? 0 : -1))
            if (r0 != 0) goto L_0x00c8
            int r0 = r7.mTotalLength
            r24 = r0
            goto L_0x00ca
        L_0x00c8:
            r24 = 0
        L_0x00ca:
            r29 = 1073741824(0x40000000, float:2.0)
            r0 = r32
            r8 = r25
            r1 = r4
            r31 = r2
            r30 = r27
            r2 = r5
            r9 = r3
            r3 = r33
            r23 = r11
            r25 = r13
            r13 = r22
            r11 = r29
            r22 = r4
            r4 = r28
            r11 = r5
            r5 = r34
            r21 = r9
            r28 = r13
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            r13 = r6
            r6 = r24
            r0.measureChildBeforeLayout(r1, r2, r3, r4, r5, r6)
            r0 = r31
            if (r0 == r9) goto L_0x00fa
            r13.height = r0
        L_0x00fa:
            int r0 = r22.getMeasuredHeight()
            int r1 = r7.mTotalLength
            int r2 = r1 + r0
            int r3 = r13.topMargin
            int r2 = r2 + r3
            int r3 = r13.bottomMargin
            int r2 = r2 + r3
            r3 = r22
            int r4 = r7.getNextLocationOffset(r3)
            int r2 = r2 + r4
            int r1 = java.lang.Math.max(r1, r2)
            r7.mTotalLength = r1
            r1 = r30
            if (r15 == 0) goto L_0x011e
            int r2 = java.lang.Math.max(r0, r1)
            goto L_0x011f
        L_0x011e:
            r2 = r1
        L_0x011f:
            if (r14 < 0) goto L_0x0129
            int r5 = r11 + 1
            if (r14 != r5) goto L_0x0129
            int r0 = r7.mTotalLength
            r7.mBaselineChildTop = r0
        L_0x0129:
            if (r11 >= r14) goto L_0x013a
            float r0 = r13.weight
            int r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1))
            if (r0 > 0) goto L_0x0132
            goto L_0x013a
        L_0x0132:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex."
            r0.<init>(r1)
            throw r0
        L_0x013a:
            r0 = 1073741824(0x40000000, float:2.0)
            if (r12 == r0) goto L_0x0148
            int r0 = r13.width
            r1 = -1
            if (r0 != r1) goto L_0x0148
            r0 = r17
            r20 = r0
            goto L_0x0149
        L_0x0148:
            r0 = 0
        L_0x0149:
            int r1 = r13.leftMargin
            int r4 = r13.rightMargin
            int r1 = r1 + r4
            int r4 = r3.getMeasuredWidth()
            int r4 = r4 + r1
            int r5 = java.lang.Math.max(r8, r4)
            int r6 = r3.getMeasuredState()
            int r6 = android.view.ViewGroup.combineMeasuredStates(r10, r6)
            if (r19 == 0) goto L_0x0169
            int r8 = r13.width
            r10 = -1
            if (r8 != r10) goto L_0x0169
            r8 = r17
            goto L_0x016a
        L_0x0169:
            r8 = 0
        L_0x016a:
            float r10 = r13.weight
            int r10 = (r10 > r16 ? 1 : (r10 == r16 ? 0 : -1))
            if (r10 <= 0) goto L_0x017e
            if (r0 == 0) goto L_0x0173
            goto L_0x0174
        L_0x0173:
            r1 = r4
        L_0x0174:
            r13 = r21
            int r0 = java.lang.Math.max(r13, r1)
            r13 = r0
            r4 = r28
            goto L_0x018a
        L_0x017e:
            r13 = r21
            if (r0 == 0) goto L_0x0183
            goto L_0x0184
        L_0x0183:
            r1 = r4
        L_0x0184:
            r4 = r28
            int r4 = java.lang.Math.max(r4, r1)
        L_0x018a:
            int r0 = r7.getChildrenSkipCount(r3, r11)
            int r0 = r0 + r11
            r1 = r5
            r10 = r6
            r19 = r8
            r3 = r13
            r5 = r0
            r0 = r26
        L_0x0197:
            int r5 = r5 + 1
            r8 = r33
            r9 = r34
            r11 = r23
            r13 = r25
            goto L_0x002b
        L_0x01a3:
            r8 = r1
            r1 = r2
            r23 = r11
            r25 = r13
            r4 = r22
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            r13 = r3
            int r2 = r7.mTotalLength
            if (r2 <= 0) goto L_0x01c2
            r2 = r23
            boolean r3 = r7.hasDividerBeforeChildAt(r2)
            if (r3 == 0) goto L_0x01c4
            int r3 = r7.mTotalLength
            int r5 = r7.mDividerHeight
            int r3 = r3 + r5
            r7.mTotalLength = r3
            goto L_0x01c4
        L_0x01c2:
            r2 = r23
        L_0x01c4:
            r3 = r25
            if (r15 == 0) goto L_0x0211
            if (r3 == r9) goto L_0x01cc
            if (r3 != 0) goto L_0x0211
        L_0x01cc:
            r5 = 0
            r7.mTotalLength = r5
            r5 = 0
        L_0x01d0:
            if (r5 >= r2) goto L_0x0211
            android.view.View r9 = r7.getVirtualChildAt(r5)
            if (r9 != 0) goto L_0x01e2
            int r9 = r7.mTotalLength
            int r11 = r7.measureNullChild(r5)
            int r9 = r9 + r11
            r7.mTotalLength = r9
            goto L_0x020c
        L_0x01e2:
            int r11 = r9.getVisibility()
            if (r11 != r6) goto L_0x01ee
            int r9 = r7.getChildrenSkipCount(r9, r5)
            int r5 = r5 + r9
            goto L_0x020c
        L_0x01ee:
            android.view.ViewGroup$LayoutParams r11 = r9.getLayoutParams()
            com.android.settings.widget.MatchParentShrinkingLinearLayout$LayoutParams r11 = (com.android.settings.widget.MatchParentShrinkingLinearLayout.LayoutParams) r11
            int r14 = r7.mTotalLength
            int r21 = r14 + r1
            int r6 = r11.topMargin
            int r21 = r21 + r6
            int r6 = r11.bottomMargin
            int r21 = r21 + r6
            int r6 = r7.getNextLocationOffset(r9)
            int r6 = r21 + r6
            int r6 = java.lang.Math.max(r14, r6)
            r7.mTotalLength = r6
        L_0x020c:
            int r5 = r5 + 1
            r6 = 8
            goto L_0x01d0
        L_0x0211:
            int r5 = r7.mTotalLength
            int r6 = r7.mPaddingTop
            int r9 = r7.mPaddingBottom
            int r6 = r6 + r9
            int r5 = r5 + r6
            r7.mTotalLength = r5
            int r6 = r32.getSuggestedMinimumHeight()
            int r5 = java.lang.Math.max(r5, r6)
            r6 = r34
            r9 = r13
            r11 = 0
            int r5 = android.view.ViewGroup.resolveSizeAndState(r5, r6, r11)
            r11 = 16777215(0xffffff, float:2.3509886E-38)
            r11 = r11 & r5
            int r13 = r7.mTotalLength
            int r11 = r11 - r13
            if (r18 != 0) goto L_0x027e
            if (r11 == 0) goto L_0x023b
            int r13 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1))
            if (r13 <= 0) goto L_0x023b
            goto L_0x027e
        L_0x023b:
            int r0 = java.lang.Math.max(r4, r9)
            if (r15 == 0) goto L_0x0277
            r4 = 1073741824(0x40000000, float:2.0)
            if (r3 == r4) goto L_0x0277
            r3 = 0
        L_0x0246:
            if (r3 >= r2) goto L_0x0277
            android.view.View r4 = r7.getVirtualChildAt(r3)
            if (r4 == 0) goto L_0x0274
            int r9 = r4.getVisibility()
            r11 = 8
            if (r9 != r11) goto L_0x0257
            goto L_0x0274
        L_0x0257:
            android.view.ViewGroup$LayoutParams r9 = r4.getLayoutParams()
            com.android.settings.widget.MatchParentShrinkingLinearLayout$LayoutParams r9 = (com.android.settings.widget.MatchParentShrinkingLinearLayout.LayoutParams) r9
            float r9 = r9.weight
            int r9 = (r9 > r16 ? 1 : (r9 == r16 ? 0 : -1))
            if (r9 <= 0) goto L_0x0274
            int r9 = r4.getMeasuredWidth()
            r11 = 1073741824(0x40000000, float:2.0)
            int r9 = android.view.View.MeasureSpec.makeMeasureSpec(r9, r11)
            int r13 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r11)
            r4.measure(r9, r13)
        L_0x0274:
            int r3 = r3 + 1
            goto L_0x0246
        L_0x0277:
            r23 = r2
            r1 = r8
            r2 = r33
            goto L_0x03ac
        L_0x027e:
            float r1 = r7.mWeightSum
            int r9 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1))
            if (r9 <= 0) goto L_0x0285
            r0 = r1
        L_0x0285:
            r1 = 0
            r7.mTotalLength = r1
            r9 = r1
        L_0x0289:
            if (r9 >= r2) goto L_0x039c
            android.view.View r13 = r7.getVirtualChildAt(r9)
            int r14 = r13.getVisibility()
            r15 = 8
            if (r14 != r15) goto L_0x029d
            r23 = r2
            r2 = r33
            goto L_0x0395
        L_0x029d:
            android.view.ViewGroup$LayoutParams r14 = r13.getLayoutParams()
            com.android.settings.widget.MatchParentShrinkingLinearLayout$LayoutParams r14 = (com.android.settings.widget.MatchParentShrinkingLinearLayout.LayoutParams) r14
            float r1 = r14.weight
            int r18 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1))
            if (r18 <= 0) goto L_0x02fa
            if (r11 <= 0) goto L_0x02fa
            float r15 = (float) r11
            float r15 = r15 * r1
            float r15 = r15 / r0
            int r15 = (int) r15
            float r0 = r0 - r1
            int r11 = r11 - r15
            int r1 = r7.mPaddingLeft
            r18 = r0
            int r0 = r7.mPaddingRight
            int r1 = r1 + r0
            int r0 = r14.leftMargin
            int r1 = r1 + r0
            int r0 = r14.rightMargin
            int r1 = r1 + r0
            int r0 = r14.width
            r23 = r2
            r2 = r33
            int r0 = android.view.ViewGroup.getChildMeasureSpec(r2, r1, r0)
            int r1 = r14.height
            if (r1 != 0) goto L_0x02dd
            r1 = 1073741824(0x40000000, float:2.0)
            if (r3 == r1) goto L_0x02d1
            goto L_0x02df
        L_0x02d1:
            if (r15 <= 0) goto L_0x02d4
            goto L_0x02d5
        L_0x02d4:
            r15 = 0
        L_0x02d5:
            int r15 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r1)
            r13.measure(r0, r15)
            goto L_0x02ef
        L_0x02dd:
            r1 = 1073741824(0x40000000, float:2.0)
        L_0x02df:
            int r21 = r13.getMeasuredHeight()
            int r15 = r21 + r15
            if (r15 >= 0) goto L_0x02e8
            r15 = 0
        L_0x02e8:
            int r15 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r1)
            r13.measure(r0, r15)
        L_0x02ef:
            int r0 = r13.getMeasuredState()
            r0 = r0 & -256(0xffffffffffffff00, float:NaN)
            int r10 = android.view.ViewGroup.combineMeasuredStates(r10, r0)
            goto L_0x033e
        L_0x02fa:
            r23 = r2
            r2 = r33
            if (r11 >= 0) goto L_0x033c
            int r1 = r14.height
            r15 = -1
            if (r1 != r15) goto L_0x033c
            int r1 = r7.mPaddingLeft
            int r15 = r7.mPaddingRight
            int r1 = r1 + r15
            int r15 = r14.leftMargin
            int r1 = r1 + r15
            int r15 = r14.rightMargin
            int r1 = r1 + r15
            int r15 = r14.width
            int r1 = android.view.ViewGroup.getChildMeasureSpec(r2, r1, r15)
            int r15 = r13.getMeasuredHeight()
            int r15 = r15 + r11
            if (r15 >= 0) goto L_0x031e
            r15 = 0
        L_0x031e:
            int r18 = r13.getMeasuredHeight()
            int r18 = r15 - r18
            int r11 = r11 - r18
            r18 = r0
            r0 = 1073741824(0x40000000, float:2.0)
            int r15 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r0)
            r13.measure(r1, r15)
            int r0 = r13.getMeasuredState()
            r0 = r0 & -256(0xffffffffffffff00, float:NaN)
            int r10 = android.view.ViewGroup.combineMeasuredStates(r10, r0)
            goto L_0x033e
        L_0x033c:
            r18 = r0
        L_0x033e:
            r0 = r18
            int r1 = r14.leftMargin
            int r15 = r14.rightMargin
            int r1 = r1 + r15
            int r15 = r13.getMeasuredWidth()
            int r15 = r15 + r1
            int r8 = java.lang.Math.max(r8, r15)
            r18 = r0
            r0 = 1073741824(0x40000000, float:2.0)
            if (r12 == r0) goto L_0x035e
            int r0 = r14.width
            r21 = r1
            r1 = -1
            if (r0 != r1) goto L_0x0361
            r0 = r17
            goto L_0x0362
        L_0x035e:
            r21 = r1
            r1 = -1
        L_0x0361:
            r0 = 0
        L_0x0362:
            if (r0 == 0) goto L_0x0366
            r15 = r21
        L_0x0366:
            int r0 = java.lang.Math.max(r4, r15)
            if (r19 == 0) goto L_0x0373
            int r4 = r14.width
            if (r4 != r1) goto L_0x0373
            r4 = r17
            goto L_0x0374
        L_0x0373:
            r4 = 0
        L_0x0374:
            int r15 = r7.mTotalLength
            int r19 = r13.getMeasuredHeight()
            int r19 = r15 + r19
            int r1 = r14.topMargin
            int r19 = r19 + r1
            int r1 = r14.bottomMargin
            int r19 = r19 + r1
            int r1 = r7.getNextLocationOffset(r13)
            int r1 = r19 + r1
            int r1 = java.lang.Math.max(r15, r1)
            r7.mTotalLength = r1
            r19 = r4
            r4 = r0
            r0 = r18
        L_0x0395:
            int r9 = r9 + 1
            r2 = r23
            r1 = 0
            goto L_0x0289
        L_0x039c:
            r23 = r2
            r2 = r33
            int r0 = r7.mTotalLength
            int r1 = r7.mPaddingTop
            int r3 = r7.mPaddingBottom
            int r1 = r1 + r3
            int r0 = r0 + r1
            r7.mTotalLength = r0
            r0 = r4
            r1 = r8
        L_0x03ac:
            if (r19 != 0) goto L_0x03b3
            r3 = 1073741824(0x40000000, float:2.0)
            if (r12 == r3) goto L_0x03b3
            goto L_0x03b4
        L_0x03b3:
            r0 = r1
        L_0x03b4:
            int r1 = r7.mPaddingLeft
            int r3 = r7.mPaddingRight
            int r1 = r1 + r3
            int r0 = r0 + r1
            int r1 = r32.getSuggestedMinimumWidth()
            int r0 = java.lang.Math.max(r0, r1)
            int r0 = android.view.ViewGroup.resolveSizeAndState(r0, r2, r10)
            r7.setMeasuredDimension(r0, r5)
            if (r20 == 0) goto L_0x03d0
            r0 = r23
            r7.forceUniformWidth(r0, r6)
        L_0x03d0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.widget.MatchParentShrinkingLinearLayout.measureVertical(int, int):void");
    }

    private void forceUniformWidth(int i, int i2) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.width == -1) {
                    int i4 = layoutParams.height;
                    layoutParams.height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i2, 0);
                    layoutParams.height = i4;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void measureHorizontal(int i, int i2) {
        throw new IllegalStateException("horizontal mode not supported.");
    }

    /* access modifiers changed from: package-private */
    public void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0091  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutVertical(int r18, int r19, int r20, int r21) {
        /*
            r17 = this;
            r6 = r17
            int r7 = r6.mPaddingLeft
            int r0 = r20 - r18
            int r1 = r6.mPaddingRight
            int r8 = r0 - r1
            int r0 = r0 - r7
            int r9 = r0 - r1
            int r10 = r17.getVirtualChildCount()
            int r0 = r6.mGravity
            r1 = r0 & 112(0x70, float:1.57E-43)
            r2 = 8388615(0x800007, float:1.1754953E-38)
            r11 = r0 & r2
            r0 = 16
            if (r1 == r0) goto L_0x002f
            r0 = 80
            if (r1 == r0) goto L_0x0025
            int r0 = r6.mPaddingTop
            goto L_0x0039
        L_0x0025:
            int r0 = r6.mPaddingTop
            int r0 = r0 + r21
            int r0 = r0 - r19
            int r1 = r6.mTotalLength
            int r0 = r0 - r1
            goto L_0x0039
        L_0x002f:
            int r0 = r6.mPaddingTop
            int r1 = r21 - r19
            int r2 = r6.mTotalLength
            int r1 = r1 - r2
            int r1 = r1 / 2
            int r0 = r0 + r1
        L_0x0039:
            r1 = 0
            r12 = r1
        L_0x003b:
            if (r12 >= r10) goto L_0x00bd
            android.view.View r13 = r6.getVirtualChildAt(r12)
            r14 = 1
            if (r13 != 0) goto L_0x004b
            int r1 = r6.measureNullChild(r12)
            int r0 = r0 + r1
            goto L_0x00b9
        L_0x004b:
            int r1 = r13.getVisibility()
            r2 = 8
            if (r1 == r2) goto L_0x00b9
            int r4 = r13.getMeasuredWidth()
            int r15 = r13.getMeasuredHeight()
            android.view.ViewGroup$LayoutParams r1 = r13.getLayoutParams()
            r5 = r1
            com.android.settings.widget.MatchParentShrinkingLinearLayout$LayoutParams r5 = (com.android.settings.widget.MatchParentShrinkingLinearLayout.LayoutParams) r5
            int r1 = r5.gravity
            if (r1 >= 0) goto L_0x0067
            r1 = r11
        L_0x0067:
            int r2 = r17.getLayoutDirection()
            int r1 = android.view.Gravity.getAbsoluteGravity(r1, r2)
            r1 = r1 & 7
            if (r1 == r14) goto L_0x007f
            r2 = 5
            if (r1 == r2) goto L_0x007a
            int r1 = r5.leftMargin
            int r1 = r1 + r7
            goto L_0x008a
        L_0x007a:
            int r1 = r8 - r4
            int r2 = r5.rightMargin
            goto L_0x0089
        L_0x007f:
            int r1 = r9 - r4
            int r1 = r1 / 2
            int r1 = r1 + r7
            int r2 = r5.leftMargin
            int r1 = r1 + r2
            int r2 = r5.rightMargin
        L_0x0089:
            int r1 = r1 - r2
        L_0x008a:
            r2 = r1
            boolean r1 = r6.hasDividerBeforeChildAt(r12)
            if (r1 == 0) goto L_0x0094
            int r1 = r6.mDividerHeight
            int r0 = r0 + r1
        L_0x0094:
            int r1 = r5.topMargin
            int r16 = r0 + r1
            int r0 = r6.getLocationOffset(r13)
            int r3 = r16 + r0
            r0 = r17
            r1 = r13
            r14 = r5
            r5 = r15
            r0.setChildFrame(r1, r2, r3, r4, r5)
            int r0 = r14.bottomMargin
            int r15 = r15 + r0
            int r0 = r6.getNextLocationOffset(r13)
            int r15 = r15 + r0
            int r16 = r16 + r15
            int r0 = r6.getChildrenSkipCount(r13, r12)
            int r12 = r12 + r0
            r0 = r16
            r1 = 1
            goto L_0x00ba
        L_0x00b9:
            r1 = r14
        L_0x00ba:
            int r12 = r12 + r1
            goto L_0x003b
        L_0x00bd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.widget.MatchParentShrinkingLinearLayout.layoutVertical(int, int, int, int):void");
    }

    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        if (i != this.mLayoutDirection) {
            this.mLayoutDirection = i;
            if (this.mOrientation == 0) {
                requestLayout();
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a1  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00f1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutHorizontal(int r25, int r26, int r27, int r28) {
        /*
            r24 = this;
            r6 = r24
            boolean r0 = r24.isLayoutRtl()
            int r7 = r6.mPaddingTop
            int r1 = r28 - r26
            int r2 = r6.mPaddingBottom
            int r8 = r1 - r2
            int r1 = r1 - r7
            int r9 = r1 - r2
            int r10 = r24.getVirtualChildCount()
            int r1 = r6.mGravity
            r2 = 8388615(0x800007, float:1.1754953E-38)
            r2 = r2 & r1
            r11 = r1 & 112(0x70, float:1.57E-43)
            boolean r12 = r6.mBaselineAligned
            int[] r13 = r6.mMaxAscent
            int[] r14 = r6.mMaxDescent
            int r1 = r24.getLayoutDirection()
            int r1 = android.view.Gravity.getAbsoluteGravity(r2, r1)
            r15 = 2
            r5 = 1
            if (r1 == r5) goto L_0x003f
            r2 = 5
            if (r1 == r2) goto L_0x0035
            int r1 = r6.mPaddingLeft
            goto L_0x0048
        L_0x0035:
            int r1 = r6.mPaddingLeft
            int r1 = r1 + r27
            int r1 = r1 - r25
            int r2 = r6.mTotalLength
            int r1 = r1 - r2
            goto L_0x0048
        L_0x003f:
            int r1 = r6.mPaddingLeft
            int r2 = r27 - r25
            int r3 = r6.mTotalLength
            int r2 = r2 - r3
            int r2 = r2 / r15
            int r1 = r1 + r2
        L_0x0048:
            r2 = 0
            if (r0 == 0) goto L_0x0052
            int r0 = r10 + -1
            r16 = r0
            r17 = -1
            goto L_0x0056
        L_0x0052:
            r16 = r2
            r17 = r5
        L_0x0056:
            r3 = r2
        L_0x0057:
            if (r3 >= r10) goto L_0x013a
            int r0 = r17 * r3
            int r2 = r16 + r0
            android.view.View r0 = r6.getVirtualChildAt(r2)
            if (r0 != 0) goto L_0x0072
            int r0 = r6.measureNullChild(r2)
            int r1 = r1 + r0
            r21 = r5
            r22 = r7
            r19 = r10
            r20 = r11
            goto L_0x012b
        L_0x0072:
            int r5 = r0.getVisibility()
            r15 = 8
            if (r5 == r15) goto L_0x0121
            int r15 = r0.getMeasuredWidth()
            int r5 = r0.getMeasuredHeight()
            android.view.ViewGroup$LayoutParams r18 = r0.getLayoutParams()
            r4 = r18
            com.android.settings.widget.MatchParentShrinkingLinearLayout$LayoutParams r4 = (com.android.settings.widget.MatchParentShrinkingLinearLayout.LayoutParams) r4
            r18 = r3
            if (r12 == 0) goto L_0x009a
            int r3 = r4.height
            r19 = r10
            r10 = -1
            if (r3 == r10) goto L_0x009c
            int r10 = r0.getBaseline()
            goto L_0x009d
        L_0x009a:
            r19 = r10
        L_0x009c:
            r10 = -1
        L_0x009d:
            int r3 = r4.gravity
            if (r3 >= 0) goto L_0x00a2
            r3 = r11
        L_0x00a2:
            r3 = r3 & 112(0x70, float:1.57E-43)
            r20 = r11
            r11 = 16
            if (r3 == r11) goto L_0x00dd
            r11 = 48
            if (r3 == r11) goto L_0x00cb
            r11 = 80
            if (r3 == r11) goto L_0x00b5
            r3 = r7
            r11 = -1
            goto L_0x00da
        L_0x00b5:
            int r3 = r8 - r5
            int r11 = r4.bottomMargin
            int r3 = r3 - r11
            r11 = -1
            if (r10 == r11) goto L_0x00da
            int r21 = r0.getMeasuredHeight()
            int r21 = r21 - r10
            r10 = 2
            r22 = r14[r10]
            int r22 = r22 - r21
            int r3 = r3 - r22
            goto L_0x00da
        L_0x00cb:
            r11 = -1
            int r3 = r4.topMargin
            int r3 = r3 + r7
            if (r10 == r11) goto L_0x00da
            r21 = 1
            r22 = r13[r21]
            int r22 = r22 - r10
            int r3 = r3 + r22
            goto L_0x00eb
        L_0x00da:
            r21 = 1
            goto L_0x00eb
        L_0x00dd:
            r11 = -1
            r21 = 1
            int r3 = r9 - r5
            r10 = 2
            int r3 = r3 / r10
            int r3 = r3 + r7
            int r10 = r4.topMargin
            int r3 = r3 + r10
            int r10 = r4.bottomMargin
            int r3 = r3 - r10
        L_0x00eb:
            boolean r10 = r6.hasDividerBeforeChildAt(r2)
            if (r10 == 0) goto L_0x00f4
            int r10 = r6.mDividerWidth
            int r1 = r1 + r10
        L_0x00f4:
            int r10 = r4.leftMargin
            int r10 = r10 + r1
            int r1 = r6.getLocationOffset(r0)
            int r22 = r10 + r1
            r1 = r0
            r0 = r24
            r25 = r1
            r11 = r2
            r2 = r22
            r22 = r7
            r23 = -1
            r7 = r4
            r4 = r15
            r0.setChildFrame(r1, r2, r3, r4, r5)
            int r0 = r7.rightMargin
            int r15 = r15 + r0
            r0 = r25
            int r1 = r6.getNextLocationOffset(r0)
            int r15 = r15 + r1
            int r10 = r10 + r15
            int r0 = r6.getChildrenSkipCount(r0, r11)
            int r3 = r18 + r0
            r1 = r10
            goto L_0x012d
        L_0x0121:
            r18 = r3
            r22 = r7
            r19 = r10
            r20 = r11
            r21 = 1
        L_0x012b:
            r23 = -1
        L_0x012d:
            int r3 = r3 + 1
            r10 = r19
            r11 = r20
            r5 = r21
            r7 = r22
            r15 = 2
            goto L_0x0057
        L_0x013a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.widget.MatchParentShrinkingLinearLayout.layoutHorizontal(int, int, int, int):void");
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    @RemotableViewMethod
    public void setGravity(int i) {
        if (this.mGravity != i) {
            if ((8388615 & i) == 0) {
                i |= 8388611;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravity = i;
            requestLayout();
        }
    }

    @RemotableViewMethod
    public void setHorizontalGravity(int i) {
        int i2 = i & 8388615;
        int i3 = this.mGravity;
        if ((8388615 & i3) != i2) {
            this.mGravity = i2 | (-8388616 & i3);
            requestLayout();
        }
    }

    @RemotableViewMethod
    public void setVerticalGravity(int i) {
        int i2 = i & 112;
        int i3 = this.mGravity;
        if ((i3 & 112) != i2) {
            this.mGravity = i2 | (i3 & -113);
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public CharSequence getAccessibilityClassName() {
        return MatchParentShrinkingLinearLayout.class.getName();
    }

    /* access modifiers changed from: protected */
    public void encodeProperties(ViewHierarchyEncoder viewHierarchyEncoder) {
        super.encodeProperties(viewHierarchyEncoder);
        viewHierarchyEncoder.addProperty("layout:baselineAligned", this.mBaselineAligned);
        viewHierarchyEncoder.addProperty("layout:baselineAlignedChildIndex", this.mBaselineAlignedChildIndex);
        viewHierarchyEncoder.addProperty("measurement:baselineChildTop", this.mBaselineChildTop);
        viewHierarchyEncoder.addProperty("measurement:orientation", this.mOrientation);
        viewHierarchyEncoder.addProperty("measurement:gravity", this.mGravity);
        viewHierarchyEncoder.addProperty("measurement:totalLength", this.mTotalLength);
        viewHierarchyEncoder.addProperty("layout:totalLength", this.mTotalLength);
        viewHierarchyEncoder.addProperty("layout:useLargestChild", this.mUseLargestChild);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        @ViewDebug.ExportedProperty(category = "layout", mapping = {@ViewDebug.IntToString(from = -1, to = "NONE"), @ViewDebug.IntToString(from = 0, to = "NONE"), @ViewDebug.IntToString(from = 48, to = "TOP"), @ViewDebug.IntToString(from = 80, to = "BOTTOM"), @ViewDebug.IntToString(from = 3, to = "LEFT"), @ViewDebug.IntToString(from = 5, to = "RIGHT"), @ViewDebug.IntToString(from = 8388611, to = "START"), @ViewDebug.IntToString(from = 8388613, to = "END"), @ViewDebug.IntToString(from = 16, to = "CENTER_VERTICAL"), @ViewDebug.IntToString(from = 112, to = "FILL_VERTICAL"), @ViewDebug.IntToString(from = 1, to = "CENTER_HORIZONTAL"), @ViewDebug.IntToString(from = 7, to = "FILL_HORIZONTAL"), @ViewDebug.IntToString(from = 17, to = "CENTER"), @ViewDebug.IntToString(from = 119, to = "FILL")})
        public int gravity;
        @ViewDebug.ExportedProperty(category = "layout")
        public float weight;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayout_Layout);
            this.weight = obtainStyledAttributes.getFloat(3, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(0, -1);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
        }

        public String debug(String str) {
            return str + "MatchParentShrinkingLinearLayout.LayoutParams={width=" + ViewGroup.MarginLayoutParams.sizeToString(this.width) + ", height=" + ViewGroup.MarginLayoutParams.sizeToString(this.height) + " weight=" + this.weight + "}";
        }

        /* access modifiers changed from: protected */
        public void encodeProperties(ViewHierarchyEncoder viewHierarchyEncoder) {
            super.encodeProperties(viewHierarchyEncoder);
            viewHierarchyEncoder.addProperty("layout:weight", this.weight);
            viewHierarchyEncoder.addProperty("layout:gravity", this.gravity);
        }
    }
}
