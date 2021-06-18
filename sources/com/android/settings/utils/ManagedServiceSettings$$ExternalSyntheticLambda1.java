package com.android.settings.utils;

import com.android.settingslib.applications.ServiceListing;
import java.util.List;

public final /* synthetic */ class ManagedServiceSettings$$ExternalSyntheticLambda1 implements ServiceListing.Callback {
    public final /* synthetic */ ManagedServiceSettings f$0;

    public /* synthetic */ ManagedServiceSettings$$ExternalSyntheticLambda1(ManagedServiceSettings managedServiceSettings) {
        this.f$0 = managedServiceSettings;
    }

    public final void onServicesReloaded(List list) {
        this.f$0.updateList(list);
    }
}
