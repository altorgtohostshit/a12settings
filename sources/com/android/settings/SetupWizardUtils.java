package com.android.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sysprop.SetupWizardProperties;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.util.ThemeHelper;
import java.util.Arrays;

public class SetupWizardUtils {
    public static String getThemeString(Intent intent) {
        String stringExtra = intent.getStringExtra("theme");
        return stringExtra == null ? (String) SetupWizardProperties.theme().orElse("") : stringExtra;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0047, code lost:
        if (r0.equals("glif_v2") == false) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x008d, code lost:
        if (r0.equals("glif_v3") == false) goto L_0x007e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00c9, code lost:
        if (r0.equals("glif_v3") == false) goto L_0x00c3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getTheme(android.content.Context r13, android.content.Intent r14) {
        /*
            java.lang.String r0 = getThemeString(r14)
            if (r0 == 0) goto L_0x00ec
            boolean r14 = com.google.android.setupcompat.util.WizardManagerHelper.isAnySetupWizard(r14)
            r1 = 2131951916(0x7f13012c, float:1.954026E38)
            r2 = 2131951924(0x7f130134, float:1.9540276E38)
            r3 = 3
            java.lang.String r4 = "glif_v3"
            r5 = 2
            java.lang.String r6 = "glif_v2"
            r7 = 1
            java.lang.String r8 = "glif_v3_light"
            r9 = 0
            java.lang.String r10 = "glif_v2_light"
            r11 = -1
            if (r14 == 0) goto L_0x00bc
            boolean r13 = com.google.android.setupdesign.util.ThemeHelper.isSetupWizardDayNightEnabled(r13)
            r14 = 4
            java.lang.String r12 = "glif_light"
            if (r13 == 0) goto L_0x0077
            int r13 = r0.hashCode()
            switch(r13) {
                case -2128555920: goto L_0x005e;
                case -1241052239: goto L_0x0055;
                case 3175618: goto L_0x004a;
                case 115650329: goto L_0x0043;
                case 115650330: goto L_0x003a;
                case 767685465: goto L_0x0031;
                default: goto L_0x002f;
            }
        L_0x002f:
            r3 = r11
            goto L_0x0066
        L_0x0031:
            boolean r13 = r0.equals(r12)
            if (r13 != 0) goto L_0x0038
            goto L_0x002f
        L_0x0038:
            r3 = 5
            goto L_0x0066
        L_0x003a:
            boolean r13 = r0.equals(r4)
            if (r13 != 0) goto L_0x0041
            goto L_0x002f
        L_0x0041:
            r3 = r14
            goto L_0x0066
        L_0x0043:
            boolean r13 = r0.equals(r6)
            if (r13 != 0) goto L_0x0066
            goto L_0x002f
        L_0x004a:
            java.lang.String r13 = "glif"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x0053
            goto L_0x002f
        L_0x0053:
            r3 = r5
            goto L_0x0066
        L_0x0055:
            boolean r13 = r0.equals(r8)
            if (r13 != 0) goto L_0x005c
            goto L_0x002f
        L_0x005c:
            r3 = r7
            goto L_0x0066
        L_0x005e:
            boolean r13 = r0.equals(r10)
            if (r13 != 0) goto L_0x0065
            goto L_0x002f
        L_0x0065:
            r3 = r9
        L_0x0066:
            switch(r3) {
                case 0: goto L_0x0073;
                case 1: goto L_0x006f;
                case 2: goto L_0x006b;
                case 3: goto L_0x0073;
                case 4: goto L_0x006f;
                case 5: goto L_0x006b;
                default: goto L_0x0069;
            }
        L_0x0069:
            goto L_0x00ec
        L_0x006b:
            r13 = 2131951914(0x7f13012a, float:1.9540256E38)
            return r13
        L_0x006f:
            r13 = 2131951925(0x7f130135, float:1.9540278E38)
            return r13
        L_0x0073:
            r13 = 2131951917(0x7f13012d, float:1.9540262E38)
            return r13
        L_0x0077:
            int r13 = r0.hashCode()
            switch(r13) {
                case -2128555920: goto L_0x00a2;
                case -1241052239: goto L_0x0099;
                case 115650329: goto L_0x0090;
                case 115650330: goto L_0x0089;
                case 767685465: goto L_0x0080;
                default: goto L_0x007e;
            }
        L_0x007e:
            r3 = r11
            goto L_0x00aa
        L_0x0080:
            boolean r13 = r0.equals(r12)
            if (r13 != 0) goto L_0x0087
            goto L_0x007e
        L_0x0087:
            r3 = r14
            goto L_0x00aa
        L_0x0089:
            boolean r13 = r0.equals(r4)
            if (r13 != 0) goto L_0x00aa
            goto L_0x007e
        L_0x0090:
            boolean r13 = r0.equals(r6)
            if (r13 != 0) goto L_0x0097
            goto L_0x007e
        L_0x0097:
            r3 = r5
            goto L_0x00aa
        L_0x0099:
            boolean r13 = r0.equals(r8)
            if (r13 != 0) goto L_0x00a0
            goto L_0x007e
        L_0x00a0:
            r3 = r7
            goto L_0x00aa
        L_0x00a2:
            boolean r13 = r0.equals(r10)
            if (r13 != 0) goto L_0x00a9
            goto L_0x007e
        L_0x00a9:
            r3 = r9
        L_0x00aa:
            switch(r3) {
                case 0: goto L_0x00b8;
                case 1: goto L_0x00b4;
                case 2: goto L_0x00b3;
                case 3: goto L_0x00b2;
                case 4: goto L_0x00ae;
                default: goto L_0x00ad;
            }
        L_0x00ad:
            goto L_0x00ec
        L_0x00ae:
            r13 = 2131951915(0x7f13012b, float:1.9540258E38)
            return r13
        L_0x00b2:
            return r2
        L_0x00b3:
            return r1
        L_0x00b4:
            r13 = 2131951929(0x7f130139, float:1.9540286E38)
            return r13
        L_0x00b8:
            r13 = 2131951919(0x7f13012f, float:1.9540266E38)
            return r13
        L_0x00bc:
            int r13 = r0.hashCode()
            switch(r13) {
                case -2128555920: goto L_0x00de;
                case -1241052239: goto L_0x00d5;
                case 115650329: goto L_0x00cc;
                case 115650330: goto L_0x00c5;
                default: goto L_0x00c3;
            }
        L_0x00c3:
            r3 = r11
            goto L_0x00e6
        L_0x00c5:
            boolean r13 = r0.equals(r4)
            if (r13 != 0) goto L_0x00e6
            goto L_0x00c3
        L_0x00cc:
            boolean r13 = r0.equals(r6)
            if (r13 != 0) goto L_0x00d3
            goto L_0x00c3
        L_0x00d3:
            r3 = r5
            goto L_0x00e6
        L_0x00d5:
            boolean r13 = r0.equals(r8)
            if (r13 != 0) goto L_0x00dc
            goto L_0x00c3
        L_0x00dc:
            r3 = r7
            goto L_0x00e6
        L_0x00de:
            boolean r13 = r0.equals(r10)
            if (r13 != 0) goto L_0x00e5
            goto L_0x00c3
        L_0x00e5:
            r3 = r9
        L_0x00e6:
            switch(r3) {
                case 0: goto L_0x00eb;
                case 1: goto L_0x00ea;
                case 2: goto L_0x00eb;
                case 3: goto L_0x00ea;
                default: goto L_0x00e9;
            }
        L_0x00e9:
            goto L_0x00ec
        L_0x00ea:
            return r2
        L_0x00eb:
            return r1
        L_0x00ec:
            r13 = 2131951913(0x7f130129, float:1.9540254E38)
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.SetupWizardUtils.getTheme(android.content.Context, android.content.Intent):int");
    }

    public static int getTransparentTheme(Context context, Intent intent) {
        int theme = getTheme(context, intent);
        int i = ThemeHelper.isSetupWizardDayNightEnabled(context) ? 2131951918 : 2131951920;
        if (theme == 2131951925) {
            return 2131951927;
        }
        if (theme == 2131951929) {
            return 2131951931;
        }
        if (theme == 2131951917) {
            return 2131951918;
        }
        if (theme == 2131951919) {
            return 2131951920;
        }
        if (theme == 2131951914) {
            return 2131952069;
        }
        if (theme == 2131951915) {
            return 2131952070;
        }
        if (theme == R.style.GlifV3Theme) {
            return 2131951933;
        }
        if (theme == R.style.GlifV2Theme) {
            return 2131951921;
        }
        if (theme == R.style.GlifTheme) {
            return 2131952071;
        }
        return i;
    }

    public static void copySetupExtras(Intent intent, Intent intent2) {
        WizardManagerHelper.copyWizardManagerExtras(intent, intent2);
    }

    public static Bundle copyLifecycleExtra(Bundle bundle, Bundle bundle2) {
        for (String str : Arrays.asList(new String[]{"firstRun", "isSetupFlow"})) {
            bundle2.putBoolean(str, bundle.getBoolean(str, false));
        }
        return bundle2;
    }
}
