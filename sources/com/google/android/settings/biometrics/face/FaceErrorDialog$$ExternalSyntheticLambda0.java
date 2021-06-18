package com.google.android.settings.biometrics.face;

import android.content.DialogInterface;

public final /* synthetic */ class FaceErrorDialog$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ FaceErrorDialog f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ FaceErrorDialog$$ExternalSyntheticLambda0(FaceErrorDialog faceErrorDialog, boolean z, int i) {
        this.f$0 = faceErrorDialog;
        this.f$1 = z;
        this.f$2 = i;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onCreateDialog$0(this.f$1, this.f$2, dialogInterface, i);
    }
}
