package com.android.settings.biometrics.combination;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.Settings;
import com.android.settings.slices.SliceBackgroundWorker;

public class CombinedBiometricProfileStatusPreferenceController extends CombinedBiometricStatusPreferenceController {
    private static final String KEY_BIOMETRIC_SETTINGS = "biometric_settings_profile";

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

    public CombinedBiometricProfileStatusPreferenceController(Context context) {
        super(context, KEY_BIOMETRIC_SETTINGS);
    }

    public CombinedBiometricProfileStatusPreferenceController(Context context, String str) {
        super(context, str);
    }

    /* access modifiers changed from: protected */
    public boolean isUserSupported() {
        int i = this.mProfileChallengeUserId;
        return i != -10000 && this.mLockPatternUtils.isSeparateProfileChallengeAllowed(i);
    }

    /* access modifiers changed from: protected */
    public int getUserId() {
        return this.mProfileChallengeUserId;
    }

    /* access modifiers changed from: protected */
    public String getSettingsClassName() {
        return Settings.CombinedBiometricProfileSettingsActivity.class.getName();
    }

    /* access modifiers changed from: protected */
    public String getEnrollClassName() {
        return Settings.CombinedBiometricProfileSettingsActivity.class.getName();
    }
}
