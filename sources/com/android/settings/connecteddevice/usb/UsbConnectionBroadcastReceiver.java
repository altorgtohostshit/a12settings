package com.android.settings.connecteddevice.usb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbPortStatus;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;

public class UsbConnectionBroadcastReceiver extends BroadcastReceiver implements LifecycleObserver, OnResume, OnPause {
    private boolean mConnected;
    private Context mContext;
    private int mDataRole = 0;
    private long mFunctions = 0;
    private boolean mListeningToUsbEvents;
    private int mPowerRole = 0;
    private UsbBackend mUsbBackend;
    private UsbConnectionListener mUsbConnectionListener;

    interface UsbConnectionListener {
        void onUsbConnectionChanged(boolean z, long j, int i, int i2);
    }

    public UsbConnectionBroadcastReceiver(Context context, UsbConnectionListener usbConnectionListener, UsbBackend usbBackend) {
        this.mContext = context;
        this.mUsbConnectionListener = usbConnectionListener;
        this.mUsbBackend = usbBackend;
    }

    public void onReceive(Context context, Intent intent) {
        UsbPortStatus parcelable;
        if ("android.hardware.usb.action.USB_STATE".equals(intent.getAction())) {
            this.mConnected = intent.getExtras().getBoolean("connected") || intent.getExtras().getBoolean("host_connected");
            long j = 0;
            if (intent.getExtras().getBoolean("mtp") && intent.getExtras().getBoolean("unlocked", false)) {
                j = 4;
            }
            if (intent.getExtras().getBoolean("ptp") && intent.getExtras().getBoolean("unlocked", false)) {
                j |= 16;
            }
            if (intent.getExtras().getBoolean("midi")) {
                j |= 8;
            }
            if (intent.getExtras().getBoolean("rndis")) {
                j |= 32;
            }
            if (intent.getExtras().getBoolean("accessory")) {
                j |= 2;
            }
            this.mFunctions = j;
            this.mDataRole = this.mUsbBackend.getDataRole();
            this.mPowerRole = this.mUsbBackend.getPowerRole();
        } else if ("android.hardware.usb.action.USB_PORT_CHANGED".equals(intent.getAction()) && (parcelable = intent.getExtras().getParcelable("portStatus")) != null) {
            this.mDataRole = parcelable.getCurrentDataRole();
            this.mPowerRole = parcelable.getCurrentPowerRole();
        }
        UsbConnectionListener usbConnectionListener = this.mUsbConnectionListener;
        if (usbConnectionListener != null) {
            usbConnectionListener.onUsbConnectionChanged(this.mConnected, this.mFunctions, this.mPowerRole, this.mDataRole);
        }
    }

    public void register() {
        if (!this.mListeningToUsbEvents) {
            this.mConnected = false;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.hardware.usb.action.USB_STATE");
            intentFilter.addAction("android.hardware.usb.action.USB_PORT_CHANGED");
            Intent registerReceiver = this.mContext.registerReceiver(this, intentFilter);
            if (registerReceiver != null) {
                onReceive(this.mContext, registerReceiver);
            }
            this.mListeningToUsbEvents = true;
        }
    }

    public void unregister() {
        if (this.mListeningToUsbEvents) {
            this.mContext.unregisterReceiver(this);
            this.mListeningToUsbEvents = false;
        }
    }

    public void onResume() {
        register();
    }

    public void onPause() {
        unregister();
    }
}
