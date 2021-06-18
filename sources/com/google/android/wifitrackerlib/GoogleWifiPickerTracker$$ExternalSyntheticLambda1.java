package com.google.android.wifitrackerlib;

import com.google.android.wifitrackerlib.WsuManager;

public final /* synthetic */ class GoogleWifiPickerTracker$$ExternalSyntheticLambda1 implements WsuManager.WsuProvisionStatusUpdateCallback {
    public final /* synthetic */ GoogleWifiPickerTracker f$0;

    public /* synthetic */ GoogleWifiPickerTracker$$ExternalSyntheticLambda1(GoogleWifiPickerTracker googleWifiPickerTracker) {
        this.f$0 = googleWifiPickerTracker;
    }

    public final void onProvisionStatusChanged(WsuProvider wsuProvider, int i) {
        this.f$0.lambda$new$1(wsuProvider, i);
    }
}
