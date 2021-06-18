package com.android.settings.applications.specialaccess.zenaccess;

import android.app.NotificationManager;
import android.content.Context;

public final /* synthetic */ class ZenAccessController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Context f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ ZenAccessController$$ExternalSyntheticLambda0(Context context, String str) {
        this.f$0 = context;
        this.f$1 = str;
    }

    public final void run() {
        ((NotificationManager) this.f$0.getSystemService(NotificationManager.class)).removeAutomaticZenRules(this.f$1);
    }
}
