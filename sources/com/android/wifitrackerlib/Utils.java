package com.android.wifitrackerlib;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkKey;
import android.net.NetworkScoreManager;
import android.net.ScoredNetwork;
import android.net.WifiKey;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Annotation;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import com.android.settingslib.HelpUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

public class Utils {
    private static NetworkScoreManager sNetworkScoreManager;

    private static int roundToClosestSpeedEnum(int i) {
        if (i == 0) {
            return 0;
        }
        if (i < 7) {
            return 5;
        }
        if (i < 15) {
            return 10;
        }
        return i < 25 ? 20 : 30;
    }

    private static String getActiveScorerPackage(Context context) {
        if (sNetworkScoreManager == null) {
            sNetworkScoreManager = (NetworkScoreManager) context.getSystemService(NetworkScoreManager.class);
        }
        return sNetworkScoreManager.getActiveScorerPackage();
    }

    public static ScanResult getBestScanResultByLevel(List<ScanResult> list) {
        if (list.isEmpty()) {
            return null;
        }
        return (ScanResult) Collections.max(list, Comparator.comparingInt(Utils$$ExternalSyntheticLambda1.INSTANCE));
    }

    static List<Integer> getSecurityTypesFromScanResult(ScanResult scanResult) {
        ArrayList arrayList = new ArrayList();
        String str = scanResult.capabilities;
        if (str == null) {
            arrayList.add(0);
        } else if (str.contains("PSK") && scanResult.capabilities.contains("SAE")) {
            arrayList.add(2);
            arrayList.add(5);
        } else if (scanResult.capabilities.contains("OWE_TRANSITION")) {
            arrayList.add(0);
            arrayList.add(4);
        } else if (scanResult.capabilities.contains("OWE")) {
            arrayList.add(4);
        } else if (scanResult.capabilities.contains("WEP")) {
            arrayList.add(1);
        } else if (scanResult.capabilities.contains("SAE")) {
            arrayList.add(5);
        } else if (scanResult.capabilities.contains("PSK")) {
            arrayList.add(2);
        } else if (scanResult.capabilities.contains("EAP_SUITE_B_192")) {
            arrayList.add(6);
        } else if (scanResult.capabilities.contains("EAP")) {
            arrayList.add(3);
        } else {
            arrayList.add(0);
        }
        return arrayList;
    }

