package androidx.core.p002os;

import android.os.Build;

/* renamed from: androidx.core.os.BuildCompat */
public class BuildCompat {
    @Deprecated
    public static boolean isAtLeastR() {
        return Build.VERSION.SDK_INT >= 30;
    }

    public static boolean isAtLeastS() {
        return Build.VERSION.CODENAME.equals("S");
    }
}
