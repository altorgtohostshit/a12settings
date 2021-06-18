package com.android.settings.utils;

import android.content.ComponentName;
import androidx.preference.Preference;

public final /* synthetic */ class ManagedServiceSettings$$ExternalSyntheticLambda0 implements Preference.OnPreferenceChangeListener {
    public final /* synthetic */ ManagedServiceSettings f$0;
    public final /* synthetic */ CharSequence f$1;
    public final /* synthetic */ ComponentName f$2;

    public /* synthetic */ ManagedServiceSettings$$ExternalSyntheticLambda0(ManagedServiceSettings managedServiceSettings, CharSequence charSequence, ComponentName componentName) {
        this.f$0 = managedServiceSettings;
        this.f$1 = charSequence;
        this.f$2 = componentName;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        return this.f$0.lambda$updateList$0(this.f$1, this.f$2, preference, obj);
    }
}
