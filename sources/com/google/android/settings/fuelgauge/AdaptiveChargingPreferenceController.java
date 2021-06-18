package com.google.android.settings.fuelgauge;

import android.content.Context;
import android.content.IntentFilter;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.google.android.systemui.adaptivecharging.AdaptiveChargingManager;

public class AdaptiveChargingPreferenceController extends TogglePreferenceController {
    @VisibleForTesting
    AdaptiveChargingManager mAdaptiveChargingManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AdaptiveChargingPreferenceController(Context context, String str) {
        super(context, str);
        this.mAdaptiveChargingManager = new AdaptiveChargingManager(context);
    }

    public int getAvailabilityStatus() {
        return this.mAdaptiveChargingManager.isAvailable() ? 0 : 3;
    }

    public boolean isChecked() {
        return this.mAdaptiveChargingManager.isEnabled();
    }

    public boolean setChecked(boolean z) {
        this.mAdaptiveChargingManager.setEnabled(z);
        if (z) {
            return true;
        }
        this.mAdaptiveChargingManager.setAdaptiveChargingDeadline(-1);
        return true;
    }
}
