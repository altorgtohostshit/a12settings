package com.android.settings.sim.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.settings.R;
import com.android.settingslib.utils.ThreadUtils;
import java.util.Objects;

public class SuwFinishReceiver extends BroadcastReceiver {
    private final Object mLock = new Object();
    private final SimSlotChangeHandler mSlotChangeHandler = SimSlotChangeHandler.get();

    public void onReceive(Context context, Intent intent) {
        if (!context.getResources().getBoolean(R.bool.config_handle_sim_slot_change)) {
            Log.i("SuwFinishReceiver", "The flag is off. Ignore SUW finish event.");
        } else {
            ThreadUtils.postOnBackgroundThread((Runnable) new SuwFinishReceiver$$ExternalSyntheticLambda0(this, context, goAsync()));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onReceive$0(Context context, BroadcastReceiver.PendingResult pendingResult) {
        synchronized (this.mLock) {
            Log.i("SuwFinishReceiver", "Detected SUW finished. Checking slot events.");
            this.mSlotChangeHandler.onSuwFinish(context.getApplicationContext());
        }
        Objects.requireNonNull(pendingResult);
        ThreadUtils.postOnMainThread(new SimSlotChangeReceiver$$ExternalSyntheticLambda0(pendingResult));
    }
}
