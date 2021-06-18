package com.google.android.setupdesign.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.R$style;
import java.util.Objects;

public final class ThemeHelper {
    public static boolean isSetupWizardDayNightEnabled(Context context) {
        return PartnerConfigHelper.isSetupWizardDayNightEnabled(context);
    }

    public static boolean shouldApplyExtendedPartnerConfig(Context context) {
        return PartnerConfigHelper.shouldApplyExtendedPartnerConfig(context);
    }

    public static boolean isSetupWizardDynamicColorEnabled(Context context) {
        return PartnerConfigHelper.isSetupWizardDynamicColorEnabled(context);
    }

    public static int getDynamicColorTheme(Context context) {
        int i;
        try {
            boolean isAnySetupWizard = WizardManagerHelper.isAnySetupWizard(PartnerCustomizationLayout.lookupActivityFromContext(context).getIntent());
            boolean isSetupWizardDayNightEnabled = isSetupWizardDayNightEnabled(context);
            if (!isAnySetupWizard) {
                if (isSetupWizardDayNightEnabled) {
                    i = R$style.SudFullDynamicColorThemeGlifV3_DayNight;
                } else {
                    i = R$style.SudFullDynamicColorThemeGlifV3_Light;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Return ");
                sb.append(isSetupWizardDayNightEnabled ? "SudFullDynamicColorThemeGlifV3_DayNight" : "SudFullDynamicColorThemeGlifV3_Light");
                Log.i("ThemeHelper", sb.toString());
                return i;
            } else if (isSetupWizardDayNightEnabled) {
                return R$style.SudDynamicColorThemeGlifV3_DayNight;
            } else {
                return R$style.SudDynamicColorThemeGlifV3_Light;
            }
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            Objects.requireNonNull(message);
            Log.e("ThemeHelper", message);
            return 0;
        }
    }

    public static boolean trySetDynamicColor(Context context) {
        if (!shouldApplyExtendedPartnerConfig(context)) {
            Log.w("ThemeHelper", "SetupWizard does not supports the extended partner configs.");
            return false;
        } else if (!isSetupWizardDynamicColorEnabled(context)) {
            Log.w("ThemeHelper", "SetupWizard does not support the dynamic color or supporting status unknown.");
            return false;
        } else {
            try {
                Activity lookupActivityFromContext = PartnerCustomizationLayout.lookupActivityFromContext(context);
                if (getDynamicColorTheme(context) != 0) {
                    lookupActivityFromContext.setTheme(getDynamicColorTheme(context));
                    return true;
                }
                Log.w("ThemeHelper", "Error occurred on getting dynamic color theme.");
                return false;
            } catch (IllegalArgumentException e) {
                String message = e.getMessage();
                Objects.requireNonNull(message);
                Log.e("ThemeHelper", message);
                return false;
            }
        }
    }
}
