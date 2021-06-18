package com.android.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class AlwaysDiscoverable extends BroadcastReceiver {
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Context mContext;
    private IntentFilter mIntentFilter;
    boolean mStarted;

    public AlwaysDiscoverable(Context context) {
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        this.mIntentFilter = intentFilter;
        intentFilter.addAction("android.bluetooth.adapter.action.SCAN_MODE_CHANGED");
    }

    public void start() {
        if (!this.mStarted) {
            this.mContext.registerReceiver(this, this.mIntentFilter);
            this.mStarted = true;
            if (this.mBluetoothAdapter.getScanMode() != 23) {
                this.mBluetoothAdapter.setScanMode(23);
            }
        }
    }

    public void stop() {
        if (this.mStarted) {
            this.mContext.unregisterReceiver(this);
            this.mStarted = false;
            this.mBluetoothAdapter.setScanMode(21);
        }
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == "android.bluetooth.adapter.action.SCAN_MODE_CHANGED" && this.mBluetoothAdapter.getScanMode() != 23) {
            this.mBluetoothAdapter.setScanMode(23);
        }
    }
}
