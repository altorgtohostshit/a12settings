package com.android.settings.password;

import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.password.ConfirmLockPassword;
import com.android.settings.password.ConfirmLockPattern;

public abstract class ConfirmDeviceCredentialBaseFragment extends InstrumentedFragment {
    public static final String TAG = ConfirmDeviceCredentialBaseFragment.class.getSimpleName();
    protected BiometricManager mBiometricManager;
    protected Button mCancelButton;
    protected DevicePolicyManager mDevicePolicyManager;
    protected int mEffectiveUserId;
    protected TextView mErrorTextView;
    protected boolean mForceVerifyPath = false;
    protected Button mForgotButton;
    protected boolean mFrp;
    private CharSequence mFrpAlternateButtonText;
    protected final Handler mHandler = new Handler();
    protected LockPatternUtils mLockPatternUtils;
    private final Runnable mResetErrorRunnable = new Runnable() {
        public void run() {
            ConfirmDeviceCredentialBaseFragment.this.mErrorTextView.setText("");
        }
    };
    protected boolean mReturnCredentials = false;
    protected boolean mReturnGatekeeperPassword = false;
    protected int mUserId;
    protected UserManager mUserManager;

    /* access modifiers changed from: protected */
    public abstract int getLastTryErrorMessage(int i);

    /* access modifiers changed from: protected */
    public abstract void onShowError();

    public void prepareEnterAnimation() {
    }

    public void startEnterAnimation() {
    }

