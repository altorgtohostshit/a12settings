package com.google.android.settings.connecteddevice.dock;

import java.util.function.Predicate;

public final /* synthetic */ class SavedDockUpdater$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ SavedDockUpdater f$0;

    public /* synthetic */ SavedDockUpdater$$ExternalSyntheticLambda0(SavedDockUpdater savedDockUpdater) {
        this.f$0 = savedDockUpdater;
    }

    public final boolean test(Object obj) {
        return this.f$0.hasDeviceBeenRemoved((String) obj);
    }
}
