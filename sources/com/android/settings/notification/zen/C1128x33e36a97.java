package com.android.settings.notification.zen;

import androidx.preference.Preference;
import com.android.settingslib.applications.ApplicationsState;

/* renamed from: com.android.settings.notification.zen.ZenModeAddBypassingAppsPreferenceController$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C1128x33e36a97 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ ZenModeAddBypassingAppsPreferenceController f$0;
    public final /* synthetic */ ApplicationsState.AppEntry f$1;

    public /* synthetic */ C1128x33e36a97(ZenModeAddBypassingAppsPreferenceController zenModeAddBypassingAppsPreferenceController, ApplicationsState.AppEntry appEntry) {
        this.f$0 = zenModeAddBypassingAppsPreferenceController;
        this.f$1 = appEntry;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$updateAppList$0(this.f$1, preference);
    }
}
