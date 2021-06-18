package com.google.android.setupcompat.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Window;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.R$styleable;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;

public class SystemNavBarMixin implements Mixin {
    final boolean applyPartnerResources;
    private int sucSystemNavBarBackgroundColor = 0;
    private final TemplateLayout templateLayout;
    final boolean useFullDynamicColor;
    private final Window windowOfActivity;

    public SystemNavBarMixin(TemplateLayout templateLayout2, Window window) {
        boolean z = false;
        this.templateLayout = templateLayout2;
        this.windowOfActivity = window;
        boolean z2 = templateLayout2 instanceof PartnerCustomizationLayout;
        this.applyPartnerResources = z2 && ((PartnerCustomizationLayout) templateLayout2).shouldApplyPartnerResource();
        if (z2 && ((PartnerCustomizationLayout) templateLayout2).useFullDynamicColor()) {
            z = true;
        }
        this.useFullDynamicColor = z;
    }

    public void applyPartnerCustomizations(AttributeSet attributeSet, int i) {
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 27) {
            TypedArray obtainStyledAttributes = this.templateLayout.getContext().obtainStyledAttributes(attributeSet, R$styleable.SucSystemNavBarMixin, i, 0);
            int color = obtainStyledAttributes.getColor(R$styleable.SucSystemNavBarMixin_sucSystemNavBarBackgroundColor, 0);
            this.sucSystemNavBarBackgroundColor = color;
            setSystemNavBarBackground(color);
            setLightSystemNavBar(obtainStyledAttributes.getBoolean(R$styleable.SucSystemNavBarMixin_sucLightSystemNavBar, isLightSystemNavBar()));
            if (i2 >= 28) {
                TypedArray obtainStyledAttributes2 = this.templateLayout.getContext().obtainStyledAttributes(new int[]{16844141});
                setSystemNavBarDividerColor(obtainStyledAttributes.getColor(R$styleable.SucSystemNavBarMixin_sucSystemNavBarDividerColor, obtainStyledAttributes2.getColor(0, 0)));
                obtainStyledAttributes2.recycle();
            }
            obtainStyledAttributes.recycle();
        }
    }

    public void setSystemNavBarBackground(int i) {
        if (Build.VERSION.SDK_INT >= 21 && this.windowOfActivity != null) {
            if (this.applyPartnerResources && !this.useFullDynamicColor) {
                Context context = this.templateLayout.getContext();
                i = PartnerConfigHelper.get(context).getColor(context, PartnerConfig.CONFIG_NAVIGATION_BAR_BG_COLOR);
            }
            this.windowOfActivity.setNavigationBarColor(i);
        }
    }

    public void setLightSystemNavBar(boolean z) {
        if (Build.VERSION.SDK_INT >= 26 && this.windowOfActivity != null) {
            if (this.applyPartnerResources) {
                Context context = this.templateLayout.getContext();
                z = PartnerConfigHelper.get(context).getBoolean(context, PartnerConfig.CONFIG_LIGHT_NAVIGATION_BAR, false);
            }
            if (z) {
                this.windowOfActivity.getDecorView().setSystemUiVisibility(this.windowOfActivity.getDecorView().getSystemUiVisibility() | 16);
            } else {
                this.windowOfActivity.getDecorView().setSystemUiVisibility(this.windowOfActivity.getDecorView().getSystemUiVisibility() & -17);
            }
        }
    }

    public boolean isLightSystemNavBar() {
        Window window;
        if (Build.VERSION.SDK_INT < 26 || (window = this.windowOfActivity) == null || (window.getDecorView().getSystemUiVisibility() & 16) == 16) {
            return true;
        }
        return false;
    }

    public void setSystemNavBarDividerColor(int i) {
        if (Build.VERSION.SDK_INT >= 28 && this.windowOfActivity != null) {
            if (this.applyPartnerResources) {
                Context context = this.templateLayout.getContext();
                PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
                PartnerConfig partnerConfig = PartnerConfig.CONFIG_NAVIGATION_BAR_DIVIDER_COLOR;
                if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
                    i = PartnerConfigHelper.get(context).getColor(context, partnerConfig);
                }
            }
            this.windowOfActivity.setNavigationBarDividerColor(i);
        }
    }
}
