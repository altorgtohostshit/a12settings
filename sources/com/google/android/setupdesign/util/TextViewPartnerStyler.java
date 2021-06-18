package com.google.android.setupdesign.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;

final class TextViewPartnerStyler {
    public static void applyPartnerCustomizationStyle(TextView textView, TextPartnerConfigs textPartnerConfigs) {
        int i;
        int i2;
        Typeface create;
        int color;
        int color2;
        if (textView != null && textPartnerConfigs != null) {
            Context context = textView.getContext();
            if (!(textPartnerConfigs.getTextColorConfig() == null || !PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.getTextColorConfig()) || (color2 = PartnerConfigHelper.get(context).getColor(context, textPartnerConfigs.getTextColorConfig())) == 0)) {
                textView.setTextColor(color2);
            }
            if (!(textPartnerConfigs.getTextLinkedColorConfig() == null || !PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.getTextLinkedColorConfig()) || (color = PartnerConfigHelper.get(context).getColor(context, textPartnerConfigs.getTextLinkedColorConfig())) == 0)) {
                textView.setLinkTextColor(color);
            }
            if (textPartnerConfigs.getTextSizeConfig() != null && PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.getTextSizeConfig())) {
                float dimension = PartnerConfigHelper.get(context).getDimension(context, textPartnerConfigs.getTextSizeConfig(), 0.0f);
                if (dimension > 0.0f) {
                    textView.setTextSize(0, dimension);
                }
            }
            if (!(textPartnerConfigs.getTextFontFamilyConfig() == null || !PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.getTextFontFamilyConfig()) || (create = Typeface.create(PartnerConfigHelper.get(context).getString(context, textPartnerConfigs.getTextFontFamilyConfig()), 0)) == null)) {
                textView.setTypeface(create);
            }
            if (!(textPartnerConfigs.getTextMarginTop() == null && textPartnerConfigs.getTextMarginBottom() == null)) {
                ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
                if (layoutParams instanceof LinearLayout.LayoutParams) {
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
                    if (textPartnerConfigs.getTextMarginTop() == null || !PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.getTextMarginTop())) {
                        i = layoutParams2.topMargin;
                    } else {
                        i = (int) PartnerConfigHelper.get(context).getDimension(context, textPartnerConfigs.getTextMarginTop());
                    }
                    if (textPartnerConfigs.getTextMarginBottom() == null || !PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.getTextMarginBottom())) {
                        i2 = layoutParams2.bottomMargin;
                    } else {
                        i2 = (int) PartnerConfigHelper.get(context).getDimension(context, textPartnerConfigs.getTextMarginBottom());
                    }
                    layoutParams2.setMargins(layoutParams2.leftMargin, i, layoutParams2.rightMargin, i2);
                    textView.setLayoutParams(layoutParams);
                }
            }
            textView.setGravity(textPartnerConfigs.getTextGravity());
        }
    }

    public static void applyPartnerCustomizationLightStyle(TextView textView, TextPartnerConfigs textPartnerConfigs) {
        if (textView != null && textPartnerConfigs != null) {
            textView.setGravity(textPartnerConfigs.getTextGravity());
        }
    }

    public static class TextPartnerConfigs {
        private final PartnerConfig textColorConfig;
        private final PartnerConfig textFontFamilyConfig;
        private final int textGravity;
        private final PartnerConfig textLinkedColorConfig;
        private final PartnerConfig textMarginBottomConfig;
        private final PartnerConfig textMarginTopConfig;
        private final PartnerConfig textSizeConfig;

        public TextPartnerConfigs(PartnerConfig partnerConfig, PartnerConfig partnerConfig2, PartnerConfig partnerConfig3, PartnerConfig partnerConfig4, PartnerConfig partnerConfig5, PartnerConfig partnerConfig6, int i) {
            this.textColorConfig = partnerConfig;
            this.textLinkedColorConfig = partnerConfig2;
            this.textSizeConfig = partnerConfig3;
            this.textFontFamilyConfig = partnerConfig4;
            this.textMarginTopConfig = partnerConfig5;
            this.textMarginBottomConfig = partnerConfig6;
            this.textGravity = i;
        }

        public PartnerConfig getTextColorConfig() {
            return this.textColorConfig;
        }

        public PartnerConfig getTextLinkedColorConfig() {
            return this.textLinkedColorConfig;
        }

        public PartnerConfig getTextSizeConfig() {
            return this.textSizeConfig;
        }

        public PartnerConfig getTextFontFamilyConfig() {
            return this.textFontFamilyConfig;
        }

        public PartnerConfig getTextMarginTop() {
            return this.textMarginTopConfig;
        }

        public PartnerConfig getTextMarginBottom() {
            return this.textMarginBottomConfig;
        }

        public int getTextGravity() {
            return this.textGravity;
        }
    }
}
