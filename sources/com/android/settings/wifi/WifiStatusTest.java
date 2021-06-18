package com.android.settings.wifi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.settings.R;
import com.android.settingslib.wifi.AccessPoint;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public class WifiStatusTest extends Activity {
    /* access modifiers changed from: private */
    public TextView mBSSID;
    /* access modifiers changed from: private */
    public TextView mHiddenSSID;
    /* access modifiers changed from: private */
    public TextView mHttpClientTest;
    /* access modifiers changed from: private */
    public String mHttpClientTestResult;
    /* access modifiers changed from: private */
    public TextView mIPAddr;
    /* access modifiers changed from: private */
    public TextView mMACAddr;
    /* access modifiers changed from: private */
    public TextView mNetworkId;
    private TextView mNetworkState;
    View.OnClickListener mPingButtonHandler = new View.OnClickListener() {
        public void onClick(View view) {
            WifiStatusTest.this.updatePingState();
        }
    };
    /* access modifiers changed from: private */
    public TextView mPingHostname;
    /* access modifiers changed from: private */
    public String mPingHostnameResult;
    /* access modifiers changed from: private */
    public TextView mRSSI;
    /* access modifiers changed from: private */
    public TextView mRxLinkSpeed;
    /* access modifiers changed from: private */
    public TextView mSSID;
    private TextView mScanList;
    private TextView mSupplicantState;
    /* access modifiers changed from: private */
    public TextView mTxLinkSpeed;
    /* access modifiers changed from: private */
    public WifiManager mWifiManager;
    private TextView mWifiState;
    private IntentFilter mWifiStateFilter;
    private final BroadcastReceiver mWifiStateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
                WifiStatusTest.this.handleWifiStateChanged(intent.getIntExtra("wifi_state", 4));
            } else if (intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
                WifiStatusTest.this.handleNetworkStateChanged((NetworkInfo) intent.getParcelableExtra("networkInfo"));
            } else if (intent.getAction().equals("android.net.wifi.SCAN_RESULTS")) {
                WifiStatusTest.this.handleScanResultsAvailable();
            } else if (!intent.getAction().equals("android.net.wifi.supplicant.CONNECTION_CHANGE")) {
                if (intent.getAction().equals("android.net.wifi.supplicant.STATE_CHANGE")) {
                    WifiStatusTest.this.handleSupplicantStateChanged((SupplicantState) intent.getParcelableExtra("newState"), intent.hasExtra("supplicantError"), intent.getIntExtra("supplicantError", 0));
                } else if (intent.getAction().equals("android.net.wifi.RSSI_CHANGED")) {
                    WifiStatusTest.this.handleSignalChanged(intent.getIntExtra("newRssi", 0));
                } else if (!intent.getAction().equals("android.net.wifi.NETWORK_IDS_CHANGED")) {
                    Log.e("WifiStatusTest", "Received an unknown Wifi Intent");
                }
            }
        }
    };
    private Button pingTestButton;
    private Button updateButton;
    View.OnClickListener updateButtonHandler = new View.OnClickListener() {
        public void onClick(View view) {
            WifiInfo connectionInfo = WifiStatusTest.this.mWifiManager.getConnectionInfo();
            WifiStatusTest wifiStatusTest = WifiStatusTest.this;
            wifiStatusTest.setWifiStateText(wifiStatusTest.mWifiManager.getWifiState());
            WifiStatusTest.this.mBSSID.setText(connectionInfo.getBSSID());
            WifiStatusTest.this.mHiddenSSID.setText(String.valueOf(connectionInfo.getHiddenSSID()));
            int ipAddress = connectionInfo.getIpAddress();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(ipAddress & 255);
            stringBuffer.append('.');
            int i = ipAddress >>> 8;
            stringBuffer.append(i & 255);
            stringBuffer.append('.');
            int i2 = i >>> 8;
            stringBuffer.append(i2 & 255);
            stringBuffer.append('.');
            stringBuffer.append((i2 >>> 8) & 255);
            WifiStatusTest.this.mIPAddr.setText(stringBuffer);
            TextView access$1100 = WifiStatusTest.this.mTxLinkSpeed;
            access$1100.setText(String.valueOf(connectionInfo.getTxLinkSpeedMbps()) + " Mbps");
            TextView access$1200 = WifiStatusTest.this.mRxLinkSpeed;
            access$1200.setText(String.valueOf(connectionInfo.getRxLinkSpeedMbps()) + " Mbps");
            WifiStatusTest.this.mMACAddr.setText(connectionInfo.getMacAddress());
            WifiStatusTest.this.mNetworkId.setText(String.valueOf(connectionInfo.getNetworkId()));
            WifiStatusTest.this.mRSSI.setText(String.valueOf(connectionInfo.getRssi()));
            WifiStatusTest.this.mSSID.setText(connectionInfo.getSSID());
            WifiStatusTest.this.setSupplicantStateText(connectionInfo.getSupplicantState());
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mWifiManager = (WifiManager) getSystemService("wifi");
        IntentFilter intentFilter = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
        this.mWifiStateFilter = intentFilter;
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        this.mWifiStateFilter.addAction("android.net.wifi.SCAN_RESULTS");
        this.mWifiStateFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        this.mWifiStateFilter.addAction("android.net.wifi.RSSI_CHANGED");
        this.mWifiStateFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(this.mWifiStateReceiver, this.mWifiStateFilter);
        setContentView(R.layout.wifi_status_test);
        Button button = (Button) findViewById(R.id.update);
        this.updateButton = button;
        button.setOnClickListener(this.updateButtonHandler);
        this.mWifiState = (TextView) findViewById(R.id.wifi_state);
        this.mNetworkState = (TextView) findViewById(R.id.network_state);
        this.mSupplicantState = (TextView) findViewById(R.id.supplicant_state);
        this.mRSSI = (TextView) findViewById(R.id.rssi);
        this.mBSSID = (TextView) findViewById(R.id.bssid);
        this.mSSID = (TextView) findViewById(R.id.ssid);
        this.mHiddenSSID = (TextView) findViewById(R.id.hidden_ssid);
        this.mIPAddr = (TextView) findViewById(R.id.ipaddr);
        this.mMACAddr = (TextView) findViewById(R.id.macaddr);
        this.mNetworkId = (TextView) findViewById(R.id.networkid);
        this.mTxLinkSpeed = (TextView) findViewById(R.id.tx_link_speed);
        this.mRxLinkSpeed = (TextView) findViewById(R.id.rx_link_speed);
        this.mScanList = (TextView) findViewById(R.id.scan_list);
        this.mPingHostname = (TextView) findViewById(R.id.pingHostname);
        this.mHttpClientTest = (TextView) findViewById(R.id.httpClientTest);
        Button button2 = (Button) findViewById(R.id.ping_test);
        this.pingTestButton = button2;
        button2.setOnClickListener(this.mPingButtonHandler);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        registerReceiver(this.mWifiStateReceiver, this.mWifiStateFilter);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        unregisterReceiver(this.mWifiStateReceiver);
    }

    /* access modifiers changed from: private */
    public void setSupplicantStateText(SupplicantState supplicantState) {
        if (SupplicantState.FOUR_WAY_HANDSHAKE.equals(supplicantState)) {
            this.mSupplicantState.setText("FOUR WAY HANDSHAKE");
        } else if (SupplicantState.ASSOCIATED.equals(supplicantState)) {
            this.mSupplicantState.setText("ASSOCIATED");
        } else if (SupplicantState.ASSOCIATING.equals(supplicantState)) {
            this.mSupplicantState.setText("ASSOCIATING");
        } else if (SupplicantState.COMPLETED.equals(supplicantState)) {
            this.mSupplicantState.setText("COMPLETED");
        } else if (SupplicantState.DISCONNECTED.equals(supplicantState)) {
            this.mSupplicantState.setText("DISCONNECTED");
        } else if (SupplicantState.DORMANT.equals(supplicantState)) {
            this.mSupplicantState.setText("DORMANT");
        } else if (SupplicantState.GROUP_HANDSHAKE.equals(supplicantState)) {
            this.mSupplicantState.setText("GROUP HANDSHAKE");
        } else if (SupplicantState.INACTIVE.equals(supplicantState)) {
            this.mSupplicantState.setText("INACTIVE");
        } else if (SupplicantState.INVALID.equals(supplicantState)) {
            this.mSupplicantState.setText("INVALID");
        } else if (SupplicantState.SCANNING.equals(supplicantState)) {
            this.mSupplicantState.setText("SCANNING");
        } else if (SupplicantState.UNINITIALIZED.equals(supplicantState)) {
            this.mSupplicantState.setText("UNINITIALIZED");
        } else {
            this.mSupplicantState.setText("BAD");
            Log.e("WifiStatusTest", "supplicant state is bad");
        }
    }

    /* access modifiers changed from: private */
    public void setWifiStateText(int i) {
        String str;
        if (i == 0) {
            str = getString(R.string.wifi_state_disabling);
        } else if (i == 1) {
            str = getString(R.string.wifi_state_disabled);
        } else if (i == 2) {
            str = getString(R.string.wifi_state_enabling);
        } else if (i == 3) {
            str = getString(R.string.wifi_state_enabled);
        } else if (i != 4) {
            Log.e("WifiStatusTest", "wifi state is bad");
            str = "BAD";
        } else {
            str = getString(R.string.wifi_state_unknown);
        }
        this.mWifiState.setText(str);
    }

    /* access modifiers changed from: private */
    public void handleSignalChanged(int i) {
        this.mRSSI.setText(String.valueOf(i));
    }

    /* access modifiers changed from: private */
    public void handleWifiStateChanged(int i) {
        setWifiStateText(i);
    }

    /* access modifiers changed from: private */
    public void handleScanResultsAvailable() {
        List<ScanResult> scanResults = this.mWifiManager.getScanResults();
        StringBuffer stringBuffer = new StringBuffer();
        if (scanResults != null) {
            for (int size = scanResults.size() - 1; size >= 0; size--) {
                ScanResult scanResult = scanResults.get(size);
                if (scanResult != null && !TextUtils.isEmpty(scanResult.SSID)) {
                    stringBuffer.append(scanResult.SSID + " ");
                }
            }
        }
        this.mScanList.setText(stringBuffer);
    }

    /* access modifiers changed from: private */
    public void handleSupplicantStateChanged(SupplicantState supplicantState, boolean z, int i) {
        if (z) {
            this.mSupplicantState.setText("ERROR AUTHENTICATING");
        } else {
            setSupplicantStateText(supplicantState);
        }
    }

    /* access modifiers changed from: private */
    public void handleNetworkStateChanged(NetworkInfo networkInfo) {
        if (this.mWifiManager.isWifiEnabled()) {
            WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
            this.mNetworkState.setText(AccessPoint.getSummary(this, connectionInfo.getSSID(), networkInfo.getDetailedState(), connectionInfo.getNetworkId() == -1, (String) null));
        }
    }

    /* access modifiers changed from: private */
    public final void updatePingState() {
        final Handler handler = new Handler();
        this.mPingHostnameResult = getResources().getString(R.string.radioInfo_unknown);
        this.mHttpClientTestResult = getResources().getString(R.string.radioInfo_unknown);
        this.mPingHostname.setText(this.mPingHostnameResult);
        this.mHttpClientTest.setText(this.mHttpClientTestResult);
        final C13814 r1 = new Runnable() {
            public void run() {
                WifiStatusTest.this.mPingHostname.setText(WifiStatusTest.this.mPingHostnameResult);
                WifiStatusTest.this.mHttpClientTest.setText(WifiStatusTest.this.mHttpClientTestResult);
            }
        };
        new Thread() {
            public void run() {
                WifiStatusTest.this.pingHostname();
                handler.post(r1);
            }
        }.start();
        new Thread() {
            public void run() {
                WifiStatusTest.this.httpClientTest();
                handler.post(r1);
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public final void pingHostname() {
        try {
            if (Runtime.getRuntime().exec("ping -c 1 -w 100 www.google.com").waitFor() == 0) {
                this.mPingHostnameResult = "Pass";
            } else {
                this.mPingHostnameResult = "Fail: Host unreachable";
            }
        } catch (UnknownHostException unused) {
            this.mPingHostnameResult = "Fail: Unknown Host";
        } catch (IOException unused2) {
            this.mPingHostnameResult = "Fail: IOException";
        } catch (InterruptedException unused3) {
            this.mPingHostnameResult = "Fail: InterruptedException";
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void httpClientTest() {
        /*
            r3 = this;
            r0 = 0
            java.net.URL r1 = new java.net.URL     // Catch:{ IOException -> 0x003d }
            java.lang.String r2 = "https://www.google.com"
            r1.<init>(r2)     // Catch:{ IOException -> 0x003d }
            java.net.URLConnection r1 = r1.openConnection()     // Catch:{ IOException -> 0x003d }
            java.net.HttpURLConnection r1 = (java.net.HttpURLConnection) r1     // Catch:{ IOException -> 0x003d }
            int r0 = r1.getResponseCode()     // Catch:{ IOException -> 0x0039, all -> 0x0036 }
            r2 = 200(0xc8, float:2.8E-43)
            if (r0 != r2) goto L_0x001b
            java.lang.String r0 = "Pass"
            r3.mHttpClientTestResult = r0     // Catch:{ IOException -> 0x0039, all -> 0x0036 }
            goto L_0x0032
        L_0x001b:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0039, all -> 0x0036 }
            r0.<init>()     // Catch:{ IOException -> 0x0039, all -> 0x0036 }
            java.lang.String r2 = "Fail: Code: "
            r0.append(r2)     // Catch:{ IOException -> 0x0039, all -> 0x0036 }
            java.lang.String r2 = r1.getResponseMessage()     // Catch:{ IOException -> 0x0039, all -> 0x0036 }
            r0.append(r2)     // Catch:{ IOException -> 0x0039, all -> 0x0036 }
            java.lang.String r0 = r0.toString()     // Catch:{ IOException -> 0x0039, all -> 0x0036 }
            r3.mHttpClientTestResult = r0     // Catch:{ IOException -> 0x0039, all -> 0x0036 }
        L_0x0032:
            r1.disconnect()
            goto L_0x0046
        L_0x0036:
            r3 = move-exception
            r0 = r1
            goto L_0x0047
        L_0x0039:
            r0 = r1
            goto L_0x003d
        L_0x003b:
            r3 = move-exception
            goto L_0x0047
        L_0x003d:
            java.lang.String r1 = "Fail: IOException"
            r3.mHttpClientTestResult = r1     // Catch:{ all -> 0x003b }
            if (r0 == 0) goto L_0x0046
            r0.disconnect()
        L_0x0046:
            return
        L_0x0047:
            if (r0 == 0) goto L_0x004c
            r0.disconnect()
        L_0x004c:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.wifi.WifiStatusTest.httpClientTest():void");
    }
}
