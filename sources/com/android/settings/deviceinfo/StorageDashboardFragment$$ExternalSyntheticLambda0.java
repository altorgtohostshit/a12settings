package com.android.settings.deviceinfo;

import com.android.settings.deviceinfo.storage.StorageEntry;
import com.android.settings.deviceinfo.storage.StorageSelectionPreferenceController;

public final /* synthetic */ class StorageDashboardFragment$$ExternalSyntheticLambda0 implements StorageSelectionPreferenceController.OnItemSelectedListener {
    public final /* synthetic */ StorageDashboardFragment f$0;

    public /* synthetic */ StorageDashboardFragment$$ExternalSyntheticLambda0(StorageDashboardFragment storageDashboardFragment) {
        this.f$0 = storageDashboardFragment;
    }

    public final void onItemSelected(StorageEntry storageEntry) {
        this.f$0.lambda$onAttach$0(storageEntry);
    }
}
