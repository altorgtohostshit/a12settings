package com.android.settings.notification.app;

import android.os.Bundle;
import androidx.preference.Preference;

/* renamed from: com.android.settings.notification.app.AppChannelsBypassingDndPreferenceController$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C1099x5981a397 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ AppChannelsBypassingDndPreferenceController f$0;
    public final /* synthetic */ Bundle f$1;

    public /* synthetic */ C1099x5981a397(AppChannelsBypassingDndPreferenceController appChannelsBypassingDndPreferenceController, Bundle bundle) {
        this.f$0 = appChannelsBypassingDndPreferenceController;
        this.f$1 = bundle;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$populateList$0(this.f$1, preference);
    }
}
