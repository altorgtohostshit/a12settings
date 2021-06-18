package com.android.settings.biometrics.combination;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.provider.Settings;
import com.android.settings.Utils;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;

public class BiometricSettingsAppPreferenceController extends TogglePreferenceController {
    private static final int DEFAULT = 1;
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f56ON = 1;
    private FaceManager mFaceManager;
    private FingerprintManager mFingerprintManager;
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

    public BiometricSettingsAppPreferenceController(Context context, String str) {
        super(context, str);
        this.mFaceManager = Utils.getFaceManagerOrNull(context);
        this.mFingerprintManager = Utils.getFingerprintManagerOrNull(context);
    }

    /* access modifiers changed from: protected */
    public RestrictedLockUtils.EnforcedAdmin getRestrictingAdmin() {
        return RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(this.mContext, 416, this.mUserId);
    }

    public void setUserId(int i) {
        this.mUserId = i;
    }

    public boolean isChecked() {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "biometric_app_enabled", 1, this.mUserId) == 1;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "biometric_app_enabled", z ? 1 : 0, this.mUserId);
    }

    public int getAvailabilityStatus() {
        if (!Utils.isMultipleBiometricsSupported(this.mContext)) {
            return 3;
        }
        FaceManager faceManager = this.mFaceManager;
        if (faceManager == null || this.mFingerprintManager == null) {
            return 1;
        }
        boolean hasEnrolledTemplates = faceManager.hasEnrolledTemplates(this.mUserId);
        boolean hasEnrolledTemplates2 = this.mFingerprintManager.hasEnrolledTemplates(this.mUserId);
        if (hasEnrolledTemplates || hasEnrolledTemplates2) {
            return 0;
        }
        return 1;
    }
}
