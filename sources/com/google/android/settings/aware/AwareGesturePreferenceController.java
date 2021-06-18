package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;

public abstract class AwareGesturePreferenceController extends AwareBasePreferenceController {
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    /* access modifiers changed from: protected */
    public abstract CharSequence getGestureSummary();

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AwareGesturePreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        if (!SystemProperties.getBoolean("ro.vendor.aware_available", false)) {
            return 3;
        }
        if (this.mHelper.isAirplaneModeOn() || this.mHelper.isBatterySaverModeOn()) {
            return 5;
        }
        return 0;
    }

    public CharSequence getSummary() {
        boolean isBatterySaverModeOn = this.mHelper.isBatterySaverModeOn();
        boolean isAirplaneModeOn = this.mHelper.isAirplaneModeOn();
        if (!isBatterySaverModeOn && !isAirplaneModeOn) {
            return getGestureSummary();
        }
        return this.mContext.getText((!isBatterySaverModeOn || !isAirplaneModeOn) ? isBatterySaverModeOn ? R.string.aware_summary_when_batterysaver_on : isAirplaneModeOn ? R.string.aware_summary_when_airplane_on : 0 : R.string.aware_summary_when_airplane_batterysaver_on);
    }
}
