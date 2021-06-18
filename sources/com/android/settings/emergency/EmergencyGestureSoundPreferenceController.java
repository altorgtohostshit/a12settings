package com.android.settings.emergency;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.emergencynumber.EmergencyNumberUtils;

public class EmergencyGestureSoundPreferenceController extends TogglePreferenceController {
    EmergencyNumberUtils mEmergencyNumberUtils;

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

    public boolean isSliceable() {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public EmergencyGestureSoundPreferenceController(Context context, String str) {
        super(context, str);
        this.mEmergencyNumberUtils = new EmergencyNumberUtils(context);
    }

    private static boolean isGestureAvailable(Context context) {
        return context.getResources().getBoolean(R.bool.config_show_emergency_gesture_settings);
    }

    public int getAvailabilityStatus() {
        return isGestureAvailable(this.mContext) ? 0 : 3;
    }

    public boolean isChecked() {
        return this.mEmergencyNumberUtils.getEmergencyGestureSoundEnabled();
    }

    public boolean setChecked(boolean z) {
        this.mEmergencyNumberUtils.setEmergencySoundEnabled(z);
        return true;
    }
}
