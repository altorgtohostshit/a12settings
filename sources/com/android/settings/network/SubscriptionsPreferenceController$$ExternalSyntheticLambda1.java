package com.android.settings.network;

import androidx.preference.Preference;

public final /* synthetic */ class SubscriptionsPreferenceController$$ExternalSyntheticLambda1 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ SubscriptionsPreferenceController f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ SubscriptionsPreferenceController$$ExternalSyntheticLambda1(SubscriptionsPreferenceController subscriptionsPreferenceController, int i) {
        this.f$0 = subscriptionsPreferenceController;
        this.f$1 = i;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$updateForBase$2(this.f$1, preference);
    }
}
