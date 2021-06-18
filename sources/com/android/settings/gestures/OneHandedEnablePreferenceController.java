package com.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.SettingsMainSwitchPreferenceController;

public class OneHandedEnablePreferenceController extends SettingsMainSwitchPreferenceController {
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

    public OneHandedEnablePreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return OneHandedSettingsUtils.isSupportOneHandedMode() ? 0 : 3;
    }

    public boolean setChecked(boolean z) {
        OneHandedSettingsUtils.setOneHandedModeEnabled(this.mContext, z);
        OneHandedSettingsUtils.setSwipeDownNotificationEnabled(this.mContext, !z);
        return true;
    }

    public boolean isChecked() {
        return OneHandedSettingsUtils.isOneHandedModeEnabled(this.mContext);
    }

    public CharSequence getSummary() {
        return this.mContext.getText(isChecked() ? R.string.gesture_setting_on : R.string.gesture_setting_off);
    }
}
