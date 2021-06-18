package com.google.android.wifitrackerlib;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.android.wifitrackerlib.NetworkDetailsTracker;
import com.android.wifitrackerlib.WifiEntry;
import com.google.android.wifitrackerlib.WsuManager;

public final class WsuNetworkDetailsController {
    private final NetworkDetailsTracker mNetworkDetailsTracker;
    private final Handler mWorkerHandler;
    private final WsuManager mWsuManager;
    private final WsuManager.WsuProvidersLoadCallback mWsuProvidersLoadCallback = new WsuNetworkDetailsController$$ExternalSyntheticLambda0(this);

    public WsuNetworkDetailsController(Context context, WifiManager wifiManager, Handler handler, NetworkDetailsTracker networkDetailsTracker) {
        this.mWorkerHandler = handler;
        this.mNetworkDetailsTracker = networkDetailsTracker;
        this.mWsuManager = new WsuManager(context, wifiManager, handler);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        WifiEntry.ManageSubscriptionAction tryGetManageSubscriptionAction = this.mWsuManager.tryGetManageSubscriptionAction(this.mNetworkDetailsTracker.getWifiEntry());
        if (tryGetManageSubscriptionAction != null) {
            this.mNetworkDetailsTracker.getWifiEntry().setManageSubscriptionAction(tryGetManageSubscriptionAction);
        }
    }

    public void onUiStart() {
        this.mWorkerHandler.post(new WsuNetworkDetailsController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onUiStart$1() {
        this.mWsuManager.bindAllServices();
        this.mWsuManager.addWsuProvidersLoadCallback(this.mWsuProvidersLoadCallback);
    }

    public void onUiStop() {
        this.mWorkerHandler.post(new WsuNetworkDetailsController$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onUiStop$2() {
        this.mWsuManager.unbindAllServices();
        this.mWsuManager.removeWsuProvidersLoadCallback(this.mWsuProvidersLoadCallback);
    }
}
