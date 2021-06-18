package com.android.settings.sound;

import androidx.preference.Preference;
import java.util.Set;

public final /* synthetic */ class ResumableMediaAppsController$$ExternalSyntheticLambda0 implements Preference.OnPreferenceChangeListener {
    public final /* synthetic */ ResumableMediaAppsController f$0;
    public final /* synthetic */ Set f$1;

    public /* synthetic */ ResumableMediaAppsController$$ExternalSyntheticLambda0(ResumableMediaAppsController resumableMediaAppsController, Set set) {
        this.f$0 = resumableMediaAppsController;
        this.f$1 = set;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        return this.f$0.lambda$displayPreference$0(this.f$1, preference, obj);
    }
}
