package com.android.settings.biometrics.face;

import android.content.DialogInterface;
import android.content.Intent;

public final /* synthetic */ class FaceEnrollEducation$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ FaceEnrollEducation f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ FaceEnrollEducation$$ExternalSyntheticLambda0(FaceEnrollEducation faceEnrollEducation, Intent intent) {
        this.f$0 = faceEnrollEducation;
        this.f$1 = intent;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onNextButtonClick$2(this.f$1, dialogInterface, i);
    }
}