    static int getSecurityTypeFromWifiConfiguration(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.allowedKeyManagement.get(8)) {
            return 5;
        }
        if (wifiConfiguration.allowedKeyManagement.get(1)) {
            return 2;
        }
        if (wifiConfiguration.allowedKeyManagement.get(10)) {
            return 6;
        }
        if (wifiConfiguration.allowedKeyManagement.get(2) || wifiConfiguration.allowedKeyManagement.get(3)) {
            return 3;
        }
        if (wifiConfiguration.allowedKeyManagement.get(9)) {
            return 4;
        }
        if (wifiConfiguration.wepKeys[0] != null) {
            return 1;
        }
        return 0;
    }

    static int getSecurityTypeFromWifiInfo(WifiInfo wifiInfo) {
        switch (wifiInfo.getCurrentSecurityType()) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 4;
            case 7:
            case 8:
            case 9:
                return 7;
            case 11:
            case 12:
                return 3;
            default:
                return 0;
        }
    }

    public static int getAverageSpeedFromScanResults(WifiNetworkScoreCache wifiNetworkScoreCache, List<ScanResult> list) {
        int calculateBadge;
        int i = 0;
        int i2 = 0;
        for (ScanResult next : list) {
            ScoredNetwork scoredNetwork = wifiNetworkScoreCache.getScoredNetwork(next);
            if (!(scoredNetwork == null || (calculateBadge = scoredNetwork.calculateBadge(next.level)) == 0)) {
                i++;
                i2 += calculateBadge;
            }
        }
        if (i == 0) {
            return 0;
        }
        return roundToClosestSpeedEnum(i2 / i);
    }

    public static int getSpeedFromWifiInfo(WifiNetworkScoreCache wifiNetworkScoreCache, WifiInfo wifiInfo) {
        try {
            ScoredNetwork scoredNetwork = wifiNetworkScoreCache.getScoredNetwork(new NetworkKey(new WifiKey(wifiInfo.getSSID(), wifiInfo.getBSSID())));
            if (scoredNetwork == null) {
                return 0;
            }
            return roundToClosestSpeedEnum(scoredNetwork.calculateBadge(wifiInfo.getRssi()));
        } catch (IllegalArgumentException unused) {
            return 0;
        }
    }

    static String getAppLabel(Context context, String str) {
        try {
            String string = Settings.Global.getString(context.getContentResolver(), "use_open_wifi_package");
            if (!TextUtils.isEmpty(string) && TextUtils.equals(str, getActiveScorerPackage(context))) {
                str = string;
            }
            return context.getPackageManager().getApplicationInfo(str, 0).loadLabel(context.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException unused) {
            return "";
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0051, code lost:
        if (r5 != 9) goto L_0x008a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.String getDisconnectedStateDescription(android.content.Context r4, com.android.wifitrackerlib.WifiEntry r5) {
        /*
            java.lang.String r0 = ""
            if (r4 == 0) goto L_0x00b5
            if (r5 != 0) goto L_0x0008
            goto L_0x00b5
        L_0x0008:
            android.net.wifi.WifiConfiguration r1 = r5.getWifiConfiguration()
            if (r1 != 0) goto L_0x0010
            r4 = 0
            return r4
        L_0x0010:
            boolean r2 = r1.hasNoInternetAccess()
            r3 = 2
            if (r2 == 0) goto L_0x002b
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r5 = r1.getNetworkSelectionStatus()
            int r5 = r5.getNetworkSelectionStatus()
            if (r5 != r3) goto L_0x0024
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_no_internet_no_reconnect
            goto L_0x0026
        L_0x0024:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_no_internet
        L_0x0026:
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x002b:
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r2 = r1.getNetworkSelectionStatus()
            int r2 = r2.getNetworkSelectionStatus()
            if (r2 == 0) goto L_0x0077
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r5 = r1.getNetworkSelectionStatus()
            int r5 = r5.getNetworkSelectionDisableReason()
            r1 = 1
            if (r5 == r1) goto L_0x0070
            if (r5 == r3) goto L_0x0069
            r1 = 3
            if (r5 == r1) goto L_0x0062
            r1 = 4
            if (r5 == r1) goto L_0x005b
            r1 = 6
            if (r5 == r1) goto L_0x005b
            r1 = 8
            if (r5 == r1) goto L_0x0054
            r1 = 9
            if (r5 == r1) goto L_0x0069
            goto L_0x008a
        L_0x0054:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_check_password_try_again
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x005b:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_no_internet_no_reconnect
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0062:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_disabled_network_failure
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0069:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_disabled_password_failure
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0070:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_disabled_generic
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0077:
            int r5 = r5.getLevel()
            r2 = -1
            if (r5 != r2) goto L_0x007f
            goto L_0x008a
        L_0x007f:
            int r5 = r1.getRecentFailureReason()
            r1 = 17
            if (r5 == r1) goto L_0x00ae
            switch(r5) {
                case 1002: goto L_0x00ae;
                case 1003: goto L_0x00a7;
                case 1004: goto L_0x00ae;
                case 1005: goto L_0x00a0;
                case 1006: goto L_0x0099;
                case 1007: goto L_0x00a0;
                case 1008: goto L_0x00a0;
                case 1009: goto L_0x0092;
                case 1010: goto L_0x0092;
                case 1011: goto L_0x008b;
                default: goto L_0x008a;
            }
        L_0x008a:
            return r0
        L_0x008b:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_network_not_found
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0092:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_mbo_oce_assoc_disallowed_insufficient_rssi
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0099:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_mbo_assoc_disallowed_max_num_sta_associated
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x00a0:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_mbo_assoc_disallowed_cannot_connect
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x00a7:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_poor_channel_conditions
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x00ae:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_ap_unable_to_handle_new_sta
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x00b5:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.Utils.getDisconnectedStateDescription(android.content.Context, com.android.wifitrackerlib.WifiEntry):java.lang.String");
    }

    static String getAutoConnectDescription(Context context, WifiEntry wifiEntry) {
        if (context == null || wifiEntry == null || !wifiEntry.canSetAutoJoinEnabled() || wifiEntry.isAutoJoinEnabled()) {
            return "";
        }
        return context.getString(R$string.wifitrackerlib_auto_connect_disable);
    }

    static String getMeteredDescription(Context context, WifiEntry wifiEntry) {
        if (context == null || wifiEntry == null) {
            return "";
        }
        if (!wifiEntry.canSetMeteredChoice() && wifiEntry.getMeteredChoice() != 1) {
            return "";
        }
        if (wifiEntry.getMeteredChoice() == 1) {
            return context.getString(R$string.wifitrackerlib_wifi_metered_label);
        }
        if (wifiEntry.getMeteredChoice() == 2) {
            return context.getString(R$string.wifitrackerlib_wifi_unmetered_label);
        }
        if (wifiEntry.isMetered()) {
            return context.getString(R$string.wifitrackerlib_wifi_metered_label);
        }
        return "";
    }

    static String getSpeedDescription(Context context, WifiEntry wifiEntry) {
        if (context == null || wifiEntry == null) {
            return "";
        }
        int speed = wifiEntry.getSpeed();
        if (speed == 5) {
            return context.getString(R$string.wifitrackerlib_speed_label_slow);
        }
        if (speed == 10) {
            return context.getString(R$string.wifitrackerlib_speed_label_okay);
        }
        if (speed == 20) {
            return context.getString(R$string.wifitrackerlib_speed_label_fast);
        }
        if (speed != 30) {
            return "";
        }
        return context.getString(R$string.wifitrackerlib_speed_label_very_fast);
    }

    static String getVerboseLoggingDescription(WifiEntry wifiEntry) {
        if (!BaseWifiTracker.isVerboseLoggingEnabled() || wifiEntry == null) {
            return "";
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        String wifiInfoDescription = wifiEntry.getWifiInfoDescription();
        if (!TextUtils.isEmpty(wifiInfoDescription)) {
            stringJoiner.add(wifiInfoDescription);
        }
        String networkCapabilityDescription = wifiEntry.getNetworkCapabilityDescription();
        if (!TextUtils.isEmpty(networkCapabilityDescription)) {
            stringJoiner.add(networkCapabilityDescription);
        }
        String scanResultDescription = wifiEntry.getScanResultDescription();
        if (!TextUtils.isEmpty(scanResultDescription)) {
            stringJoiner.add(scanResultDescription);
        }
        String networkSelectionDescription = wifiEntry.getNetworkSelectionDescription();
        if (!TextUtils.isEmpty(networkSelectionDescription)) {
            stringJoiner.add(networkSelectionDescription);
        }
        return stringJoiner.toString();
    }

    static String getNetworkSelectionDescription(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        WifiConfiguration.NetworkSelectionStatus networkSelectionStatus = wifiConfiguration.getNetworkSelectionStatus();
        if (networkSelectionStatus.getNetworkSelectionStatus() != 0) {
            sb.append(" (" + networkSelectionStatus.getNetworkStatusString());
            if (networkSelectionStatus.getDisableTime() > 0) {
                sb.append(" " + DateUtils.formatElapsedTime((System.currentTimeMillis() - networkSelectionStatus.getDisableTime()) / 1000));
            }
            sb.append(")");
        }
        int maxNetworkSelectionDisableReason = WifiConfiguration.NetworkSelectionStatus.getMaxNetworkSelectionDisableReason();
        for (int i = 0; i <= maxNetworkSelectionDisableReason; i++) {
            int disableReasonCounter = networkSelectionStatus.getDisableReasonCounter(i);
            if (disableReasonCounter != 0) {
                sb.append(" ");
                sb.append(WifiConfiguration.NetworkSelectionStatus.getNetworkSelectionDisableReasonString(i));
                sb.append("=");
                sb.append(disableReasonCounter);
            }
        }
        return sb.toString();
    }

    static String getCurrentNetworkCapabilitiesInformation(Context context, NetworkCapabilities networkCapabilities) {
        if (!(context == null || networkCapabilities == null)) {
            if (networkCapabilities.hasCapability(17)) {
                return context.getString(context.getResources().getIdentifier("network_available_sign_in", "string", "android"));
            }
            if (networkCapabilities.hasCapability(24)) {
                return context.getString(R$string.wifitrackerlib_wifi_limited_connection);
            }
            if (!networkCapabilities.hasCapability(16)) {
                if (networkCapabilities.isPrivateDnsBroken()) {
                    return context.getString(R$string.wifitrackerlib_private_dns_broken);
                }
                return context.getString(R$string.wifitrackerlib_wifi_connected_cannot_provide_internet);
            }
        }
        return "";
    }

    static String getNetworkDetailedState(Context context, NetworkInfo networkInfo) {
        NetworkInfo.DetailedState detailedState;
        if (context == null || networkInfo == null || (detailedState = networkInfo.getDetailedState()) == null) {
            return "";
        }
        String[] stringArray = context.getResources().getStringArray(R$array.wifitrackerlib_wifi_status);
        int ordinal = detailedState.ordinal();
        if (ordinal >= stringArray.length) {
            return "";
        }
        return stringArray[ordinal];
    }

    static boolean isSimPresent(Context context, int i) {
        List<SubscriptionInfo> activeSubscriptionInfoList;
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
        if (subscriptionManager == null || (activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList()) == null || activeSubscriptionInfoList.isEmpty()) {
            return false;
        }
        return activeSubscriptionInfoList.stream().anyMatch(new Utils$$ExternalSyntheticLambda0(i));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$isSimPresent$1(int i, SubscriptionInfo subscriptionInfo) {
        return subscriptionInfo.getCarrierId() == i;
    }

    static String getCarrierNameForSubId(Context context, int i) {
        TelephonyManager telephonyManager;
        TelephonyManager createForSubscriptionId;
        CharSequence simCarrierIdName;
        if (i == -1 || (telephonyManager = (TelephonyManager) context.getSystemService("phone")) == null || (createForSubscriptionId = telephonyManager.createForSubscriptionId(i)) == null || (simCarrierIdName = createForSubscriptionId.getSimCarrierIdName()) == null) {
            return null;
        }
        return simCarrierIdName.toString();
    }

    static boolean isSimCredential(WifiConfiguration wifiConfiguration) {
        WifiEnterpriseConfig wifiEnterpriseConfig = wifiConfiguration.enterpriseConfig;
        return wifiEnterpriseConfig != null && wifiEnterpriseConfig.isAuthenticationSimBased();
    }

    static int getSubIdForConfig(Context context, WifiConfiguration wifiConfiguration) {
        SubscriptionManager subscriptionManager;
        int i = -1;
        if (wifiConfiguration.carrierId == -1 || (subscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service")) == null) {
            return -1;
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList != null && !activeSubscriptionInfoList.isEmpty()) {
            int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            for (SubscriptionInfo next : activeSubscriptionInfoList) {
                if (next.getCarrierId() == wifiConfiguration.carrierId && (i = next.getSubscriptionId()) == defaultDataSubscriptionId) {
                    break;
                }
            }
        }
        return i;
    }

    static boolean isImsiPrivacyProtectionProvided(Context context, int i) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null || (configForSubId.getInt("imsi_key_availability_int") & 2) == 0) {
            return false;
        }
        return true;
    }

    static CharSequence getImsiProtectionDescription(Context context, WifiConfiguration wifiConfiguration) {
        int i;
        if (!(context == null || wifiConfiguration == null || !isSimCredential(wifiConfiguration))) {
            if (wifiConfiguration.carrierId == -1) {
                i = SubscriptionManager.getDefaultSubscriptionId();
            } else {
                i = getSubIdForConfig(context, wifiConfiguration);
            }
            if (i != -1 && !isImsiPrivacyProtectionProvided(context, i)) {
                return linkifyAnnotation(context, context.getText(R$string.wifitrackerlib_imsi_protection_warning), "url", context.getString(R$string.wifitrackerlib_help_url_imsi_protection));
            }
        }
        return "";
    }

    static CharSequence linkifyAnnotation(final Context context, CharSequence charSequence, String str, final String str2) {
        if (TextUtils.isEmpty(str2)) {
            return charSequence;
        }
        SpannableString spannableString = new SpannableString(charSequence);
        for (Annotation annotation : (Annotation[]) spannableString.getSpans(0, spannableString.length(), Annotation.class)) {
            if (TextUtils.equals(annotation.getValue(), str)) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spannableString);
                C15321 r8 = new ClickableSpan() {
                    public void onClick(View view) {
                        view.startActivityForResult(HelpUtils.getHelpIntent(context, str2, view.getClass().getName()), 0);
                    }
                };
                spannableStringBuilder.setSpan(r8, spannableString.getSpanStart(annotation), spannableString.getSpanEnd(annotation), spannableString.getSpanFlags(r8));
                return spannableStringBuilder;
            }
        }
        return charSequence;
    }
}
