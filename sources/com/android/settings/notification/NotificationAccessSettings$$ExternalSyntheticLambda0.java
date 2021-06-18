package com.android.settings.notification;

import android.content.ComponentName;
import android.content.pm.ServiceInfo;
import androidx.preference.Preference;

public final /* synthetic */ class NotificationAccessSettings$$ExternalSyntheticLambda0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ NotificationAccessSettings f$0;
    public final /* synthetic */ ComponentName f$1;
    public final /* synthetic */ ServiceInfo f$2;

    public /* synthetic */ NotificationAccessSettings$$ExternalSyntheticLambda0(NotificationAccessSettings notificationAccessSettings, ComponentName componentName, ServiceInfo serviceInfo) {
        this.f$0 = notificationAccessSettings;
        this.f$1 = componentName;
        this.f$2 = serviceInfo;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$updateList$0(this.f$1, this.f$2, preference);
    }
}
