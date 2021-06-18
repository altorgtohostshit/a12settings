package com.google.android.setupdesign.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.view.View;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.R$attr;
import com.google.android.setupdesign.R$id;
import java.util.Locale;

public final class PartnerStyleHelper {
    public static int getLayoutGravity(Context context) {
        String string = PartnerConfigHelper.get(context).getString(context, PartnerConfig.CONFIG_LAYOUT_GRAVITY);
        if (string == null) {
            return 0;
        }
        String lowerCase = string.toLowerCase(Locale.ROOT);
        lowerCase.hashCode();
        if (lowerCase.equals("center")) {
            return 17;
        }
        if (!lowerCase.equals("start")) {
            return 0;
        }
        return 8388611;
    }

    public static boolean isPartnerHeavyThemeLayout(TemplateLayout templateLayout) {
        if (!(templateLayout instanceof GlifLayout)) {
            return false;
        }
        return ((GlifLayout) templateLayout).shouldApplyPartnerHeavyThemeResource();
    }

    public static boolean isPartnerLightThemeLayout(TemplateLayout templateLayout) {
        if (!(templateLayout instanceof PartnerCustomizationLayout)) {
            return false;
        }
        return ((PartnerCustomizationLayout) templateLayout).shouldApplyPartnerResource();
    }

    public static boolean shouldApplyPartnerResource(View view) {
        if (view == null) {
            return false;
        }
        if (view instanceof PartnerCustomizationLayout) {
            return isPartnerLightThemeLayout((PartnerCustomizationLayout) view);
        }
        return shouldApplyPartnerResource(view.getContext());
    }

    private static boolean shouldApplyPartnerResource(Context context) {
        if (Build.VERSION.SDK_INT < 29 || !PartnerConfigHelper.get(context).isAvailable()) {
            return false;
        }
        Activity activity = null;
        try {
            activity = PartnerCustomizationLayout.lookupActivityFromContext(context);
            if (activity != null) {
                TemplateLayout findLayoutFromActivity = findLayoutFromActivity(activity);
                if (findLayoutFromActivity instanceof PartnerCustomizationLayout) {
                    return ((PartnerCustomizationLayout) findLayoutFromActivity).shouldApplyPartnerResource();
                }
            }
        } catch (ClassCastException | IllegalArgumentException unused) {
        }
        boolean isAnySetupWizard = activity != null ? WizardManagerHelper.isAnySetupWizard(activity.getIntent()) : false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{R$attr.sucUsePartnerResource});
        boolean z = obtainStyledAttributes.getBoolean(0, true);
        obtainStyledAttributes.recycle();
        if (isAnySetupWizard || z) {
            return true;
        }
        return false;
    }

    public static boolean shouldApplyPartnerHeavyThemeResource(View view) {
        if (view == null) {
            return false;
        }
        if (view instanceof GlifLayout) {
            return isPartnerHeavyThemeLayout((GlifLayout) view);
        }
        return shouldApplyPartnerHeavyThemeResource(view.getContext());
    }

    static boolean shouldApplyPartnerHeavyThemeResource(Context context) {
        try {
            TemplateLayout findLayoutFromActivity = findLayoutFromActivity(PartnerCustomizationLayout.lookupActivityFromContext(context));
            if (findLayoutFromActivity instanceof GlifLayout) {
                return ((GlifLayout) findLayoutFromActivity).shouldApplyPartnerHeavyThemeResource();
            }
        } catch (ClassCastException | IllegalArgumentException unused) {
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{R$attr.sudUsePartnerHeavyTheme});
        boolean z = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        boolean z2 = z || PartnerConfigHelper.shouldApplyExtendedPartnerConfig(context);
        if (!shouldApplyPartnerResource(context) || !z2) {
            return false;
        }
        return true;
    }

    private static TemplateLayout findLayoutFromActivity(Activity activity) {
        View findViewById;
        if (activity == null || (findViewById = activity.findViewById(R$id.suc_layout_status)) == null) {
            return null;
        }
        return (TemplateLayout) findViewById.getParent();
    }
}
