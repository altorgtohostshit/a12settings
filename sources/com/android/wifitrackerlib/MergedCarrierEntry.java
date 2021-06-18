package com.android.wifitrackerlib;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.wifitrackerlib.WifiEntry;
import java.util.StringJoiner;

public class MergedCarrierEntry extends WifiEntry {
    private final Context mContext;
    boolean mIsCellDefaultRoute;
    private final String mKey;
    private final int mSubscriptionId;

    MergedCarrierEntry(Handler handler, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z, Context context, int i) throws IllegalArgumentException {
        super(handler, wifiManager, wifiNetworkScoreCache, z);
        this.mContext = context;
        this.mSubscriptionId = i;
        this.mKey = "MergedCarrierEntry:" + i;
    }

    public String getKey() {
        return this.mKey;
    }

    public String getSummary(boolean z) {
        StringJoiner stringJoiner = new StringJoiner(this.mContext.getString(R$string.wifitrackerlib_summary_separator));
        if (!z) {
            String verboseLoggingDescription = Utils.getVerboseLoggingDescription(this);
            if (!TextUtils.isEmpty(verboseLoggingDescription)) {
                stringJoiner.add(verboseLoggingDescription);
            }
        }
        return stringJoiner.toString();
    }

    public String getSsid() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null) {
            return wifiInfo.getSSID();
        }
        return null;
    }

    public String getMacAddress() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo == null) {
            return null;
        }
        String macAddress = wifiInfo.getMacAddress();
        if (TextUtils.isEmpty(macAddress) || TextUtils.equals(macAddress, "02:00:00:00:00:00")) {
            return null;
        }
        return macAddress;
    }

    public boolean canConnect() {
        return getConnectedState() == 0 && !this.mIsCellDefaultRoute;
    }

    public void connect(WifiEntry.ConnectCallback connectCallback) {
        this.mConnectCallback = connectCallback;
        this.mWifiManager.startRestrictingAutoJoinToSubscriptionId(this.mSubscriptionId);
        Toast.makeText(this.mContext, R$string.wifitrackerlib_wifi_wont_autoconnect_for_now, 0).show();
        if (this.mConnectCallback != null) {
            this.mCallbackHandler.post(new MergedCarrierEntry$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$connect$0() {
        this.mConnectCallback.onConnectResult(0);
    }

    public boolean canDisconnect() {
        return getConnectedState() == 2;
    }

    public void disconnect(WifiEntry.DisconnectCallback disconnectCallback) {
        this.mDisconnectCallback = disconnectCallback;
        this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
        this.mWifiManager.startScan();
        if (this.mDisconnectCallback != null) {
            this.mCallbackHandler.post(new MergedCarrierEntry$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$disconnect$1() {
        this.mDisconnectCallback.onDisconnectResult(0);
    }

    /* access modifiers changed from: protected */
    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        return wifiInfo.isCarrierMerged() && this.mSubscriptionId == wifiInfo.getSubscriptionId();
    }

    public void setEnabled(boolean z) {
        this.mWifiManager.setCarrierNetworkOffloadEnabled(this.mSubscriptionId, true, z);
        if (!z) {
            this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
            this.mWifiManager.startScan();
        }
    }

    /* access modifiers changed from: package-private */
    public int getSubscriptionId() {
        return this.mSubscriptionId;
    }

    /* access modifiers changed from: package-private */
    public void updateIsCellDefaultRoute(boolean z) {
        this.mIsCellDefaultRoute = z;
        notifyOnUpdated();
    }
}
