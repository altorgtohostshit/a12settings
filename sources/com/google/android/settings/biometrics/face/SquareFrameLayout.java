package com.google.android.settings.biometrics.face;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class SquareFrameLayout extends FrameLayout {
    private int mOuterRegionChild;
    private int mPaddingDp;

    public SquareFrameLayout(Context context) {
        super(context);
    }

    public SquareFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SquareFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public SquareFrameLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setOuterRegion(int i, int i2) {
        this.mOuterRegionChild = i;
        this.mPaddingDp = i2;
    }

    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        setMeasuredDimension(size, size);
        int dpToPx = (int) Utils.dpToPx(getContext(), this.mPaddingDp);
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
            View childAt = getChildAt(i3);
            childAt.measure(makeMeasureSpec, makeMeasureSpec);
            if (childAt.getId() != this.mOuterRegionChild) {
                childAt.setPadding(dpToPx, dpToPx, dpToPx, dpToPx);
            }
        }
    }
}
