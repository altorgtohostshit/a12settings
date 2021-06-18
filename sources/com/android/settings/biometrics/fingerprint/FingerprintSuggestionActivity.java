package com.android.settings.biometrics.fingerprint;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import com.android.settings.R;
import com.android.settings.Utils;

public class FingerprintSuggestionActivity extends SetupFingerprintEnrollIntroduction {
    /* access modifiers changed from: protected */
    public void initViews() {
        super.initViews();
        getCancelButton().setText(this, R.string.security_settings_fingerprint_enroll_introduction_cancel);
    }

    public void finish() {
        setResult(0);
        super.finish();
    }

    public static boolean isSuggestionComplete(Context context) {
        return !Utils.hasFingerprintHardware(context) || !isFingerprintEnabled(context) || isNotSingleFingerprintEnrolled(context);
    }

    private static boolean isNotSingleFingerprintEnrolled(Context context) {
        FingerprintManager fingerprintManagerOrNull = Utils.getFingerprintManagerOrNull(context);
        if (fingerprintManagerOrNull == null || fingerprintManagerOrNull.getEnrolledFingerprints().size() != 1) {
            return true;
        }
        return false;
    }

    static boolean isFingerprintEnabled(Context context) {
        return (((DevicePolicyManager) context.getSystemService("device_policy")).getKeyguardDisabledFeatures((ComponentName) null, context.getUserId()) & 32) == 0;
    }
}
