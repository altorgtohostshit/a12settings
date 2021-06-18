package com.android.settings.notification;

import androidx.preference.Preference;
import com.android.settingslib.applications.ApplicationsState;

/* renamed from: com.android.settings.notification.RecentNotifyingAppsPreferenceController$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C1075xfd336856 implements Preference.OnPreferenceChangeListener {
    public final /* synthetic */ RecentNotifyingAppsPreferenceController f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ApplicationsState.AppEntry f$2;

    public /* synthetic */ C1075xfd336856(RecentNotifyingAppsPreferenceController recentNotifyingAppsPreferenceController, String str, ApplicationsState.AppEntry appEntry) {
        this.f$0 = recentNotifyingAppsPreferenceController;
        this.f$1 = str;
        this.f$2 = appEntry;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        return this.f$0.lambda$displayRecentApps$3(this.f$1, this.f$2, preference, obj);
    }
}
