package com.android.settings.wifi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class NetworkRequestErrorDialogFragment extends InstrumentedDialogFragment {
    private WifiManager.NetworkRequestUserSelectionCallback mRejectCallback;

    public enum ERROR_DIALOG_TYPE {
        TIME_OUT,
        ABORT
    }

    public int getMetricsCategory() {
        return 1373;
    }

    public static NetworkRequestErrorDialogFragment newInstance() {
        return new NetworkRequestErrorDialogFragment();
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        rejectNetworkRequestAndFinish();
    }

    public Dialog onCreateDialog(Bundle bundle) {
        ERROR_DIALOG_TYPE error_dialog_type = ERROR_DIALOG_TYPE.TIME_OUT;
        ERROR_DIALOG_TYPE error_dialog_type2 = getArguments() != null ? (ERROR_DIALOG_TYPE) getArguments().getSerializable("DIALOG_ERROR_TYPE") : error_dialog_type;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (error_dialog_type2 == error_dialog_type) {
            builder.setMessage((int) R.string.network_connection_timeout_dialog_message).setPositiveButton((int) R.string.network_connection_timeout_dialog_ok, (DialogInterface.OnClickListener) new NetworkRequestErrorDialogFragment$$ExternalSyntheticLambda0(this)).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) new NetworkRequestErrorDialogFragment$$ExternalSyntheticLambda1(this));
        } else {
            builder.setMessage((int) R.string.network_connection_errorstate_dialog_message).setPositiveButton((int) R.string.okay, (DialogInterface.OnClickListener) new NetworkRequestErrorDialogFragment$$ExternalSyntheticLambda2(this));
        }
        return builder.create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        onRescanClick();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        rejectNetworkRequestAndFinish();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$2(DialogInterface dialogInterface, int i) {
        rejectNetworkRequestAndFinish();
    }

    public void setRejectCallback(WifiManager.NetworkRequestUserSelectionCallback networkRequestUserSelectionCallback) {
        this.mRejectCallback = networkRequestUserSelectionCallback;
    }

    /* access modifiers changed from: protected */
    public void onRescanClick() {
        if (getActivity() != null) {
            dismiss();
            ((NetworkRequestDialogActivity) getActivity()).onClickRescanButton();
        }
    }

    private void rejectNetworkRequestAndFinish() {
        if (getActivity() != null) {
            WifiManager.NetworkRequestUserSelectionCallback networkRequestUserSelectionCallback = this.mRejectCallback;
            if (networkRequestUserSelectionCallback != null) {
                networkRequestUserSelectionCallback.reject();
            }
            getActivity().finish();
        }
    }
}
