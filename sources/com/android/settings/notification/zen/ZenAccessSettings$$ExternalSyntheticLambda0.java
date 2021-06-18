package com.android.settings.notification.zen;

import android.content.pm.ApplicationInfo;
import androidx.preference.Preference;

public final /* synthetic */ class ZenAccessSettings$$ExternalSyntheticLambda0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ ZenAccessSettings f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ApplicationInfo f$2;

    public /* synthetic */ ZenAccessSettings$$ExternalSyntheticLambda0(ZenAccessSettings zenAccessSettings, String str, ApplicationInfo applicationInfo) {
        this.f$0 = zenAccessSettings;
        this.f$1 = str;
        this.f$2 = applicationInfo;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$reloadList$0(this.f$1, this.f$2, preference);
    }
}
