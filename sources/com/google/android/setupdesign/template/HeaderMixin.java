package com.google.android.setupdesign.template;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.R$id;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.util.HeaderAreaStyler;
import com.google.android.setupdesign.util.LayoutStyler;
import com.google.android.setupdesign.util.PartnerStyleHelper;

public class HeaderMixin implements Mixin {
    boolean autoTextSizeEnabled = false;
    /* access modifiers changed from: private */
    public float headerAutoSizeLineExtraSpacingInPx;
    /* access modifiers changed from: private */
    public int headerAutoSizeMaxLineOfMaxSize;
    private float headerAutoSizeMaxTextSizeInPx;
    /* access modifiers changed from: private */
    public float headerAutoSizeMinTextSizeInPx;
    private final TemplateLayout templateLayout;

    public HeaderMixin(TemplateLayout templateLayout2, AttributeSet attributeSet, int i) {
        this.templateLayout = templateLayout2;
        TypedArray obtainStyledAttributes = templateLayout2.getContext().obtainStyledAttributes(attributeSet, R$styleable.SucHeaderMixin, i, 0);
        CharSequence text = obtainStyledAttributes.getText(R$styleable.SucHeaderMixin_sucHeaderText);
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R$styleable.SucHeaderMixin_sucHeaderTextColor);
        obtainStyledAttributes.recycle();
        updateAutoTextSizeWithPartnerConfig();
        if (text != null) {
            setText(text);
        }
        if (colorStateList != null) {
            setTextColor(colorStateList);
        }
    }

    private void updateAutoTextSizeWithPartnerConfig() {
        Context context = this.templateLayout.getContext();
        if (!PartnerStyleHelper.isPartnerHeavyThemeLayout(this.templateLayout) || !PartnerConfigHelper.shouldApplyExtendedPartnerConfig(context)) {
            this.autoTextSizeEnabled = false;
            return;
        }
        PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
        PartnerConfig partnerConfig = PartnerConfig.CONFIG_HEADER_AUTO_SIZE_ENABLED;
        if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
            this.autoTextSizeEnabled = PartnerConfigHelper.get(context).getBoolean(context, partnerConfig, this.autoTextSizeEnabled);
        }
        if (this.autoTextSizeEnabled) {
            PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig2 = PartnerConfig.CONFIG_HEADER_AUTO_SIZE_MAX_TEXT_SIZE;
            if (partnerConfigHelper2.isPartnerConfigAvailable(partnerConfig2)) {
                this.headerAutoSizeMaxTextSizeInPx = PartnerConfigHelper.get(context).getDimension(context, partnerConfig2);
            }
            PartnerConfigHelper partnerConfigHelper3 = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig3 = PartnerConfig.CONFIG_HEADER_AUTO_SIZE_MIN_TEXT_SIZE;
            if (partnerConfigHelper3.isPartnerConfigAvailable(partnerConfig3)) {
                this.headerAutoSizeMinTextSizeInPx = PartnerConfigHelper.get(context).getDimension(context, partnerConfig3);
            }
            PartnerConfigHelper partnerConfigHelper4 = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig4 = PartnerConfig.CONFIG_HEADER_AUTO_SIZE_LINE_SPACING_EXTRA;
            if (partnerConfigHelper4.isPartnerConfigAvailable(partnerConfig4)) {
                this.headerAutoSizeLineExtraSpacingInPx = PartnerConfigHelper.get(context).getDimension(context, partnerConfig4);
            }
            PartnerConfigHelper partnerConfigHelper5 = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig5 = PartnerConfig.CONFIG_HEADER_AUTO_SIZE_MAX_LINE_OF_MAX_SIZE;
            if (partnerConfigHelper5.isPartnerConfigAvailable(partnerConfig5)) {
                this.headerAutoSizeMaxLineOfMaxSize = PartnerConfigHelper.get(context).getInteger(context, partnerConfig5, 0);
            }
            if (this.headerAutoSizeMaxLineOfMaxSize >= 1) {
                float f = this.headerAutoSizeMinTextSizeInPx;
                if (f > 0.0f && this.headerAutoSizeMaxTextSizeInPx >= f) {
                    return;
                }
            }
            Log.w("HeaderMixin", "Invalid configs, disable auto text size.");
            this.autoTextSizeEnabled = false;
        }
    }

    public void tryApplyPartnerCustomizationStyle() {
        TextView textView = (TextView) this.templateLayout.findManagedViewById(R$id.suc_layout_title);
        boolean isPartnerLightThemeLayout = PartnerStyleHelper.isPartnerLightThemeLayout(this.templateLayout);
        if (PartnerStyleHelper.isPartnerHeavyThemeLayout(this.templateLayout)) {
            View findManagedViewById = this.templateLayout.findManagedViewById(R$id.sud_layout_header);
            HeaderAreaStyler.applyPartnerCustomizationHeaderHeavyStyle(textView);
            HeaderAreaStyler.applyPartnerCustomizationHeaderAreaStyle((ViewGroup) findManagedViewById);
            LayoutStyler.applyPartnerCustomizationExtraPaddingStyle(findManagedViewById);
            updateAutoTextSizeWithPartnerConfig();
        } else if (isPartnerLightThemeLayout) {
            HeaderAreaStyler.applyPartnerCustomizationHeaderLightStyle(textView);
        }
        if (this.autoTextSizeEnabled) {
            autoAdjustTextSize(textView);
        }
    }

    public TextView getTextView() {
        return (TextView) this.templateLayout.findManagedViewById(R$id.suc_layout_title);
    }

    public void setText(int i) {
        TextView textView = getTextView();
        if (textView != null) {
            if (this.autoTextSizeEnabled) {
                autoAdjustTextSize(textView);
            }
            textView.setText(i);
        }
    }

    public void setText(CharSequence charSequence) {
        TextView textView = getTextView();
        if (textView != null) {
            if (this.autoTextSizeEnabled) {
                autoAdjustTextSize(textView);
            }
            textView.setText(charSequence);
        }
    }

    private void autoAdjustTextSize(final TextView textView) {
        if (textView != null) {
            textView.setTextSize(0, this.headerAutoSizeMaxTextSizeInPx);
            if (Build.VERSION.SDK_INT >= 28) {
                textView.setLineHeight(Math.round(this.headerAutoSizeLineExtraSpacingInPx + this.headerAutoSizeMaxTextSizeInPx));
            }
            textView.setMaxLines(6);
            textView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    textView.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (textView.getLineCount() <= HeaderMixin.this.headerAutoSizeMaxLineOfMaxSize) {
                        return true;
                    }
                    textView.setTextSize(0, HeaderMixin.this.headerAutoSizeMinTextSizeInPx);
                    if (Build.VERSION.SDK_INT >= 28) {
                        textView.setLineHeight(Math.round(HeaderMixin.this.headerAutoSizeLineExtraSpacingInPx + HeaderMixin.this.headerAutoSizeMinTextSizeInPx));
                    }
                    textView.invalidate();
                    return false;
                }
            });
        }
    }

    public CharSequence getText() {
        TextView textView = getTextView();
        if (textView != null) {
            return textView.getText();
        }
        return null;
    }

    public void setTextColor(ColorStateList colorStateList) {
        TextView textView = getTextView();
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    public ColorStateList getTextColor() {
        TextView textView = getTextView();
        if (textView != null) {
            return textView.getTextColors();
        }
        return null;
    }
}
