package com.android.wifitrackerlib;

import com.android.wifitrackerlib.WifiPickerTracker;

public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WifiPickerTracker.WifiPickerTrackerCallback f$0;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda1(WifiPickerTracker.WifiPickerTrackerCallback wifiPickerTrackerCallback) {
        this.f$0 = wifiPickerTrackerCallback;
    }

    public final void run() {
        this.f$0.onNumSavedSubscriptionsChanged();
    }
}
