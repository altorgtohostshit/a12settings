package com.android.settings.deviceinfo;

import android.os.storage.VolumeInfo;
import java.util.function.Predicate;

public final /* synthetic */ class StorageDashboardFragment$$ExternalSyntheticLambda6 implements Predicate {
    public static final /* synthetic */ StorageDashboardFragment$$ExternalSyntheticLambda6 INSTANCE = new StorageDashboardFragment$$ExternalSyntheticLambda6();

    private /* synthetic */ StorageDashboardFragment$$ExternalSyntheticLambda6() {
    }

    public final boolean test(Object obj) {
        return StorageDashboardFragment.isInteresting((VolumeInfo) obj);
    }
}
