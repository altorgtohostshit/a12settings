package com.android.settings.notification.zen;

import androidx.preference.Preference;
import com.android.settingslib.applications.ApplicationsState;

/* renamed from: com.android.settings.notification.zen.ZenModeAllBypassingAppsPreferenceController$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C1130x37aec997 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ ZenModeAllBypassingAppsPreferenceController f$0;
    public final /* synthetic */ ApplicationsState.AppEntry f$1;

    public /* synthetic */ C1130x37aec997(ZenModeAllBypassingAppsPreferenceController zenModeAllBypassingAppsPreferenceController, ApplicationsState.AppEntry appEntry) {
        this.f$0 = zenModeAllBypassingAppsPreferenceController;
        this.f$1 = appEntry;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$updateAppList$0(this.f$1, preference);
    }
}
