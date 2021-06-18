package com.google.android.setupdesign.util;

import android.widget.TextView;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupdesign.util.TextViewPartnerStyler;

public final class DescriptionStyler {
    public static void applyPartnerCustomizationHeavyStyle(TextView textView) {
        TextViewPartnerStyler.applyPartnerCustomizationStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs(PartnerConfig.CONFIG_DESCRIPTION_TEXT_COLOR, PartnerConfig.CONFIG_DESCRIPTION_LINK_TEXT_COLOR, PartnerConfig.CONFIG_DESCRIPTION_TEXT_SIZE, PartnerConfig.CONFIG_DESCRIPTION_FONT_FAMILY, (PartnerConfig) null, (PartnerConfig) null, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
    }

    public static void applyPartnerCustomizationLightStyle(TextView textView) {
        TextViewPartnerStyler.applyPartnerCustomizationLightStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs((PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
    }
}
