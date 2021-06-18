package com.google.android.settings.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import com.android.settings.wifi.WifiTrackerLibProvider;
import com.android.wifitrackerlib.NetworkDetailsTracker;
import com.android.wifitrackerlib.WifiPickerTracker;
import com.google.android.wifitrackerlib.GooglePasspointNetworkDetailsTracker;
import com.google.android.wifitrackerlib.GoogleStandardNetworkDetailsTracker;
import com.google.android.wifitrackerlib.GoogleWifiPickerTracker;
import java.time.Clock;

public class WifiTrackerLibProviderGoogleImpl implements WifiTrackerLibProvider {
    public WifiPickerTracker createWifiPickerTracker(Lifecycle lifecycle, Context context, Handler handler, Handler handler2, Clock clock, long j, long j2, WifiPickerTracker.WifiPickerTrackerCallback wifiPickerTrackerCallback) {
        Context context2 = context;
        return new GoogleWifiPickerTracker(lifecycle, context2, (WifiManager) context2.getSystemService(WifiManager.class), (ConnectivityManager) context2.getSystemService(ConnectivityManager.class), (NetworkScoreManager) context2.getSystemService(NetworkScoreManager.class), handler, handler2, clock, j, j2, wifiPickerTrackerCallback);
    }

    public NetworkDetailsTracker createNetworkDetailsTracker(Lifecycle lifecycle, Context context, Handler handler, Handler handler2, Clock clock, long j, long j2, String str) {
        Context context2 = context;
        String str2 = str;
        if (str2.startsWith("StandardWifiEntry:") || str2.startsWith("NetworkRequestEntry:")) {
            return new GoogleStandardNetworkDetailsTracker(lifecycle, context, (WifiManager) context2.getSystemService(WifiManager.class), (ConnectivityManager) context2.getSystemService(ConnectivityManager.class), (NetworkScoreManager) context2.getSystemService(NetworkScoreManager.class), handler, handler2, clock, j, j2, str);
        } else if (str2.startsWith("PasspointWifiEntry:")) {
            return new GooglePasspointNetworkDetailsTracker(lifecycle, context, (WifiManager) context2.getSystemService(WifiManager.class), (ConnectivityManager) context2.getSystemService(ConnectivityManager.class), (NetworkScoreManager) context2.getSystemService(NetworkScoreManager.class), handler, handler2, clock, j, j2, str);
        } else {
            throw new IllegalArgumentException("Key does not contain valid key prefix!");
        }
    }
}
