package com.google.android.wifitrackerlib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import com.android.wifitrackerlib.StandardNetworkDetailsTracker;
import java.time.Clock;

public class GoogleStandardNetworkDetailsTracker extends StandardNetworkDetailsTracker {
    private final WsuNetworkDetailsController mWsuNetworkDetailsController;

    public GoogleStandardNetworkDetailsTracker(Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, NetworkScoreManager networkScoreManager, Handler handler, Handler handler2, Clock clock, long j, long j2, String str) {
        super(lifecycle, context, wifiManager, connectivityManager, networkScoreManager, handler, handler2, clock, j, j2, str);
        this.mWsuNetworkDetailsController = new WsuNetworkDetailsController(context, wifiManager, handler2, this);
    }

    /* access modifiers changed from: protected */
    public void handleOnStart() {
        super.handleOnStart();
        this.mWsuNetworkDetailsController.onUiStart();
    }

    public void onStop() {
        super.onStop();
        this.mWsuNetworkDetailsController.onUiStop();
    }
}
