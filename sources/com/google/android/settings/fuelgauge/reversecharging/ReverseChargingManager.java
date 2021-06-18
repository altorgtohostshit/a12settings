package com.google.android.settings.fuelgauge.reversecharging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.android.settings.R;
import com.google.android.systemui.reversecharging.ReverseWirelessCharger;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import vendor.google.wireless_charger.V1_2.RtxStatusInfo;

public class ReverseChargingManager {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable("ReverseChargingManager", 3);
    private static ReverseChargingManager sInstance;
    Context mContext;
    boolean mIsRtxSupported;
    Collection<ReverseChargingCallback> mReverseChargingCallbacks = new CopyOnWriteArrayList();
    ReverseWirelessCharger mReverseWirelessCharger;
    RtxInformationCallback mRtxInformationCallback;
    RtxStatusCallback mRtxStatusCallback;

    public interface ReverseChargingCallback {
        void onReverseChargingStateChanged();
    }

    public static ReverseChargingManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ReverseChargingManager(context);
        }
        return sInstance;
    }

    private ReverseChargingManager(Context context) {
        this.mContext = context;
        this.mReverseWirelessCharger = new ReverseWirelessCharger(context);
        this.mRtxInformationCallback = new RtxInformationCallback();
        this.mRtxStatusCallback = new RtxStatusCallback();
        this.mIsRtxSupported = isRtxSupported();
        this.mReverseWirelessCharger.addRtxInformationCallback(this.mRtxInformationCallback);
        this.mReverseWirelessCharger.addRtxStatusCallback(this.mRtxStatusCallback);
    }

    private boolean isRtxSupported() {
        if (this.mContext.getResources().getBoolean(R.bool.config_show_reverse_charging)) {
            return this.mReverseWirelessCharger.isRtxSupported();
        }
        return false;
    }

    public void registerCallback(ReverseChargingCallback reverseChargingCallback) {
        this.mReverseChargingCallbacks.add(reverseChargingCallback);
    }

    public void unregisterCallback(ReverseChargingCallback reverseChargingCallback) {
        this.mReverseChargingCallbacks.remove(reverseChargingCallback);
    }

    public boolean isReverseChargingOn() {
        if (!isOnWirelessCharge() && this.mIsRtxSupported) {
            return this.mReverseWirelessCharger.isRtxModeOn();
        }
        return false;
    }

    public void setReverseChargingState(boolean z) {
        this.mReverseWirelessCharger.setRtxMode(z);
    }

    public boolean isOnWirelessCharge() {
        Intent registerReceiver = this.mContext.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver == null || registerReceiver.getIntExtra("plugged", -1) != 4) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isSupportedReverseCharging() {
        return this.mIsRtxSupported;
    }

    /* access modifiers changed from: package-private */
    public void dispatchReverseChargingStateChanged() {
        for (ReverseChargingCallback onReverseChargingStateChanged : new CopyOnWriteArrayList(this.mReverseChargingCallbacks)) {
            onReverseChargingStateChanged.onReverseChargingStateChanged();
        }
    }

    class RtxInformationCallback implements ReverseWirelessCharger.RtxInformationCallback {
        RtxInformationCallback() {
        }
    }

    class RtxStatusCallback implements ReverseWirelessCharger.RtxStatusCallback {
        RtxStatusCallback() {
        }

        public void onRtxStatusChanged(RtxStatusInfo rtxStatusInfo) {
            if (ReverseChargingManager.DEBUG) {
                Log.d("ReverseChargingManager", "rtxStatusInfoChanged() rtxStatusInfo : " + rtxStatusInfo.toString());
            }
            ReverseChargingManager.this.dispatchReverseChargingStateChanged();
        }
    }
}
