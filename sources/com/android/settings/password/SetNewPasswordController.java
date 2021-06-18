package com.android.settings.password;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.UserManager;
import com.android.internal.util.Preconditions;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.Utils;

final class SetNewPasswordController {
    private final DevicePolicyManager mDevicePolicyManager;
    private final FaceManager mFaceManager;
    private final FingerprintManager mFingerprintManager;
    private final PackageManager mPackageManager;
    private final int mTargetUserId;
    private final C1223Ui mUi;

    /* renamed from: com.android.settings.password.SetNewPasswordController$Ui */
    interface C1223Ui {
        void launchChooseLock(Bundle bundle);
    }

    public static SetNewPasswordController create(Context context, C1223Ui ui, Intent intent, IBinder iBinder) {
        int i;
        int currentUser = ActivityManager.getCurrentUser();
        if ("android.app.action.SET_NEW_PASSWORD".equals(intent.getAction())) {
            int identifier = Utils.getSecureTargetUser(iBinder, UserManager.get(context), (Bundle) null, intent.getExtras()).getIdentifier();
            if (new LockPatternUtils(context).isSeparateProfileChallengeAllowed(identifier)) {
                i = identifier;
                return new SetNewPasswordController(i, context.getPackageManager(), Utils.getFingerprintManagerOrNull(context), Utils.getFaceManagerOrNull(context), (DevicePolicyManager) context.getSystemService("device_policy"), ui);
            }
        }
        i = currentUser;
        return new SetNewPasswordController(i, context.getPackageManager(), Utils.getFingerprintManagerOrNull(context), Utils.getFaceManagerOrNull(context), (DevicePolicyManager) context.getSystemService("device_policy"), ui);
    }

    SetNewPasswordController(int i, PackageManager packageManager, FingerprintManager fingerprintManager, FaceManager faceManager, DevicePolicyManager devicePolicyManager, C1223Ui ui) {
        this.mTargetUserId = i;
        this.mPackageManager = (PackageManager) Preconditions.checkNotNull(packageManager);
        this.mFingerprintManager = fingerprintManager;
        this.mFaceManager = faceManager;
        this.mDevicePolicyManager = (DevicePolicyManager) Preconditions.checkNotNull(devicePolicyManager);
        this.mUi = (C1223Ui) Preconditions.checkNotNull(ui);
    }

    public void dispatchSetNewPasswordIntent() {
        Bundle bundle;
        boolean hasSystemFeature = this.mPackageManager.hasSystemFeature("android.hardware.fingerprint");
        boolean hasSystemFeature2 = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.face");
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        boolean z = true;
        boolean z2 = fingerprintManager != null && fingerprintManager.isHardwareDetected() && !this.mFingerprintManager.hasEnrolledFingerprints(this.mTargetUserId) && !isFingerprintDisabledByAdmin();
        FaceManager faceManager = this.mFaceManager;
        if (faceManager == null || !faceManager.isHardwareDetected() || this.mFaceManager.hasEnrolledTemplates(this.mTargetUserId) || isFaceDisabledByAdmin()) {
            z = false;
        }
        if (hasSystemFeature2 && z && hasSystemFeature && z2) {
            bundle = getBiometricChooseLockExtras();
        } else if (hasSystemFeature2 && z) {
            bundle = getFaceChooseLockExtras();
        } else if (!hasSystemFeature || !z2) {
            bundle = new Bundle();
        } else {
            bundle = getFingerprintChooseLockExtras();
        }
        bundle.putInt("android.intent.extra.USER_ID", this.mTargetUserId);
        this.mUi.launchChooseLock(bundle);
    }

    private Bundle getBiometricChooseLockExtras() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("hide_insecure_options", true);
        bundle.putBoolean("request_gk_pw_handle", true);
        bundle.putBoolean("for_biometrics", true);
        return bundle;
    }

    private Bundle getFingerprintChooseLockExtras() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("hide_insecure_options", true);
        bundle.putBoolean("request_gk_pw_handle", true);
        bundle.putBoolean("for_fingerprint", true);
        return bundle;
    }

    private Bundle getFaceChooseLockExtras() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("hide_insecure_options", true);
        bundle.putBoolean("request_gk_pw_handle", true);
        bundle.putBoolean("for_face", true);
        return bundle;
    }

    private boolean isFingerprintDisabledByAdmin() {
        return (this.mDevicePolicyManager.getKeyguardDisabledFeatures((ComponentName) null, this.mTargetUserId) & 32) != 0;
    }

    private boolean isFaceDisabledByAdmin() {
        return (this.mDevicePolicyManager.getKeyguardDisabledFeatures((ComponentName) null, this.mTargetUserId) & 128) != 0;
    }
}
