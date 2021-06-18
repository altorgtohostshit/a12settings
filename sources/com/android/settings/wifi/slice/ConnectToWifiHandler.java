package com.android.settings.wifi.slice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.wifi.WifiDialogActivity;
import com.android.wifitrackerlib.WifiEntry;

public class ConnectToWifiHandler extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        WifiScanWorker wifiScanWorker;
        WifiEntry wifiEntry;
        if (context != null && intent != null) {
            String stringExtra = intent.getStringExtra("key_chosen_wifientry_key");
            if (!TextUtils.isEmpty(stringExtra) && intent.getParcelableExtra("key_wifi_slice_uri") != null && (wifiScanWorker = getWifiScanWorker(intent)) != null && (wifiEntry = wifiScanWorker.getWifiEntry(stringExtra)) != null) {
                wifiEntry.connect(new WifiEntryConnectCallback(context, wifiEntry));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public WifiScanWorker getWifiScanWorker(Intent intent) {
        return (WifiScanWorker) SliceBackgroundWorker.getInstance((Uri) intent.getParcelableExtra("key_wifi_slice_uri"));
    }

    static class WifiEntryConnectCallback implements WifiEntry.ConnectCallback {
        final Context mContext;
        final WifiEntry mWifiEntry;

        WifiEntryConnectCallback(Context context, WifiEntry wifiEntry) {
            this.mContext = context;
            this.mWifiEntry = wifiEntry;
        }

        public void onConnectResult(int i) {
            if (i == 1) {
                Intent putExtra = new Intent(this.mContext, WifiDialogActivity.class).putExtra("key_chosen_wifientry_key", this.mWifiEntry.getKey());
                putExtra.addFlags(268435456);
                this.mContext.startActivity(putExtra);
            } else if (i == 2) {
                Toast.makeText(this.mContext, R.string.wifi_failed_connect_message, 0).show();
            }
        }
    }
}
