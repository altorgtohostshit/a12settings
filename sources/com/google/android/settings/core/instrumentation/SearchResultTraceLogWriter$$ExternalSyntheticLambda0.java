package com.google.android.settings.core.instrumentation;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.net.Uri;

public final /* synthetic */ class SearchResultTraceLogWriter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ContentProviderClient f$0;
    public final /* synthetic */ Uri f$1;
    public final /* synthetic */ ContentValues f$2;

    public /* synthetic */ SearchResultTraceLogWriter$$ExternalSyntheticLambda0(ContentProviderClient contentProviderClient, Uri uri, ContentValues contentValues) {
        this.f$0 = contentProviderClient;
        this.f$1 = uri;
        this.f$2 = contentValues;
    }

    public final void run() {
        SearchResultTraceLogWriter.lambda$action$0(this.f$0, this.f$1, this.f$2);
    }
}
