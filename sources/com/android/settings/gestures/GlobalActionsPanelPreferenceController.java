package com.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.slices.SliceBackgroundWorker;

public class GlobalActionsPanelPreferenceController extends GesturePreferenceController {
    @VisibleForTesting
    protected static final String AVAILABLE_SETTING = "global_actions_panel_available";
    @VisibleForTesting
    protected static final String ENABLED_SETTING = "global_actions_panel_enabled";
    private static final String PREF_KEY_VIDEO = "global_actions_panel_video";
    @VisibleForTesting
    protected static final String TOGGLE_KEY = "gesture_global_actions_panel_switch";

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    /* access modifiers changed from: protected */
    public String getVideoPrefKey() {
        return PREF_KEY_VIDEO;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlobalActionsPanelPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), AVAILABLE_SETTING, 0) == 1 ? 0 : 2;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), ENABLED_SETTING, z ? 1 : 0);
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), TOGGLE_KEY);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), ENABLED_SETTING, 0) == 1;
    }
}
