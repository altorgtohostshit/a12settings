package com.android.settingslib.users;

import android.content.DialogInterface;

public final /* synthetic */ class EditUserInfoController$$ExternalSyntheticLambda0 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ EditUserInfoController f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ EditUserInfoController$$ExternalSyntheticLambda0(EditUserInfoController editUserInfoController, Runnable runnable) {
        this.f$0 = editUserInfoController;
        this.f$1 = runnable;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$buildDialog$2(this.f$1, dialogInterface);
    }
}
