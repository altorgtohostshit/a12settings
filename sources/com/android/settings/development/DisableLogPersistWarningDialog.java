package com.android.settings.development;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class DisableLogPersistWarningDialog extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    public int getMetricsCategory() {
        return 1225;
    }

    public static void show(LogPersistDialogHost logPersistDialogHost) {
        if (logPersistDialogHost instanceof Fragment) {
            Fragment fragment = (Fragment) logPersistDialogHost;
            FragmentManager supportFragmentManager = fragment.getActivity().getSupportFragmentManager();
            if (supportFragmentManager.findFragmentByTag("DisableLogPersistDlg") == null) {
                DisableLogPersistWarningDialog disableLogPersistWarningDialog = new DisableLogPersistWarningDialog();
                disableLogPersistWarningDialog.setTargetFragment(fragment, 0);
                disableLogPersistWarningDialog.show(supportFragmentManager, "DisableLogPersistDlg");
            }
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle((int) R.string.dev_logpersist_clear_warning_title).setMessage((int) R.string.dev_logpersist_clear_warning_message).setPositiveButton(17039379, (DialogInterface.OnClickListener) this).setNegativeButton(17039369, (DialogInterface.OnClickListener) this).create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        LogPersistDialogHost logPersistDialogHost = (LogPersistDialogHost) getTargetFragment();
        if (logPersistDialogHost != null) {
            if (i == -1) {
                logPersistDialogHost.onDisableLogPersistDialogConfirmed();
            } else {
                logPersistDialogHost.onDisableLogPersistDialogRejected();
            }
        }
    }
}
