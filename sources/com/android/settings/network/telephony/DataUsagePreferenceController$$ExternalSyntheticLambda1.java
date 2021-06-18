package com.android.settings.network.telephony;

import com.android.settingslib.net.DataUsageController;
import java.util.concurrent.Callable;

public final /* synthetic */ class DataUsagePreferenceController$$ExternalSyntheticLambda1 implements Callable {
    public final /* synthetic */ DataUsagePreferenceController f$0;
    public final /* synthetic */ DataUsageController f$1;

    public /* synthetic */ DataUsagePreferenceController$$ExternalSyntheticLambda1(DataUsagePreferenceController dataUsagePreferenceController, DataUsageController dataUsageController) {
        this.f$0 = dataUsagePreferenceController;
        this.f$1 = dataUsageController;
    }

    public final Object call() {
        return this.f$0.lambda$getDataUsageSummary$1(this.f$1);
    }
}
