package com.google.android.setupcompat.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import java.util.Arrays;

public final class WizardManagerHelper {
    public static final String ACTION_NEXT = "com.android.wizard.NEXT";
    static final String EXTRA_ACTION_ID = "actionId";
    static final String EXTRA_SCRIPT_URI = "scriptUri";
    static final String EXTRA_WIZARD_BUNDLE = "wizardBundle";

    public static void copyWizardManagerExtras(Intent intent, Intent intent2) {
        intent2.putExtra(EXTRA_WIZARD_BUNDLE, intent.getBundleExtra(EXTRA_WIZARD_BUNDLE));
        for (String str : Arrays.asList(new String[]{"firstRun", "deferredSetup", "preDeferredSetup", "portalSetup", "isSetupFlow"})) {
            intent2.putExtra(str, intent.getBooleanExtra(str, false));
        }
        for (String str2 : Arrays.asList(new String[]{"theme", EXTRA_SCRIPT_URI, EXTRA_ACTION_ID})) {
            intent2.putExtra(str2, intent.getStringExtra(str2));
        }
    }

    @Deprecated
    public static boolean isSetupWizardIntent(Intent intent) {
        return intent.getBooleanExtra("firstRun", false);
    }

    public static boolean isDeviceProvisioned(Context context) {
        if (Build.VERSION.SDK_INT >= 17) {
            if (Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) == 1) {
                return true;
            }
            return false;
        } else if (Settings.Secure.getInt(context.getContentResolver(), "device_provisioned", 0) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDeferredSetupWizard(Intent intent) {
        return intent != null && intent.getBooleanExtra("deferredSetup", false);
    }

    public static boolean isPreDeferredSetupWizard(Intent intent) {
        return intent != null && intent.getBooleanExtra("preDeferredSetup", false);
    }

    public static boolean isInitialSetupWizard(Intent intent) {
        return intent.getBooleanExtra("firstRun", false);
    }

    public static boolean isAnySetupWizard(Intent intent) {
        if (intent == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            return intent.getBooleanExtra("isSetupFlow", false);
        }
        if (isInitialSetupWizard(intent) || isPreDeferredSetupWizard(intent) || isDeferredSetupWizard(intent)) {
            return true;
        }
        return false;
    }
}
