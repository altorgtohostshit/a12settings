package com.google.android.settings.experiments;

import android.content.ContentResolver;
import android.os.Bundle;
import java.util.concurrent.Callable;

public final /* synthetic */ class GServicesProxy$$ExternalSyntheticLambda0 implements Callable {
    public final /* synthetic */ ContentResolver f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ Bundle f$2;

    public /* synthetic */ GServicesProxy$$ExternalSyntheticLambda0(ContentResolver contentResolver, String str, Bundle bundle) {
        this.f$0 = contentResolver;
        this.f$1 = str;
        this.f$2 = bundle;
    }

    public final Object call() {
        return GServicesProxy.lambda$getResult$0(this.f$0, this.f$1, this.f$2);
    }
}
