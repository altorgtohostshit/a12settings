package com.google.android.setupdesign.util;

import android.app.Activity;
import android.content.Intent;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.R$style;

public class ThemeResolver {
    private static ThemeResolver defaultResolver;
    private final int defaultTheme;
    private final ThemeSupplier defaultThemeSupplier;
    private final String oldestSupportedTheme;
    private final boolean useDayNight;

    public interface ThemeSupplier {
        String getTheme();
    }

    public static ThemeResolver getDefault() {
        if (defaultResolver == null) {
            defaultResolver = new Builder().setDefaultTheme(R$style.SudThemeGlif_DayNight).setUseDayNight(true).build();
        }
        return defaultResolver;
    }

    private ThemeResolver(int i, String str, ThemeSupplier themeSupplier, boolean z) {
        this.defaultTheme = i;
        this.oldestSupportedTheme = str;
        this.defaultThemeSupplier = themeSupplier;
        this.useDayNight = z;
    }

    public int resolve(Intent intent, boolean z) {
        return resolve(intent.getStringExtra("theme"), z);
    }

    public int resolve(String str, boolean z) {
        int themeRes = (!this.useDayNight || z) ? getThemeRes(str) : getDayNightThemeRes(str);
        if (themeRes == 0) {
            ThemeSupplier themeSupplier = this.defaultThemeSupplier;
            if (themeSupplier != null) {
                str = themeSupplier.getTheme();
                themeRes = (!this.useDayNight || z) ? getThemeRes(str) : getDayNightThemeRes(str);
            }
            if (themeRes == 0) {
                return this.defaultTheme;
            }
        }
        String str2 = this.oldestSupportedTheme;
        return (str2 == null || compareThemes(str, str2) >= 0) ? themeRes : this.defaultTheme;
    }

    public void applyTheme(Activity activity) {
        activity.setTheme(resolve(activity.getIntent(), WizardManagerHelper.isAnySetupWizard(activity.getIntent()) && !ThemeHelper.isSetupWizardDayNightEnabled(activity)));
    }

