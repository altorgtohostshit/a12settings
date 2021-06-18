package com.google.android.setupdesign.util;

import android.content.Context;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.BuildCompatUtils;
import com.google.android.setupdesign.util.TextViewPartnerStyler;

public final class HeaderAreaStyler {
    static final String WARN_TO_USE_DRAWABLE = "To achieve scaling icon in SetupDesign lib, should use vector drawable icon from ";

    public static void applyPartnerCustomizationHeaderHeavyStyle(TextView textView) {
        if (textView != null) {
            TextViewPartnerStyler.applyPartnerCustomizationStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs(PartnerConfig.CONFIG_HEADER_TEXT_COLOR, (PartnerConfig) null, PartnerConfig.CONFIG_HEADER_TEXT_SIZE, PartnerConfig.CONFIG_HEADER_FONT_FAMILY, PartnerConfig.CONFIG_HEADER_TEXT_MARGIN_TOP, PartnerConfig.CONFIG_HEADER_TEXT_MARGIN_BOTTOM, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
        }
    }

    public static void applyPartnerCustomizationDescriptionHeavyStyle(TextView textView) {
        if (textView != null) {
            TextViewPartnerStyler.applyPartnerCustomizationStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs(PartnerConfig.CONFIG_DESCRIPTION_TEXT_COLOR, PartnerConfig.CONFIG_DESCRIPTION_LINK_TEXT_COLOR, PartnerConfig.CONFIG_DESCRIPTION_TEXT_SIZE, PartnerConfig.CONFIG_DESCRIPTION_FONT_FAMILY, PartnerConfig.CONFIG_DESCRIPTION_TEXT_MARGIN_TOP, PartnerConfig.CONFIG_DESCRIPTION_TEXT_MARGIN_BOTTOM, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
        }
    }

    public static void applyPartnerCustomizationHeaderLightStyle(TextView textView) {
        if (textView != null) {
            TextViewPartnerStyler.applyPartnerCustomizationLightStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs((PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
        }
    }

    public static void applyPartnerCustomizationDescriptionLightStyle(TextView textView) {
        if (textView != null) {
            TextViewPartnerStyler.applyPartnerCustomizationLightStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs((PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
        }
    }

    public static void applyPartnerCustomizationHeaderAreaStyle(ViewGroup viewGroup) {
        if (viewGroup != null && PartnerStyleHelper.shouldApplyPartnerHeavyThemeResource((View) viewGroup)) {
            Context context = viewGroup.getContext();
            viewGroup.setBackgroundColor(PartnerConfigHelper.get(context).getColor(context, PartnerConfig.CONFIG_HEADER_AREA_BACKGROUND_COLOR));
            PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig = PartnerConfig.CONFIG_HEADER_CONTAINER_MARGIN_BOTTOM;
            if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
                ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, (int) PartnerConfigHelper.get(context).getDimension(context, partnerConfig));
                    viewGroup.setLayoutParams(layoutParams);
                }
            }
        }
    }

    public static void applyPartnerCustomizationIconStyle(ImageView imageView, FrameLayout frameLayout) {
        if (imageView != null && frameLayout != null && PartnerStyleHelper.shouldApplyPartnerResource((View) imageView)) {
            Context context = imageView.getContext();
            int layoutGravity = PartnerStyleHelper.getLayoutGravity(context);
            if (layoutGravity != 0) {
                setGravity(imageView, layoutGravity);
            }
            if (PartnerStyleHelper.shouldApplyPartnerHeavyThemeResource((View) frameLayout)) {
                ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
                PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
                PartnerConfig partnerConfig = PartnerConfig.CONFIG_ICON_MARGIN_TOP;
                if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig) && (layoutParams instanceof ViewGroup.MarginLayoutParams)) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, (int) PartnerConfigHelper.get(context).getDimension(context, partnerConfig), marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
                }
                PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(context);
                PartnerConfig partnerConfig2 = PartnerConfig.CONFIG_ICON_SIZE;
                if (partnerConfigHelper2.isPartnerConfigAvailable(partnerConfig2)) {
                    checkImageType(imageView);
                    ViewGroup.LayoutParams layoutParams2 = imageView.getLayoutParams();
                    layoutParams2.height = (int) PartnerConfigHelper.get(context).getDimension(context, partnerConfig2);
                    layoutParams2.width = -2;
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }
        }
    }

    private static void checkImageType(final ImageView imageView) {
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                if (!BuildCompatUtils.isAtLeastS() || imageView.getDrawable() == null) {
                    return true;
                }
                if ((Build.VERSION.SDK_INT >= 21 && (imageView.getDrawable() instanceof VectorDrawable)) || (imageView.getDrawable() instanceof VectorDrawableCompat)) {
                    return true;
                }
                String str = Build.TYPE;
                if (!str.equals("userdebug") && !str.equals("eng")) {
                    return true;
                }
                Log.w("HeaderAreaStyler", HeaderAreaStyler.WARN_TO_USE_DRAWABLE + imageView.getContext().getPackageName());
                return true;
            }
        });
    }

    private static void setGravity(ImageView imageView, int i) {
        if (imageView.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.gravity = i;
            imageView.setLayoutParams(layoutParams);
        }
    }
}
