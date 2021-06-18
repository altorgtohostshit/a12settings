package com.android.settings.applications;

import android.view.View;
import com.android.settingslib.applications.ApplicationsState;

public final /* synthetic */ class RecentAppsPreferenceController$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ RecentAppsPreferenceController f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ApplicationsState.AppEntry f$2;

    public /* synthetic */ RecentAppsPreferenceController$$ExternalSyntheticLambda1(RecentAppsPreferenceController recentAppsPreferenceController, String str, ApplicationsState.AppEntry appEntry) {
        this.f$0 = recentAppsPreferenceController;
        this.f$1 = str;
        this.f$2 = appEntry;
    }

    public final void onClick(View view) {
        this.f$0.lambda$createAppEntity$1(this.f$1, this.f$2, view);
    }
}
