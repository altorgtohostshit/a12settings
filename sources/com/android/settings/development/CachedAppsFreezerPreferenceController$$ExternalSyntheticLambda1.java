package com.android.settings.development;

import android.content.DialogInterface;

public final /* synthetic */ class CachedAppsFreezerPreferenceController$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ CachedAppsFreezerPreferenceController f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ CachedAppsFreezerPreferenceController$$ExternalSyntheticLambda1(CachedAppsFreezerPreferenceController cachedAppsFreezerPreferenceController, Object obj) {
        this.f$0 = cachedAppsFreezerPreferenceController;
        this.f$1 = obj;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$getRebootDialogOkListener$0(this.f$1, dialogInterface, i);
    }
}
