package com.google.android.libraries.material.autoresizetext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.widget.TextView;

public class AutoResizeTextView extends TextView {
    private final RectF availableSpaceRect = new RectF();
    private final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    private float lineSpacingExtra = 0.0f;
    private float lineSpacingMultiplier = 1.0f;
    private int maxLines;
    private float maxTextSize;
    private int maxWidth;
    private float minTextSize = 16.0f;
    private int resizeStepUnit = 0;
    private final TextPaint textPaint = new TextPaint();
    private final SparseIntArray textSizesCache = new SparseIntArray();

    public AutoResizeTextView(Context context) {
        super(context, (AttributeSet) null, 0);
        initialize(context, (AttributeSet) null, 0, 0);
    }

    public AutoResizeTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        initialize(context, attributeSet, 0, 0);
    }

    public AutoResizeTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context, attributeSet, i, 0);
    }

    public AutoResizeTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initialize(context, attributeSet, i, i2);
    }

    private void initialize(Context context, AttributeSet attributeSet, int i, int i2) {
        readAttrs(context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.AutoResizeTextView, i, i2));
        this.textPaint.set(getPaint());
    }

    public final int getMaxLines() {
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getMaxLines();
        }
        return this.maxLines;
    }

    public final void setMaxLines(int i) {
        super.setMaxLines(i);
        this.maxLines = i;
    }

    public final float getLineSpacingMultiplier() {
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getLineSpacingMultiplier();
        }
        return this.lineSpacingMultiplier;
    }

    public final float getLineSpacingExtra() {
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getLineSpacingExtra();
        }
        return this.lineSpacingExtra;
    }

    public final void setLineSpacing(float f, float f2) {
        super.setLineSpacing(f, f2);
        this.lineSpacingMultiplier = f2;
        this.lineSpacingExtra = f;
    }

    public final void setTextSize(int i, float f) {
        float applyDimension = TypedValue.applyDimension(i, f, this.displayMetrics);
        if (this.maxTextSize != applyDimension) {
            this.maxTextSize = applyDimension;
            this.textSizesCache.clear();
            requestLayout();
        }
    }

    public final void setMinTextSize(int i, float f) {
        float applyDimension = TypedValue.applyDimension(i, f, this.displayMetrics);
        if (this.minTextSize != applyDimension) {
            this.minTextSize = applyDimension;
            this.textSizesCache.clear();
            requestLayout();
        }
    }

    public final void setResizeStepUnit(int i) {
        if (this.resizeStepUnit != i) {
            this.resizeStepUnit = i;
            requestLayout();
        }
    }

    private void readAttrs(TypedArray typedArray) {
        this.resizeStepUnit = typedArray.getInt(R$styleable.AutoResizeTextView_autoResizeText_resizeStepUnit, 0);
        this.minTextSize = (float) ((int) typedArray.getDimension(R$styleable.AutoResizeTextView_autoResizeText_minTextSize, 16.0f));
        this.maxTextSize = (float) ((int) getTextSize());
    }

    private void adjustTextSize() {
        int measuredWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
        int measuredHeight = (getMeasuredHeight() - getPaddingBottom()) - getPaddingTop();
        if (measuredWidth > 0 && measuredHeight > 0) {
            this.maxWidth = measuredWidth;
            RectF rectF = this.availableSpaceRect;
            rectF.right = (float) measuredWidth;
            rectF.bottom = (float) measuredHeight;
            super.setTextSize(this.resizeStepUnit, computeTextSize((int) Math.ceil((double) convertToResizeStepUnits(this.minTextSize)), (int) Math.floor((double) convertToResizeStepUnits(this.maxTextSize)), this.availableSpaceRect));
        }
    }

    private boolean suggestedSizeFitsInSpace(float f, RectF rectF) {
        this.textPaint.setTextSize(f);
        String charSequence = getText().toString();
        int maxLines2 = getMaxLines();
        if (maxLines2 != 1) {
            StaticLayout staticLayout = new StaticLayout(charSequence, this.textPaint, this.maxWidth, Layout.Alignment.ALIGN_NORMAL, getLineSpacingMultiplier(), getLineSpacingExtra(), true);
            return (maxLines2 == -1 || staticLayout.getLineCount() <= maxLines2) && ((float) staticLayout.getHeight()) <= rectF.bottom;
        } else if (this.textPaint.getFontSpacing() > rectF.bottom || this.textPaint.measureText(charSequence) > rectF.right) {
            return false;
        } else {
            return true;
        }
    }

    private float computeTextSize(int i, int i2, RectF rectF) {
        CharSequence text = getText();
        if (text != null && this.textSizesCache.get(text.hashCode()) != 0) {
            return (float) this.textSizesCache.get(text.hashCode());
        }
        int binarySearchSizes = binarySearchSizes(i, i2, rectF);
        this.textSizesCache.put(text == null ? 0 : text.hashCode(), binarySearchSizes);
        return (float) binarySearchSizes;
    }

    private int binarySearchSizes(int i, int i2, RectF rectF) {
        int i3 = i + 1;
        while (i3 <= i2) {
            int i4 = (i3 + i2) / 2;
            if (suggestedSizeFitsInSpace(TypedValue.applyDimension(this.resizeStepUnit, (float) i4, this.displayMetrics), rectF)) {
                int i5 = i3;
                i3 = i4 + 1;
                i = i5;
            } else {
                i = i4 - 1;
                i2 = i;
            }
        }
        return i;
    }

    private float convertToResizeStepUnits(float f) {
        return f * (1.0f / TypedValue.applyDimension(this.resizeStepUnit, 1.0f, this.displayMetrics));
    }

    /* access modifiers changed from: protected */
    public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        super.onTextChanged(charSequence, i, i2, i3);
        adjustTextSize();
    }

    /* access modifiers changed from: protected */
    public final void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3 || i2 != i4) {
            this.textSizesCache.clear();
            adjustTextSize();
        }
    }

    /* access modifiers changed from: protected */
    public final void onMeasure(int i, int i2) {
        adjustTextSize();
        super.onMeasure(i, i2);
    }
}
