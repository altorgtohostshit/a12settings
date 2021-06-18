package com.android.settingslib.wifi;

import com.android.settingslib.wifi.AccessPoint;

/* renamed from: com.android.settingslib.wifi.AccessPoint$AccessPointProvisioningCallback$$ExternalSyntheticLambda2 */
public final /* synthetic */ class C1519xb3759df7 implements Runnable {
    public final /* synthetic */ AccessPoint.AccessPointProvisioningCallback f$0;

    public /* synthetic */ C1519xb3759df7(AccessPoint.AccessPointProvisioningCallback accessPointProvisioningCallback) {
        this.f$0 = accessPointProvisioningCallback;
    }

    public final void run() {
        this.f$0.lambda$onProvisioningStatus$1();
    }
}
