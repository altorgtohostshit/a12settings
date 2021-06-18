package com.google.android.wifitrackerlib;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.util.Preconditions;
import com.android.wifitrackerlib.Utils;
import com.android.wifitrackerlib.WifiEntry;
import com.google.android.wifitrackerlib.WsuManager;
import java.util.ArrayList;
import java.util.List;

class WsuWifiEntry extends WifiEntry {
    private final Context mContext;
    private final List<ScanResult> mCurrentScanResults = new ArrayList();
    private final String mKey;
    private final Object mLock = new Object();
    private int mProvisionStatus = 0;
    private WsuManager.WsuSignupAction mSignupAction;
    private final WsuProvider mWsuProvider;

    public boolean canDisconnect() {
        return false;
    }

    public boolean canEasyConnect() {
        return false;
    }

    public boolean canForget() {
        return false;
    }

    public boolean canSetAutoJoinEnabled() {
        return false;
    }

    public boolean canSetMeteredChoice() {
        return false;
    }

    public boolean canSetPrivacy() {
        return false;
    }

    public boolean canShare() {
        return false;
    }

    public boolean canSignIn() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        return false;
    }

    public String getMacAddress() {
        return null;
    }

    public int getMeteredChoice() {
        return 0;
    }

    public int getPrivacy() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public String getScanResultDescription() {
        return "";
    }

    public int getSecurity() {
        return 0;
    }

    public String getSecurityString(boolean z) {
        return "";
    }

    public String getSsid() {
        return "";
    }

    public WifiConfiguration getWifiConfiguration() {
        return null;
    }

    public boolean isAutoJoinEnabled() {
        return false;
    }

    public boolean isMetered() {
        return false;
    }

    public boolean isSaved() {
        return false;
    }

    public boolean isSubscription() {
        return false;
    }

    public boolean isSuggestion() {
        return false;
    }

    public void setAutoJoinEnabled(boolean z) {
    }

    public void setMeteredChoice(int i) {
    }

    public void setPrivacy(int i) {
    }

    public void signIn(WifiEntry.SignInCallback signInCallback) {
    }

    WsuWifiEntry(Context context, Handler handler, WsuProvider wsuProvider, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache) throws IllegalArgumentException {
        super(handler, wifiManager, wifiNetworkScoreCache, false);
        this.mContext = context;
        this.mWsuProvider = wsuProvider;
        this.mKey = generateWsuWifiEntryKey(wsuProvider);
    }

    public String getKey() {
        return this.mKey;
    }

    public String getTitle() {
        return this.mWsuProvider.wsuProviderName;
    }

    /* access modifiers changed from: package-private */
    public void notifyProvisionStatusChanged(int i) {
        if (this.mProvisionStatus != i) {
            this.mProvisionStatus = i;
            notifyOnUpdated();
        }
    }

    public String getSummary() {
        return getSummary(true);
    }

    public String getSummary(boolean z) {
        int i = this.mProvisionStatus;
        if (i != 0) {
            if (i == 1) {
                return this.mContext.getResources().getString(R$string.wifitrackerlib_wsu_entry_summary_provisioning);
            }
            if (i == 3) {
                return this.mContext.getResources().getString(R$string.wifitrackerlib_wsu_entry_summary_provision_error);
            }
            if (i != 4) {
                String string = this.mContext.getResources().getString(R$string.wifitrackerlib_wsu_entry_summary_not_provisioned);
                Log.e("WsuWifiEntry", "unhandled provision status: " + this.mProvisionStatus);
                return string;
            }
        }
        return this.mContext.getResources().getString(R$string.wifitrackerlib_wsu_entry_summary_not_provisioned);
    }

    public String getHelpUriString() {
        if (TextUtils.isEmpty(this.mWsuProvider.helpUriString)) {
            return null;
        }
        return this.mWsuProvider.helpUriString;
    }

    public boolean canConnect() {
        return this.mLevel != -1 && getConnectedState() == 0;
    }

    public void connect(WifiEntry.ConnectCallback connectCallback) {
        WsuManager.WsuSignupAction wsuSignupAction = this.mSignupAction;
        if (wsuSignupAction != null) {
            wsuSignupAction.onExecute();
        }
    }

    /* access modifiers changed from: package-private */
    public void setSignupAction(WsuManager.WsuSignupAction wsuSignupAction) {
        this.mSignupAction = wsuSignupAction;
    }

    public void disconnect(WifiEntry.DisconnectCallback disconnectCallback) {
        throw new IllegalStateException("This shouldn't be called.");
    }

    public void forget(WifiEntry.ForgetCallback forgetCallback) {
        throw new IllegalStateException("This shouldn't be called.");
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
        if (getConnectedState() == 0) {
            this.mLevel = bestScanResultByLevel != null ? this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level) : -1;
        }
        notifyOnUpdated();
    }

    static String generateWsuWifiEntryKey(WsuProvider wsuProvider) {
        Preconditions.checkNotNull(wsuProvider, "Cannot create key with null WsuProvider!");
        return "WsuWifiEntry:" + wsuProvider.getWsuIdentity();
    }
}
