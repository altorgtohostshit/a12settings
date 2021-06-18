package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.UserHandle;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;

public class WakeDisplayPreferenceController extends AwareTogglePreferenceController {
    private AmbientDisplayConfiguration mAmbientConfig;
    private final int mUserId = UserHandle.myUserId();

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

    public WakeDisplayPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return (!this.mHelper.isGestureConfigurable() || !getAmbientConfig().alwaysOnEnabled(this.mUserId)) ? 5 : 0;
    }

    public boolean isChecked() {
        return getAmbientConfig().wakeDisplayGestureEnabled(this.mUserId);
    }

    public boolean setChecked(boolean z) {
        this.mHelper.writeFeatureEnabled("doze_wake_display_gesture", z);
        Settings.Secure.putInt(this.mContext.getContentResolver(), "doze_wake_display_gesture", z ? 1 : 0);
        return true;
    }

    public CharSequence getSummary() {
        return this.mContext.getText(getAmbientConfig().alwaysOnEnabled(this.mUserId) ? R.string.aware_wake_display_summary : R.string.aware_wake_display_summary_aod_off);
    }

    public void setConfig(AmbientDisplayConfiguration ambientDisplayConfiguration) {
        this.mAmbientConfig = ambientDisplayConfiguration;
    }

    private AmbientDisplayConfiguration getAmbientConfig() {
        if (this.mAmbientConfig == null) {
            this.mAmbientConfig = new AmbientDisplayConfiguration(this.mContext);
        }
        return this.mAmbientConfig;
    }
}
