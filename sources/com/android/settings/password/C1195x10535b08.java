package com.android.settings.password;

import android.content.DialogInterface;
import android.os.Bundle;
import com.android.settings.password.ChooseLockGeneric;

/* renamed from: com.android.settings.password.ChooseLockGeneric$ChooseLockGenericFragment$FactoryResetProtectionWarningDialog$$ExternalSyntheticLambda1 */
public final /* synthetic */ class C1195x10535b08 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ChooseLockGeneric.ChooseLockGenericFragment.FactoryResetProtectionWarningDialog f$0;
    public final /* synthetic */ Bundle f$1;

    public /* synthetic */ C1195x10535b08(ChooseLockGeneric.ChooseLockGenericFragment.FactoryResetProtectionWarningDialog factoryResetProtectionWarningDialog, Bundle bundle) {
        this.f$0 = factoryResetProtectionWarningDialog;
        this.f$1 = bundle;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onCreateDialog$0(this.f$1, dialogInterface, i);
    }
}
