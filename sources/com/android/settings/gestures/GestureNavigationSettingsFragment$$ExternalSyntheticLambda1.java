package com.android.settings.gestures;

import androidx.preference.Preference;

public final /* synthetic */ class GestureNavigationSettingsFragment$$ExternalSyntheticLambda1 implements Preference.OnPreferenceChangeListener {
    public final /* synthetic */ GestureNavigationSettingsFragment f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ GestureNavigationSettingsFragment$$ExternalSyntheticLambda1(GestureNavigationSettingsFragment gestureNavigationSettingsFragment, String str, String str2) {
        this.f$0 = gestureNavigationSettingsFragment;
        this.f$1 = str;
        this.f$2 = str2;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        return this.f$0.lambda$initSeekBarPreference$1(this.f$1, this.f$2, preference, obj);
    }
}
