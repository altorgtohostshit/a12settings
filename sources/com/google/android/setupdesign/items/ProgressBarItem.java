package com.google.android.setupdesign.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.google.android.setupdesign.R$layout;

public class ProgressBarItem extends Item {
    public boolean isEnabled() {
        return false;
    }

    public void onBindView(View view) {
    }

    public ProgressBarItem() {
    }

    public ProgressBarItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public int getDefaultLayoutResource() {
        return R$layout.sud_items_progress_bar;
    }
}
