package com.android.settings.deviceinfo;

import android.os.storage.VolumeRecord;
import com.android.settings.deviceinfo.StorageDashboardFragment;
import com.android.settings.deviceinfo.storage.StorageEntry;
import java.util.function.Predicate;

public final /* synthetic */ class StorageDashboardFragment$1$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ VolumeRecord f$0;

    public /* synthetic */ StorageDashboardFragment$1$$ExternalSyntheticLambda0(VolumeRecord volumeRecord) {
        this.f$0 = volumeRecord;
    }

    public final boolean test(Object obj) {
        return StorageDashboardFragment.C08581.lambda$onVolumeRecordChanged$1(this.f$0, (StorageEntry) obj);
    }
}
