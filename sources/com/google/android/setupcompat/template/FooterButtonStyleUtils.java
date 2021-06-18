package com.google.android.setupcompat.template;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.StateSet;
import android.widget.Button;
import com.google.android.setupcompat.internal.FooterButtonPartnerConfig;
import com.google.android.setupcompat.internal.Preconditions;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;

public class FooterButtonStyleUtils {
    static void updateButtonTextEnabledColorWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig) {
        int color = PartnerConfigHelper.get(context).getColor(context, partnerConfig);
        if (color != 0) {
            button.setTextColor(ColorStateList.valueOf(color));
        }
    }

    static void updateButtonTextDisableColor(Button button, ColorStateList colorStateList) {
        button.setTextColor(colorStateList);
    }

    @TargetApi(29)
    static void updateButtonBackgroundWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig, PartnerConfig partnerConfig2, PartnerConfig partnerConfig3) {
        Preconditions.checkArgument(Build.VERSION.SDK_INT >= 29, "Update button background only support on sdk Q or higher");
        int[] iArr = {-16842910};
        int[] iArr2 = new int[0];
        int color = PartnerConfigHelper.get(context).getColor(context, partnerConfig);
        float fraction = PartnerConfigHelper.get(context).getFraction(context, partnerConfig2, 0.0f);
        int color2 = PartnerConfigHelper.get(context).getColor(context, partnerConfig3);
        if (color != 0) {
            if (fraction <= 0.0f) {
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16842803});
                fraction = obtainStyledAttributes.getFloat(0, 0.26f);
                obtainStyledAttributes.recycle();
            }
            if (color2 == 0) {
                color2 = color;
            }
            ColorStateList colorStateList = new ColorStateList(new int[][]{iArr, iArr2}, new int[]{convertRgbToArgb(color2, fraction), color});
            button.getBackground().mutate().setState(new int[0]);
            button.refreshDrawableState();
            button.setBackgroundTintList(colorStateList);
        }
    }

    static void updateButtonRippleColorWithPartnerConfig(Context context, Button button, FooterButtonPartnerConfig footerButtonPartnerConfig) {
        RippleDrawable rippleDrawable;
        if (Build.VERSION.SDK_INT >= 21 && (rippleDrawable = getRippleDrawable(button)) != null) {
            int color = PartnerConfigHelper.get(context).getColor(context, footerButtonPartnerConfig.getButtonTextColorConfig());
            float fraction = PartnerConfigHelper.get(context).getFraction(context, footerButtonPartnerConfig.getButtonRippleColorAlphaConfig());
            rippleDrawable.setColor(new ColorStateList(new int[][]{new int[]{16842919}, StateSet.NOTHING}, new int[]{convertRgbToArgb(color, fraction), 0}));
        }
    }

    static void updateButtonTextSizeWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig) {
        float dimension = PartnerConfigHelper.get(context).getDimension(context, partnerConfig);
        if (dimension > 0.0f) {
            button.setTextSize(0, dimension);
        }
    }

    static void updateButtonMinHeightWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig) {
        if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(partnerConfig)) {
            float dimension = PartnerConfigHelper.get(context).getDimension(context, partnerConfig);
            if (dimension > 0.0f) {
                button.setMinHeight((int) dimension);
            }
        }
    }

    static void updateButtonTypeFaceWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig, PartnerConfig partnerConfig2) {
        String string = PartnerConfigHelper.get(context).getString(context, partnerConfig);
        int i = 0;
        if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(partnerConfig2)) {
            i = PartnerConfigHelper.get(context).getInteger(context, partnerConfig2, 0);
        }
        Typeface create = Typeface.create(string, i);
        if (create != null) {
            button.setTypeface(create);
        }
    }

    static void updateButtonRadiusWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig) {
        if (Build.VERSION.SDK_INT >= 24) {
            float dimension = PartnerConfigHelper.get(context).getDimension(context, partnerConfig);
            GradientDrawable gradientDrawable = getGradientDrawable(button);
            if (gradientDrawable != null) {
                gradientDrawable.setCornerRadius(dimension);
            }
        }
    }

    static void updateButtonIconWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig, boolean z) {
        if (button != null) {
            Drawable drawable = null;
            if (partnerConfig != null) {
                drawable = PartnerConfigHelper.get(context).getDrawable(context, partnerConfig);
            }
            setButtonIcon(button, drawable, z);
        }
    }

    private static void setButtonIcon(Button button, Drawable drawable, boolean z) {
        Drawable drawable2;
        if (button != null) {
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            if (z) {
                drawable2 = drawable;
                drawable = null;
            } else {
                drawable2 = null;
            }
            if (Build.VERSION.SDK_INT >= 17) {
                button.setCompoundDrawablesRelative(drawable, (Drawable) null, drawable2, (Drawable) null);
            } else {
                button.setCompoundDrawables(drawable, (Drawable) null, drawable2, (Drawable) null);
            }
        }
    }

    static void updateButtonBackground(Button button, int i) {
        button.getBackground().mutate().setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
    }

    public static GradientDrawable getGradientDrawable(Button button) {
        if (Build.VERSION.SDK_INT < 21) {
            return null;
        }
        Drawable background = button.getBackground();
        if (background instanceof InsetDrawable) {
            return (GradientDrawable) ((LayerDrawable) ((InsetDrawable) background).getDrawable()).getDrawable(0);
        }
        if (background instanceof RippleDrawable) {
            return (GradientDrawable) ((InsetDrawable) ((RippleDrawable) background).getDrawable(0)).getDrawable();
        }
        return null;
    }

    static RippleDrawable getRippleDrawable(Button button) {
        if (Build.VERSION.SDK_INT < 21) {
            return null;
        }
        Drawable background = button.getBackground();
        if (background instanceof InsetDrawable) {
            return (RippleDrawable) ((InsetDrawable) background).getDrawable();
        }
        if (background instanceof RippleDrawable) {
            return (RippleDrawable) background;
        }
        return null;
    }

    private static int convertRgbToArgb(int i, float f) {
        return Color.argb((int) (f * 255.0f), Color.red(i), Color.green(i), Color.blue(i));
    }
}
