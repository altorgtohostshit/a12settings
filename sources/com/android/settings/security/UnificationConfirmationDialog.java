package com.android.settings.security;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class UnificationConfirmationDialog extends InstrumentedDialogFragment {
    public int getMetricsCategory() {
        return 532;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        SecuritySettings securitySettings = (SecuritySettings) getParentFragment();
        boolean z = getArguments().getBoolean("compliant");
        return new AlertDialog.Builder(getActivity()).setTitle((int) R.string.lock_settings_profile_unification_dialog_title).setMessage(z ? R.string.lock_settings_profile_unification_dialog_body : R.string.lock_settings_profile_unification_dialog_uncompliant_body).setPositiveButton(z ? R.string.lock_settings_profile_unification_dialog_confirm : R.string.lock_settings_profile_unification_dialog_uncompliant_confirm, (DialogInterface.OnClickListener) new UnificationConfirmationDialog$$ExternalSyntheticLambda0(securitySettings)).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) null).create();
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        ((SecuritySettings) getParentFragment()).updateUnificationPreference();
    }
}
