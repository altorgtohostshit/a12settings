package com.android.settings.applications.specialaccess.zenaccess;

import android.content.DialogInterface;

public final /* synthetic */ class FriendlyWarningDialogFragment$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ FriendlyWarningDialogFragment f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ZenAccessDetails f$2;

    public /* synthetic */ FriendlyWarningDialogFragment$$ExternalSyntheticLambda0(FriendlyWarningDialogFragment friendlyWarningDialogFragment, String str, ZenAccessDetails zenAccessDetails) {
        this.f$0 = friendlyWarningDialogFragment;
        this.f$1 = str;
        this.f$2 = zenAccessDetails;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onCreateDialog$0(this.f$1, this.f$2, dialogInterface, i);
    }
}
