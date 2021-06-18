package com.android.settings.network;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.system.ResetDashboardFragment;

public class EraseEuiccDataDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    public int getMetricsCategory() {
        return 1857;
    }

    public static void show(ResetDashboardFragment resetDashboardFragment) {
        EraseEuiccDataDialogFragment eraseEuiccDataDialogFragment = new EraseEuiccDataDialogFragment();
        eraseEuiccDataDialogFragment.setTargetFragment(resetDashboardFragment, 0);
        eraseEuiccDataDialogFragment.show(resetDashboardFragment.getActivity().getSupportFragmentManager(), "EraseEuiccDataDlg");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.reset_esim_title).setMessage(R.string.reset_esim_desc).setPositiveButton(R.string.erase_euicc_data_button, this).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).setOnDismissListener(this).create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (!(getTargetFragment() instanceof ResetDashboardFragment)) {
            Log.e("EraseEuiccDataDlg", "getTargetFragment return unexpected type");
        }
        if (i == -1) {
            AsyncTask.execute(new Runnable() {
                public void run() {
                    RecoverySystem.wipeEuiccData(EraseEuiccDataDialogFragment.this.getContext(), "com.android.settings.network");
                }
            });
        }
    }
}
