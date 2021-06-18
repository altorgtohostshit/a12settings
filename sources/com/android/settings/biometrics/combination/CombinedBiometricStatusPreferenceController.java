package com.android.settings.biometrics.combination;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricStatusPreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class CombinedBiometricStatusPreferenceController extends BiometricStatusPreferenceController {
    private static final String KEY_BIOMETRIC_SETTINGS = "biometric_settings";
    FaceManager mFaceManager;
    FingerprintManager mFingerprintManager;

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

    /* access modifiers changed from: protected */
    public boolean hasEnrolledBiometrics() {
        return false;
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

    public CombinedBiometricStatusPreferenceController(Context context) {
        this(context, KEY_BIOMETRIC_SETTINGS);
    }

    public CombinedBiometricStatusPreferenceController(Context context, String str) {
        super(context, str);
        this.mFingerprintManager = Utils.getFingerprintManagerOrNull(context);
        this.mFaceManager = Utils.getFaceManagerOrNull(context);
    }

    /* access modifiers changed from: protected */
    public boolean isDeviceSupported() {
        return Utils.hasFingerprintHardware(this.mContext) && Utils.hasFaceHardware(this.mContext);
    }

    /* access modifiers changed from: protected */
    public String getSummaryTextEnrolled() {
        return this.mContext.getString(R.string.security_settings_biometric_preference_summary_none_enrolled);
    }

    /* access modifiers changed from: protected */
    public String getSummaryTextNoneEnrolled() {
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        int size = fingerprintManager != null ? fingerprintManager.getEnrolledFingerprints(getUserId()).size() : 0;
        FaceManager faceManager = this.mFaceManager;
        boolean z = faceManager != null && faceManager.hasEnrolledTemplates(getUserId());
        if (z && size > 1) {
            return this.mContext.getString(R.string.security_settings_biometric_preference_summary_both_fp_multiple);
        }
        if (z && size == 1) {
            return this.mContext.getString(R.string.security_settings_biometric_preference_summary_both_fp_single);
        }
        if (z) {
            return this.mContext.getString(R.string.security_settings_face_preference_summary);
        }
        if (size <= 0) {
            return this.mContext.getString(R.string.security_settings_biometric_preference_summary_none_enrolled);
        }
        return this.mContext.getResources().getQuantityString(R.plurals.security_settings_fingerprint_preference_summary, size, new Object[]{Integer.valueOf(size)});
    }

    /* access modifiers changed from: protected */
    public String getSettingsClassName() {
        return Settings.CombinedBiometricSettingsActivity.class.getName();
    }

    /* access modifiers changed from: protected */
    public String getEnrollClassName() {
        return Settings.CombinedBiometricSettingsActivity.class.getName();
    }
}
