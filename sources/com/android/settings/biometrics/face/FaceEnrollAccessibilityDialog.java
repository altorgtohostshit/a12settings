package com.android.settings.biometrics.face;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class FaceEnrollAccessibilityDialog extends InstrumentedDialogFragment {
    private DialogInterface.OnClickListener mPositiveButtonListener;

    public int getMetricsCategory() {
        return 1506;
    }

    public static FaceEnrollAccessibilityDialog newInstance() {
        return new FaceEnrollAccessibilityDialog();
    }

    public void setPositiveButtonListener(DialogInterface.OnClickListener onClickListener) {
        this.mPositiveButtonListener = onClickListener;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.security_settings_face_enroll_education_accessibility_dialog_message).setNegativeButton(R.string.security_settings_face_enroll_education_accessibility_dialog_negative, FaceEnrollAccessibilityDialog$$ExternalSyntheticLambda1.INSTANCE).setPositiveButton(R.string.security_settings_face_enroll_education_accessibility_dialog_positive, new FaceEnrollAccessibilityDialog$$ExternalSyntheticLambda0(this));
        return builder.create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        this.mPositiveButtonListener.onClick(dialogInterface, i);
    }
}
