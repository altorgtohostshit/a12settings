package com.android.settings.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import java.util.concurrent.CountDownLatch;

public class CarrierConfigChangedReceiver extends BroadcastReceiver {
    private final CountDownLatch mLatch;

    public CarrierConfigChangedReceiver(CountDownLatch countDownLatch) {
        this.mLatch = countDownLatch;
    }

    public void registerOn(Context context) {
        context.registerReceiver(this, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
    }

    public void onReceive(Context context, Intent intent) {
        if (!isInitialStickyBroadcast() && "android.telephony.action.CARRIER_CONFIG_CHANGED".equals(intent.getAction())) {
            checkSubscriptionIndex(intent);
        }
    }

    private void checkSubscriptionIndex(Intent intent) {
        if (intent.hasExtra("android.telephony.extra.SUBSCRIPTION_INDEX")) {
            int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
            Log.i("CarrierConfigChangedReceiver", "subId from config changed: " + intExtra);
            this.mLatch.countDown();
        }
    }
}
