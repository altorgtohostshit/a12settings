package com.android.settings.sim.smartForwarding;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.android.settings.R;
import com.android.settings.core.SettingsBaseActivity;
import com.android.settings.sim.smartForwarding.EnableSmartForwardingTask;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Executors;

public class SmartForwardingActivity extends SettingsBaseActivity {
    final ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        toolbar.setVisibility(0);
        setActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SmartForwardingFragment()).commit();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public void enableSmartForwarding(String[] strArr) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.smart_forwarding_ongoing_title);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getText(R.string.smart_forwarding_ongoing_text));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Futures.addCallback(this.service.submit(new EnableSmartForwardingTask(this, strArr)), new FutureCallback<EnableSmartForwardingTask.FeatureResult>() {
            public void onSuccess(EnableSmartForwardingTask.FeatureResult featureResult) {
                Log.e("SmartForwarding", "Enable Feature result: " + featureResult.getResult());
                if (featureResult.getResult()) {
                    SmartForwardingUtils.backupPrevStatus(SmartForwardingActivity.this, featureResult.getSlotUTData());
                    SmartForwardingFragment smartForwardingFragment = (SmartForwardingFragment) SmartForwardingActivity.this.getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    if (smartForwardingFragment != null) {
                        smartForwardingFragment.turnOnSwitchPreference();
                    }
                } else {
                    SmartForwardingActivity.this.onError(featureResult);
                }
                progressDialog.dismiss();
            }

            public void onFailure(Throwable th) {
                Log.e("SmartForwarding", "Enable Feature exception", th);
                progressDialog.dismiss();
                new AlertDialog.Builder(SmartForwardingActivity.this).setTitle(R.string.smart_forwarding_failed_title).setMessage(R.string.smart_forwarding_failed_text).setPositiveButton(R.string.smart_forwarding_missing_alert_dialog_text, SmartForwardingActivity$1$$ExternalSyntheticLambda0.INSTANCE).create().show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    public void disableSmartForwarding() {
        final TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TelephonyManager.class);
        final SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService(SubscriptionManager.class);
        Futures.addCallback(this.service.submit((Runnable) new DisableSmartForwardingTask(telephonyManager, SmartForwardingUtils.getAllSlotCallWaitingStatus(this, subscriptionManager, telephonyManager), SmartForwardingUtils.getAllSlotCallForwardingStatus(this, subscriptionManager, telephonyManager))), new FutureCallback() {
            public void onSuccess(Object obj) {
                SmartForwardingUtils.clearAllBackupData(SmartForwardingActivity.this, subscriptionManager, telephonyManager);
            }

            public void onFailure(Throwable th) {
                Log.e("SmartForwarding", "Disable Feature exception" + th);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    public void onError(EnableSmartForwardingTask.FeatureResult featureResult) {
        new AlertDialog.Builder(this).setTitle(R.string.smart_forwarding_failed_title).setMessage(featureResult.getReason() == EnableSmartForwardingTask.FeatureResult.FailedReason.SIM_NOT_ACTIVE ? R.string.smart_forwarding_failed_not_activated_text : R.string.smart_forwarding_failed_text).setPositiveButton(R.string.smart_forwarding_missing_alert_dialog_text, SmartForwardingActivity$$ExternalSyntheticLambda0.INSTANCE).create().show();
    }
}
