package com.android.settings.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.android.settings.R;
import com.android.settings.bluetooth.BluetoothPairingDetail;
import com.android.settings.core.SubSettingLauncher;

public class BluetoothPairingReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals("com.android.settings.action.LAUNCH_BLUETOOTH_PAIRING", intent.getAction())) {
            context.startActivity(new SubSettingLauncher(context).setDestination(BluetoothPairingDetail.class.getName()).setTitleRes(R.string.bluetooth_pairing_page_title).setSourceMetricsCategory(1851).addFlags(268468224).toIntent());
        }
    }
}
