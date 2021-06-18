package com.android.settings.biometrics.combination;

import android.content.Context;
import android.content.Intent;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricUtils;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.password.ChooseLockSettingsHelper;

public abstract class BiometricsSettingsBase extends DashboardFragment {
    private boolean mConfirmCredential;
    private boolean mDoNotFinishActivity;
    private FaceManager mFaceManager;
    private FingerprintManager mFingerprintManager;
    protected long mGkPwHandle;
    protected int mUserId;

    public abstract String getFacePreferenceKey();

    public abstract String getFingerprintPreferenceKey();

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mUserId = getActivity().getIntent().getIntExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mFaceManager = Utils.getFaceManagerOrNull(getActivity());
        this.mFingerprintManager = Utils.getFingerprintManagerOrNull(getActivity());
        if (BiometricUtils.containsGatekeeperPasswordHandle(getIntent())) {
            this.mGkPwHandle = BiometricUtils.getGatekeeperPasswordHandle(getIntent());
        }
        if (bundle != null) {
            this.mConfirmCredential = bundle.getBoolean("confirm_credential");
            this.mDoNotFinishActivity = bundle.getBoolean("do_not_finish_activity");
            if (bundle.containsKey("request_gk_pw_handle")) {
                this.mGkPwHandle = bundle.getLong("request_gk_pw_handle");
            }
        }
        if (this.mGkPwHandle == 0 && !this.mConfirmCredential) {
            this.mConfirmCredential = true;
            launchChooseOrConfirmLock();
        }
    }

    public void onResume() {
        super.onResume();
        if (!this.mConfirmCredential) {
            this.mDoNotFinishActivity = false;
        }
    }

    public void onStop() {
        super.onStop();
        if (!getActivity().isChangingConfigurations() && !this.mDoNotFinishActivity) {
            BiometricUtils.removeGatekeeperPasswordHandle((Context) getActivity(), this.mGkPwHandle);
            getActivity().finish();
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        if (getFacePreferenceKey().equals(key)) {
            this.mDoNotFinishActivity = true;
            this.mFaceManager.generateChallenge(this.mUserId, new BiometricsSettingsBase$$ExternalSyntheticLambda0(this, preference));
        } else if (getFingerprintPreferenceKey().equals(key)) {
            this.mDoNotFinishActivity = true;
            this.mFingerprintManager.generateChallenge(this.mUserId, new BiometricsSettingsBase$$ExternalSyntheticLambda1(this, preference));
        }
        return super.onPreferenceTreeClick(preference);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPreferenceTreeClick$0(Preference preference, int i, int i2, long j) {
        byte[] requestGatekeeperHat = BiometricUtils.requestGatekeeperHat((Context) getActivity(), this.mGkPwHandle, this.mUserId, j);
        Bundle extras = preference.getExtras();
        extras.putByteArray("hw_auth_token", requestGatekeeperHat);
        extras.putInt("sensor_id", i);
        extras.putLong("challenge", j);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPreferenceTreeClick$1(Preference preference, int i, int i2, long j) {
        byte[] requestGatekeeperHat = BiometricUtils.requestGatekeeperHat((Context) getActivity(), this.mGkPwHandle, this.mUserId, j);
        Bundle extras = preference.getExtras();
        extras.putByteArray("hw_auth_token", requestGatekeeperHat);
        extras.putLong("challenge", j);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("confirm_credential", this.mConfirmCredential);
        bundle.putBoolean("do_not_finish_activity", this.mDoNotFinishActivity);
        long j = this.mGkPwHandle;
        if (j != 0) {
            bundle.putLong("request_gk_pw_handle", j);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2001 || i == 2002) {
            this.mConfirmCredential = false;
            this.mDoNotFinishActivity = false;
            if (i2 != 1 && i2 != -1) {
                Log.d(getLogTag(), "Password not confirmed.");
                finish();
            } else if (BiometricUtils.containsGatekeeperPasswordHandle(intent)) {
                this.mGkPwHandle = BiometricUtils.getGatekeeperPasswordHandle(intent);
            } else {
                Log.d(getLogTag(), "Data null or GK PW missing.");
                finish();
            }
        }
    }

    private void launchChooseOrConfirmLock() {
        ChooseLockSettingsHelper.Builder returnCredentials = new ChooseLockSettingsHelper.Builder(getActivity(), this).setRequestCode(2001).setTitle(getString(R.string.security_settings_biometric_preference_title)).setRequestGatekeeperPasswordHandle(true).setForegroundOnly(true).setReturnCredentials(true);
        int i = this.mUserId;
        if (i != -10000) {
            returnCredentials.setUserId(i);
        }
        this.mDoNotFinishActivity = true;
        if (!returnCredentials.show()) {
            Intent chooseLockIntent = BiometricUtils.getChooseLockIntent(getActivity(), getIntent());
            chooseLockIntent.putExtra("hide_insecure_options", true);
            chooseLockIntent.putExtra("request_gk_pw_handle", true);
            chooseLockIntent.putExtra("for_biometrics", true);
            int i2 = this.mUserId;
            if (i2 != -10000) {
                chooseLockIntent.putExtra("android.intent.extra.USER_ID", i2);
            }
            startActivityForResult(chooseLockIntent, 2002);
        }
    }
}
