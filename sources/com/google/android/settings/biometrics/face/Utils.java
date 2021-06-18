package com.google.android.settings.biometrics.face;

import android.content.Context;

public class Utils {
    public static float dpToPx(Context context, int i) {
        return ((float) i) * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }
}
