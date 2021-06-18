package com.android.settings.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import com.android.wifitrackerlib.NetworkDetailsTracker;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.time.Clock;

public class WifiTrackerLibProviderImpl implements WifiTrackerLibProvider {
    public WifiPickerTracker createWifiPickerTracker(Lifecycle lifecycle, Context context, Handler handler, Handler handler2, Clock clock, long j, long j2, WifiPickerTracker.WifiPickerTrackerCallback wifiPickerTrackerCallback) {
        Context context2 = context;
        return new WifiPickerTracker(lifecycle, context2, (WifiManager) context2.getSystemService(WifiManager.class), (ConnectivityManager) context2.getSystemService(ConnectivityManager.class), (NetworkScoreManager) context2.getSystemService(NetworkScoreManager.class), handler, handler2, clock, j, j2, wifiPickerTrackerCallback);
    }

    public NetworkDetailsTracker createNetworkDetailsTracker(Lifecycle lifecycle, Context context, Handler handler, Handler handler2, Clock clock, long j, long j2, String str) {
        return NetworkDetailsTracker.createNetworkDetailsTracker(lifecycle, context, (WifiManager) context.getSystemService(WifiManager.class), (ConnectivityManager) context.getSystemService(ConnectivityManager.class), (NetworkScoreManager) context.getSystemService(NetworkScoreManager.class), handler, handler2, clock, j, j2, str);
    }
}
