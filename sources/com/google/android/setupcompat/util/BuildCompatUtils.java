package com.google.android.setupcompat.util;

import android.os.Build;

public final class BuildCompatUtils {
    public static boolean isAtLeastS() {
        int i = Build.VERSION.SDK_INT;
        if (i < 30) {
            return false;
        }
        String str = Build.VERSION.CODENAME;
        if ((!str.equals("REL") || i < 31) && (str.length() != 1 || str.charAt(0) < 'S' || str.charAt(0) > 'Z')) {
            return false;
        }
        return true;
    }
}
