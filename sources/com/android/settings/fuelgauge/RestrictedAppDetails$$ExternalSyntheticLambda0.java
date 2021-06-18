package com.android.settings.fuelgauge;

import androidx.preference.Preference;
import com.android.settings.fuelgauge.batterytip.AppInfo;

public final /* synthetic */ class RestrictedAppDetails$$ExternalSyntheticLambda0 implements Preference.OnPreferenceChangeListener {
    public final /* synthetic */ RestrictedAppDetails f$0;
    public final /* synthetic */ AppInfo f$1;

    public /* synthetic */ RestrictedAppDetails$$ExternalSyntheticLambda0(RestrictedAppDetails restrictedAppDetails, AppInfo appInfo) {
        this.f$0 = restrictedAppDetails;
        this.f$1 = appInfo;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        return this.f$0.lambda$refreshUi$0(this.f$1, preference, obj);
    }
}
