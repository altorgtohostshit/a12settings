package com.android.settings.notification;

import android.os.Bundle;
import androidx.preference.Preference;
import com.android.settingslib.applications.ApplicationsState;

/* renamed from: com.android.settings.notification.RecentNotifyingAppsPreferenceController$$ExternalSyntheticLambda1 */
public final /* synthetic */ class C1076xfd336857 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ RecentNotifyingAppsPreferenceController f$0;
    public final /* synthetic */ Bundle f$1;
    public final /* synthetic */ ApplicationsState.AppEntry f$2;

    public /* synthetic */ C1076xfd336857(RecentNotifyingAppsPreferenceController recentNotifyingAppsPreferenceController, Bundle bundle, ApplicationsState.AppEntry appEntry) {
        this.f$0 = recentNotifyingAppsPreferenceController;
        this.f$1 = bundle;
        this.f$2 = appEntry;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$displayRecentApps$2(this.f$1, this.f$2, preference);
    }
}
