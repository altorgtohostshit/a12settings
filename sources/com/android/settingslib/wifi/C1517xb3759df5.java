package com.android.settingslib.wifi;

import com.android.settingslib.wifi.AccessPoint;

/* renamed from: com.android.settingslib.wifi.AccessPoint$AccessPointProvisioningCallback$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C1517xb3759df5 implements Runnable {
    public final /* synthetic */ AccessPoint.AccessPointProvisioningCallback f$0;

    public /* synthetic */ C1517xb3759df5(AccessPoint.AccessPointProvisioningCallback accessPointProvisioningCallback) {
        this.f$0 = accessPointProvisioningCallback;
    }

    public final void run() {
        this.f$0.lambda$onProvisioningComplete$2();
    }
}
