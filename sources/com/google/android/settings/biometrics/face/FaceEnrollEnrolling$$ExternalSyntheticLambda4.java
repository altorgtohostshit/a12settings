package com.google.android.settings.biometrics.face;

import android.content.DialogInterface;
import android.view.KeyEvent;
import com.google.android.settings.biometrics.face.FaceEnrollDialogFactory;

public final /* synthetic */ class FaceEnrollEnrolling$$ExternalSyntheticLambda4 implements FaceEnrollDialogFactory.OnBackKeyListener {
    public final /* synthetic */ FaceEnrollEnrolling f$0;

    public /* synthetic */ FaceEnrollEnrolling$$ExternalSyntheticLambda4(FaceEnrollEnrolling faceEnrollEnrolling) {
        this.f$0 = faceEnrollEnrolling;
    }

    public final void onBackKeyUp(DialogInterface dialogInterface, KeyEvent keyEvent) {
        this.f$0.lambda$showPartialEnrollmentDialog$5(dialogInterface, keyEvent);
    }
}
