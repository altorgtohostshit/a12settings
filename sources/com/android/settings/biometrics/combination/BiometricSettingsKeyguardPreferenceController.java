package com.android.settings.biometrics.combination;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.Utils;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;

public class BiometricSettingsKeyguardPreferenceController extends TogglePreferenceController {
    private static final int DEFAULT = 1;
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f57ON = 1;
    private int mUserId;

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

    public BiometricSettingsKeyguardPreferenceController(Context context, String str) {
        super(context, str);
    }

    /* access modifiers changed from: protected */
    public RestrictedLockUtils.EnforcedAdmin getRestrictingAdmin() {
        return RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(this.mContext, 416, this.mUserId);
    }

    public void setUserId(int i) {
        this.mUserId = i;
    }

    public boolean isChecked() {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "biometric_keyguard_enabled", 1, this.mUserId) == 1;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "biometric_keyguard_enabled", z ? 1 : 0, this.mUserId);
    }

    public int getAvailabilityStatus() {
        if (!Utils.isMultipleBiometricsSupported(this.mContext)) {
            return 3;
        }
        return getRestrictingAdmin() != null ? 4 : 0;
    }
}
