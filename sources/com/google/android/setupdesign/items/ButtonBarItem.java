package com.google.android.setupdesign.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.setupdesign.R$layout;
import com.google.android.setupdesign.items.ItemInflater;
import java.util.ArrayList;
import java.util.Iterator;

public class ButtonBarItem extends AbstractItem implements ItemInflater.ItemParent {
    private final ArrayList<ButtonItem> buttons = new ArrayList<>();
    private boolean visible = true;

    public boolean isEnabled() {
        return false;
    }

    public ButtonBarItem() {
    }

    public ButtonBarItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public int getCount() {
        return isVisible() ? 1 : 0;
    }

    public int getLayoutResource() {
        return R$layout.sud_items_button_bar;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public int getViewId() {
        return getId();
    }

    public void onBindView(View view) {
        LinearLayout linearLayout = (LinearLayout) view;
        linearLayout.removeAllViews();
        Iterator<ButtonItem> it = this.buttons.iterator();
        while (it.hasNext()) {
            linearLayout.addView(it.next().createButton((ViewGroup) linearLayout));
        }
        view.setId(getViewId());
    }

    public void addChild(ItemHierarchy itemHierarchy) {
        if (itemHierarchy instanceof ButtonItem) {
            this.buttons.add((ButtonItem) itemHierarchy);
            return;
        }
        throw new UnsupportedOperationException("Cannot add non-button item to Button Bar");
    }
}
