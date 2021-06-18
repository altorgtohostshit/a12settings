package com.android.settings.applications.specialaccess.zenaccess;

import android.app.NotificationManager;
import android.content.Context;

public final /* synthetic */ class ZenAccessController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ Context f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ ZenAccessController$$ExternalSyntheticLambda1(Context context, String str, boolean z) {
        this.f$0 = context;
        this.f$1 = str;
        this.f$2 = z;
    }

    public final void run() {
        ((NotificationManager) this.f$0.getSystemService(NotificationManager.class)).setNotificationPolicyAccessGranted(this.f$1, this.f$2);
    }
}
