package com.android.settingslib.collapsingtoolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.lang.reflect.Field;

public class AdjustableToolbarLayout extends CollapsingToolbarLayout {
    public AdjustableToolbarLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public AdjustableToolbarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AdjustableToolbarLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initCollapsingToolbar();
    }

    private void initCollapsingToolbar() {
        addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                view.removeOnLayoutChangeListener(this);
                int access$000 = AdjustableToolbarLayout.this.getLineCountWithReflection();
                if (access$000 > 2) {
                    ViewGroup.LayoutParams layoutParams = AdjustableToolbarLayout.this.getLayoutParams();
                    layoutParams.height = AdjustableToolbarLayout.this.getResources().getDimensionPixelSize(R$dimen.toolbar_three_lines_height);
                    AdjustableToolbarLayout.this.setLayoutParams(layoutParams);
                } else if (access$000 == 2) {
                    ViewGroup.LayoutParams layoutParams2 = AdjustableToolbarLayout.this.getLayoutParams();
                    layoutParams2.height = AdjustableToolbarLayout.this.getResources().getDimensionPixelSize(R$dimen.toolbar_two_lines_height);
                    AdjustableToolbarLayout.this.setLayoutParams(layoutParams2);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public int getLineCountWithReflection() {
        try {
            Field declaredField = getClass().getSuperclass().getDeclaredField("collapsingTextHelper");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(this);
            Field declaredField2 = obj.getClass().getDeclaredField("textLayout");
            declaredField2.setAccessible(true);
            Object obj2 = declaredField2.get(obj);
            return ((Integer) obj2.getClass().getDeclaredMethod("getLineCount", new Class[0]).invoke(obj2, new Object[0])).intValue();
        } catch (Exception unused) {
            return 0;
        }
    }
}
