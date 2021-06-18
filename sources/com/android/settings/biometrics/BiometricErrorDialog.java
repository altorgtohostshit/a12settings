package com.android.settings.biometrics;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public abstract class BiometricErrorDialog extends InstrumentedDialogFragment {
    public abstract int getOkButtonTextResId();

    public abstract int getTitleResId();

    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence charSequence = getArguments().getCharSequence("error_msg");
        final int i = getArguments().getInt("error_id");
        builder.setTitle(getTitleResId()).setMessage(charSequence).setCancelable(false).setPositiveButton(getOkButtonTextResId(), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                int i2 = 1;
                boolean z = i == 3;
                FragmentActivity activity = BiometricErrorDialog.this.getActivity();
                if (z) {
                    i2 = 3;
                }
                activity.setResult(i2);
                activity.finish();
            }
        });
        AlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }
}
