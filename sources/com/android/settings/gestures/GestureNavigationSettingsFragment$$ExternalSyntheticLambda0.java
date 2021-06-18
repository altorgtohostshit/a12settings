package com.android.settings.gestures;

import androidx.preference.Preference;

public final /* synthetic */ class GestureNavigationSettingsFragment$$ExternalSyntheticLambda0 implements Preference.OnPreferenceChangeListener {
    public final /* synthetic */ GestureNavigationSettingsFragment f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ GestureNavigationSettingsFragment$$ExternalSyntheticLambda0(GestureNavigationSettingsFragment gestureNavigationSettingsFragment, String str) {
        this.f$0 = gestureNavigationSettingsFragment;
        this.f$1 = str;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        return this.f$0.lambda$initSeekBarPreference$0(this.f$1, preference, obj);
    }
}
