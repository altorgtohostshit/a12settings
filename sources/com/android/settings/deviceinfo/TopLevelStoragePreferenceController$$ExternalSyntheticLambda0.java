package com.android.settings.deviceinfo;

import androidx.preference.Preference;

public final /* synthetic */ class TopLevelStoragePreferenceController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TopLevelStoragePreferenceController f$0;
    public final /* synthetic */ Preference f$1;

    public /* synthetic */ TopLevelStoragePreferenceController$$ExternalSyntheticLambda0(TopLevelStoragePreferenceController topLevelStoragePreferenceController, Preference preference) {
        this.f$0 = topLevelStoragePreferenceController;
        this.f$1 = preference;
    }

    public final void run() {
        this.f$0.lambda$refreshSummaryThread$1(this.f$1);
    }
}
