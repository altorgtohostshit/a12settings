package com.android.settings.biometrics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.SetupWizardUtils;
import com.google.android.setupdesign.span.LinkSpan;

public abstract class BiometricEnrollIntroduction extends BiometricEnrollBase implements LinkSpan.OnClickListener {
    private boolean mBiometricUnlockDisabledByAdmin;
    protected boolean mConfirmingCredentials;
    private TextView mErrorText;
    private boolean mHasPassword;
    protected boolean mNextClicked;
    private UserManager mUserManager;

    protected interface GenerateChallengeCallback {
        void onChallengeGenerated(int i, int i2, long j);
    }

    /* access modifiers changed from: protected */
    public abstract int checkMaxEnrolled();

    /* access modifiers changed from: protected */
    public abstract void getChallenge(GenerateChallengeCallback generateChallengeCallback);

    /* access modifiers changed from: protected */
    public abstract int getConfirmLockTitleResId();

    /* access modifiers changed from: protected */
    public abstract int getDescriptionResDisabledByAdmin();

    /* access modifiers changed from: protected */
    public abstract Intent getEnrollingIntent();

    /* access modifiers changed from: protected */
    public abstract TextView getErrorTextView();

    /* access modifiers changed from: protected */
    public abstract String getExtraKeyForBiometric();

    /* access modifiers changed from: protected */
    public abstract int getHeaderResDefault();

    /* access modifiers changed from: protected */
    public abstract int getHeaderResDisabledByAdmin();

    /* access modifiers changed from: protected */
    public abstract int getLayoutResource();

