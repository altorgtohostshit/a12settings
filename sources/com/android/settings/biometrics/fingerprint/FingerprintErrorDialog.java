package com.android.settings.biometrics.fingerprint;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.android.settings.R;
import com.android.settings.biometrics.BiometricEnrollBase;
import com.android.settings.biometrics.BiometricErrorDialog;

public class FingerprintErrorDialog extends BiometricErrorDialog {
    private static int getErrorMessage(int i) {
        return i != 3 ? R.string.security_settings_fingerprint_enroll_error_generic_dialog_message : R.string.security_settings_fingerprint_enroll_error_timeout_dialog_message;
    }

    public int getMetricsCategory() {
        return 569;
    }

    public int getOkButtonTextResId() {
        return R.string.security_settings_fingerprint_enroll_dialog_ok;
    }

    public int getTitleResId() {
        return R.string.security_settings_fingerprint_enroll_error_dialog_title;
    }

    public static void showErrorDialog(BiometricEnrollBase biometricEnrollBase, int i) {
        FingerprintErrorDialog newInstance = newInstance(biometricEnrollBase.getText(getErrorMessage(i)), i);
        FragmentManager supportFragmentManager = biometricEnrollBase.getSupportFragmentManager();
        if (!supportFragmentManager.isDestroyed()) {
            newInstance.show(supportFragmentManager, FingerprintErrorDialog.class.getName());
        }
    }

    private static FingerprintErrorDialog newInstance(CharSequence charSequence, int i) {
        FingerprintErrorDialog fingerprintErrorDialog = new FingerprintErrorDialog();
        Bundle bundle = new Bundle();
        bundle.putCharSequence("error_msg", charSequence);
        bundle.putInt("error_id", i);
        fingerprintErrorDialog.setArguments(bundle);
        return fingerprintErrorDialog;
    }
}
