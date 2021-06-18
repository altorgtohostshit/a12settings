package com.google.android.settings.biometrics.face;

import android.content.DialogInterface;
import android.view.KeyEvent;
import com.google.android.settings.biometrics.face.FaceEnrollDialogFactory;

public final /* synthetic */ class FaceErrorDialog$$ExternalSyntheticLambda1 implements FaceEnrollDialogFactory.OnBackKeyListener {
    public final /* synthetic */ FaceErrorDialog f$0;

    public /* synthetic */ FaceErrorDialog$$ExternalSyntheticLambda1(FaceErrorDialog faceErrorDialog) {
        this.f$0 = faceErrorDialog;
    }

    public final void onBackKeyUp(DialogInterface dialogInterface, KeyEvent keyEvent) {
        this.f$0.lambda$onCreateDialog$1(dialogInterface, keyEvent);
    }
}
