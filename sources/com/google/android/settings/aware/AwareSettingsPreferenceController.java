package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.util.FeatureFlagUtils;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;

public class AwareSettingsPreferenceController extends AwareGesturePreferenceController {
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

    public AwareSettingsPreferenceController(Context context, String str) {
        super(context, str);
    }

    /* access modifiers changed from: protected */
    public CharSequence getGestureSummary() {
        if (FeatureFlagUtils.isEnabled(this.mContext, "settings_silky_home")) {
            return null;
        }
        return this.mContext.getText(this.mHelper.isEnabled() ? R.string.aware_settings_summary : R.string.gesture_setting_off);
    }
}
