package com.android.settings.security;

import android.content.DialogInterface;

public final /* synthetic */ class UnificationConfirmationDialog$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ SecuritySettings f$0;

    public /* synthetic */ UnificationConfirmationDialog$$ExternalSyntheticLambda0(SecuritySettings securitySettings) {
        this.f$0 = securitySettings;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.startUnification();
    }
}
