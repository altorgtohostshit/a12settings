package com.google.android.settings.biometrics.face;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import com.android.settings.R;

public class FaceGazeDialog extends DialogFragment {
    private DialogInterface.OnClickListener mButtonListener;

    static FaceGazeDialog newInstance() {
        return new FaceGazeDialog();
    }

    public void setButtonListener(DialogInterface.OnClickListener onClickListener) {
        this.mButtonListener = onClickListener;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return FaceEnrollDialogFactory.newBuilder(getActivity()).setTitle(R.string.face_enrolling_gaze_dialog_title).setMessage((int) R.string.face_enrolling_gaze_dialog_message).setPositiveButton(R.string.face_enrolling_gaze_dialog_continue, new FaceGazeDialog$$ExternalSyntheticLambda0(this)).setNegativeButton(R.string.face_enrolling_gaze_dialog_cancel, new FaceGazeDialog$$ExternalSyntheticLambda1(this)).build();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        this.mButtonListener.onClick(dialogInterface, i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        this.mButtonListener.onClick(dialogInterface, i);
    }
}
