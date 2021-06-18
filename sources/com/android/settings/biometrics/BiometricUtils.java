package com.android.settings.biometrics;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.storage.StorageManager;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.settings.R;
import com.android.settings.SetupWizardUtils;
import com.android.settings.biometrics.BiometricEnrollActivity;
import com.android.settings.biometrics.fingerprint.FingerprintEnrollFindSensor;
import com.android.settings.biometrics.fingerprint.FingerprintEnrollIntroduction;
import com.android.settings.biometrics.fingerprint.SetupFingerprintEnrollIntroduction;
import com.android.settings.password.ChooseLockGeneric;
import com.android.settings.password.SetupChooseLockGeneric;
import com.google.android.setupcompat.util.WizardManagerHelper;

public class BiometricUtils {
    public static byte[] requestGatekeeperHat(Context context, Intent intent, int i, long j) {
        if (containsGatekeeperPasswordHandle(intent)) {
            return requestGatekeeperHat(context, intent.getLongExtra("gk_pw_handle", 0), i, j);
        }
        throw new IllegalStateException("Gatekeeper Password is missing!!");
    }

    public static byte[] requestGatekeeperHat(Context context, long j, int i, long j2) {
        VerifyCredentialResponse verifyGatekeeperPasswordHandle = new LockPatternUtils(context).verifyGatekeeperPasswordHandle(j, j2, i);
        if (verifyGatekeeperPasswordHandle.isMatched()) {
            return verifyGatekeeperPasswordHandle.getGatekeeperHAT();
        }
        throw new IllegalStateException("Unable to request Gatekeeper HAT");
    }

    public static boolean containsGatekeeperPasswordHandle(Intent intent) {
        return intent != null && intent.hasExtra("gk_pw_handle");
    }

    public static long getGatekeeperPasswordHandle(Intent intent) {
        return intent.getLongExtra("gk_pw_handle", 0);
    }

    public static void removeGatekeeperPasswordHandle(Context context, Intent intent) {
        if (intent != null && containsGatekeeperPasswordHandle(intent)) {
            removeGatekeeperPasswordHandle(context, getGatekeeperPasswordHandle(intent));
        }
    }

    public static void removeGatekeeperPasswordHandle(Context context, long j) {
        new LockPatternUtils(context).removeGatekeeperPasswordHandle(j);
        Log.d("BiometricUtils", "Removed handle");
    }

    public static Intent getChooseLockIntent(Context context, Intent intent) {
        if (!WizardManagerHelper.isAnySetupWizard(intent)) {
            return new Intent(context, ChooseLockGeneric.class);
        }
        Intent intent2 = new Intent(context, SetupChooseLockGeneric.class);
        if (StorageManager.isFileEncryptedNativeOrEmulated()) {
            intent2.putExtra("lockscreen.password_type", 131072);
            intent2.putExtra("show_options_button", true);
        }
        WizardManagerHelper.copyWizardManagerExtras(intent, intent2);
        return intent2;
    }

    public static Intent getFingerprintFindSensorIntent(Context context, Intent intent) {
        Intent intent2 = new Intent(context, FingerprintEnrollFindSensor.class);
        SetupWizardUtils.copySetupExtras(intent, intent2);
        return intent2;
    }

    public static Intent getFingerprintIntroIntent(Context context, Intent intent) {
        if (!WizardManagerHelper.isAnySetupWizard(intent)) {
            return new Intent(context, FingerprintEnrollIntroduction.class);
        }
        Intent intent2 = new Intent(context, SetupFingerprintEnrollIntroduction.class);
        WizardManagerHelper.copyWizardManagerExtras(intent, intent2);
        return intent2;
    }

    public static Intent getFaceIntroIntent(Context context, Intent intent) {
        String string = context.getString(R.string.config_face_enroll_introduction);
        Intent intent2 = new Intent();
        intent2.setClassName("com.android.settings", string);
        WizardManagerHelper.copyWizardManagerExtras(intent, intent2);
        return intent2;
    }

    public static void launchEnrollForResult(FragmentActivity fragmentActivity, Intent intent, int i, byte[] bArr, Long l, int i2) {
        if (bArr != null) {
            intent.putExtra("hw_auth_token", bArr);
        }
        if (l != null) {
            intent.putExtra("gk_pw_handle", l.longValue());
        }
        if (fragmentActivity instanceof BiometricEnrollActivity.InternalActivity) {
            intent.putExtra("android.intent.extra.USER_ID", i2);
        }
        if (i != 0) {
            fragmentActivity.startActivityForResult(intent, i);
            return;
        }
        fragmentActivity.startActivity(intent);
        fragmentActivity.finish();
    }

    public static boolean isMultiBiometricEnrollmentFlow(Activity activity) {
        return activity.getIntent().hasExtra("enroll_after_face");
    }

    public static void copyMultiBiometricExtras(Intent intent, Intent intent2) {
        PendingIntent pendingIntent = (PendingIntent) intent.getExtra("enroll_after_face", (Object) null);
        if (pendingIntent != null) {
            intent2.putExtra("enroll_after_face", pendingIntent);
        }
    }

    public static boolean tryStartingNextBiometricEnroll(Activity activity, int i) {
        PendingIntent pendingIntent = (PendingIntent) activity.getIntent().getExtra("enroll_after_face");
        if (pendingIntent == null) {
            return false;
        }
        try {
            Log.d("BiometricUtils", "Starting pendingIntent: " + pendingIntent);
            activity.startIntentSenderForResult(pendingIntent.getIntentSender(), i, (Intent) null, 0, 0, 0);
            return true;
        } catch (IntentSender.SendIntentException e) {
            Log.e("BiometricUtils", "Pending intent canceled: " + e);
            return false;
        }
    }

    public static boolean isReverseLandscape(Context context) {
        return context.getDisplay().getRotation() == 3;
    }
}