    /* access modifiers changed from: protected */
    public abstract boolean isDisabledByAdmin();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.mConfirmingCredentials = bundle.getBoolean("confirming_credentials");
        }
        Intent intent = getIntent();
        if (intent.getStringExtra("theme") == null) {
            intent.putExtra("theme", SetupWizardUtils.getThemeString(intent));
        }
        this.mBiometricUnlockDisabledByAdmin = isDisabledByAdmin();
        setContentView(getLayoutResource());
        if (this.mBiometricUnlockDisabledByAdmin) {
            setHeaderText(getHeaderResDisabledByAdmin());
        } else {
            setHeaderText(getHeaderResDefault());
        }
        this.mErrorText = getErrorTextView();
        this.mUserManager = UserManager.get(this);
        updatePasswordQuality();
        if (this.mConfirmingCredentials) {
            return;
        }
        if (!this.mHasPassword) {
            this.mConfirmingCredentials = true;
            launchChooseLock();
        } else if (!BiometricUtils.containsGatekeeperPasswordHandle(getIntent()) && this.mToken == null) {
            this.mConfirmingCredentials = true;
            launchConfirmLock(getConfirmLockTitleResId());
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        int checkMaxEnrolled = checkMaxEnrolled();
        if (checkMaxEnrolled == 0) {
            this.mErrorText.setText((CharSequence) null);
            this.mErrorText.setVisibility(8);
            getNextButton().setVisibility(0);
            return;
        }
        this.mErrorText.setText(checkMaxEnrolled);
        this.mErrorText.setVisibility(0);
        getNextButton().setText(getResources().getString(R.string.done));
        getNextButton().setVisibility(0);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("confirming_credentials", this.mConfirmingCredentials);
    }

    /* access modifiers changed from: protected */
    public boolean shouldFinishWhenBackgrounded() {
        return super.shouldFinishWhenBackgrounded() && !this.mConfirmingCredentials && !this.mNextClicked;
    }

    private void updatePasswordQuality() {
        this.mHasPassword = new LockPatternUtils(this).getActivePasswordQuality(this.mUserManager.getCredentialOwnerProfile(this.mUserId)) != 0;
    }

    /* access modifiers changed from: protected */
    public void onNextButtonClick(View view) {
        this.mNextClicked = true;
        if (checkMaxEnrolled() == 0) {
            launchNextEnrollingActivity(this.mToken);
            return;
        }
        setResult(1);
        finish();
    }

    private void launchChooseLock() {
        Intent chooseLockIntent = BiometricUtils.getChooseLockIntent(this, getIntent());
        chooseLockIntent.putExtra("hide_insecure_options", true);
        chooseLockIntent.putExtra("request_gk_pw_handle", true);
        chooseLockIntent.putExtra(getExtraKeyForBiometric(), true);
        int i = this.mUserId;
        if (i != -10000) {
            chooseLockIntent.putExtra("android.intent.extra.USER_ID", i);
        }
        startActivityForResult(chooseLockIntent, 1);
    }

    private void launchNextEnrollingActivity(byte[] bArr) {
        Intent enrollingIntent = getEnrollingIntent();
        if (bArr != null) {
            enrollingIntent.putExtra("hw_auth_token", bArr);
        }
        int i = this.mUserId;
        if (i != -10000) {
            enrollingIntent.putExtra("android.intent.extra.USER_ID", i);
        }
        BiometricUtils.copyMultiBiometricExtras(getIntent(), enrollingIntent);
        enrollingIntent.putExtra("from_settings_summary", this.mFromSettingsSummary);
        enrollingIntent.putExtra("challenge", this.mChallenge);
        enrollingIntent.putExtra("sensor_id", this.mSensorId);
        startActivityForResult(enrollingIntent, 2);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 2) {
            if (i2 == 1 || i2 == 2 || i2 == 3) {
                setResult(i2, intent);
                finish();
                return;
            }
        } else if (i == 1) {
            this.mConfirmingCredentials = false;
            if (i2 == 1) {
                updatePasswordQuality();
                overridePendingTransition(R.anim.sud_slide_next_in, R.anim.sud_slide_next_out);
                getNextButton().setEnabled(false);
                getChallenge(new BiometricEnrollIntroduction$$ExternalSyntheticLambda1(this, intent));
            } else {
                setResult(i2, intent);
                finish();
            }
        } else if (i == 4) {
            this.mConfirmingCredentials = false;
            if (i2 != -1 || intent == null) {
                setResult(i2, intent);
                finish();
            } else {
                overridePendingTransition(R.anim.sud_slide_next_in, R.anim.sud_slide_next_out);
                getNextButton().setEnabled(false);
                getChallenge(new BiometricEnrollIntroduction$$ExternalSyntheticLambda0(this, intent));
            }
        } else if (i == 3) {
            overridePendingTransition(R.anim.sud_slide_back_in, R.anim.sud_slide_back_out);
        } else if (i == 6) {
            Log.d("BiometricEnrollIntroduction", "ENROLL_NEXT_BIOMETRIC_REQUEST, result: " + i2);
            if (i2 != 0) {
                setResult(i2, intent);
                finish();
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityResult$0(Intent intent, int i, int i2, long j) {
        this.mSensorId = i;
        this.mChallenge = j;
        this.mToken = BiometricUtils.requestGatekeeperHat((Context) this, intent, this.mUserId, j);
        BiometricUtils.removeGatekeeperPasswordHandle((Context) this, intent);
        getNextButton().setEnabled(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityResult$1(Intent intent, int i, int i2, long j) {
        this.mSensorId = i;
        this.mChallenge = j;
        this.mToken = BiometricUtils.requestGatekeeperHat((Context) this, intent, this.mUserId, j);
        BiometricUtils.removeGatekeeperPasswordHandle((Context) this, intent);
        getNextButton().setEnabled(true);
    }

    /* access modifiers changed from: protected */
    public void onSkipButtonClick(View view) {
        setResult(2);
        finish();
    }

    /* access modifiers changed from: protected */
    public void initViews() {
        super.initViews();
        if (this.mBiometricUnlockDisabledByAdmin) {
            setDescriptionText(getDescriptionResDisabledByAdmin());
        }
    }
}
