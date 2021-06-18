package com.android.settings.network.apn;

import android.content.ContentValues;
import android.net.Uri;

public final /* synthetic */ class ApnEditor$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ApnEditor f$0;
    public final /* synthetic */ Uri f$1;
    public final /* synthetic */ ContentValues f$2;

    public /* synthetic */ ApnEditor$$ExternalSyntheticLambda0(ApnEditor apnEditor, Uri uri, ContentValues contentValues) {
        this.f$0 = apnEditor;
        this.f$1 = uri;
        this.f$2 = contentValues;
    }

    public final void run() {
        this.f$0.lambda$updateApnDataToDatabase$0(this.f$1, this.f$2);
    }
}
