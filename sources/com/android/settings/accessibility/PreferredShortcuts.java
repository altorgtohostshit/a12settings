package com.android.settings.accessibility;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public final class PreferredShortcuts {
    public static int retrieveUserShortcutType(Context context, String str, int i) {
        if (str == null) {
            return i;
        }
        HashSet hashSet = new HashSet(getFromSharedPreferences(context));
        hashSet.removeIf(new PreferredShortcuts$$ExternalSyntheticLambda0(str));
        if (hashSet.isEmpty()) {
            return i;
        }
        return PreferredShortcut.fromString((String) hashSet.stream().findFirst().get()).getType();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$retrieveUserShortcutType$0(String str, String str2) {
        return !str2.contains(str);
    }

    public static void saveUserShortcutType(Context context, PreferredShortcut preferredShortcut) {
        String componentName = preferredShortcut.getComponentName();
        if (componentName != null) {
            HashSet hashSet = new HashSet(getFromSharedPreferences(context));
            hashSet.removeIf(new PreferredShortcuts$$ExternalSyntheticLambda1(componentName));
            hashSet.add(preferredShortcut.toString());
            saveToSharedPreferences(context, hashSet);
        }
    }

    private static Set<String> getFromSharedPreferences(Context context) {
        return getSharedPreferences(context).getStringSet("user_shortcut_type", Set.of());
    }

    private static void saveToSharedPreferences(Context context, Set<String> set) {
        getSharedPreferences(context).edit().putStringSet("user_shortcut_type", set).apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("accessibility_prefs", 0);
    }
}
