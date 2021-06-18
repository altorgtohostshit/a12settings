package com.android.settings.biometrics;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;

public class MultiBiometricEnrollHelper {
    private final FragmentActivity mActivity;
    private final long mGkPwHandle;
    private final boolean mRequestEnrollFace;
    private final boolean mRequestEnrollFingerprint;
    private final int mUserId;

    MultiBiometricEnrollHelper(FragmentActivity fragmentActivity, int i, boolean z, boolean z2, long j) {
        this.mActivity = fragmentActivity;
        this.mUserId = i;
        this.mGkPwHandle = j;
        this.mRequestEnrollFace = z;
        this.mRequestEnrollFingerprint = z2;
    }

    /* access modifiers changed from: package-private */
    public void startNextStep() {
        if (this.mRequestEnrollFace) {
            launchFaceEnroll();
        } else if (this.mRequestEnrollFingerprint) {
            launchFingerprintEnroll();
        } else {
            this.mActivity.setResult(2);
            this.mActivity.finish();
        }
    }

    private void launchFaceEnroll() {
        ((FaceManager) this.mActivity.getSystemService(FaceManager.class)).generateChallenge(this.mUserId, new MultiBiometricEnrollHelper$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$launchFaceEnroll$0(int i, int i2, long j) {
        byte[] requestGatekeeperHat = BiometricUtils.requestGatekeeperHat((Context) this.mActivity, this.mGkPwHandle, this.mUserId, j);
        FragmentActivity fragmentActivity = this.mActivity;
        Intent faceIntroIntent = BiometricUtils.getFaceIntroIntent(fragmentActivity, fragmentActivity.getIntent());
        faceIntroIntent.putExtra("sensor_id", i);
        faceIntroIntent.putExtra("challenge", j);
        if (this.mRequestEnrollFingerprint) {
            FragmentActivity fragmentActivity2 = this.mActivity;
            Intent fingerprintIntroIntent = BiometricUtils.getFingerprintIntroIntent(fragmentActivity2, fragmentActivity2.getIntent());
            fingerprintIntroIntent.putExtra("gk_pw_handle", this.mGkPwHandle);
            faceIntroIntent.putExtra("enroll_after_face", PendingIntent.getActivity(this.mActivity, 0, fingerprintIntroIntent, 201326592));
        }
        BiometricUtils.launchEnrollForResult(this.mActivity, faceIntroIntent, 3000, requestGatekeeperHat, Long.valueOf(this.mGkPwHandle), this.mUserId);
    }

    private void launchFingerprintEnroll() {
        ((FingerprintManager) this.mActivity.getSystemService(FingerprintManager.class)).generateChallenge(this.mUserId, new MultiBiometricEnrollHelper$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$launchFingerprintEnroll$1(int i, int i2, long j) {
        byte[] requestGatekeeperHat = BiometricUtils.requestGatekeeperHat((Context) this.mActivity, this.mGkPwHandle, this.mUserId, j);
        FragmentActivity fragmentActivity = this.mActivity;
        Intent fingerprintIntroIntent = BiometricUtils.getFingerprintIntroIntent(fragmentActivity, fragmentActivity.getIntent());
        fingerprintIntroIntent.putExtra("sensor_id", i);
        fingerprintIntroIntent.putExtra("challenge", j);
        BiometricUtils.launchEnrollForResult(this.mActivity, fingerprintIntroIntent, 3001, requestGatekeeperHat, Long.valueOf(this.mGkPwHandle), this.mUserId);
    }

    /* access modifiers changed from: package-private */
    public void onActivityResult(int i, int i2, Intent intent) {
        Log.d("MultiBiometricEnrollHelper", "RequestCode: " + i + " resultCode: " + i2);
        BiometricUtils.removeGatekeeperPasswordHandle((Context) this.mActivity, this.mGkPwHandle);
        this.mActivity.setResult(i2);
        this.mActivity.finish();
    }
}
