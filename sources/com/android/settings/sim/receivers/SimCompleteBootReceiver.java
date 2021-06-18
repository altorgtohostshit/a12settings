package com.android.settings.sim.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.settings.sim.SimActivationNotifier;
import com.android.settings.sim.SimNotificationService;

public class SimCompleteBootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (!"android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.e("SimCompleteBootReceiver", "Invalid broadcast received.");
        } else if (SimActivationNotifier.getShowSimSettingsNotification(context)) {
            SimNotificationService.scheduleSimNotification(context, 1);
        }
    }
}
