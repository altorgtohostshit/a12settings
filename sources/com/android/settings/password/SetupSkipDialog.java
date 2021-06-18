package com.android.settings.password;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class SetupSkipDialog extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    public int getMetricsCategory() {
        return 573;
    }

    public static SetupSkipDialog newInstance(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
        SetupSkipDialog setupSkipDialog = new SetupSkipDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean("frp_supported", z);
        bundle.putBoolean("lock_type_pattern", z2);
        bundle.putBoolean("lock_type_alphanumeric", z3);
        bundle.putBoolean("for_fingerprint", z4);
        bundle.putBoolean("for_face", z5);
        bundle.putBoolean("for_biometrics", z6);
        setupSkipDialog.setArguments(bundle);
        return setupSkipDialog;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return onCreateDialogBuilder().create();
    }

    public AlertDialog.Builder onCreateDialogBuilder() {
        int i;
        Bundle arguments = getArguments();
        boolean z = arguments.getBoolean("for_face");
        boolean z2 = arguments.getBoolean("for_fingerprint");
        boolean z3 = arguments.getBoolean("for_biometrics");
        if (z || z2 || z3) {
            if (arguments.getBoolean("lock_type_pattern")) {
                i = R.string.lock_screen_pattern_skip_title;
            } else {
                i = arguments.getBoolean("lock_type_alphanumeric") ? R.string.lock_screen_password_skip_title : R.string.lock_screen_pin_skip_title;
            }
            return new AlertDialog.Builder(getContext()).setPositiveButton((int) R.string.skip_lock_screen_dialog_button_label, (DialogInterface.OnClickListener) this).setNegativeButton((int) R.string.cancel_lock_screen_dialog_button_label, (DialogInterface.OnClickListener) this).setTitle(i).setMessage(z3 ? R.string.biometrics_lock_screen_setup_skip_dialog_text : z ? R.string.face_lock_screen_setup_skip_dialog_text : R.string.fingerprint_lock_screen_setup_skip_dialog_text);
        }
        return new AlertDialog.Builder(getContext()).setPositiveButton((int) R.string.skip_anyway_button_label, (DialogInterface.OnClickListener) this).setNegativeButton((int) R.string.go_back_button_label, (DialogInterface.OnClickListener) this).setTitle((int) R.string.lock_screen_intro_skip_title).setMessage(arguments.getBoolean("frp_supported") ? R.string.lock_screen_intro_skip_dialog_text_frp : R.string.lock_screen_intro_skip_dialog_text);
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            FragmentActivity activity = getActivity();
            activity.setResult(11);
            activity.finish();
        }
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "skip_dialog");
    }
}
