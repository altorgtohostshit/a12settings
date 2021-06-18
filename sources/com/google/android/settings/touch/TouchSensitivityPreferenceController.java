package com.google.android.settings.touch;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class TouchSensitivityPreferenceController extends TogglePreferenceController {
    public static final int OFF = 0;

    /* renamed from: ON */
    public static final int f129ON = 1;
    public static final String TOUCH_SENSITIVITY_ENABLED = "touch_sensitivity_enabled";
    public static final String TOUCH_SENSITIVITY_PROP = "debug.touch_sensitivity_mode";
    private Fragment mParent;

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

    public TouchSensitivityPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_touch_sensitivity) ? 0 : 3;
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), TOUCH_SENSITIVITY_ENABLED, 0) == 1;
    }

    public boolean setChecked(boolean z) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), TOUCH_SENSITIVITY_ENABLED, z ? 1 : 0);
        SystemProperties.set(TOUCH_SENSITIVITY_PROP, z ? "1" : "0");
        return true;
    }
}
