package com.google.android.libraries.hats20.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import com.google.android.libraries.material.autoresizetext.AutoResizeTextView;

public final class LayoutUtils {
    public static Point getNavigationBarDimensionPixelSize(Activity activity) {
        Point realScreenDimensions = getRealScreenDimensions(activity);
        Point usableContentDimensions = getUsableContentDimensions(activity);
        if (usableContentDimensions.x < realScreenDimensions.x) {
            return new Point(realScreenDimensions.x - usableContentDimensions.x, usableContentDimensions.y);
        }
        if (usableContentDimensions.y < realScreenDimensions.y) {
            return new Point(usableContentDimensions.x, realScreenDimensions.y - usableContentDimensions.y);
        }
        return new Point();
    }

    public static Point getUsableContentDimensions(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point;
    }

    public static Point getRealScreenDimensions(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static boolean isNavigationBarOnRight(Activity activity) {
        return getUsableContentDimensions(activity).x < getRealScreenDimensions(activity).x;
    }

    public static void fitTextInTextViewWrapIfNeeded(float f, int i, int i2, String str, AutoResizeTextView autoResizeTextView) {
        Integer fontSizeGivenSpace = getFontSizeGivenSpace(f, i, i2, str, autoResizeTextView.getContext());
        if (fontSizeGivenSpace == null) {
            float f2 = (float) i2;
            autoResizeTextView.setMinTextSize(1, f2);
            autoResizeTextView.setTextSize(2, f2);
            autoResizeTextView.setLines(2);
            autoResizeTextView.setMaxLines(2);
            return;
        }
        autoResizeTextView.setTextSize(1, (float) fontSizeGivenSpace.intValue());
        autoResizeTextView.setLines(1);
        autoResizeTextView.setMaxLines(1);
    }

    /* JADX WARNING: Removed duplicated region for block: B:6:0x0028 A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x002a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Integer getFontSizeGivenSpace(float r1, int r2, int r3, java.lang.String r4, android.content.Context r5) {
        /*
            android.widget.TextView r0 = new android.widget.TextView
            r0.<init>(r5)
            float r2 = (float) r2
            android.content.res.Resources r5 = r5.getResources()
            android.content.res.Configuration r5 = r5.getConfiguration()
            float r5 = r5.fontScale
            float r2 = r2 * r5
            int r2 = java.lang.Math.round(r2)
            float r5 = measureTextAtFontSize(r2, r4, r0)
        L_0x0019:
            int r5 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r5 <= 0) goto L_0x0026
            if (r2 <= r3) goto L_0x0026
            int r2 = r2 + -1
            float r5 = measureTextAtFontSize(r2, r4, r0)
            goto L_0x0019
        L_0x0026:
            if (r5 <= 0) goto L_0x002a
            r1 = 0
            goto L_0x002e
        L_0x002a:
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)
        L_0x002e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.libraries.hats20.util.LayoutUtils.getFontSizeGivenSpace(float, int, int, java.lang.String, android.content.Context):java.lang.Integer");
    }

    private static float measureTextAtFontSize(int i, String str, TextView textView) {
        textView.setTextSize(1, (float) i);
        return textView.getPaint().measureText(str);
    }
}
