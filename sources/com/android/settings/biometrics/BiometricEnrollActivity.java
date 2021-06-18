package com.android.settings.biometrics;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.biometrics.BiometricManager;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.SetupWizardUtils;
import com.android.settings.core.InstrumentedActivity;
import com.android.settings.password.ChooseLockGeneric;
import com.android.settings.password.ChooseLockSettingsHelper;
import com.google.android.setupcompat.util.WizardManagerHelper;
import java.util.List;

public class BiometricEnrollActivity extends InstrumentedActivity {
    private boolean mConfirmingCredentials;
    private Long mGkPwHandle;
    private boolean mIsEnrollActionLogged;
    private boolean mIsFaceEnrollable;
    private boolean mIsFingerprintEnrollable;
    private MultiBiometricEnrollHelper mMultiBiometricEnrollHelper;
    private int mUserId = UserHandle.myUserId();

    public static final class InternalActivity extends BiometricEnrollActivity {
    }

    public int getMetricsCategory() {
        return 1586;
    }

    public void onCreate(Bundle bundle) {
        int i;
        int i2;
        super.onCreate(bundle);
        if (this instanceof InternalActivity) {
            this.mUserId = getIntent().getIntExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
            if (BiometricUtils.containsGatekeeperPasswordHandle(getIntent())) {
                this.mGkPwHandle = Long.valueOf(BiometricUtils.getGatekeeperPasswordHandle(getIntent()));
            }
        }
        if (bundle != null) {
            this.mConfirmingCredentials = bundle.getBoolean("confirming_credentials", false);
            this.mIsEnrollActionLogged = bundle.getBoolean("enroll_action_logged", false);
            if (bundle.containsKey("gk_pw_handle")) {
                this.mGkPwHandle = Long.valueOf(bundle.getLong("gk_pw_handle"));
            }
        }
        Intent intent = getIntent();
        if (!this.mIsEnrollActionLogged && "android.settings.BIOMETRIC_ENROLL".equals(intent.getAction())) {
            this.mIsEnrollActionLogged = true;
            BiometricManager biometricManager = (BiometricManager) getSystemService(BiometricManager.class);
            int i3 = 12;
            if (biometricManager != null) {
                i3 = biometricManager.canAuthenticate(15);
                i = biometricManager.canAuthenticate(255);
                i2 = biometricManager.canAuthenticate(32768);
            } else {
                i2 = 12;
                i = 12;
            }
            FrameworkStatsLog.write(355, i3 == 0, i == 0, i2 == 0, intent.hasExtra("android.provider.extra.BIOMETRIC_AUTHENTICATORS_ALLOWED"), intent.getIntExtra("android.provider.extra.BIOMETRIC_AUTHENTICATORS_ALLOWED", 0));
        }
        if (intent.getStringExtra("theme") == null) {
            intent.putExtra("theme", SetupWizardUtils.getThemeString(intent));
        }
        int intExtra = intent.getIntExtra("android.provider.extra.BIOMETRIC_AUTHENTICATORS_ALLOWED", 255);
        Log.d("BiometricEnrollActivity", "Authenticators: " + intExtra);
        PackageManager packageManager = getApplicationContext().getPackageManager();
        boolean hasSystemFeature = packageManager.hasSystemFeature("android.hardware.fingerprint");
        boolean hasSystemFeature2 = packageManager.hasSystemFeature("android.hardware.biometrics.face");
        if (!WizardManagerHelper.isAnySetupWizard(getIntent())) {
            int canAuthenticate = ((BiometricManager) getSystemService(BiometricManager.class)).canAuthenticate(intExtra);
            if (canAuthenticate != 11) {
                Log.e("BiometricEnrollActivity", "Unexpected result: " + canAuthenticate);
                finish();
            } else if (intExtra == 32768) {
                launchCredentialOnlyEnroll();
            } else if (hasSystemFeature2 && hasSystemFeature) {
                setupForMultiBiometricEnroll();
            } else if (hasSystemFeature) {
                launchFingerprintOnlyEnroll();
            } else if (hasSystemFeature2) {
                launchFaceOnlyEnroll();
            } else {
                Log.e("BiometricEnrollActivity", "Unknown state, finishing");
                finish();
            }
        } else if (hasSystemFeature2 && hasSystemFeature) {
            setupForMultiBiometricEnroll();
        } else if (hasSystemFeature2) {
            launchFaceOnlyEnroll();
        } else if (hasSystemFeature) {
            launchFingerprintOnlyEnroll();
        } else {
            Log.e("BiometricEnrollActivity", "No biometrics but started by SUW?");
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("confirming_credentials", this.mConfirmingCredentials);
        bundle.putBoolean("enroll_action_logged", this.mIsEnrollActionLogged);
        Long l = this.mGkPwHandle;
        if (l != null) {
            bundle.putLong("gk_pw_handle", l.longValue());
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        MultiBiometricEnrollHelper multiBiometricEnrollHelper = this.mMultiBiometricEnrollHelper;
        if (multiBiometricEnrollHelper == null) {
            overridePendingTransition(R.anim.sud_slide_next_in, R.anim.sud_slide_next_out);
            if (i == 1) {
                this.mConfirmingCredentials = false;
                if (i2 == 1) {
                    startMultiBiometricEnroll(intent);
                    return;
                }
                Log.d("BiometricEnrollActivity", "Unknown result for chooseLock: " + i2);
                setResult(i2);
                finish();
            } else if (i != 2) {
                Log.d("BiometricEnrollActivity", "Unknown requestCode: " + i + ", finishing");
                finish();
            } else {
                this.mConfirmingCredentials = false;
                if (i2 == -1) {
                    startMultiBiometricEnroll(intent);
                    return;
                }
                Log.d("BiometricEnrollActivity", "Unknown result for confirmLock: " + i2);
                finish();
            }
        } else {
            multiBiometricEnrollHelper.onActivityResult(i, i2, intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onApplyThemeResource(Resources.Theme theme, int i, boolean z) {
        int theme2 = SetupWizardUtils.getTheme(this, getIntent());
        theme.applyStyle(R.style.SetupWizardPartnerResource, true);
        super.onApplyThemeResource(theme, theme2, z);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (!this.mConfirmingCredentials && this.mMultiBiometricEnrollHelper == null && !isChangingConfigurations()) {
            Log.d("BiometricEnrollActivity", "Finishing in onStop");
            finish();
        }
    }

    private void setupForMultiBiometricEnroll() {
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FingerprintManager.class);
        FaceManager faceManager = (FaceManager) getSystemService(FaceManager.class);
        List sensorPropertiesInternal = fingerprintManager.getSensorPropertiesInternal();
        boolean z = false;
        this.mIsFaceEnrollable = faceManager.getEnrolledFaces(this.mUserId).size() < ((FaceSensorPropertiesInternal) faceManager.getSensorPropertiesInternal().get(0)).maxEnrollmentsPerUser;
        if (fingerprintManager.getEnrolledFingerprints(this.mUserId).size() < ((FingerprintSensorPropertiesInternal) sensorPropertiesInternal.get(0)).maxEnrollmentsPerUser) {
            z = true;
        }
        this.mIsFingerprintEnrollable = z;
        if (!this.mConfirmingCredentials) {
            this.mConfirmingCredentials = true;
            if (!userHasPassword(this.mUserId)) {
                launchChooseLock();
            } else {
                launchConfirmLock();
            }
        }
    }

    private void startMultiBiometricEnroll(Intent intent) {
        Long valueOf = Long.valueOf(BiometricUtils.getGatekeeperPasswordHandle(intent));
        this.mGkPwHandle = valueOf;
        MultiBiometricEnrollHelper multiBiometricEnrollHelper = new MultiBiometricEnrollHelper(this, this.mUserId, this.mIsFaceEnrollable, this.mIsFingerprintEnrollable, valueOf.longValue());
        this.mMultiBiometricEnrollHelper = multiBiometricEnrollHelper;
        multiBiometricEnrollHelper.startNextStep();
    }

    private boolean userHasPassword(int i) {
        return new LockPatternUtils(this).getActivePasswordQuality(((UserManager) getSystemService(UserManager.class)).getCredentialOwnerProfile(i)) != 0;
    }

    private void launchChooseLock() {
        Log.d("BiometricEnrollActivity", "launchChooseLock");
        Intent chooseLockIntent = BiometricUtils.getChooseLockIntent(this, getIntent());
        chooseLockIntent.putExtra("hide_insecure_options", true);
        chooseLockIntent.putExtra("request_gk_pw_handle", true);
        chooseLockIntent.putExtra("for_biometrics", true);
        int i = this.mUserId;
        if (i != -10000) {
            chooseLockIntent.putExtra("android.intent.extra.USER_ID", i);
        }
        startActivityForResult(chooseLockIntent, 1);
    }

    private void launchConfirmLock() {
        Log.d("BiometricEnrollActivity", "launchConfirmLock");
        ChooseLockSettingsHelper.Builder builder = new ChooseLockSettingsHelper.Builder(this);
        builder.setRequestCode(2).setRequestGatekeeperPasswordHandle(true).setForegroundOnly(true).setReturnCredentials(true);
        int i = this.mUserId;
        if (i != -10000) {
            builder.setUserId(i);
        }
        if (!builder.show()) {
            finish();
        }
    }

    private void launchEnrollActivity(Intent intent) {
        intent.setFlags(33554432);
        BiometricUtils.launchEnrollForResult(this, intent, 0, this instanceof InternalActivity ? getIntent().getByteArrayExtra("hw_auth_token") : null, this.mGkPwHandle, this.mUserId);
    }

    private void launchCredentialOnlyEnroll() {
        Intent intent = new Intent(this, ChooseLockGeneric.class);
        intent.putExtra("hide_insecure_options", true);
        launchEnrollActivity(intent);
    }

    private void launchFingerprintOnlyEnroll() {
        Intent intent;
        if (!getIntent().getBooleanExtra("skip_intro", false) || !(this instanceof InternalActivity)) {
            intent = BiometricUtils.getFingerprintIntroIntent(this, getIntent());
        } else {
            intent = BiometricUtils.getFingerprintFindSensorIntent(this, getIntent());
        }
        launchEnrollActivity(intent);
    }

    private void launchFaceOnlyEnroll() {
        launchEnrollActivity(BiometricUtils.getFaceIntroIntent(this, getIntent()));
    }
}
