package com.android.settings.wifi.dpp;

import android.content.Intent;
import android.util.Log;
import androidx.fragment.app.FragmentTransaction;
import com.android.settings.R;
import com.android.settings.wifi.dpp.WifiDppQrCodeScannerFragment;

public class WifiDppEnrolleeActivity extends WifiDppBaseActivity implements WifiDppQrCodeScannerFragment.OnScanWifiDppSuccessListener {
    public int getMetricsCategory() {
        return 1596;
    }

    public void onScanWifiDppSuccess(WifiQrCode wifiQrCode) {
    }

    /* access modifiers changed from: protected */
    public void handleIntent(Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (action == null) {
            finish();
        } else if (!action.equals("android.settings.WIFI_DPP_ENROLLEE_QR_CODE_SCANNER")) {
            Log.e("WifiDppEnrolleeActivity", "Launch with an invalid action");
            finish();
        } else {
            showQrCodeScannerFragment(intent.getStringExtra("ssid"));
        }
    }

    private void showQrCodeScannerFragment(String str) {
        WifiDppQrCodeScannerFragment wifiDppQrCodeScannerFragment = (WifiDppQrCodeScannerFragment) this.mFragmentManager.findFragmentByTag("qr_code_scanner_fragment");
        if (wifiDppQrCodeScannerFragment == null) {
            WifiDppQrCodeScannerFragment wifiDppQrCodeScannerFragment2 = new WifiDppQrCodeScannerFragment(str);
            FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
            beginTransaction.replace(R.id.fragment_container, wifiDppQrCodeScannerFragment2, "qr_code_scanner_fragment");
            beginTransaction.commit();
        } else if (!wifiDppQrCodeScannerFragment.isVisible()) {
            this.mFragmentManager.popBackStackImmediate();
        }
    }
}
