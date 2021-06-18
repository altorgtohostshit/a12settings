package com.android.settings.applications;

import androidx.preference.Preference;
import com.android.settingslib.applications.ApplicationsState;

public final /* synthetic */ class AppsPreferenceController$$ExternalSyntheticLambda0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ AppsPreferenceController f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ApplicationsState.AppEntry f$2;

    public /* synthetic */ AppsPreferenceController$$ExternalSyntheticLambda0(AppsPreferenceController appsPreferenceController, String str, ApplicationsState.AppEntry appEntry) {
        this.f$0 = appsPreferenceController;
        this.f$1 = str;
        this.f$2 = appEntry;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$displayRecentApps$0(this.f$1, this.f$2, preference);
    }
}
