package com.android.settingslib.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.provider.Settings;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ConnectivitySubsystemsRecoveryManager {
    private final BroadcastReceiver mApmMonitor = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            RecoveryAvailableListener access$000 = ConnectivitySubsystemsRecoveryManager.this.mRecoveryAvailableListener;
            if (access$000 != null) {
                access$000.onRecoveryAvailableChangeListener(ConnectivitySubsystemsRecoveryManager.this.isRecoveryAvailable());
            }
        }
    };
    private boolean mApmMonitorRegistered = false;
    private final Context mContext;
    /* access modifiers changed from: private */
    public RecoveryStatusCallback mCurrentRecoveryCallback = null;
    private final Handler mHandler;
    /* access modifiers changed from: private */
    public RecoveryAvailableListener mRecoveryAvailableListener = null;
    private final MobileTelephonyCallback mTelephonyCallback = new MobileTelephonyCallback();
    private TelephonyManager mTelephonyManager = null;
    /* access modifiers changed from: private */
    public boolean mTelephonyRestartInProgress = false;
    private WifiManager mWifiManager = null;
    /* access modifiers changed from: private */
    public boolean mWifiRestartInProgress = false;
    private final WifiManager.SubsystemRestartTrackingCallback mWifiSubsystemRestartTrackingCallback = new WifiManager.SubsystemRestartTrackingCallback() {
        public void onSubsystemRestarting() {
        }

        public void onSubsystemRestarted() {
            boolean unused = ConnectivitySubsystemsRecoveryManager.this.mWifiRestartInProgress = false;
            ConnectivitySubsystemsRecoveryManager.this.stopTrackingWifiRestart();
            ConnectivitySubsystemsRecoveryManager.this.checkIfAllSubsystemsRestartsAreDone();
        }
    };

    public interface RecoveryAvailableListener {
        void onRecoveryAvailableChangeListener(boolean z);
    }

    public interface RecoveryStatusCallback {
        void onSubsystemRestartOperationBegin();

        void onSubsystemRestartOperationEnd();
    }

    private class MobileTelephonyCallback extends TelephonyCallback implements TelephonyCallback.RadioPowerStateListener {
        private MobileTelephonyCallback() {
        }

        public void onRadioPowerStateChanged(int i) {
            if (!ConnectivitySubsystemsRecoveryManager.this.mTelephonyRestartInProgress || ConnectivitySubsystemsRecoveryManager.this.mCurrentRecoveryCallback == null) {
                ConnectivitySubsystemsRecoveryManager.this.stopTrackingTelephonyRestart();
            }
            if (i == 1) {
                boolean unused = ConnectivitySubsystemsRecoveryManager.this.mTelephonyRestartInProgress = false;
                ConnectivitySubsystemsRecoveryManager.this.stopTrackingTelephonyRestart();
                ConnectivitySubsystemsRecoveryManager.this.checkIfAllSubsystemsRestartsAreDone();
            }
        }
    }

    public ConnectivitySubsystemsRecoveryManager(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = new Handler(handler.getLooper());
        if (context.getPackageManager().hasSystemFeature("android.hardware.wifi")) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(WifiManager.class);
            this.mWifiManager = wifiManager;
            if (wifiManager == null) {
                Log.e("ConnectivitySubsystemsRecoveryManager", "WifiManager not available!?");
            }
        }
        if (context.getPackageManager().hasSystemFeature("android.hardware.telephony")) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
            this.mTelephonyManager = telephonyManager;
            if (telephonyManager == null) {
                Log.e("ConnectivitySubsystemsRecoveryManager", "TelephonyManager not available!?");
            }
        }
    }

    private boolean isApmEnabled() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    private boolean isWifiEnabled() {
        WifiManager wifiManager = this.mWifiManager;
        return wifiManager != null && (wifiManager.isWifiEnabled() || this.mWifiManager.isWifiApEnabled());
    }

    public boolean isRecoveryAvailable() {
        if (!isApmEnabled()) {
            return true;
        }
        return isWifiEnabled();
    }

    private void startTrackingWifiRestart() {
        this.mWifiManager.registerSubsystemRestartTrackingCallback(new HandlerExecutor(this.mHandler), this.mWifiSubsystemRestartTrackingCallback);
    }

    /* access modifiers changed from: private */
    public void stopTrackingWifiRestart() {
        this.mWifiManager.unregisterSubsystemRestartTrackingCallback(this.mWifiSubsystemRestartTrackingCallback);
    }

    private void startTrackingTelephonyRestart() {
        this.mTelephonyManager.registerTelephonyCallback(new HandlerExecutor(this.mHandler), this.mTelephonyCallback);
    }

    /* access modifiers changed from: private */
    public void stopTrackingTelephonyRestart() {
        this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
    }

    /* access modifiers changed from: private */
    public void checkIfAllSubsystemsRestartsAreDone() {
        RecoveryStatusCallback recoveryStatusCallback;
        if (!this.mWifiRestartInProgress && !this.mTelephonyRestartInProgress && (recoveryStatusCallback = this.mCurrentRecoveryCallback) != null) {
            recoveryStatusCallback.onSubsystemRestartOperationEnd();
            this.mCurrentRecoveryCallback = null;
        }
    }

    public void triggerSubsystemRestart(String str, RecoveryStatusCallback recoveryStatusCallback) {
        this.mHandler.post(new ConnectivitySubsystemsRecoveryManager$$ExternalSyntheticLambda1(this, recoveryStatusCallback));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerSubsystemRestart$3(RecoveryStatusCallback recoveryStatusCallback) {
        boolean z;
        if (this.mWifiRestartInProgress) {
            Log.e("ConnectivitySubsystemsRecoveryManager", "Wifi restart still in progress");
        } else if (this.mTelephonyRestartInProgress) {
            Log.e("ConnectivitySubsystemsRecoveryManager", "Telephony restart still in progress");
        } else {
            boolean z2 = true;
            if (isWifiEnabled()) {
                this.mWifiManager.restartWifiSubsystem();
                this.mWifiRestartInProgress = true;
                startTrackingWifiRestart();
                z = true;
            } else {
                z = false;
            }
            if (this.mTelephonyManager == null || isApmEnabled() || !this.mTelephonyManager.rebootRadio()) {
                z2 = z;
            } else {
                this.mTelephonyRestartInProgress = true;
                startTrackingTelephonyRestart();
            }
            if (z2) {
                this.mCurrentRecoveryCallback = recoveryStatusCallback;
                recoveryStatusCallback.onSubsystemRestartOperationBegin();
                this.mHandler.postDelayed(new ConnectivitySubsystemsRecoveryManager$$ExternalSyntheticLambda0(this), 15000);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerSubsystemRestart$2() {
        stopTrackingWifiRestart();
        stopTrackingTelephonyRestart();
        this.mWifiRestartInProgress = false;
        this.mTelephonyRestartInProgress = false;
        RecoveryStatusCallback recoveryStatusCallback = this.mCurrentRecoveryCallback;
        if (recoveryStatusCallback != null) {
            recoveryStatusCallback.onSubsystemRestartOperationEnd();
            this.mCurrentRecoveryCallback = null;
        }
    }
}
