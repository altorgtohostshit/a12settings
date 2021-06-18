package com.android.settings.accessibility;

import android.app.Dialog;
import android.view.View;
import com.android.settings.accessibility.AccessibilityEditDialogUtils;

public final /* synthetic */ class AccessibilityEditDialogUtils$$ExternalSyntheticLambda4 implements View.OnClickListener {
    public final /* synthetic */ AccessibilityEditDialogUtils.CustomButtonsClickListener f$0;
    public final /* synthetic */ Dialog f$1;

    public /* synthetic */ AccessibilityEditDialogUtils$$ExternalSyntheticLambda4(AccessibilityEditDialogUtils.CustomButtonsClickListener customButtonsClickListener, Dialog dialog) {
        this.f$0 = customButtonsClickListener;
        this.f$1 = dialog;
    }

    public final void onClick(View view) {
        AccessibilityEditDialogUtils.lambda$setCustomButtonsClickListener$2(this.f$0, this.f$1, view);
    }
}
