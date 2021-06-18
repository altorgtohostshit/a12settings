package com.android.settings.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.text.TextUtils;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.settings.Utils;
import com.android.settings.slices.SliceBackgroundWorker;

public final class UsbTetherPreferenceController extends TetherBasePreferenceController {
    private static final String TAG = "UsbTetherPrefController";
    /* access modifiers changed from: private */
    public boolean mMassStorageActive;
    final BroadcastReceiver mUsbChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals("android.intent.action.MEDIA_SHARED", action)) {
                boolean unused = UsbTetherPreferenceController.this.mMassStorageActive = true;
            } else if (TextUtils.equals("android.intent.action.MEDIA_UNSHARED", action)) {
                boolean unused2 = UsbTetherPreferenceController.this.mMassStorageActive = false;
            } else if (TextUtils.equals("android.hardware.usb.action.USB_STATE", action)) {
                boolean unused3 = UsbTetherPreferenceController.this.mUsbConnected = intent.getBooleanExtra("connected", false);
            }
            UsbTetherPreferenceController usbTetherPreferenceController = UsbTetherPreferenceController.this;
            usbTetherPreferenceController.updateState(usbTetherPreferenceController.mPreference);
        }
    };
    /* access modifiers changed from: private */
    public boolean mUsbConnected;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getTetherType() {
        return 1;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public UsbTetherPreferenceController(Context context, String str) {
        super(context, str);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        this.mMassStorageActive = "shared".equals(Environment.getExternalStorageState());
        IntentFilter intentFilter = new IntentFilter("android.hardware.usb.action.USB_STATE");
        intentFilter.addAction("android.intent.action.MEDIA_SHARED");
        intentFilter.addAction("android.intent.action.MEDIA_UNSHARED");
        this.mContext.registerReceiver(this.mUsbChangeReceiver, intentFilter);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mContext.unregisterReceiver(this.mUsbChangeReceiver);
    }

    public boolean shouldEnable() {
        return this.mUsbConnected && !this.mMassStorageActive;
    }

    public boolean shouldShow() {
        String[] tetherableUsbRegexs = this.mTm.getTetherableUsbRegexs();
        return (tetherableUsbRegexs == null || tetherableUsbRegexs.length == 0 || Utils.isMonkeyRunning()) ? false : true;
    }
}
