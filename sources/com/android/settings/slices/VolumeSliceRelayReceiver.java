package com.android.settings.slices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class VolumeSliceRelayReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        VolumeSliceHelper.onReceive(context, intent);
    }
}
