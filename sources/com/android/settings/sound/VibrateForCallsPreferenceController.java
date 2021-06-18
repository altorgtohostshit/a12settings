package com.android.settings.sound;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.DeviceConfig;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class VibrateForCallsPreferenceController extends BasePreferenceController {
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f104ON = 1;
    static final String RAMPING_RINGER_ENABLED = "ramping_ringer_enabled";

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

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public VibrateForCallsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return (!Utils.isVoiceCapable(this.mContext) || DeviceConfig.getBoolean("telephony", RAMPING_RINGER_ENABLED, false)) ? 3 : 0;
    }

    public CharSequence getSummary() {
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "apply_ramping_ringer", 0) == 1) {
            return this.mContext.getText(R.string.vibrate_when_ringing_option_ramping_ringer);
        }
        if (Settings.System.getInt(this.mContext.getContentResolver(), "vibrate_when_ringing", 0) == 1) {
            return this.mContext.getText(R.string.vibrate_when_ringing_option_always_vibrate);
        }
        return this.mContext.getText(R.string.vibrate_when_ringing_option_never_vibrate);
    }
}
