package com.android.settings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkPolicyManager;
import android.net.Uri;
import android.net.VpnManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.RecoverySystem;
import android.os.UserHandle;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.enterprise.ActionDisabledByAdminDialogHelper;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;

public class ResetNetworkConfirm extends InstrumentedFragment {
    Activity mActivity;
    /* access modifiers changed from: private */
    public AlertDialog mAlertDialog;
    View mContentView;
    boolean mEraseEsim;
    View.OnClickListener mFinalClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (!Utils.isMonkeyRunning()) {
                if (ResetNetworkConfirm.this.mSubId != -1) {
                    SubscriptionManager access$400 = ResetNetworkConfirm.this.getSubscriptionManager();
                    ResetNetworkConfirm.this.stopMonitorSubscriptionChange(access$400);
                    ResetNetworkConfirm resetNetworkConfirm = ResetNetworkConfirm.this;
                    if (!resetNetworkConfirm.isSubscriptionRemainActive(access$400, resetNetworkConfirm.mSubId)) {
                        Log.w("ResetNetworkConfirm", "subId " + ResetNetworkConfirm.this.mSubId + " disappear when confirm");
                        ResetNetworkConfirm.this.mActivity.finish();
                        return;
                    }
                }
                ResetNetworkConfirm resetNetworkConfirm2 = ResetNetworkConfirm.this;
                ProgressDialog unused = resetNetworkConfirm2.mProgressDialog = resetNetworkConfirm2.getProgressDialog(resetNetworkConfirm2.mActivity);
                ResetNetworkConfirm.this.mProgressDialog.show();
                ResetNetworkConfirm resetNetworkConfirm3 = ResetNetworkConfirm.this;
                ResetNetworkConfirm resetNetworkConfirm4 = ResetNetworkConfirm.this;
                resetNetworkConfirm3.mResetNetworkTask = new ResetNetworkTask(resetNetworkConfirm4.mActivity);
                ResetNetworkConfirm.this.mResetNetworkTask.execute(new Void[0]);
            }
        }
    };
    /* access modifiers changed from: private */
    public ProgressDialog mProgressDialog;
    ResetNetworkTask mResetNetworkTask;
    /* access modifiers changed from: private */
    public int mSubId = -1;
    private SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionsChangedListener;

    public int getMetricsCategory() {
        return 84;
    }

    private class ResetNetworkTask extends AsyncTask<Void, Void, Boolean> {
        private final Context mContext;
        private final String mPackageName;

        ResetNetworkTask(Context context) {
            this.mContext = context;
            this.mPackageName = context.getPackageName();
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(Void... voidArr) {
            BluetoothAdapter adapter;
            ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
            if (connectivityManager != null) {
                connectivityManager.factoryReset();
            }
            VpnManager vpnManager = (VpnManager) this.mContext.getSystemService(VpnManager.class);
            if (vpnManager != null) {
                vpnManager.factoryReset();
            }
            WifiManager wifiManager = (WifiManager) this.mContext.getSystemService("wifi");
            if (wifiManager != null) {
                wifiManager.factoryReset();
            }
            ResetNetworkConfirm.this.p2pFactoryReset(this.mContext);
            boolean wipeEuiccData = ResetNetworkConfirm.this.mEraseEsim ? RecoverySystem.wipeEuiccData(this.mContext, this.mPackageName) : true;
            TelephonyManager createForSubscriptionId = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(ResetNetworkConfirm.this.mSubId);
            if (createForSubscriptionId != null) {
                createForSubscriptionId.resetSettings();
            }
            NetworkPolicyManager networkPolicyManager = (NetworkPolicyManager) this.mContext.getSystemService("netpolicy");
            if (networkPolicyManager != null) {
                networkPolicyManager.factoryReset(createForSubscriptionId.getSubscriberId());
            }
            BluetoothManager bluetoothManager = (BluetoothManager) this.mContext.getSystemService("bluetooth");
            if (!(bluetoothManager == null || (adapter = bluetoothManager.getAdapter()) == null)) {
                adapter.factoryReset();
            }
            ResetNetworkConfirm.this.restoreDefaultApn(this.mContext);
            Log.d("ResetNetworkTask", "network factoryReset complete. succeeded: " + String.valueOf(wipeEuiccData));
            return Boolean.valueOf(wipeEuiccData);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean bool) {
            ResetNetworkConfirm.this.mProgressDialog.dismiss();
            if (bool.booleanValue()) {
                Toast.makeText(this.mContext, R.string.reset_network_complete_toast, 0).show();
            } else {
                AlertDialog unused = ResetNetworkConfirm.this.mAlertDialog = new AlertDialog.Builder(this.mContext).setTitle((int) R.string.reset_esim_error_title).setMessage((int) R.string.reset_esim_error_msg).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).show();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void p2pFactoryReset(Context context) {
        WifiP2pManager.Channel initialize;
        WifiP2pManager wifiP2pManager = (WifiP2pManager) context.getSystemService("wifip2p");
        if (wifiP2pManager != null && (initialize = wifiP2pManager.initialize(context.getApplicationContext(), context.getMainLooper(), (WifiP2pManager.ChannelListener) null)) != null) {
            wifiP2pManager.factoryReset(initialize, (WifiP2pManager.ActionListener) null);
        }
    }

    /* access modifiers changed from: private */
    public ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getString(R.string.main_clear_progress_text));
        return progressDialog;
    }

    /* access modifiers changed from: private */
    public void restoreDefaultApn(Context context) {
        Uri parse = Uri.parse("content://telephony/carriers/restore");
        if (SubscriptionManager.isUsableSubscriptionId(this.mSubId)) {
            parse = Uri.withAppendedPath(parse, "subId/" + String.valueOf(this.mSubId));
        }
        context.getContentResolver().delete(parse, (String) null, (String[]) null);
    }

    private void establishFinalConfirmationState() {
        this.mContentView.findViewById(R.id.execute_reset_network).setOnClickListener(this.mFinalClickListener);
    }

    /* access modifiers changed from: package-private */
    public void setSubtitle() {
        if (this.mEraseEsim) {
            ((TextView) this.mContentView.findViewById(R.id.reset_network_confirm)).setText(R.string.reset_network_final_desc_esim);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mActivity, "no_network_reset", UserHandle.myUserId());
        if (RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mActivity, "no_network_reset", UserHandle.myUserId())) {
            return layoutInflater.inflate(R.layout.network_reset_disallowed_screen, (ViewGroup) null);
        }
        if (checkIfRestrictionEnforced != null) {
            new ActionDisabledByAdminDialogHelper(this.mActivity).prepareDialogBuilder("no_network_reset", checkIfRestrictionEnforced).setOnDismissListener(new ResetNetworkConfirm$$ExternalSyntheticLambda0(this)).show();
            return new View(this.mActivity);
        }
        this.mContentView = layoutInflater.inflate(R.layout.reset_network_confirm, (ViewGroup) null);
        establishFinalConfirmationState();
        setSubtitle();
        return this.mContentView;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$0(DialogInterface dialogInterface) {
        this.mActivity.finish();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mSubId = arguments.getInt("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
            this.mEraseEsim = arguments.getBoolean("erase_esim");
        }
        this.mActivity = getActivity();
        if (this.mSubId != -1) {
            startMonitorSubscriptionChange(getSubscriptionManager());
        }
    }

    /* access modifiers changed from: private */
    public SubscriptionManager getSubscriptionManager() {
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mActivity.getSystemService(SubscriptionManager.class);
        if (subscriptionManager == null) {
            Log.w("ResetNetworkConfirm", "No SubscriptionManager");
        }
        return subscriptionManager;
    }

    private void startMonitorSubscriptionChange(SubscriptionManager subscriptionManager) {
        if (subscriptionManager != null) {
            this.mSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener(Looper.getMainLooper()) {
                public void onSubscriptionsChanged() {
                    SubscriptionManager access$400 = ResetNetworkConfirm.this.getSubscriptionManager();
                    ResetNetworkConfirm resetNetworkConfirm = ResetNetworkConfirm.this;
                    if (!resetNetworkConfirm.isSubscriptionRemainActive(access$400, resetNetworkConfirm.mSubId)) {
                        Log.w("ResetNetworkConfirm", "subId " + ResetNetworkConfirm.this.mSubId + " no longer active.");
                        ResetNetworkConfirm.this.stopMonitorSubscriptionChange(access$400);
                        ResetNetworkConfirm.this.mActivity.finish();
                    }
                }
            };
            subscriptionManager.addOnSubscriptionsChangedListener(this.mActivity.getMainExecutor(), this.mSubscriptionsChangedListener);
        }
    }

    /* access modifiers changed from: private */
    public boolean isSubscriptionRemainActive(SubscriptionManager subscriptionManager, int i) {
        return (subscriptionManager == null || subscriptionManager.getActiveSubscriptionInfo(i) == null) ? false : true;
    }

    /* access modifiers changed from: private */
    public void stopMonitorSubscriptionChange(SubscriptionManager subscriptionManager) {
        SubscriptionManager.OnSubscriptionsChangedListener onSubscriptionsChangedListener;
        if (subscriptionManager != null && (onSubscriptionsChangedListener = this.mSubscriptionsChangedListener) != null) {
            subscriptionManager.removeOnSubscriptionsChangedListener(onSubscriptionsChangedListener);
            this.mSubscriptionsChangedListener = null;
        }
    }

    public void onDestroy() {
        ResetNetworkTask resetNetworkTask = this.mResetNetworkTask;
        if (resetNetworkTask != null) {
            resetNetworkTask.cancel(true);
            this.mResetNetworkTask = null;
        }
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        stopMonitorSubscriptionChange(getSubscriptionManager());
        super.onDestroy();
    }
}
