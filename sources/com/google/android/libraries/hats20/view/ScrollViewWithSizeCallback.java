package com.google.android.libraries.hats20.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ScrollViewWithSizeCallback extends ScrollView {
    private OnHeightChangedListener onHeightChangedListener;

    interface OnHeightChangedListener {
        void onHeightChanged(int i);
    }

    public ScrollViewWithSizeCallback(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setOnHeightChangedListener(OnHeightChangedListener onHeightChangedListener2) {
        this.onHeightChangedListener = onHeightChangedListener2;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        OnHeightChangedListener onHeightChangedListener2 = this.onHeightChangedListener;
        if (onHeightChangedListener2 != null && i4 != i2) {
            onHeightChangedListener2.onHeightChanged(i2);
        }
    }
}
