package com.android.settings.network.telephony;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.euicc.EuiccManager;
import android.util.Log;
import com.android.settings.SidecarFragment;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class EuiccOperationSidecar extends SidecarFragment {
    private static AtomicInteger sCurrentOpId = new AtomicInteger((int) SystemClock.elapsedRealtime());
    /* access modifiers changed from: private */
    public int mDetailedCode;
    protected EuiccManager mEuiccManager;
    /* access modifiers changed from: private */
    public int mOpId;
    protected final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (EuiccOperationSidecar.this.getReceiverAction().equals(intent.getAction()) && EuiccOperationSidecar.this.mOpId == intent.getIntExtra("op_id", -1)) {
                int unused = EuiccOperationSidecar.this.mResultCode = getResultCode();
                int unused2 = EuiccOperationSidecar.this.mDetailedCode = intent.getIntExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DETAILED_CODE", 0);
                Intent unused3 = EuiccOperationSidecar.this.mResultIntent = intent;
                Log.i("EuiccOperationSidecar", String.format("Result code : %d; detailed code : %d", new Object[]{Integer.valueOf(EuiccOperationSidecar.this.mResultCode), Integer.valueOf(EuiccOperationSidecar.this.mDetailedCode)}));
                EuiccOperationSidecar.this.onActionReceived();
            }
        }
    };
    /* access modifiers changed from: private */
    public int mResultCode;
    /* access modifiers changed from: private */
    public Intent mResultIntent;

    /* access modifiers changed from: protected */
    public abstract String getReceiverAction();

    /* access modifiers changed from: protected */
    public void onActionReceived() {
        int i = this.mResultCode;
        if (i == 0) {
            setState(2, 0);
        } else {
            setState(3, i);
        }
    }

    /* access modifiers changed from: protected */
    public PendingIntent createCallbackIntent() {
        this.mOpId = sCurrentOpId.incrementAndGet();
        Intent intent = new Intent(getReceiverAction());
        intent.putExtra("op_id", this.mOpId);
        return PendingIntent.getBroadcast(getContext(), 0, intent, 335544320);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mEuiccManager = (EuiccManager) getContext().getSystemService(EuiccManager.class);
        getContext().getApplicationContext().registerReceiver(this.mReceiver, new IntentFilter(getReceiverAction()), "android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS", (Handler) null);
    }

    public void onDestroy() {
        getContext().getApplicationContext().unregisterReceiver(this.mReceiver);
        super.onDestroy();
    }

    public int getResultCode() {
        return this.mResultCode;
    }
}
