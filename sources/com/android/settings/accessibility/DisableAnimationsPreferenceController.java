package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class DisableAnimationsPreferenceController extends TogglePreferenceController {
    static final String ANIMATION_OFF_VALUE = "0";
    static final String ANIMATION_ON_VALUE = "1";
    static final String[] TOGGLE_ANIMATION_TARGETS = {"window_animation_scale", "transition_animation_scale", "animator_duration_scale"};

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
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

    public DisableAnimationsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        for (String string : TOGGLE_ANIMATION_TARGETS) {
            if (!TextUtils.equals(Settings.Global.getString(this.mContext.getContentResolver(), string), ANIMATION_OFF_VALUE)) {
                return false;
            }
        }
        return true;
    }

    public boolean setChecked(boolean z) {
        String str = z ? ANIMATION_OFF_VALUE : ANIMATION_ON_VALUE;
        boolean z2 = true;
        for (String putString : TOGGLE_ANIMATION_TARGETS) {
            z2 &= Settings.Global.putString(this.mContext.getContentResolver(), putString, str);
        }
        return z2;
    }
}
