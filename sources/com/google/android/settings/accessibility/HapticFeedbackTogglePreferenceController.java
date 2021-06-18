package com.google.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.slices.SliceBackgroundWorker;

public class HapticFeedbackTogglePreferenceController extends VibrationTogglePreferenceController {
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
    public int getPreviewVibrationAudioAttributesUsage() {
        return 13;
    }

    /* access modifiers changed from: protected */
    public String getVibrationEnabledSetting() {
        return "haptic_feedback_enabled";
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

    public HapticFeedbackTogglePreferenceController(Context context, String str) {
        super(context, str, "haptic_feedback_intensity");
    }

    /* access modifiers changed from: protected */
    public int getDefaultIntensity() {
        return this.mVibrator.getDefaultHapticFeedbackIntensity();
    }

    /* access modifiers changed from: protected */
    public void saveFakePreference() {
        this.mSharedPrefs.getSharedPreferences().edit().putInt(this.mFakePositionSetting, Settings.System.getInt(this.mContext.getContentResolver(), "haptic_feedback_enabled", 1)).apply();
    }
}
