package com.google.android.setupdesign.template;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.R$id;
import com.google.android.setupdesign.R$styleable;

public class ProgressBarMixin implements Mixin {
    private ColorStateList color;
    private final TemplateLayout templateLayout;
    private final boolean useBottomProgressBar;

    public ProgressBarMixin(TemplateLayout templateLayout2) {
        this(templateLayout2, (AttributeSet) null, 0);
    }

    public ProgressBarMixin(TemplateLayout templateLayout2, AttributeSet attributeSet, int i) {
        this.templateLayout = templateLayout2;
        boolean z = false;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = templateLayout2.getContext().obtainStyledAttributes(attributeSet, R$styleable.SudProgressBarMixin, i, 0);
            int i2 = R$styleable.SudProgressBarMixin_sudUseBottomProgressBar;
            boolean z2 = obtainStyledAttributes.hasValue(i2) ? obtainStyledAttributes.getBoolean(i2, false) : false;
            obtainStyledAttributes.recycle();
            setShown(false);
            z = z2;
        }
        this.useBottomProgressBar = z;
    }

    public boolean isShown() {
        View findManagedViewById = this.templateLayout.findManagedViewById(this.useBottomProgressBar ? R$id.sud_glif_progress_bar : R$id.sud_layout_progress);
        return findManagedViewById != null && findManagedViewById.getVisibility() == 0;
    }

    public void setShown(boolean z) {
        if (z) {
            ProgressBar progressBar = getProgressBar();
            if (progressBar != null) {
                progressBar.setVisibility(0);
                return;
            }
            return;
        }
        ProgressBar peekProgressBar = peekProgressBar();
        if (peekProgressBar != null) {
            peekProgressBar.setVisibility(this.useBottomProgressBar ? 4 : 8);
        }
    }

    private ProgressBar getProgressBar() {
        if (peekProgressBar() == null && !this.useBottomProgressBar) {
            ViewStub viewStub = (ViewStub) this.templateLayout.findManagedViewById(R$id.sud_layout_progress_stub);
            if (viewStub != null) {
                viewStub.inflate();
            }
            setColor(this.color);
        }
        return peekProgressBar();
    }

    public ProgressBar peekProgressBar() {
        return (ProgressBar) this.templateLayout.findManagedViewById(this.useBottomProgressBar ? R$id.sud_glif_progress_bar : R$id.sud_layout_progress);
    }

    public void setColor(ColorStateList colorStateList) {
        ProgressBar peekProgressBar;
        this.color = colorStateList;
        int i = Build.VERSION.SDK_INT;
        if (i >= 21 && (peekProgressBar = peekProgressBar()) != null) {
            peekProgressBar.setIndeterminateTintList(colorStateList);
            if (i >= 23 || colorStateList != null) {
                peekProgressBar.setProgressBackgroundTintList(colorStateList);
            }
        }
    }

    public ColorStateList getColor() {
        return this.color;
    }
}
