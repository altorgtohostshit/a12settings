package com.android.settings.connecteddevice.dock;

import android.content.Context;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.overlay.DockUpdaterFeatureProvider;

public class DockUpdaterFeatureProviderImpl implements DockUpdaterFeatureProvider {
    public DockUpdater getConnectedDockUpdater(Context context, DevicePreferenceCallback devicePreferenceCallback) {
        return new DockUpdater() {
        };
    }

    public DockUpdater getSavedDockUpdater(Context context, DevicePreferenceCallback devicePreferenceCallback) {
        return new DockUpdater() {
        };
    }
}
