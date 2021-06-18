package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.preference.Preference;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.settings.R;

public class BluetoothPermissionActivity extends AlertActivity implements DialogInterface.OnClickListener, Preference.OnPreferenceChangeListener {
    /* access modifiers changed from: private */
    public BluetoothDevice mDevice;
    private Button mOkButton;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.device.action.CONNECTION_ACCESS_CANCEL") && intent.getIntExtra("android.bluetooth.device.extra.ACCESS_REQUEST_TYPE", 2) == BluetoothPermissionActivity.this.mRequestType) {
                if (BluetoothPermissionActivity.this.mDevice.equals((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE"))) {
                    BluetoothPermissionActivity.this.dismissDialog();
                }
            }
        }
    };
    private boolean mReceiverRegistered = false;
    /* access modifiers changed from: private */
    public int mRequestType = 0;
    private View mView;
    private TextView messageView;

    public boolean onPreferenceChange(Preference preference, Object obj) {
        return true;
    }

    /* access modifiers changed from: private */
    public void dismissDialog() {
        dismiss();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        BluetoothPermissionActivity.super.onCreate(bundle);
        getWindow().addPrivateFlags(524288);
        Intent intent = getIntent();
        if (!intent.getAction().equals("android.bluetooth.device.action.CONNECTION_ACCESS_REQUEST")) {
            Log.e("BluetoothPermissionActivity", "Error: this activity may be started only with intent ACTION_CONNECTION_ACCESS_REQUEST");
            finish();
            return;
        }
        this.mDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
        this.mRequestType = intent.getIntExtra("android.bluetooth.device.extra.ACCESS_REQUEST_TYPE", 2);
        Log.i("BluetoothPermissionActivity", "onCreate() Request type: " + this.mRequestType);
        int i = this.mRequestType;
        if (i == 1) {
            showDialog(getString(R.string.bluetooth_connection_permission_request), this.mRequestType);
        } else if (i == 2) {
            showDialog(getString(R.string.bluetooth_phonebook_access_dialog_title), this.mRequestType);
        } else if (i == 3) {
            showDialog(getString(R.string.bluetooth_message_access_dialog_title), this.mRequestType);
        } else if (i == 4) {
            showDialog(getString(R.string.bluetooth_sap_request), this.mRequestType);
        } else {
            Log.e("BluetoothPermissionActivity", "Error: bad request type: " + this.mRequestType);
            finish();
            return;
        }
        registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.device.action.CONNECTION_ACCESS_CANCEL"));
        this.mReceiverRegistered = true;
    }

    private void showDialog(String str, int i) {
        AlertController.AlertParams alertParams = this.mAlertParams;
        alertParams.mTitle = str;
        Log.i("BluetoothPermissionActivity", "showDialog() Request type: " + this.mRequestType + " this: " + this);
        if (i == 1) {
            alertParams.mView = createConnectionDialogView();
        } else if (i == 2) {
            alertParams.mView = createPhonebookDialogView();
        } else if (i == 3) {
            alertParams.mView = createMapDialogView();
        } else if (i == 4) {
            alertParams.mView = createSapDialogView();
        }
        alertParams.mPositiveButtonText = getString(R.string.allow);
        alertParams.mPositiveButtonListener = this;
        alertParams.mNegativeButtonText = getString(R.string.request_manage_bluetooth_permission_dont_allow);
        alertParams.mNegativeButtonListener = this;
        this.mOkButton = this.mAlert.getButton(-1);
        setupAlert();
    }

    public void onBackPressed() {
        Log.i("BluetoothPermissionActivity", "Back button pressed! ignoring");
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [android.content.Context, com.android.settings.bluetooth.BluetoothPermissionActivity, com.android.internal.app.AlertActivity] */
    private View createConnectionDialogView() {
        String createRemoteName = Utils.createRemoteName(this, this.mDevice);
        View inflate = getLayoutInflater().inflate(R.layout.bluetooth_access, (ViewGroup) null);
        this.mView = inflate;
        TextView textView = (TextView) inflate.findViewById(R.id.message);
        this.messageView = textView;
        textView.setText(getString(R.string.bluetooth_connection_dialog_text, new Object[]{createRemoteName}));
        return this.mView;
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [android.content.Context, com.android.settings.bluetooth.BluetoothPermissionActivity, com.android.internal.app.AlertActivity] */
    private View createPhonebookDialogView() {
        String createRemoteName = Utils.createRemoteName(this, this.mDevice);
        View inflate = getLayoutInflater().inflate(R.layout.bluetooth_access, (ViewGroup) null);
        this.mView = inflate;
        TextView textView = (TextView) inflate.findViewById(R.id.message);
        this.messageView = textView;
        textView.setText(getString(R.string.bluetooth_phonebook_access_dialog_content, new Object[]{createRemoteName, createRemoteName}));
        return this.mView;
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [android.content.Context, com.android.settings.bluetooth.BluetoothPermissionActivity, com.android.internal.app.AlertActivity] */
    private View createMapDialogView() {
        String createRemoteName = Utils.createRemoteName(this, this.mDevice);
        View inflate = getLayoutInflater().inflate(R.layout.bluetooth_access, (ViewGroup) null);
        this.mView = inflate;
        TextView textView = (TextView) inflate.findViewById(R.id.message);
        this.messageView = textView;
        textView.setText(getString(R.string.bluetooth_message_access_dialog_content, new Object[]{createRemoteName, createRemoteName}));
        return this.mView;
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [android.content.Context, com.android.settings.bluetooth.BluetoothPermissionActivity, com.android.internal.app.AlertActivity] */
    private View createSapDialogView() {
        String createRemoteName = Utils.createRemoteName(this, this.mDevice);
        View inflate = getLayoutInflater().inflate(R.layout.bluetooth_access, (ViewGroup) null);
        this.mView = inflate;
        TextView textView = (TextView) inflate.findViewById(R.id.message);
        this.messageView = textView;
        textView.setText(getString(R.string.bluetooth_sap_acceptance_dialog_text, new Object[]{createRemoteName, createRemoteName}));
        return this.mView;
    }

    private void onPositive() {
        Log.d("BluetoothPermissionActivity", "onPositive");
        sendReplyIntentToReceiver(true, true);
        finish();
    }

    private void onNegative() {
        Log.d("BluetoothPermissionActivity", "onNegative");
        sendReplyIntentToReceiver(false, true);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void sendReplyIntentToReceiver(boolean z, boolean z2) {
        Intent intent = new Intent("android.bluetooth.device.action.CONNECTION_ACCESS_REPLY");
        Log.i("BluetoothPermissionActivity", "sendReplyIntentToReceiver() Request type: " + this.mRequestType + " mReturnPackage");
        intent.putExtra("android.bluetooth.device.extra.CONNECTION_ACCESS_RESULT", z ? 1 : 2);
        intent.putExtra("android.bluetooth.device.extra.ALWAYS_ALLOWED", z2);
        intent.putExtra("android.bluetooth.device.extra.DEVICE", this.mDevice);
        intent.putExtra("android.bluetooth.device.extra.ACCESS_REQUEST_TYPE", this.mRequestType);
        sendBroadcast(intent, "android.permission.BLUETOOTH_ADMIN");
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            onNegative();
        } else if (i == -1) {
            onPositive();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        BluetoothPermissionActivity.super.onDestroy();
        if (this.mReceiverRegistered) {
            unregisterReceiver(this.mReceiver);
            this.mReceiverRegistered = false;
        }
    }
}
