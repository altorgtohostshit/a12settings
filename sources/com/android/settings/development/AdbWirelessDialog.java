package com.android.settings.development;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;

public class AdbWirelessDialog extends AlertDialog implements AdbWirelessDialogUiBase, DialogInterface.OnClickListener {
    private AdbWirelessDialogController mController;
    private final AdbWirelessDialogListener mListener;
    private final int mMode;
    private View mView;

    public interface AdbWirelessDialogListener {
        void onCancel() {
        }

        void onDismiss() {
        }
    }

    public static AdbWirelessDialog createModal(Context context, AdbWirelessDialogListener adbWirelessDialogListener, int i) {
        return new AdbWirelessDialog(context, adbWirelessDialogListener, i);
    }

    AdbWirelessDialog(Context context, AdbWirelessDialogListener adbWirelessDialogListener, int i) {
        super(context);
        this.mListener = adbWirelessDialogListener;
        this.mMode = i;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        View inflate = getLayoutInflater().inflate(R.layout.adb_wireless_dialog, (ViewGroup) null);
        this.mView = inflate;
        setView(inflate);
        this.mController = new AdbWirelessDialogController(this, this.mView, this.mMode);
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        dismiss();
        AdbWirelessDialogListener adbWirelessDialogListener = this.mListener;
        if (adbWirelessDialogListener != null) {
            adbWirelessDialogListener.onDismiss();
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        AdbWirelessDialogListener adbWirelessDialogListener = this.mListener;
        if (adbWirelessDialogListener != null && i == -2) {
            adbWirelessDialogListener.onCancel();
        }
    }

    public AdbWirelessDialogController getController() {
        return this.mController;
    }

    public void setSubmitButton(CharSequence charSequence) {
        setButton(-1, charSequence, this);
    }

    public void setCancelButton(CharSequence charSequence) {
        setButton(-2, charSequence, this);
    }
}
