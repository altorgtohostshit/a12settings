package com.android.settings.applications.specialaccess.zenaccess;

import android.content.DialogInterface;

public final /* synthetic */ class ScaryWarningDialogFragment$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ScaryWarningDialogFragment f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ZenAccessDetails f$2;

    public /* synthetic */ ScaryWarningDialogFragment$$ExternalSyntheticLambda0(ScaryWarningDialogFragment scaryWarningDialogFragment, String str, ZenAccessDetails zenAccessDetails) {
        this.f$0 = scaryWarningDialogFragment;
        this.f$1 = str;
        this.f$2 = zenAccessDetails;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onCreateDialog$0(this.f$1, this.f$2, dialogInterface, i);
    }
}
