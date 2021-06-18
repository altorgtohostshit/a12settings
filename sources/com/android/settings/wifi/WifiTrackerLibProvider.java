package com.android.settings.wifi;

import android.content.Context;
import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import com.android.wifitrackerlib.NetworkDetailsTracker;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.time.Clock;

public interface WifiTrackerLibProvider {
    NetworkDetailsTracker createNetworkDetailsTracker(Lifecycle lifecycle, Context context, Handler handler, Handler handler2, Clock clock, long j, long j2, String str);

    WifiPickerTracker createWifiPickerTracker(Lifecycle lifecycle, Context context, Handler handler, Handler handler2, Clock clock, long j, long j2, WifiPickerTracker.WifiPickerTrackerCallback wifiPickerTrackerCallback);
}
