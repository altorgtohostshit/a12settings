package com.android.settingslib.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AccessibilityUtils {
    public static Set<ComponentName> getEnabledServicesFromSettings(Context context) {
        return getEnabledServicesFromSettings(context, UserHandle.myUserId());
    }

    public static Set<ComponentName> getEnabledServicesFromSettings(Context context, int i) {
        String stringForUser = Settings.Secure.getStringForUser(context.getContentResolver(), "enabled_accessibility_services", i);
        if (TextUtils.isEmpty(stringForUser)) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet();
        TextUtils.SimpleStringSplitter<String> simpleStringSplitter = new TextUtils.SimpleStringSplitter<>(':');
        simpleStringSplitter.setString(stringForUser);
        for (String unflattenFromString : simpleStringSplitter) {
            ComponentName unflattenFromString2 = ComponentName.unflattenFromString(unflattenFromString);
            if (unflattenFromString2 != null) {
                hashSet.add(unflattenFromString2);
            }
        }
        return hashSet;
    }

    public static CharSequence getTextForLocale(Context context, Locale locale, int i) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration).getText(i);
    }

    public static void setAccessibilityServiceState(Context context, ComponentName componentName, boolean z) {
        setAccessibilityServiceState(context, componentName, z, UserHandle.myUserId());
    }

    /* JADX WARNING: Removed duplicated region for block: B:6:0x0021 A[LOOP:0: B:6:0x0021->B:9:0x0031, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void setAccessibilityServiceState(android.content.Context r3, android.content.ComponentName r4, boolean r5, int r6) {
        /*
            java.util.Set r0 = getEnabledServicesFromSettings(r3, r6)
            boolean r1 = r0.isEmpty()
            r2 = 1
            if (r1 == 0) goto L_0x0010
            android.util.ArraySet r0 = new android.util.ArraySet
            r0.<init>(r2)
        L_0x0010:
            if (r5 == 0) goto L_0x0016
            r0.add(r4)
            goto L_0x0033
        L_0x0016:
            r0.remove(r4)
            java.util.Set r4 = getInstalledServices(r3)
            java.util.Iterator r5 = r0.iterator()
        L_0x0021:
            boolean r1 = r5.hasNext()
            if (r1 == 0) goto L_0x0033
            java.lang.Object r1 = r5.next()
            android.content.ComponentName r1 = (android.content.ComponentName) r1
            boolean r1 = r4.contains(r1)
            if (r1 == 0) goto L_0x0021
        L_0x0033:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.util.Iterator r5 = r0.iterator()
        L_0x003c:
            boolean r0 = r5.hasNext()
            if (r0 == 0) goto L_0x0055
            java.lang.Object r0 = r5.next()
            android.content.ComponentName r0 = (android.content.ComponentName) r0
            java.lang.String r0 = r0.flattenToString()
            r4.append(r0)
            r0 = 58
            r4.append(r0)
            goto L_0x003c
        L_0x0055:
            int r5 = r4.length()
            if (r5 <= 0) goto L_0x005f
            int r5 = r5 - r2
            r4.deleteCharAt(r5)
        L_0x005f:
            android.content.ContentResolver r3 = r3.getContentResolver()
            java.lang.String r4 = r4.toString()
            java.lang.String r5 = "enabled_accessibility_services"
            android.provider.Settings.Secure.putStringForUser(r3, r5, r4, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.accessibility.AccessibilityUtils.setAccessibilityServiceState(android.content.Context, android.content.ComponentName, boolean, int):void");
    }

    public static String getShortcutTargetServiceComponentNameString(Context context, int i) {
        String stringForUser = Settings.Secure.getStringForUser(context.getContentResolver(), "accessibility_shortcut_target_service", i);
        if (stringForUser != null) {
            return stringForUser;
        }
        return context.getString(17039886);
    }

    private static Set<ComponentName> getInstalledServices(Context context) {
        HashSet hashSet = new HashSet();
        hashSet.clear();
        List<AccessibilityServiceInfo> installedAccessibilityServiceList = AccessibilityManager.getInstance(context).getInstalledAccessibilityServiceList();
        if (installedAccessibilityServiceList == null) {
            return hashSet;
        }
        for (AccessibilityServiceInfo resolveInfo : installedAccessibilityServiceList) {
            ServiceInfo serviceInfo = resolveInfo.getResolveInfo().serviceInfo;
            hashSet.add(new ComponentName(serviceInfo.packageName, serviceInfo.name));
        }
        return hashSet;
    }
}
