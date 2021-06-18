package com.android.wifitrackerlib;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.net.wifi.hotspot2.ProvisioningCallback;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import androidx.core.util.Preconditions;
import com.android.wifitrackerlib.WifiEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class OsuWifiEntry extends WifiEntry {
    /* access modifiers changed from: private */
    public final Context mContext;
    private final List<ScanResult> mCurrentScanResults = new ArrayList();
    private boolean mIsAlreadyProvisioned = false;
    private final String mKey;
    private final Object mLock = new Object();
    /* access modifiers changed from: private */
    public OsuProvider mOsuProvider;
    /* access modifiers changed from: private */
    public String mOsuStatusString;
    private String mSsid;

    public String getMacAddress() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getScanResultDescription() {
        return "";
    }

    OsuWifiEntry(Context context, Handler handler, OsuProvider osuProvider, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z) throws IllegalArgumentException {
        super(handler, wifiManager, wifiNetworkScoreCache, z);
        Preconditions.checkNotNull(osuProvider, "Cannot construct with null osuProvider!");
        this.mContext = context;
        this.mOsuProvider = osuProvider;
        this.mKey = osuProviderToOsuWifiEntryKey(osuProvider);
    }

    public String getKey() {
        return this.mKey;
    }

    public String getTitle() {
        String friendlyName = this.mOsuProvider.getFriendlyName();
        if (!TextUtils.isEmpty(friendlyName)) {
            return friendlyName;
        }
        if (!TextUtils.isEmpty(this.mSsid)) {
            return this.mSsid;
        }
        Uri serverUri = this.mOsuProvider.getServerUri();
        return serverUri != null ? serverUri.toString() : "";
    }

    public String getSummary(boolean z) {
        String str = this.mOsuStatusString;
        if (str != null) {
            return str;
        }
        if (!isAlreadyProvisioned()) {
            return this.mContext.getString(R$string.wifitrackerlib_tap_to_sign_up);
        }
        if (z) {
            return this.mContext.getString(R$string.wifitrackerlib_wifi_passpoint_expired);
        }
        return this.mContext.getString(R$string.wifitrackerlib_tap_to_renew_subscription_and_connect);
    }

    public String getSsid() {
        return this.mSsid;
    }

    public boolean canConnect() {
        return this.mLevel != -1 && getConnectedState() == 0;
    }

    public void connect(WifiEntry.ConnectCallback connectCallback) {
        this.mConnectCallback = connectCallback;
        this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
        this.mWifiManager.startSubscriptionProvisioning(this.mOsuProvider, this.mContext.getMainExecutor(), new OsuWifiEntryProvisioningCallback());
    }

    /* access modifiers changed from: package-private */
    public void updateScanResultInfo(List<ScanResult> list) throws IllegalArgumentException {
        if (list == null) {
            list = new ArrayList<>();
        }
        synchronized (this.mLock) {
            this.mCurrentScanResults.clear();
            this.mCurrentScanResults.addAll(list);
        }
        ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(list);
        if (bestScanResultByLevel != null) {
            this.mSsid = bestScanResultByLevel.SSID;
            if (getConnectedState() == 0) {
                this.mLevel = this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level);
            }
        } else {
            this.mLevel = -1;
        }
        notifyOnUpdated();
    }

    static String osuProviderToOsuWifiEntryKey(OsuProvider osuProvider) {
        Preconditions.checkNotNull(osuProvider, "Cannot create key with null OsuProvider!");
        return "OsuWifiEntry:" + osuProvider.getFriendlyName() + "," + osuProvider.getServerUri().toString();
    }

    /* access modifiers changed from: protected */
    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        return wifiInfo.isOsuAp() && TextUtils.equals(wifiInfo.getPasspointProviderFriendlyName(), this.mOsuProvider.getFriendlyName());
    }

    /* access modifiers changed from: package-private */
    public OsuProvider getOsuProvider() {
        return this.mOsuProvider;
    }

    /* access modifiers changed from: package-private */
    public boolean isAlreadyProvisioned() {
        return this.mIsAlreadyProvisioned;
    }

    /* access modifiers changed from: package-private */
    public void setAlreadyProvisioned(boolean z) {
        this.mIsAlreadyProvisioned = z;
    }

    class OsuWifiEntryProvisioningCallback extends ProvisioningCallback {
        OsuWifiEntryProvisioningCallback() {
        }

        public void onProvisioningFailure(int i) {
            if (TextUtils.equals(OsuWifiEntry.this.mOsuStatusString, OsuWifiEntry.this.mContext.getString(R$string.wifitrackerlib_osu_completing_sign_up))) {
                OsuWifiEntry osuWifiEntry = OsuWifiEntry.this;
                String unused = osuWifiEntry.mOsuStatusString = osuWifiEntry.mContext.getString(R$string.wifitrackerlib_osu_sign_up_failed);
            } else {
                OsuWifiEntry osuWifiEntry2 = OsuWifiEntry.this;
                String unused2 = osuWifiEntry2.mOsuStatusString = osuWifiEntry2.mContext.getString(R$string.wifitrackerlib_osu_connect_failed);
            }
            WifiEntry.ConnectCallback connectCallback = OsuWifiEntry.this.mConnectCallback;
            if (connectCallback != null) {
                connectCallback.onConnectResult(2);
            }
            OsuWifiEntry.this.notifyOnUpdated();
        }

        public void onProvisioningStatus(int i) {
            String str;
            switch (i) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    str = String.format(OsuWifiEntry.this.mContext.getString(R$string.wifitrackerlib_osu_opening_provider), new Object[]{OsuWifiEntry.this.getTitle()});
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                    str = OsuWifiEntry.this.mContext.getString(R$string.wifitrackerlib_osu_completing_sign_up);
                    break;
                default:
                    str = null;
                    break;
            }
            boolean equals = true ^ TextUtils.equals(OsuWifiEntry.this.mOsuStatusString, str);
            String unused = OsuWifiEntry.this.mOsuStatusString = str;
            if (equals) {
                OsuWifiEntry.this.notifyOnUpdated();
            }
        }

        public void onProvisioningComplete() {
            ScanResult scanResult;
            OsuWifiEntry osuWifiEntry = OsuWifiEntry.this;
            String unused = osuWifiEntry.mOsuStatusString = osuWifiEntry.mContext.getString(R$string.wifitrackerlib_osu_sign_up_complete);
            OsuWifiEntry.this.notifyOnUpdated();
            OsuWifiEntry osuWifiEntry2 = OsuWifiEntry.this;
            PasspointConfiguration passpointConfiguration = (PasspointConfiguration) osuWifiEntry2.mWifiManager.getMatchingPasspointConfigsForOsuProviders(Collections.singleton(osuWifiEntry2.mOsuProvider)).get(OsuWifiEntry.this.mOsuProvider);
            if (passpointConfiguration == null) {
                WifiEntry.ConnectCallback connectCallback = OsuWifiEntry.this.mConnectCallback;
                if (connectCallback != null) {
                    connectCallback.onConnectResult(2);
                    return;
                }
                return;
            }
            String uniqueId = passpointConfiguration.getUniqueId();
            WifiManager wifiManager = OsuWifiEntry.this.mWifiManager;
            Iterator it = wifiManager.getAllMatchingWifiConfigs(wifiManager.getScanResults()).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Pair pair = (Pair) it.next();
                WifiConfiguration wifiConfiguration = (WifiConfiguration) pair.first;
                if (TextUtils.equals(wifiConfiguration.getKey(), uniqueId)) {
                    List list = (List) ((Map) pair.second).get(0);
                    List list2 = (List) ((Map) pair.second).get(1);
                    if (list != null && !list.isEmpty()) {
                        scanResult = Utils.getBestScanResultByLevel(list);
                    } else if (list2 != null && !list2.isEmpty()) {
                        scanResult = Utils.getBestScanResultByLevel(list2);
                    }
                    wifiConfiguration.SSID = "\"" + scanResult.SSID + "\"";
                    OsuWifiEntry.this.mWifiManager.connect(wifiConfiguration, (WifiManager.ActionListener) null);
                    return;
                }
            }
            WifiEntry.ConnectCallback connectCallback2 = OsuWifiEntry.this.mConnectCallback;
            if (connectCallback2 != null) {
                connectCallback2.onConnectResult(2);
            }
        }
    }
}
