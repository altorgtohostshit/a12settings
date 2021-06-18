package com.android.settingslib.wifi;

import com.android.settingslib.wifi.AccessPoint;

/* renamed from: com.android.settingslib.wifi.AccessPoint$AccessPointProvisioningCallback$$ExternalSyntheticLambda1 */
public final /* synthetic */ class C1518xb3759df6 implements Runnable {
    public final /* synthetic */ AccessPoint.AccessPointProvisioningCallback f$0;

    public /* synthetic */ C1518xb3759df6(AccessPoint.AccessPointProvisioningCallback accessPointProvisioningCallback) {
        this.f$0 = accessPointProvisioningCallback;
    }

    public final void run() {
        this.f$0.lambda$onProvisioningFailure$0();
    }
}
