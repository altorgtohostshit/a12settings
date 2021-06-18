package com.android.settings.network;

import android.app.FragmentManager;
import android.app.PendingIntent;
import android.os.Bundle;
import com.android.settings.SidecarFragment;
import com.android.settings.network.telephony.EuiccOperationSidecar;

public class SwitchToEuiccSubscriptionSidecar extends EuiccOperationSidecar {
    private PendingIntent mCallbackIntent;

    public String getReceiverAction() {
        return "com.android.settings.network.switchToSubscription";
    }

    public static SwitchToEuiccSubscriptionSidecar get(FragmentManager fragmentManager) {
        return (SwitchToEuiccSubscriptionSidecar) SidecarFragment.get(fragmentManager, "SwitchToEuiccSubscriptionSidecar", SwitchToEuiccSubscriptionSidecar.class, (Bundle) null);
    }

    public void run(int i) {
        setState(1, 0);
        PendingIntent createCallbackIntent = createCallbackIntent();
        this.mCallbackIntent = createCallbackIntent;
        this.mEuiccManager.switchToSubscription(i, createCallbackIntent);
    }
}
