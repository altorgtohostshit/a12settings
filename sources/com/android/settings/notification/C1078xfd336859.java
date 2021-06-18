package com.android.settings.notification;

import android.content.Context;
import java.util.List;

/* renamed from: com.android.settings.notification.RecentNotifyingAppsPreferenceController$$ExternalSyntheticLambda3 */
public final /* synthetic */ class C1078xfd336859 implements Runnable {
    public final /* synthetic */ RecentNotifyingAppsPreferenceController f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ Context f$2;

    public /* synthetic */ C1078xfd336859(RecentNotifyingAppsPreferenceController recentNotifyingAppsPreferenceController, List list, Context context) {
        this.f$0 = recentNotifyingAppsPreferenceController;
        this.f$1 = list;
        this.f$2 = context;
    }

    public final void run() {
        this.f$0.lambda$refreshUi$0(this.f$1, this.f$2);
    }
}
