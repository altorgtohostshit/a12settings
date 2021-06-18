package com.android.settingslib.inputmethod;

import android.content.DialogInterface;

public final /* synthetic */ class InputMethodPreference$$ExternalSyntheticLambda0 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ InputMethodPreference f$0;

    public /* synthetic */ InputMethodPreference$$ExternalSyntheticLambda0(InputMethodPreference inputMethodPreference) {
        this.f$0 = inputMethodPreference;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$showSecurityWarnDialog$2(dialogInterface);
    }
}
