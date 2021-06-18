package com.android.settings.network;

import android.content.Context;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.concurrent.Executor;

public class AllowedNetworkTypesListener extends TelephonyCallback implements TelephonyCallback.AllowedNetworkTypesListener {
    private long mAllowedNetworkType = -1;
    private Executor mExecutor;
    OnAllowedNetworkTypesListener mListener;

    public interface OnAllowedNetworkTypesListener {
        void onAllowedNetworkTypesChanged();
    }

    public AllowedNetworkTypesListener(Executor executor) {
        this.mExecutor = executor;
    }

    public void setAllowedNetworkTypesListener(OnAllowedNetworkTypesListener onAllowedNetworkTypesListener) {
        this.mListener = onAllowedNetworkTypesListener;
    }

    public void register(Context context, int i) {
        ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i).registerTelephonyCallback(this.mExecutor, this);
    }

    public void unregister(Context context, int i) {
        ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i).unregisterTelephonyCallback(this);
    }

    public void onAllowedNetworkTypesChanged(int i, long j) {
        if (i == 0) {
            OnAllowedNetworkTypesListener onAllowedNetworkTypesListener = this.mListener;
            if (!(onAllowedNetworkTypesListener == null || this.mAllowedNetworkType == j)) {
                onAllowedNetworkTypesListener.onAllowedNetworkTypesChanged();
                Log.d("NetworkModeListener", "onAllowedNetworkChanged: " + this.mAllowedNetworkType);
            }
            this.mAllowedNetworkType = j;
        }
    }
}
