package com.android.settings.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.accessibility.AccessibilityManager;
import com.android.settings.R;
import java.util.StringJoiner;

final class AccessibilityUtil {
    private static final TextUtils.SimpleStringSplitter sStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

    static CharSequence getSummary(Context context, String str) {
        boolean z = false;
        if (Settings.Secure.getInt(context.getContentResolver(), str, 0) == 1) {
            z = true;
        }
        return context.getResources().getText(z ? R.string.accessibility_feature_state_on : R.string.accessibility_feature_state_off);
    }

    public static boolean isGestureNavigateEnabled(Context context) {
        return context.getResources().getInteger(17694858) == 2;
    }

    public static boolean isFloatingMenuEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "accessibility_button_mode", -1) == 1;
    }

    public static boolean isTouchExploreEnabled(Context context) {
        return ((AccessibilityManager) context.getSystemService(AccessibilityManager.class)).isTouchExplorationEnabled();
    }

    static int getAccessibilityServiceFragmentType(AccessibilityServiceInfo accessibilityServiceInfo) {
        int i = accessibilityServiceInfo.getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion;
        boolean z = (accessibilityServiceInfo.flags & 256) != 0;
        if (i <= 29) {
            return 0;
        }
        return z ? 1 : 2;
    }

    static void optInAllValuesToSettings(Context context, int i, ComponentName componentName) {
        if ((i & 1) == 1) {
            optInValueToSettings(context, 1, componentName);
        }
        if ((i & 2) == 2) {
            optInValueToSettings(context, 2, componentName);
        }
    }

    static void optInValueToSettings(Context context, int i, ComponentName componentName) {
        String convertKeyFromSettings = convertKeyFromSettings(i);
        String string = Settings.Secure.getString(context.getContentResolver(), convertKeyFromSettings);
        if (!hasValueInSettings(context, i, componentName)) {
            StringJoiner stringJoiner = new StringJoiner(String.valueOf(':'));
            if (!TextUtils.isEmpty(string)) {
                stringJoiner.add(string);
            }
            stringJoiner.add(componentName.flattenToString());
            Settings.Secure.putString(context.getContentResolver(), convertKeyFromSettings, stringJoiner.toString());
        }
    }

    static void optOutAllValuesFromSettings(Context context, int i, ComponentName componentName) {
        if ((i & 1) == 1) {
            optOutValueFromSettings(context, 1, componentName);
        }
        if ((i & 2) == 2) {
            optOutValueFromSettings(context, 2, componentName);
        }
    }

    static void optOutValueFromSettings(Context context, int i, ComponentName componentName) {
        StringJoiner stringJoiner = new StringJoiner(String.valueOf(':'));
        String convertKeyFromSettings = convertKeyFromSettings(i);
        String string = Settings.Secure.getString(context.getContentResolver(), convertKeyFromSettings);
        if (!TextUtils.isEmpty(string)) {
            sStringColonSplitter.setString(string);
            while (true) {
                TextUtils.SimpleStringSplitter simpleStringSplitter = sStringColonSplitter;
                if (simpleStringSplitter.hasNext()) {
                    String next = simpleStringSplitter.next();
                    if (!TextUtils.isEmpty(next) && !componentName.flattenToString().equals(next)) {
                        stringJoiner.add(next);
                    }
                } else {
                    Settings.Secure.putString(context.getContentResolver(), convertKeyFromSettings, stringJoiner.toString());
                    return;
                }
            }
        }
    }

    static boolean hasValuesInSettings(Context context, int i, ComponentName componentName) {
        boolean hasValueInSettings = (i & 1) == 1 ? hasValueInSettings(context, 1, componentName) : false;
        return (i & 2) == 2 ? hasValueInSettings | hasValueInSettings(context, 2, componentName) : hasValueInSettings;
    }

    static boolean hasValueInSettings(Context context, int i, ComponentName componentName) {
        TextUtils.SimpleStringSplitter simpleStringSplitter;
        String string = Settings.Secure.getString(context.getContentResolver(), convertKeyFromSettings(i));
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        sStringColonSplitter.setString(string);
        do {
            simpleStringSplitter = sStringColonSplitter;
            if (!simpleStringSplitter.hasNext()) {
                return false;
            }
        } while (!componentName.flattenToString().equals(simpleStringSplitter.next()));
        return true;
    }

    static int getUserShortcutTypesFromSettings(Context context, ComponentName componentName) {
        boolean hasValuesInSettings = hasValuesInSettings(context, 1, componentName);
        return hasValuesInSettings(context, 2, componentName) ? hasValuesInSettings | true ? 1 : 0 : hasValuesInSettings ? 1 : 0;
    }

    static String convertKeyFromSettings(int i) {
        if (i == 1) {
            return "accessibility_button_targets";
        }
        if (i == 2) {
            return "accessibility_shortcut_target_service";
        }
        if (i == 4) {
            return "accessibility_display_magnification_enabled";
        }
        throw new IllegalArgumentException("Unsupported userShortcutType " + i);
    }

    public static int getScreenWidthPixels(Context context) {
        Resources resources = context.getResources();
        return Math.round(TypedValue.applyDimension(1, (float) resources.getConfiguration().screenWidthDp, resources.getDisplayMetrics()));
    }

    public static int getScreenHeightPixels(Context context) {
        Resources resources = context.getResources();
        return Math.round(TypedValue.applyDimension(1, (float) resources.getConfiguration().screenHeightDp, resources.getDisplayMetrics()));
    }

    public static boolean isSystemApp(AccessibilityServiceInfo accessibilityServiceInfo) {
        return accessibilityServiceInfo.getResolveInfo().serviceInfo.applicationInfo.isSystemApp();
    }
}
