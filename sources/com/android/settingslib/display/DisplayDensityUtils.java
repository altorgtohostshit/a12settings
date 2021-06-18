package com.android.settingslib.display;

import android.os.RemoteException;
import android.view.WindowManagerGlobal;
import com.android.settingslib.R$string;

public class DisplayDensityUtils {
    private static final int[] SUMMARIES_LARGER = {R$string.screen_zoom_summary_large, R$string.screen_zoom_summary_very_large, R$string.screen_zoom_summary_extremely_large};
    private static final int[] SUMMARIES_SMALLER = {R$string.screen_zoom_summary_small};
    private static final int SUMMARY_CUSTOM = R$string.screen_zoom_summary_custom;
    public static final int SUMMARY_DEFAULT = R$string.screen_zoom_summary_default;
    private final int mCurrentIndex;
    private final int mDefaultDensity;
    private final String[] mEntries;
    private final int[] mValues;

    /* JADX WARNING: type inference failed for: r1v3, types: [java.lang.Object[]] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public DisplayDensityUtils(android.content.Context r19) {
        /*
            r18 = this;
            r0 = r18
            r18.<init>()
            r1 = 0
            int r2 = getDefaultDisplayDensity(r1)
            r3 = -1
            if (r2 > 0) goto L_0x0017
            r2 = 0
            r0.mEntries = r2
            r0.mValues = r2
            r0.mDefaultDensity = r1
            r0.mCurrentIndex = r3
            return
        L_0x0017:
            android.content.res.Resources r4 = r19.getResources()
            android.util.DisplayMetrics r5 = new android.util.DisplayMetrics
            r5.<init>()
            android.view.Display r6 = r19.getDisplayNoVerify()
            r6.getRealMetrics(r5)
            int r6 = r5.densityDpi
            int r7 = r5.widthPixels
            int r5 = r5.heightPixels
            int r5 = java.lang.Math.min(r7, r5)
            int r5 = r5 * 160
            int r5 = r5 / 320
            r7 = 1069547520(0x3fc00000, float:1.5)
            float r5 = (float) r5
            float r8 = (float) r2
            float r5 = r5 / r8
            float r5 = java.lang.Math.min(r7, r5)
            r7 = 1065353216(0x3f800000, float:1.0)
            float r5 = r5 - r7
            r9 = 1035489772(0x3db851ec, float:0.09)
            float r9 = r5 / r9
            int[] r10 = SUMMARIES_LARGER
            int r10 = r10.length
            float r10 = (float) r10
            r11 = 0
            float r9 = android.util.MathUtils.constrain(r9, r11, r10)
            int r9 = (int) r9
            r10 = 1070945619(0x3fd55553, float:1.6666664)
            int[] r12 = SUMMARIES_SMALLER
            int r12 = r12.length
            float r12 = (float) r12
            float r10 = android.util.MathUtils.constrain(r10, r11, r12)
            int r10 = (int) r10
            int r11 = r10 + 1
            int r11 = r11 + r9
            java.lang.String[] r12 = new java.lang.String[r11]
            int[] r13 = new int[r11]
            r14 = 1
            if (r10 <= 0) goto L_0x0093
            r15 = 1041865112(0x3e199998, float:0.14999998)
            float r3 = (float) r10
            float r15 = r15 / r3
            int r10 = r10 - r14
            r16 = r1
            r3 = -1
        L_0x006f:
            if (r10 < 0) goto L_0x0096
            int r1 = r10 + 1
            float r1 = (float) r1
            float r1 = r1 * r15
            float r1 = r7 - r1
            float r1 = r1 * r8
            int r1 = (int) r1
            r1 = r1 & -2
            if (r6 != r1) goto L_0x007f
            r3 = r16
        L_0x007f:
            int[] r17 = SUMMARIES_SMALLER
            r7 = r17[r10]
            java.lang.String r7 = r4.getString(r7)
            r12[r16] = r7
            r13[r16] = r1
            int r16 = r16 + 1
            int r10 = r10 + -1
            r1 = 0
            r7 = 1065353216(0x3f800000, float:1.0)
            goto L_0x006f
        L_0x0093:
            r3 = -1
            r16 = 0
        L_0x0096:
            if (r6 != r2) goto L_0x009a
            r3 = r16
        L_0x009a:
            r13[r16] = r2
            int r1 = SUMMARY_DEFAULT
            java.lang.String r1 = r4.getString(r1)
            r12[r16] = r1
            int r16 = r16 + 1
            if (r9 <= 0) goto L_0x00cc
            float r1 = (float) r9
            float r5 = r5 / r1
            r1 = 0
        L_0x00ab:
            if (r1 >= r9) goto L_0x00cc
            int r7 = r1 + 1
            float r10 = (float) r7
            float r10 = r10 * r5
            r15 = 1065353216(0x3f800000, float:1.0)
            float r10 = r10 + r15
            float r10 = r10 * r8
            int r10 = (int) r10
            r10 = r10 & -2
            if (r6 != r10) goto L_0x00bc
            r3 = r16
        L_0x00bc:
            r13[r16] = r10
            int[] r10 = SUMMARIES_LARGER
            r1 = r10[r1]
            java.lang.String r1 = r4.getString(r1)
            r12[r16] = r1
            int r16 = r16 + 1
            r1 = r7
            goto L_0x00ab
        L_0x00cc:
            if (r3 < 0) goto L_0x00cf
            goto L_0x00f0
        L_0x00cf:
            int r11 = r11 + r14
            int[] r13 = java.util.Arrays.copyOf(r13, r11)
            r13[r16] = r6
            java.lang.Object[] r1 = java.util.Arrays.copyOf(r12, r11)
            r12 = r1
            java.lang.String[] r12 = (java.lang.String[]) r12
            int r1 = SUMMARY_CUSTOM
            java.lang.Object[] r3 = new java.lang.Object[r14]
            java.lang.Integer r5 = java.lang.Integer.valueOf(r6)
            r6 = 0
            r3[r6] = r5
            java.lang.String r1 = r4.getString(r1, r3)
            r12[r16] = r1
            r3 = r16
        L_0x00f0:
            r0.mDefaultDensity = r2
            r0.mCurrentIndex = r3
            r0.mEntries = r12
            r0.mValues = r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.display.DisplayDensityUtils.<init>(android.content.Context):void");
    }

    public String[] getEntries() {
        return this.mEntries;
    }

    public int[] getValues() {
        return this.mValues;
    }

    public int getCurrentIndex() {
        return this.mCurrentIndex;
    }

    public int getDefaultDensity() {
        return this.mDefaultDensity;
    }

    private static int getDefaultDisplayDensity(int i) {
        try {
            return WindowManagerGlobal.getWindowManagerService().getInitialDisplayDensity(i);
        } catch (RemoteException unused) {
            return -1;
        }
    }
}
