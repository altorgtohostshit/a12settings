package com.android.settings.deviceinfo.storage;

import android.content.Context;
import android.content.DialogInterface;

public final /* synthetic */ class DiskInitFragment$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ DiskInitFragment f$0;
    public final /* synthetic */ Context f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ DiskInitFragment$$ExternalSyntheticLambda0(DiskInitFragment diskInitFragment, Context context, String str) {
        this.f$0 = diskInitFragment;
        this.f$1 = context;
        this.f$2 = str;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onCreateDialog$0(this.f$1, this.f$2, dialogInterface, i);
    }
}
