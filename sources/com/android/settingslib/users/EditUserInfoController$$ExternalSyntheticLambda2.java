package com.android.settingslib.users;

import android.content.DialogInterface;

public final /* synthetic */ class EditUserInfoController$$ExternalSyntheticLambda2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ EditUserInfoController f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ EditUserInfoController$$ExternalSyntheticLambda2(EditUserInfoController editUserInfoController, Runnable runnable) {
        this.f$0 = editUserInfoController;
        this.f$1 = runnable;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$buildDialog$1(this.f$1, dialogInterface, i);
    }
}
