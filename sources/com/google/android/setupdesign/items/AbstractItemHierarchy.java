package com.google.android.setupdesign.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.items.ItemHierarchy;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractItemHierarchy implements ItemHierarchy {

    /* renamed from: id */
    private int f131id = -1;
    private final ArrayList<ItemHierarchy.Observer> observers = new ArrayList<>();

    public AbstractItemHierarchy() {
    }

    public AbstractItemHierarchy(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudAbstractItem);
        this.f131id = obtainStyledAttributes.getResourceId(R$styleable.SudAbstractItem_android_id, -1);
        obtainStyledAttributes.recycle();
    }

    public void setId(int i) {
        this.f131id = i;
    }

    public int getId() {
        return this.f131id;
    }

    public int getViewId() {
        return getId();
    }

    public void registerObserver(ItemHierarchy.Observer observer) {
        this.observers.add(observer);
    }

    public void notifyItemRangeChanged(int i, int i2) {
        if (i < 0) {
            Log.w("AbstractItemHierarchy", "notifyItemRangeChanged: Invalid position=" + i);
        } else if (i2 < 0) {
            Log.w("AbstractItemHierarchy", "notifyItemRangeChanged: Invalid itemCount=" + i2);
        } else {
            Iterator<ItemHierarchy.Observer> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onItemRangeChanged(this, i, i2);
            }
        }
    }

    public void notifyItemRangeInserted(int i, int i2) {
        if (i < 0) {
            Log.w("AbstractItemHierarchy", "notifyItemRangeInserted: Invalid position=" + i);
        } else if (i2 < 0) {
            Log.w("AbstractItemHierarchy", "notifyItemRangeInserted: Invalid itemCount=" + i2);
        } else {
            Iterator<ItemHierarchy.Observer> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onItemRangeInserted(this, i, i2);
            }
        }
    }
}
