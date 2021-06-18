package com.android.wifitrackerlib;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.Handler;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wifitrackerlib.StandardWifiEntry;
import com.android.wifitrackerlib.WifiEntry;

@VisibleForTesting
public class NetworkRequestEntry extends StandardWifiEntry {
    public boolean canConnect() {
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

    public void connect(WifiEntry.ConnectCallback connectCallback) {
    }

    public void forget(WifiEntry.ForgetCallback forgetCallback) {
    }

    public int getMeteredChoice() {
        return 0;
    }

    public int getPrivacy() {
        return 1;
    }

    public WifiConfiguration getWifiConfiguration() {
        return null;
    }

    public boolean isAutoJoinEnabled() {
        return true;
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

    NetworkRequestEntry(Context context, Handler handler, StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z) throws IllegalArgumentException {
        super(context, handler, standardWifiEntryKey, wifiManager, wifiNetworkScoreCache, z);
    }
}
