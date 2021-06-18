package com.android.settings.wifi.dpp;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import com.android.settings.R;
import com.android.wifitrackerlib.WifiEntry;
import java.time.Duration;

public class WifiDppUtils {
    private static final Duration VIBRATE_DURATION_QR_CODE_RECOGNITION = Duration.ofMillis(3);

    static boolean isWifiDppEnabled(Context context) {
        return ((WifiManager) context.getSystemService(WifiManager.class)).isEasyConnectSupported();
    }

    public static Intent getEnrolleeQrCodeScannerIntent(String str) {
        Intent intent = new Intent("android.settings.WIFI_DPP_ENROLLEE_QR_CODE_SCANNER");
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra("ssid", str);
        }
        return intent;
    }

    private static String getPresharedKey(WifiManager wifiManager, WifiConfiguration wifiConfiguration) {
        for (WifiConfiguration wifiConfiguration2 : wifiManager.getPrivilegedConfiguredNetworks()) {
            if (wifiConfiguration2.networkId == wifiConfiguration.networkId) {
                if (!wifiConfiguration.allowedKeyManagement.get(0) || !wifiConfiguration.allowedAuthAlgorithms.get(1)) {
                    return wifiConfiguration2.preSharedKey;
                }
                return wifiConfiguration2.wepKeys[wifiConfiguration2.wepTxKeyIndex];
            }
        }
        return wifiConfiguration.preSharedKey;
    }

    static String removeFirstAndLastDoubleQuotes(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int i = 0;
        int length = str.length() - 1;
        if (str.charAt(0) == '\"') {
            i = 1;
        }
        if (str.charAt(length) == '\"') {
            length--;
        }
        return str.substring(i, length + 1);
    }

    static String getSecurityString(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.allowedKeyManagement.get(8)) {
            return "SAE";
        }
        if (wifiConfiguration.allowedKeyManagement.get(9)) {
            return "nopass";
        }
        if (wifiConfiguration.allowedKeyManagement.get(1) || wifiConfiguration.allowedKeyManagement.get(4)) {
            return "WPA";
        }
        if (wifiConfiguration.wepKeys[0] == null) {
            return "nopass";
        }
        return "WEP";
    }

    static String getSecurityString(WifiEntry wifiEntry) {
        int security = wifiEntry.getSecurity();
        if (security == 1) {
            return "WEP";
        }
        if (security != 2) {
            return security != 5 ? "nopass" : "SAE";
        }
        return "WPA";
    }

    public static Intent getConfiguratorQrCodeGeneratorIntentOrNull(Context context, WifiManager wifiManager, WifiEntry wifiEntry) {
        Intent intent = new Intent(context, WifiDppConfiguratorActivity.class);
        if (!wifiEntry.canShare()) {
            return null;
        }
        intent.setAction("android.settings.WIFI_DPP_CONFIGURATOR_QR_CODE_GENERATOR");
        setConfiguratorIntentExtra(intent, wifiManager, wifiEntry.getWifiConfiguration());
        return intent;
    }

    public static Intent getConfiguratorQrCodeScannerIntentOrNull(Context context, WifiManager wifiManager, WifiEntry wifiEntry) {
        Intent intent = new Intent(context, WifiDppConfiguratorActivity.class);
        if (!wifiEntry.canEasyConnect()) {
            return null;
        }
        intent.setAction("android.settings.WIFI_DPP_CONFIGURATOR_QR_CODE_SCANNER");
        WifiConfiguration wifiConfiguration = wifiEntry.getWifiConfiguration();
        setConfiguratorIntentExtra(intent, wifiManager, wifiConfiguration);
        int i = wifiConfiguration.networkId;
        if (i != -1) {
            intent.putExtra("networkId", i);
            return intent;
        }
        throw new IllegalArgumentException("Invalid network ID");
    }

    public static Intent getHotspotConfiguratorIntentOrNull(Context context, WifiManager wifiManager, SoftApConfiguration softApConfiguration) {
        Intent intent = new Intent(context, WifiDppConfiguratorActivity.class);
        if (!isSupportHotspotConfiguratorQrCodeGenerator(softApConfiguration)) {
            return null;
        }
        intent.setAction("android.settings.WIFI_DPP_CONFIGURATOR_QR_CODE_GENERATOR");
        String removeFirstAndLastDoubleQuotes = removeFirstAndLastDoubleQuotes(softApConfiguration.getSsid());
        int securityType = softApConfiguration.getSecurityType();
        String str = securityType == 3 ? "SAE" : (securityType == 1 || securityType == 2) ? "WPA" : "nopass";
        String removeFirstAndLastDoubleQuotes2 = removeFirstAndLastDoubleQuotes(softApConfiguration.getPassphrase());
        if (!TextUtils.isEmpty(removeFirstAndLastDoubleQuotes)) {
            intent.putExtra("ssid", removeFirstAndLastDoubleQuotes);
        }
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra("security", str);
        }
        if (!TextUtils.isEmpty(removeFirstAndLastDoubleQuotes2)) {
            intent.putExtra("preSharedKey", removeFirstAndLastDoubleQuotes2);
        }
        intent.putExtra("hiddenSsid", softApConfiguration.isHiddenSsid());
        intent.putExtra("networkId", -1);
        intent.putExtra("isHotspot", true);
        return intent;
    }

    private static void setConfiguratorIntentExtra(Intent intent, WifiManager wifiManager, WifiConfiguration wifiConfiguration) {
        String removeFirstAndLastDoubleQuotes = removeFirstAndLastDoubleQuotes(wifiConfiguration.SSID);
        String securityString = getSecurityString(wifiConfiguration);
        String removeFirstAndLastDoubleQuotes2 = removeFirstAndLastDoubleQuotes(getPresharedKey(wifiManager, wifiConfiguration));
        if (!TextUtils.isEmpty(removeFirstAndLastDoubleQuotes)) {
            intent.putExtra("ssid", removeFirstAndLastDoubleQuotes);
        }
        if (!TextUtils.isEmpty(securityString)) {
            intent.putExtra("security", securityString);
        }
        if (!TextUtils.isEmpty(removeFirstAndLastDoubleQuotes2)) {
            intent.putExtra("preSharedKey", removeFirstAndLastDoubleQuotes2);
        }
        intent.putExtra("hiddenSsid", wifiConfiguration.hiddenSSID);
    }

    public static void showLockScreen(Context context, final Runnable runnable) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService("keyguard");
        if (keyguardManager.isKeyguardSecure()) {
            C14021 r1 = new BiometricPrompt.AuthenticationCallback() {
                public void onAuthenticationError(int i, CharSequence charSequence) {
                }

                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult authenticationResult) {
                    runnable.run();
                }
            };
            BiometricPrompt.Builder title = new BiometricPrompt.Builder(context).setTitle(context.getText(R.string.wifi_dpp_lockscreen_title));
            if (keyguardManager.isDeviceSecure()) {
                title.setDeviceCredentialAllowed(true);
            }
            title.build().authenticate(new CancellationSignal(), new WifiDppUtils$$ExternalSyntheticLambda0(new Handler(Looper.getMainLooper())), r1);
            return;
        }
        runnable.run();
    }

    public static boolean isSupportEnrolleeQrCodeScanner(Context context, int i) {
        return isSupportWifiDpp(context, i) || isSupportZxing(context, i);
    }

    private static boolean isSupportHotspotConfiguratorQrCodeGenerator(SoftApConfiguration softApConfiguration) {
        int securityType = softApConfiguration.getSecurityType();
        return securityType == 3 || securityType == 2 || securityType == 1 || securityType == 0;
    }

    private static boolean isSupportWifiDpp(Context context, int i) {
        if (!isWifiDppEnabled(context)) {
            return false;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService(WifiManager.class);
        if (i == 2) {
            return true;
        }
        if (i == 5 && wifiManager.isWpa3SaeSupported()) {
            return true;
        }
        return false;
    }

    private static boolean isSupportZxing(Context context, int i) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WifiManager.class);
        if (i == 0 || i == 1 || i == 2) {
            return true;
        }
        if (i != 4) {
            if (i == 5 && wifiManager.isWpa3SaeSupported()) {
                return true;
            }
            return false;
        } else if (wifiManager.isEnhancedOpenSupported()) {
            return true;
        } else {
            return false;
        }
    }

    static void triggerVibrationForQrCodeRecognition(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_DURATION_QR_CODE_RECOGNITION.toMillis(), -1));
        }
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
}
