package com.google.android.setupdesign.items;

import android.content.Context;
import android.util.AttributeSet;

public abstract class AbstractItem extends AbstractItemHierarchy implements IItem {
    public int getCount() {
        return 1;
    }

    public IItem getItemAt(int i) {
        return this;
    }

    public AbstractItem() {
    }

    public AbstractItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void notifyItemChanged() {
        notifyItemRangeChanged(0, 1);
    }
}
