package com.android.settings.deviceinfo;

import androidx.preference.Preference;
import com.android.settingslib.deviceinfo.PrivateStorageInfo;
import java.text.NumberFormat;

public final /* synthetic */ class TopLevelStoragePreferenceController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ TopLevelStoragePreferenceController f$0;
    public final /* synthetic */ Preference f$1;
    public final /* synthetic */ NumberFormat f$2;
    public final /* synthetic */ double f$3;
    public final /* synthetic */ PrivateStorageInfo f$4;

    public /* synthetic */ TopLevelStoragePreferenceController$$ExternalSyntheticLambda1(TopLevelStoragePreferenceController topLevelStoragePreferenceController, Preference preference, NumberFormat numberFormat, double d, PrivateStorageInfo privateStorageInfo) {
        this.f$0 = topLevelStoragePreferenceController;
        this.f$1 = preference;
        this.f$2 = numberFormat;
        this.f$3 = d;
        this.f$4 = privateStorageInfo;
    }

    public final void run() {
        this.f$0.lambda$refreshSummaryThread$0(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
