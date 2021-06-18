package com.android.settings.notification;

import android.content.Context;

/* renamed from: com.android.settings.notification.RecentNotifyingAppsPreferenceController$$ExternalSyntheticLambda2 */
public final /* synthetic */ class C1077xfd336858 implements Runnable {
    public final /* synthetic */ RecentNotifyingAppsPreferenceController f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ C1077xfd336858(RecentNotifyingAppsPreferenceController recentNotifyingAppsPreferenceController, Context context) {
        this.f$0 = recentNotifyingAppsPreferenceController;
        this.f$1 = context;
    }

    public final void run() {
        this.f$0.lambda$refreshUi$1(this.f$1);
    }
}
