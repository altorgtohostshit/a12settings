package com.android.settings.deviceinfo;

import android.os.storage.VolumeRecord;
import java.util.function.Predicate;

public final /* synthetic */ class StorageDashboardFragment$$ExternalSyntheticLambda4 implements Predicate {
    public final /* synthetic */ StorageDashboardFragment f$0;

    public /* synthetic */ StorageDashboardFragment$$ExternalSyntheticLambda4(StorageDashboardFragment storageDashboardFragment) {
        this.f$0 = storageDashboardFragment;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$onResume$5((VolumeRecord) obj);
    }
}
