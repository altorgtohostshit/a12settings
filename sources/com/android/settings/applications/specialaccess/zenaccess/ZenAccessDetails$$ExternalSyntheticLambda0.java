package com.android.settings.applications.specialaccess.zenaccess;

import androidx.preference.Preference;

public final /* synthetic */ class ZenAccessDetails$$ExternalSyntheticLambda0 implements Preference.OnPreferenceChangeListener {
    public final /* synthetic */ ZenAccessDetails f$0;
    public final /* synthetic */ CharSequence f$1;

    public /* synthetic */ ZenAccessDetails$$ExternalSyntheticLambda0(ZenAccessDetails zenAccessDetails, CharSequence charSequence) {
        this.f$0 = zenAccessDetails;
        this.f$1 = charSequence;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        return this.f$0.lambda$updatePreference$0(this.f$1, preference, obj);
    }
}
