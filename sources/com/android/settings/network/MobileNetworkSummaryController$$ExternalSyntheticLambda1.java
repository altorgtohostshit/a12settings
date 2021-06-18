package com.android.settings.network;

import androidx.preference.Preference;
import java.util.List;

public final /* synthetic */ class MobileNetworkSummaryController$$ExternalSyntheticLambda1 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ MobileNetworkSummaryController f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ MobileNetworkSummaryController$$ExternalSyntheticLambda1(MobileNetworkSummaryController mobileNetworkSummaryController, List list) {
        this.f$0 = mobileNetworkSummaryController;
        this.f$1 = list;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$update$3(this.f$1, preference);
    }
}
