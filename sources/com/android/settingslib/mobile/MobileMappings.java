package com.android.settingslib.mobile;

import android.content.Context;
import android.content.res.Resources;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyDisplayInfo;
import com.android.settingslib.R$bool;

public class MobileMappings {
    public static String getIconKey(TelephonyDisplayInfo telephonyDisplayInfo) {
        if (telephonyDisplayInfo.getOverrideNetworkType() == 0) {
            return toIconKey(telephonyDisplayInfo.getNetworkType());
        }
        return toDisplayIconKey(telephonyDisplayInfo.getOverrideNetworkType());
    }

    public static String toIconKey(int i) {
        return Integer.toString(i);
    }

    public static String toDisplayIconKey(int i) {
        if (i == 1) {
            return toIconKey(13) + "_CA";
        } else if (i == 2) {
            return toIconKey(13) + "_CA_Plus";
        } else if (i == 3) {
            return toIconKey(20);
        } else {
            if (i != 5) {
                return "unsupported";
            }
            return toIconKey(20) + "_Plus";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x00cb  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00ea  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map<java.lang.String, com.android.settingslib.SignalIcon$MobileIconGroup> mapIconSets(com.android.settingslib.mobile.MobileMappings.Config r9) {
        /*
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r1 = 5
            java.lang.String r2 = toIconKey(r1)
            com.android.settingslib.SignalIcon$MobileIconGroup r3 = com.android.settingslib.mobile.TelephonyIcons.THREE_G
            r0.put(r2, r3)
            r2 = 6
            java.lang.String r2 = toIconKey(r2)
            r0.put(r2, r3)
            r2 = 12
            java.lang.String r2 = toIconKey(r2)
            r0.put(r2, r3)
            r2 = 14
            java.lang.String r2 = toIconKey(r2)
            r0.put(r2, r3)
            boolean r2 = r9.show4gFor3g
            r4 = 3
            if (r2 == 0) goto L_0x0038
            java.lang.String r2 = toIconKey(r4)
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.FOUR_G
            r0.put(r2, r5)
            goto L_0x003f
        L_0x0038:
            java.lang.String r2 = toIconKey(r4)
            r0.put(r2, r3)
        L_0x003f:
            r2 = 17
            java.lang.String r2 = toIconKey(r2)
            r0.put(r2, r3)
            boolean r2 = r9.showAtLeast3G
            r5 = 7
            r6 = 4
            r7 = 0
            r8 = 2
            if (r2 != 0) goto L_0x0073
            java.lang.String r2 = toIconKey(r7)
            com.android.settingslib.SignalIcon$MobileIconGroup r7 = com.android.settingslib.mobile.TelephonyIcons.UNKNOWN
            r0.put(r2, r7)
            java.lang.String r2 = toIconKey(r8)
            com.android.settingslib.SignalIcon$MobileIconGroup r7 = com.android.settingslib.mobile.TelephonyIcons.f108E
            r0.put(r2, r7)
            java.lang.String r2 = toIconKey(r6)
            com.android.settingslib.SignalIcon$MobileIconGroup r6 = com.android.settingslib.mobile.TelephonyIcons.ONE_X
            r0.put(r2, r6)
            java.lang.String r2 = toIconKey(r5)
            r0.put(r2, r6)
            goto L_0x008f
        L_0x0073:
            java.lang.String r2 = toIconKey(r7)
            r0.put(r2, r3)
            java.lang.String r2 = toIconKey(r8)
            r0.put(r2, r3)
            java.lang.String r2 = toIconKey(r6)
            r0.put(r2, r3)
            java.lang.String r2 = toIconKey(r5)
            r0.put(r2, r3)
        L_0x008f:
            boolean r2 = r9.show4gFor3g
            if (r2 == 0) goto L_0x0096
            com.android.settingslib.SignalIcon$MobileIconGroup r3 = com.android.settingslib.mobile.TelephonyIcons.FOUR_G
            goto L_0x009f
        L_0x0096:
            boolean r2 = r9.hspaDataDistinguishable
            if (r2 == 0) goto L_0x009f
            com.android.settingslib.SignalIcon$MobileIconGroup r3 = com.android.settingslib.mobile.TelephonyIcons.f110H
            com.android.settingslib.SignalIcon$MobileIconGroup r2 = com.android.settingslib.mobile.TelephonyIcons.H_PLUS
            goto L_0x00a0
        L_0x009f:
            r2 = r3
        L_0x00a0:
            r5 = 8
            java.lang.String r5 = toIconKey(r5)
            r0.put(r5, r3)
            r5 = 9
            java.lang.String r5 = toIconKey(r5)
            r0.put(r5, r3)
            r5 = 10
            java.lang.String r5 = toIconKey(r5)
            r0.put(r5, r3)
            r3 = 15
            java.lang.String r3 = toIconKey(r3)
            r0.put(r3, r2)
            boolean r2 = r9.show4gForLte
            r3 = 13
            r5 = 1
            if (r2 == 0) goto L_0x00ea
            java.lang.String r2 = toIconKey(r3)
            com.android.settingslib.SignalIcon$MobileIconGroup r3 = com.android.settingslib.mobile.TelephonyIcons.FOUR_G
            r0.put(r2, r3)
            boolean r9 = r9.hideLtePlus
            if (r9 == 0) goto L_0x00e0
            java.lang.String r9 = toDisplayIconKey(r5)
            r0.put(r9, r3)
            goto L_0x0108
        L_0x00e0:
            java.lang.String r9 = toDisplayIconKey(r5)
            com.android.settingslib.SignalIcon$MobileIconGroup r2 = com.android.settingslib.mobile.TelephonyIcons.FOUR_G_PLUS
            r0.put(r9, r2)
            goto L_0x0108
        L_0x00ea:
            java.lang.String r2 = toIconKey(r3)
            com.android.settingslib.SignalIcon$MobileIconGroup r3 = com.android.settingslib.mobile.TelephonyIcons.LTE
            r0.put(r2, r3)
            boolean r9 = r9.hideLtePlus
            if (r9 == 0) goto L_0x00ff
            java.lang.String r9 = toDisplayIconKey(r5)
            r0.put(r9, r3)
            goto L_0x0108
        L_0x00ff:
            java.lang.String r9 = toDisplayIconKey(r5)
            com.android.settingslib.SignalIcon$MobileIconGroup r2 = com.android.settingslib.mobile.TelephonyIcons.LTE_PLUS
            r0.put(r9, r2)
        L_0x0108:
            r9 = 18
            java.lang.String r9 = toIconKey(r9)
            com.android.settingslib.SignalIcon$MobileIconGroup r2 = com.android.settingslib.mobile.TelephonyIcons.WFC
            r0.put(r9, r2)
            java.lang.String r9 = toDisplayIconKey(r8)
            com.android.settingslib.SignalIcon$MobileIconGroup r2 = com.android.settingslib.mobile.TelephonyIcons.LTE_CA_5G_E
            r0.put(r9, r2)
            java.lang.String r9 = toDisplayIconKey(r4)
            com.android.settingslib.SignalIcon$MobileIconGroup r2 = com.android.settingslib.mobile.TelephonyIcons.NR_5G
            r0.put(r9, r2)
            java.lang.String r9 = toDisplayIconKey(r1)
            com.android.settingslib.SignalIcon$MobileIconGroup r1 = com.android.settingslib.mobile.TelephonyIcons.NR_5G_PLUS
            r0.put(r9, r1)
            r9 = 20
            java.lang.String r9 = toIconKey(r9)
            r0.put(r9, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.mobile.MobileMappings.mapIconSets(com.android.settingslib.mobile.MobileMappings$Config):java.util.Map");
    }

    public static class Config {
        public boolean alwaysShowCdmaRssi = false;
        public boolean alwaysShowDataRatIcon = false;
        public boolean hideLtePlus = false;
        public boolean hspaDataDistinguishable;
        public boolean show4gFor3g = false;
        public boolean show4gForLte = false;
        public boolean showAtLeast3G = false;

        public static Config readConfig(Context context) {
            Config config = new Config();
            Resources resources = context.getResources();
            config.showAtLeast3G = resources.getBoolean(R$bool.config_showMin3G);
            config.alwaysShowCdmaRssi = resources.getBoolean(17891363);
            config.hspaDataDistinguishable = resources.getBoolean(R$bool.config_hspa_data_distinguishable);
            SubscriptionManager.from(context);
            PersistableBundle configForSubId = ((CarrierConfigManager) context.getSystemService("carrier_config")).getConfigForSubId(SubscriptionManager.getDefaultDataSubscriptionId());
            if (configForSubId != null) {
                config.alwaysShowDataRatIcon = configForSubId.getBoolean("always_show_data_rat_icon_bool");
                config.show4gForLte = configForSubId.getBoolean("show_4g_for_lte_data_icon_bool");
                config.show4gFor3g = configForSubId.getBoolean("show_4g_for_3g_data_icon_bool");
                config.hideLtePlus = configForSubId.getBoolean("hide_lte_plus_data_icon_bool");
            }
            return config;
        }
    }
}
