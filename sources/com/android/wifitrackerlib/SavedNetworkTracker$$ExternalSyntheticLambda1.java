package com.android.wifitrackerlib;

import com.android.wifitrackerlib.SavedNetworkTracker;

public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ SavedNetworkTracker.SavedNetworkTrackerCallback f$0;

    public /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda1(SavedNetworkTracker.SavedNetworkTrackerCallback savedNetworkTrackerCallback) {
        this.f$0 = savedNetworkTrackerCallback;
    }

    public final void run() {
        this.f$0.onSubscriptionWifiEntriesChanged();
    }
}