    private static int getDayNightThemeRes(String str) {
        if (str != null) {
            char c = 65535;
            switch (str.hashCode()) {
                case -2128555920:
                    if (str.equals("glif_v2_light")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1270463490:
                    if (str.equals("material_light")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1241052239:
                    if (str.equals("glif_v3_light")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3175618:
                    if (str.equals("glif")) {
                        c = 3;
                        break;
                    }
                    break;
                case 115650329:
                    if (str.equals("glif_v2")) {
                        c = 4;
                        break;
                    }
                    break;
                case 115650330:
                    if (str.equals("glif_v3")) {
                        c = 5;
                        break;
                    }
                    break;
                case 299066663:
                    if (str.equals("material")) {
                        c = 6;
                        break;
                    }
                    break;
                case 767685465:
                    if (str.equals("glif_light")) {
                        c = 7;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 4:
                    return R$style.SudThemeGlifV2_DayNight;
                case 1:
                case 6:
                    return R$style.SudThemeMaterial_DayNight;
                case 2:
                case 5:
                    return R$style.SudThemeGlifV3_DayNight;
                case 3:
                case 7:
                    return R$style.SudThemeGlif_DayNight;
            }
        }
        return 0;
    }

    private static int getThemeRes(String str) {
        if (str != null) {
            char c = 65535;
            switch (str.hashCode()) {
                case -2128555920:
                    if (str.equals("glif_v2_light")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1270463490:
                    if (str.equals("material_light")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1241052239:
                    if (str.equals("glif_v3_light")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3175618:
                    if (str.equals("glif")) {
                        c = 3;
                        break;
                    }
                    break;
                case 115650329:
                    if (str.equals("glif_v2")) {
                        c = 4;
                        break;
                    }
                    break;
                case 115650330:
                    if (str.equals("glif_v3")) {
                        c = 5;
                        break;
                    }
                    break;
                case 299066663:
                    if (str.equals("material")) {
                        c = 6;
                        break;
                    }
                    break;
                case 767685465:
                    if (str.equals("glif_light")) {
                        c = 7;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return R$style.SudThemeGlifV2_Light;
                case 1:
                    return R$style.SudThemeMaterial_Light;
                case 2:
                    return R$style.SudThemeGlifV3_Light;
                case 3:
                    return R$style.SudThemeGlif;
                case 4:
                    return R$style.SudThemeGlifV2;
                case 5:
                    return R$style.SudThemeGlifV3;
                case 6:
                    return R$style.SudThemeMaterial;
                case 7:
                    return R$style.SudThemeGlif_Light;
            }
        }
        return 0;
    }

    private static int compareThemes(String str, String str2) {
        return Integer.valueOf(getThemeVersion(str)).compareTo(Integer.valueOf(getThemeVersion(str2)));
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int getThemeVersion(java.lang.String r6) {
        /*
            r0 = -1
            if (r6 == 0) goto L_0x0070
            int r1 = r6.hashCode()
            r2 = 4
            r3 = 3
            r4 = 2
            r5 = 1
            switch(r1) {
                case -2128555920: goto L_0x005e;
                case -1270463490: goto L_0x0053;
                case -1241052239: goto L_0x0048;
                case 3175618: goto L_0x003d;
                case 115650329: goto L_0x0032;
                case 115650330: goto L_0x0027;
                case 299066663: goto L_0x001c;
                case 767685465: goto L_0x0011;
                default: goto L_0x000e;
            }
        L_0x000e:
            r6 = r0
            goto L_0x0068
        L_0x0011:
            java.lang.String r1 = "glif_light"
            boolean r6 = r6.equals(r1)
            if (r6 != 0) goto L_0x001a
            goto L_0x000e
        L_0x001a:
            r6 = 7
            goto L_0x0068
        L_0x001c:
            java.lang.String r1 = "material"
            boolean r6 = r6.equals(r1)
            if (r6 != 0) goto L_0x0025
            goto L_0x000e
        L_0x0025:
            r6 = 6
            goto L_0x0068
        L_0x0027:
            java.lang.String r1 = "glif_v3"
            boolean r6 = r6.equals(r1)
            if (r6 != 0) goto L_0x0030
            goto L_0x000e
        L_0x0030:
            r6 = 5
            goto L_0x0068
        L_0x0032:
            java.lang.String r1 = "glif_v2"
            boolean r6 = r6.equals(r1)
            if (r6 != 0) goto L_0x003b
            goto L_0x000e
        L_0x003b:
            r6 = r2
            goto L_0x0068
        L_0x003d:
            java.lang.String r1 = "glif"
            boolean r6 = r6.equals(r1)
            if (r6 != 0) goto L_0x0046
            goto L_0x000e
        L_0x0046:
            r6 = r3
            goto L_0x0068
        L_0x0048:
            java.lang.String r1 = "glif_v3_light"
            boolean r6 = r6.equals(r1)
            if (r6 != 0) goto L_0x0051
            goto L_0x000e
        L_0x0051:
            r6 = r4
            goto L_0x0068
        L_0x0053:
            java.lang.String r1 = "material_light"
            boolean r6 = r6.equals(r1)
            if (r6 != 0) goto L_0x005c
            goto L_0x000e
        L_0x005c:
            r6 = r5
            goto L_0x0068
        L_0x005e:
            java.lang.String r1 = "glif_v2_light"
            boolean r6 = r6.equals(r1)
            if (r6 != 0) goto L_0x0067
            goto L_0x000e
        L_0x0067:
            r6 = 0
        L_0x0068:
            switch(r6) {
                case 0: goto L_0x006f;
                case 1: goto L_0x006e;
                case 2: goto L_0x006d;
                case 3: goto L_0x006c;
                case 4: goto L_0x006f;
                case 5: goto L_0x006d;
                case 6: goto L_0x006e;
                case 7: goto L_0x006c;
                default: goto L_0x006b;
            }
        L_0x006b:
            goto L_0x0070
        L_0x006c:
            return r4
        L_0x006d:
            return r2
        L_0x006e:
            return r5
        L_0x006f:
            return r3
        L_0x0070:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupdesign.util.ThemeResolver.getThemeVersion(java.lang.String):int");
    }

    public static class Builder {
        private int defaultTheme = R$style.SudThemeGlif_DayNight;
        private ThemeSupplier defaultThemeSupplier;
        private String oldestSupportedTheme = null;
        private boolean useDayNight = true;

        public Builder setDefaultTheme(int i) {
            this.defaultTheme = i;
            return this;
        }

        public Builder setUseDayNight(boolean z) {
            this.useDayNight = z;
            return this;
        }

        public ThemeResolver build() {
            return new ThemeResolver(this.defaultTheme, this.oldestSupportedTheme, this.defaultThemeSupplier, this.useDayNight);
        }
    }
}
