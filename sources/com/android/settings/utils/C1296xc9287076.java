package com.android.settings.utils;

import android.content.ComponentName;
import android.content.DialogInterface;

/* renamed from: com.android.settings.utils.ManagedServiceSettings$ScaryWarningDialogFragment$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C1296xc9287076 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ManagedServiceSettings f$0;
    public final /* synthetic */ ComponentName f$1;

    public /* synthetic */ C1296xc9287076(ManagedServiceSettings managedServiceSettings, ComponentName componentName) {
        this.f$0 = managedServiceSettings;
        this.f$1 = componentName;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.enable(this.f$1);
    }
}
