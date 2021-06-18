package com.android.settings.deviceinfo;

import android.os.storage.DiskInfo;
import java.util.function.Predicate;

public final /* synthetic */ class StorageDashboardFragment$$ExternalSyntheticLambda5 implements Predicate {
    public static final /* synthetic */ StorageDashboardFragment$$ExternalSyntheticLambda5 INSTANCE = new StorageDashboardFragment$$ExternalSyntheticLambda5();

    private /* synthetic */ StorageDashboardFragment$$ExternalSyntheticLambda5() {
    }

    public final boolean test(Object obj) {
        return StorageDashboardFragment.isDiskUnsupported((DiskInfo) obj);
    }
}
