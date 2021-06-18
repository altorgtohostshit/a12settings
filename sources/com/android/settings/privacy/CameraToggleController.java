package com.android.settings.privacy;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.DeviceConfig;
import com.android.settings.slices.SliceBackgroundWorker;

public class CameraToggleController extends SensorToggleController {
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSensor() {
        return 2;
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

    public CameraToggleController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        if (!this.mSensorPrivacyManagerHelper.supportsSensorToggle(getSensor()) || !DeviceConfig.getBoolean("privacy", "camera_toggle_enabled", true)) {
            return 3;
        }
        return 1;
    }
}
