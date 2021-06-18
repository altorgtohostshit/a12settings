package com.android.settings.wifi.dpp;

import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.FragmentTransaction;
import com.android.settings.R;
import com.android.settings.wifi.dpp.WifiDppAddDeviceFragment;
import com.android.settings.wifi.dpp.WifiDppQrCodeScannerFragment;
import com.android.settings.wifi.dpp.WifiNetworkConfig;
import com.android.settings.wifi.dpp.WifiNetworkListFragment;

public class WifiDppConfiguratorActivity extends WifiDppBaseActivity implements WifiNetworkConfig.Retriever, WifiDppQrCodeScannerFragment.OnScanWifiDppSuccessListener, WifiDppAddDeviceFragment.OnClickChooseDifferentNetworkListener, WifiNetworkListFragment.OnChooseNetworkListener {
    private WifiQrCode mWifiDppQrCode;
    private int[] mWifiDppRemoteBandSupport;
    private WifiNetworkConfig mWifiNetworkConfig;

    public int getMetricsCategory() {
        return 1595;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.mWifiDppQrCode = WifiQrCode.getValidWifiDppQrCodeOrNull(bundle.getString("key_qr_code"));
            this.mWifiNetworkConfig = WifiNetworkConfig.getValidConfigOrNull(bundle.getString("key_wifi_security"), bundle.getString("key_wifi_ssid"), bundle.getString("key_wifi_preshared_key"), bundle.getBoolean("key_wifi_hidden_ssid"), bundle.getInt("key_wifi_network_id"), bundle.getBoolean("key_is_hotspot"));
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleIntent(android.content.Intent r6) {
        /*
            r5 = this;
            if (r6 == 0) goto L_0x0007
            java.lang.String r0 = r6.getAction()
            goto L_0x0008
        L_0x0007:
            r0 = 0
        L_0x0008:
            if (r0 != 0) goto L_0x000e
            r5.finish()
            return
        L_0x000e:
            r1 = -1
            int r2 = r0.hashCode()
            r3 = 0
            r4 = 1
            switch(r2) {
                case -902592152: goto L_0x002f;
                case 360935630: goto L_0x0024;
                case 1361318585: goto L_0x0019;
                default: goto L_0x0018;
            }
        L_0x0018:
            goto L_0x0039
        L_0x0019:
            java.lang.String r2 = "android.settings.WIFI_DPP_CONFIGURATOR_QR_CODE_SCANNER"
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x0022
            goto L_0x0039
        L_0x0022:
            r1 = 2
            goto L_0x0039
        L_0x0024:
            java.lang.String r2 = "android.settings.WIFI_DPP_CONFIGURATOR_QR_CODE_GENERATOR"
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x002d
            goto L_0x0039
        L_0x002d:
            r1 = r4
            goto L_0x0039
        L_0x002f:
            java.lang.String r2 = "android.settings.PROCESS_WIFI_EASY_CONNECT_URI"
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x0038
            goto L_0x0039
        L_0x0038:
            r1 = r3
        L_0x0039:
            switch(r1) {
                case 0: goto L_0x005f;
                case 1: goto L_0x0052;
                case 2: goto L_0x0045;
                default: goto L_0x003c;
            }
        L_0x003c:
            java.lang.String r6 = "WifiDppConfiguratorActivity"
            java.lang.String r0 = "Launch with an invalid action"
            android.util.Log.e(r6, r0)
        L_0x0043:
            r3 = r4
            goto L_0x0067
        L_0x0045:
            com.android.settings.wifi.dpp.WifiNetworkConfig r6 = com.android.settings.wifi.dpp.WifiNetworkConfig.getValidConfigOrNull(r6)
            if (r6 != 0) goto L_0x004c
        L_0x004b:
            goto L_0x0043
        L_0x004c:
            r5.mWifiNetworkConfig = r6
            r5.showQrCodeScannerFragment()
            goto L_0x0067
        L_0x0052:
            com.android.settings.wifi.dpp.WifiNetworkConfig r6 = com.android.settings.wifi.dpp.WifiNetworkConfig.getValidConfigOrNull(r6)
            if (r6 != 0) goto L_0x0059
            goto L_0x004b
        L_0x0059:
            r5.mWifiNetworkConfig = r6
            r5.showQrCodeGeneratorFragment()
            goto L_0x0067
        L_0x005f:
            com.android.settings.wifi.dpp.WifiDppConfiguratorActivity$$ExternalSyntheticLambda0 r0 = new com.android.settings.wifi.dpp.WifiDppConfiguratorActivity$$ExternalSyntheticLambda0
            r0.<init>(r5, r6)
            com.android.settings.wifi.dpp.WifiDppUtils.showLockScreen(r5, r0)
        L_0x0067:
            if (r3 == 0) goto L_0x006c
            r5.finish()
        L_0x006c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.wifi.dpp.WifiDppConfiguratorActivity.handleIntent(android.content.Intent):void");
    }

    /* access modifiers changed from: private */
    /* renamed from: handleActionProcessWifiEasyConnectUriIntent */
    public void lambda$handleIntent$0(Intent intent) {
        String str;
        Uri data = intent.getData();
        if (data == null) {
            str = null;
        } else {
            str = data.toString();
        }
        this.mWifiDppQrCode = WifiQrCode.getValidWifiDppQrCodeOrNull(str);
        this.mWifiDppRemoteBandSupport = intent.getIntArrayExtra("android.provider.extra.EASY_CONNECT_BAND_LIST");
        boolean isWifiDppEnabled = WifiDppUtils.isWifiDppEnabled(this);
        if (!isWifiDppEnabled) {
            Log.e("WifiDppConfiguratorActivity", "ACTION_PROCESS_WIFI_EASY_CONNECT_URI for a device that doesn't support Wifi DPP - use WifiManager#isEasyConnectSupported");
        }
        if (this.mWifiDppQrCode == null) {
            Log.e("WifiDppConfiguratorActivity", "ACTION_PROCESS_WIFI_EASY_CONNECT_URI with null URI!");
        }
        if (this.mWifiDppQrCode == null || !isWifiDppEnabled) {
            finish();
            return;
        }
        WifiNetworkConfig connectedWifiNetworkConfigOrNull = getConnectedWifiNetworkConfigOrNull();
        if (connectedWifiNetworkConfigOrNull == null || !connectedWifiNetworkConfigOrNull.isSupportWifiDpp(this)) {
            showChooseSavedWifiNetworkFragment(false);
            return;
        }
        this.mWifiNetworkConfig = connectedWifiNetworkConfigOrNull;
        showAddDeviceFragment(false);
    }

    private void showQrCodeScannerFragment() {
        WifiDppQrCodeScannerFragment wifiDppQrCodeScannerFragment = (WifiDppQrCodeScannerFragment) this.mFragmentManager.findFragmentByTag("qr_code_scanner_fragment");
        if (wifiDppQrCodeScannerFragment == null) {
            WifiDppQrCodeScannerFragment wifiDppQrCodeScannerFragment2 = new WifiDppQrCodeScannerFragment();
            FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
            beginTransaction.replace(R.id.fragment_container, wifiDppQrCodeScannerFragment2, "qr_code_scanner_fragment");
            beginTransaction.commit();
        } else if (!wifiDppQrCodeScannerFragment.isVisible()) {
            this.mFragmentManager.popBackStackImmediate();
        }
    }

    private void showQrCodeGeneratorFragment() {
        WifiDppQrCodeGeneratorFragment wifiDppQrCodeGeneratorFragment = (WifiDppQrCodeGeneratorFragment) this.mFragmentManager.findFragmentByTag("qr_code_generator_fragment");
        if (wifiDppQrCodeGeneratorFragment == null) {
            WifiDppQrCodeGeneratorFragment wifiDppQrCodeGeneratorFragment2 = new WifiDppQrCodeGeneratorFragment();
            FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
            beginTransaction.replace(R.id.fragment_container, wifiDppQrCodeGeneratorFragment2, "qr_code_generator_fragment");
            beginTransaction.commit();
        } else if (!wifiDppQrCodeGeneratorFragment.isVisible()) {
            this.mFragmentManager.popBackStackImmediate();
        }
    }

    private void showChooseSavedWifiNetworkFragment(boolean z) {
        WifiDppChooseSavedWifiNetworkFragment wifiDppChooseSavedWifiNetworkFragment = (WifiDppChooseSavedWifiNetworkFragment) this.mFragmentManager.findFragmentByTag("choose_saved_wifi_network_fragment");
        if (wifiDppChooseSavedWifiNetworkFragment == null) {
            WifiDppChooseSavedWifiNetworkFragment wifiDppChooseSavedWifiNetworkFragment2 = new WifiDppChooseSavedWifiNetworkFragment();
            FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
            beginTransaction.replace(R.id.fragment_container, wifiDppChooseSavedWifiNetworkFragment2, "choose_saved_wifi_network_fragment");
            if (z) {
                beginTransaction.addToBackStack((String) null);
            }
            beginTransaction.commit();
        } else if (!wifiDppChooseSavedWifiNetworkFragment.isVisible()) {
            this.mFragmentManager.popBackStackImmediate();
        }
    }

    private void showAddDeviceFragment(boolean z) {
        WifiDppAddDeviceFragment wifiDppAddDeviceFragment = (WifiDppAddDeviceFragment) this.mFragmentManager.findFragmentByTag("add_device_fragment");
        if (wifiDppAddDeviceFragment == null) {
            WifiDppAddDeviceFragment wifiDppAddDeviceFragment2 = new WifiDppAddDeviceFragment();
            FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
            beginTransaction.replace(R.id.fragment_container, wifiDppAddDeviceFragment2, "add_device_fragment");
            if (z) {
                beginTransaction.addToBackStack((String) null);
            }
            beginTransaction.commit();
        } else if (!wifiDppAddDeviceFragment.isVisible()) {
            this.mFragmentManager.popBackStackImmediate();
        }
    }

    public WifiNetworkConfig getWifiNetworkConfig() {
        return this.mWifiNetworkConfig;
    }

    /* access modifiers changed from: package-private */
    public WifiQrCode getWifiDppQrCode() {
        return this.mWifiDppQrCode;
    }

    /* access modifiers changed from: package-private */
    public boolean setWifiNetworkConfig(WifiNetworkConfig wifiNetworkConfig) {
        if (!WifiNetworkConfig.isValidConfig(wifiNetworkConfig)) {
            return false;
        }
        this.mWifiNetworkConfig = new WifiNetworkConfig(wifiNetworkConfig);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean setWifiDppQrCode(WifiQrCode wifiQrCode) {
        if (wifiQrCode == null || !"DPP".equals(wifiQrCode.getScheme())) {
            return false;
        }
        this.mWifiDppQrCode = new WifiQrCode(wifiQrCode.getQrCode());
        return true;
    }

    public void onScanWifiDppSuccess(WifiQrCode wifiQrCode) {
        this.mWifiDppQrCode = wifiQrCode;
        showAddDeviceFragment(true);
    }

    public void onClickChooseDifferentNetwork() {
        showChooseSavedWifiNetworkFragment(true);
    }

    public void onSaveInstanceState(Bundle bundle) {
        WifiQrCode wifiQrCode = this.mWifiDppQrCode;
        if (wifiQrCode != null) {
            bundle.putString("key_qr_code", wifiQrCode.getQrCode());
        }
        WifiNetworkConfig wifiNetworkConfig = this.mWifiNetworkConfig;
        if (wifiNetworkConfig != null) {
            bundle.putString("key_wifi_security", wifiNetworkConfig.getSecurity());
            bundle.putString("key_wifi_ssid", this.mWifiNetworkConfig.getSsid());
            bundle.putString("key_wifi_preshared_key", this.mWifiNetworkConfig.getPreSharedKey());
            bundle.putBoolean("key_wifi_hidden_ssid", this.mWifiNetworkConfig.getHiddenSsid());
            bundle.putInt("key_wifi_network_id", this.mWifiNetworkConfig.getNetworkId());
            bundle.putBoolean("key_is_hotspot", this.mWifiNetworkConfig.isHotspot());
        }
        super.onSaveInstanceState(bundle);
    }

    public void onChooseNetwork(WifiNetworkConfig wifiNetworkConfig) {
        this.mWifiNetworkConfig = new WifiNetworkConfig(wifiNetworkConfig);
        showAddDeviceFragment(true);
    }

    private WifiNetworkConfig getConnectedWifiNetworkConfigOrNull() {
        WifiInfo connectionInfo;
        WifiManager wifiManager = (WifiManager) getSystemService(WifiManager.class);
        if (!wifiManager.isWifiEnabled() || (connectionInfo = wifiManager.getConnectionInfo()) == null) {
            return null;
        }
        int networkId = connectionInfo.getNetworkId();
        for (WifiConfiguration next : wifiManager.getConfiguredNetworks()) {
            if (next.networkId == networkId) {
                return WifiNetworkConfig.getValidConfigOrNull(WifiDppUtils.getSecurityString(next), next.getPrintableSsid(), next.preSharedKey, next.hiddenSSID, next.networkId, false);
            }
        }
        return null;
    }
}