    private boolean isInternalActivity() {
        return (getActivity() instanceof ConfirmLockPassword.InternalActivity) || (getActivity() instanceof ConfirmLockPattern.InternalActivity);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getActivity().getIntent();
        this.mFrpAlternateButtonText = intent.getCharSequenceExtra("android.app.extra.ALTERNATE_BUTTON_LABEL");
        boolean z = false;
        this.mReturnCredentials = intent.getBooleanExtra("return_credentials", false);
        this.mReturnGatekeeperPassword = intent.getBooleanExtra("request_gk_pw_handle", false);
        this.mForceVerifyPath = intent.getBooleanExtra("force_verify", false);
        int userIdFromBundle = Utils.getUserIdFromBundle(getActivity(), intent.getExtras(), isInternalActivity());
        this.mUserId = userIdFromBundle;
        if (userIdFromBundle == -9999) {
            z = true;
        }
        this.mFrp = z;
        UserManager userManager = UserManager.get(getActivity());
        this.mUserManager = userManager;
        this.mEffectiveUserId = userManager.getCredentialOwnerProfile(this.mUserId);
        this.mLockPatternUtils = new LockPatternUtils(getActivity());
        this.mDevicePolicyManager = (DevicePolicyManager) getActivity().getSystemService("device_policy");
        this.mBiometricManager = (BiometricManager) getActivity().getSystemService(BiometricManager.class);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mCancelButton = (Button) view.findViewById(R.id.cancelButton);
        int i = 0;
        boolean booleanExtra = getActivity().getIntent().getBooleanExtra("com.android.settings.ConfirmCredentials.showCancelButton", false);
        boolean z = this.mFrp && !TextUtils.isEmpty(this.mFrpAlternateButtonText);
        Button button = this.mCancelButton;
        if (!booleanExtra && !z) {
            i = 8;
        }
        button.setVisibility(i);
        if (z) {
            this.mCancelButton.setText(this.mFrpAlternateButtonText);
        }
        this.mCancelButton.setOnClickListener(new ConfirmDeviceCredentialBaseFragment$$ExternalSyntheticLambda1(this, z));
        setupForgotButtonIfManagedProfile(view);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$0(boolean z, View view) {
        if (z) {
            getActivity().setResult(1);
        }
        getActivity().finish();
    }

    private void setupForgotButtonIfManagedProfile(View view) {
        if (this.mUserManager.isManagedProfile(this.mUserId) && this.mUserManager.isQuietModeEnabled(UserHandle.of(this.mUserId)) && this.mDevicePolicyManager.canProfileOwnerResetPasswordWhenLocked(this.mUserId)) {
            Button button = (Button) view.findViewById(R.id.forgotButton);
            this.mForgotButton = button;
            if (button == null) {
                Log.wtf(TAG, "Forgot button not found in managed profile credential dialog");
                return;
            }
            button.setVisibility(0);
            this.mForgotButton.setOnClickListener(new ConfirmDeviceCredentialBaseFragment$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupForgotButtonIfManagedProfile$1(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", ForgotPasswordActivity.class.getName());
        intent.putExtra("android.intent.extra.USER_ID", this.mUserId);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    /* access modifiers changed from: protected */
    public boolean isStrongAuthRequired() {
        return this.mFrp || !this.mLockPatternUtils.isBiometricAllowedForUser(this.mEffectiveUserId) || !this.mUserManager.isUserUnlocked(this.mUserId);
    }

    public void onResume() {
        super.onResume();
        refreshLockScreen();
    }

    /* access modifiers changed from: protected */
    public void refreshLockScreen() {
        updateErrorMessage(this.mLockPatternUtils.getCurrentFailedPasswordAttempts(this.mEffectiveUserId));
    }

    /* access modifiers changed from: protected */
    public void setAccessibilityTitle(CharSequence charSequence) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            CharSequence charSequenceExtra = intent.getCharSequenceExtra("com.android.settings.ConfirmCredentials.title");
            if (charSequence != null) {
                if (charSequenceExtra == null) {
                    getActivity().setTitle(charSequence);
                    return;
                }
                getActivity().setTitle(Utils.createAccessibleSequence(charSequenceExtra, charSequenceExtra + "," + charSequence));
            }
        }
    }

    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void reportFailedAttempt() {
        updateErrorMessage(this.mLockPatternUtils.getCurrentFailedPasswordAttempts(this.mEffectiveUserId) + 1);
        this.mLockPatternUtils.reportFailedPasswordAttempt(this.mEffectiveUserId);
    }

    /* access modifiers changed from: protected */
    public void updateErrorMessage(int i) {
        int maximumFailedPasswordsForWipe = this.mLockPatternUtils.getMaximumFailedPasswordsForWipe(this.mEffectiveUserId);
        if (maximumFailedPasswordsForWipe > 0 && i > 0) {
            if (this.mErrorTextView != null) {
                showError((CharSequence) getActivity().getString(R.string.lock_failed_attempts_before_wipe, new Object[]{Integer.valueOf(i), Integer.valueOf(maximumFailedPasswordsForWipe)}), 0);
            }
            int i2 = maximumFailedPasswordsForWipe - i;
            if (i2 <= 1) {
                FragmentManager childFragmentManager = getChildFragmentManager();
                int userTypeForWipe = getUserTypeForWipe();
                if (i2 == 1) {
                    LastTryDialog.show(childFragmentManager, getActivity().getString(R.string.lock_last_attempt_before_wipe_warning_title), getLastTryErrorMessage(userTypeForWipe), 17039370, false);
                } else {
                    LastTryDialog.show(childFragmentManager, (String) null, getWipeMessage(userTypeForWipe), R.string.lock_failed_attempts_now_wiping_dialog_dismiss, true);
                }
            }
        }
    }

    private int getUserTypeForWipe() {
        UserInfo userInfo = this.mUserManager.getUserInfo(this.mDevicePolicyManager.getProfileWithMinimumFailedPasswordsForWipe(this.mEffectiveUserId));
        if (userInfo == null || userInfo.isPrimary()) {
            return 1;
        }
        return userInfo.isManagedProfile() ? 2 : 3;
    }

    private int getWipeMessage(int i) {
        if (i == 1) {
            return R.string.lock_failed_attempts_now_wiping_device;
        }
        if (i == 2) {
            return R.string.lock_failed_attempts_now_wiping_profile;
        }
        if (i == 3) {
            return R.string.lock_failed_attempts_now_wiping_user;
        }
        throw new IllegalArgumentException("Unrecognized user type:" + i);
    }

    /* access modifiers changed from: protected */
    public void showError(CharSequence charSequence, long j) {
        this.mErrorTextView.setText(charSequence);
        onShowError();
        this.mHandler.removeCallbacks(this.mResetErrorRunnable);
        if (j != 0) {
            this.mHandler.postDelayed(this.mResetErrorRunnable, j);
        }
    }

    /* access modifiers changed from: protected */
    public void showError(int i, long j) {
        showError(getText(i), j);
    }

    public static class LastTryDialog extends DialogFragment {
        private static final String TAG = LastTryDialog.class.getSimpleName();

        static boolean show(FragmentManager fragmentManager, String str, int i, int i2, boolean z) {
            String str2 = TAG;
            LastTryDialog lastTryDialog = (LastTryDialog) fragmentManager.findFragmentByTag(str2);
            if (lastTryDialog != null && !lastTryDialog.isRemoving()) {
                return false;
            }
            Bundle bundle = new Bundle();
            bundle.putString("title", str);
            bundle.putInt("message", i);
            bundle.putInt("button", i2);
            bundle.putBoolean("dismiss", z);
            LastTryDialog lastTryDialog2 = new LastTryDialog();
            lastTryDialog2.setArguments(bundle);
            lastTryDialog2.show(fragmentManager, str2);
            fragmentManager.executePendingTransactions();
            return true;
        }

        public Dialog onCreateDialog(Bundle bundle) {
            AlertDialog create = new AlertDialog.Builder(getActivity()).setTitle((CharSequence) getArguments().getString("title")).setMessage(getArguments().getInt("message")).setPositiveButton(getArguments().getInt("button"), (DialogInterface.OnClickListener) null).create();
            create.setCanceledOnTouchOutside(false);
            return create;
        }

        public void onDismiss(DialogInterface dialogInterface) {
            super.onDismiss(dialogInterface);
            if (getActivity() != null && getArguments().getBoolean("dismiss")) {
                getActivity().finish();
            }
        }
    }
}
