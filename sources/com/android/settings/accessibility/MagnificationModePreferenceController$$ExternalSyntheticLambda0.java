package com.android.settings.accessibility;

import android.content.DialogInterface;

public final /* synthetic */ class MagnificationModePreferenceController$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ MagnificationModePreferenceController f$0;

    public /* synthetic */ MagnificationModePreferenceController$$ExternalSyntheticLambda0(MagnificationModePreferenceController magnificationModePreferenceController) {
        this.f$0 = magnificationModePreferenceController;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.onMagnificationModeDialogPositiveButtonClicked(dialogInterface, i);
    }
}
