package com.google.android.setupdesign.items;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.setupdesign.R$id;
import com.google.android.setupdesign.R$layout;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.util.LayoutStyler;
import com.google.android.setupdesign.view.CheckableLinearLayout;

public class ExpandableSwitchItem extends SwitchItem implements View.OnClickListener {
    private final AccessibilityDelegateCompat accessibilityDelegate = new AccessibilityDelegateCompat() {
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            if (ExpandableSwitchItem.this.isExpanded()) {
                accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_COLLAPSE;
            } else {
                accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_EXPAND;
            }
            accessibilityNodeInfoCompat.addAction(accessibilityActionCompat);
        }

        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (i != 262144 && i != 524288) {
                return super.performAccessibilityAction(view, i, bundle);
            }
            ExpandableSwitchItem expandableSwitchItem = ExpandableSwitchItem.this;
            expandableSwitchItem.setExpanded(!expandableSwitchItem.isExpanded());
            return true;
        }
    };
    private CharSequence collapsedSummary;
    private CharSequence expandedSummary;
    private boolean isExpanded = false;

    public ExpandableSwitchItem() {
        setIconGravity(48);
    }

    public ExpandableSwitchItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudExpandableSwitchItem);
        this.collapsedSummary = obtainStyledAttributes.getText(R$styleable.SudExpandableSwitchItem_sudCollapsedSummary);
        this.expandedSummary = obtainStyledAttributes.getText(R$styleable.SudExpandableSwitchItem_sudExpandedSummary);
        setIconGravity(obtainStyledAttributes.getInt(R$styleable.SudItem_sudIconGravity, 48));
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public int getDefaultLayoutResource() {
        return R$layout.sud_items_expandable_switch;
    }

    public CharSequence getSummary() {
        return this.isExpanded ? getExpandedSummary() : getCollapsedSummary();
    }

    public boolean isExpanded() {
        return this.isExpanded;
    }

    public void setExpanded(boolean z) {
        if (this.isExpanded != z) {
            this.isExpanded = z;
            notifyItemChanged();
        }
    }

    public CharSequence getCollapsedSummary() {
        return this.collapsedSummary;
    }

    public CharSequence getExpandedSummary() {
        return this.expandedSummary;
    }

    public void onBindView(View view) {
        super.onBindView(view);
        View findViewById = view.findViewById(R$id.sud_items_expandable_switch_content);
        findViewById.setOnClickListener(this);
        if (findViewById instanceof CheckableLinearLayout) {
            CheckableLinearLayout checkableLinearLayout = (CheckableLinearLayout) findViewById;
            checkableLinearLayout.setChecked(isExpanded());
            ViewCompat.setAccessibilityLiveRegion(checkableLinearLayout, isExpanded() ? 1 : 0);
            ViewCompat.setAccessibilityDelegate(checkableLinearLayout, this.accessibilityDelegate);
        }
        tintCompoundDrawables(view);
        view.setFocusable(false);
        LayoutStyler.applyPartnerCustomizationLayoutPaddingStyle(findViewById);
    }

    public void onClick(View view) {
        setExpanded(!isExpanded());
    }

    private void tintCompoundDrawables(View view) {
        TypedArray obtainStyledAttributes = view.getContext().obtainStyledAttributes(new int[]{16842806});
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(0);
        obtainStyledAttributes.recycle();
        if (colorStateList != null) {
            TextView textView = (TextView) view.findViewById(R$id.sud_items_title);
            for (Drawable drawable : textView.getCompoundDrawables()) {
                if (drawable != null) {
                    drawable.setColorFilter(colorStateList.getDefaultColor(), PorterDuff.Mode.SRC_IN);
                }
            }
            if (Build.VERSION.SDK_INT >= 17) {
                for (Drawable drawable2 : textView.getCompoundDrawablesRelative()) {
                    if (drawable2 != null) {
                        drawable2.setColorFilter(colorStateList.getDefaultColor(), PorterDuff.Mode.SRC_IN);
                    }
                }
            }
        }
    }
}
