package com.android.settings.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.settings.R;

public class RequestToggleWiFiActivity extends AlertActivity implements DialogInterface.OnClickListener {
    private CharSequence mAppLabel;
    private int mLastUpdateState = -1;
    private final StateChangeReceiver mReceiver = new StateChangeReceiver();
    /* access modifiers changed from: private */
    public int mState = -1;
    private final Runnable mTimeoutCommand = new RequestToggleWiFiActivity$$ExternalSyntheticLambda0(this);
    /* access modifiers changed from: private */
    public WifiManager mWiFiManager;

    public void dismiss() {
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (!isFinishing() && !isDestroyed()) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        RequestToggleWiFiActivity.super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        this.mWiFiManager = (WifiManager) getSystemService(WifiManager.class);
        setResult(0);
        String stringExtra = getIntent().getStringExtra("android.intent.extra.PACKAGE_NAME");
        if (TextUtils.isEmpty(stringExtra)) {
            finish();
            return;
        }
        try {
            this.mAppLabel = getPackageManager().getApplicationInfo(stringExtra, 0).loadSafeLabel(getPackageManager(), 500.0f, 5);
            String action = getIntent().getAction();
            action.hashCode();
            if (action.equals("android.net.wifi.action.REQUEST_ENABLE")) {
                this.mState = 1;
            } else if (!action.equals("android.net.wifi.action.REQUEST_DISABLE")) {
                finish();
            } else {
                this.mState = 3;
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("RequestToggleWiFiActivity", "Couldn't find app with package name " + stringExtra);
            finish();
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            finish();
        } else if (i == -1) {
            int i2 = this.mState;
            if (i2 == 1) {
                this.mWiFiManager.setWifiEnabled(true);
                this.mState = 2;
                scheduleToggleTimeout();
                updateUi();
            } else if (i2 == 3) {
                this.mWiFiManager.setWifiEnabled(false);
                this.mState = 4;
                scheduleToggleTimeout();
                updateUi();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        RequestToggleWiFiActivity.super.onStart();
        this.mReceiver.register();
        int wifiState = this.mWiFiManager.getWifiState();
        int i = this.mState;
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 4) {
                        if (wifiState == 0) {
                            scheduleToggleTimeout();
                        } else if (wifiState == 1) {
                            setResult(-1);
                            finish();
                            return;
                        } else if (wifiState == 2 || wifiState == 3) {
                            this.mState = 3;
                        }
                    }
                } else if (wifiState == 1) {
                    setResult(-1);
                    finish();
                    return;
                } else if (wifiState == 2) {
                    this.mState = 4;
                    scheduleToggleTimeout();
                }
            } else if (wifiState == 0 || wifiState == 1) {
                this.mState = 1;
            } else if (wifiState == 2) {
                scheduleToggleTimeout();
            } else if (wifiState == 3) {
                setResult(-1);
                finish();
                return;
            }
        } else if (wifiState == 2) {
            this.mState = 2;
            scheduleToggleTimeout();
        } else if (wifiState == 3) {
            setResult(-1);
            finish();
            return;
        }
        updateUi();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.mReceiver.unregister();
        unscheduleToggleTimeout();
        RequestToggleWiFiActivity.super.onStop();
    }

    private void updateUi() {
        int i = this.mLastUpdateState;
        int i2 = this.mState;
        if (i != i2) {
            this.mLastUpdateState = i2;
            if (i2 == 1) {
                this.mAlertParams.mPositiveButtonText = getString(R.string.allow);
                AlertController.AlertParams alertParams = this.mAlertParams;
                alertParams.mPositiveButtonListener = this;
                alertParams.mNegativeButtonText = getString(R.string.deny);
                AlertController.AlertParams alertParams2 = this.mAlertParams;
                alertParams2.mNegativeButtonListener = this;
                alertParams2.mMessage = getString(R.string.wifi_ask_enable, new Object[]{this.mAppLabel});
            } else if (i2 == 2) {
                this.mAlert.setButton(-1, (CharSequence) null, (DialogInterface.OnClickListener) null, (Message) null);
                this.mAlert.setButton(-2, (CharSequence) null, (DialogInterface.OnClickListener) null, (Message) null);
                AlertController.AlertParams alertParams3 = this.mAlertParams;
                alertParams3.mPositiveButtonText = null;
                alertParams3.mPositiveButtonListener = null;
                alertParams3.mNegativeButtonText = null;
                alertParams3.mNegativeButtonListener = null;
                alertParams3.mMessage = getString(R.string.wifi_starting);
            } else if (i2 == 3) {
                this.mAlertParams.mPositiveButtonText = getString(R.string.allow);
                AlertController.AlertParams alertParams4 = this.mAlertParams;
                alertParams4.mPositiveButtonListener = this;
                alertParams4.mNegativeButtonText = getString(R.string.deny);
                AlertController.AlertParams alertParams5 = this.mAlertParams;
                alertParams5.mNegativeButtonListener = this;
                alertParams5.mMessage = getString(R.string.wifi_ask_disable, new Object[]{this.mAppLabel});
            } else if (i2 == 4) {
                this.mAlert.setButton(-1, (CharSequence) null, (DialogInterface.OnClickListener) null, (Message) null);
                this.mAlert.setButton(-2, (CharSequence) null, (DialogInterface.OnClickListener) null, (Message) null);
                AlertController.AlertParams alertParams6 = this.mAlertParams;
                alertParams6.mPositiveButtonText = null;
                alertParams6.mPositiveButtonListener = null;
                alertParams6.mNegativeButtonText = null;
                alertParams6.mNegativeButtonListener = null;
                alertParams6.mMessage = getString(R.string.wifi_stopping);
            }
            setupAlert();
        }
    }

    private void scheduleToggleTimeout() {
        getWindow().getDecorView().postDelayed(this.mTimeoutCommand, 10000);
    }

    private void unscheduleToggleTimeout() {
        getWindow().getDecorView().removeCallbacks(this.mTimeoutCommand);
    }

    private final class StateChangeReceiver extends BroadcastReceiver {
        private final IntentFilter mFilter;

        private StateChangeReceiver() {
            this.mFilter = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
        }

        public void register() {
            RequestToggleWiFiActivity.this.registerReceiver(this, this.mFilter);
        }

        public void unregister() {
            RequestToggleWiFiActivity.this.unregisterReceiver(this);
        }

        /* JADX WARNING: type inference failed for: r1v1, types: [com.android.settings.wifi.RequestToggleWiFiActivity, android.app.Activity] */
        public void onReceive(Context context, Intent intent) {
            ? r1 = RequestToggleWiFiActivity.this;
            if (!r1.isFinishing() && !r1.isDestroyed()) {
                int wifiState = RequestToggleWiFiActivity.this.mWiFiManager.getWifiState();
                if (wifiState != 1 && wifiState != 3) {
                    return;
                }
                if (RequestToggleWiFiActivity.this.mState == 2 || RequestToggleWiFiActivity.this.mState == 4) {
                    RequestToggleWiFiActivity.this.setResult(-1);
                    RequestToggleWiFiActivity.this.finish();
                }
            }
        }
    }
}
