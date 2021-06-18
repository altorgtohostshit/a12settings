package com.android.settings.password;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.LockPatternView;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.settings.R;
import com.android.settings.password.ConfirmDeviceCredentialBaseActivity;
import com.android.settings.password.CredentialCheckResultTracker;
import com.android.settingslib.animation.AppearAnimationCreator;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.google.android.setupdesign.GlifLayout;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfirmLockPattern extends ConfirmDeviceCredentialBaseActivity {

    public static class InternalActivity extends ConfirmLockPattern {
    }

    private enum Stage {
        NeedToUnlock,
        NeedToUnlockWrong,
        LockedOut
    }

    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        intent.putExtra(":settings:show_fragment", ConfirmLockPatternFragment.class.getName());
        return intent;
    }

    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return ConfirmLockPatternFragment.class.getName().equals(str);
    }

    public static class ConfirmLockPatternFragment extends ConfirmDeviceCredentialBaseFragment implements AppearAnimationCreator<Object>, CredentialCheckResultTracker.Listener {
        private AppearAnimationUtils mAppearAnimationUtils;
        /* access modifiers changed from: private */
        public Runnable mClearPatternRunnable = new Runnable() {
            public void run() {
                ConfirmLockPatternFragment.this.mLockPatternView.clearPattern();
            }
        };
        private LockPatternView.OnPatternListener mConfirmExistingLockPatternListener = new LockPatternView.OnPatternListener() {
            public void onPatternCellAdded(List<LockPatternView.Cell> list) {
            }

            public void onPatternStart() {
                ConfirmLockPatternFragment.this.mLockPatternView.removeCallbacks(ConfirmLockPatternFragment.this.mClearPatternRunnable);
            }

            public void onPatternCleared() {
                ConfirmLockPatternFragment.this.mLockPatternView.removeCallbacks(ConfirmLockPatternFragment.this.mClearPatternRunnable);
            }

            public void onPatternDetected(List<LockPatternView.Cell> list) {
                if (ConfirmLockPatternFragment.this.mPendingLockCheck == null && !ConfirmLockPatternFragment.this.mDisappearing) {
                    ConfirmLockPatternFragment.this.mLockPatternView.setEnabled(false);
                    LockscreenCredential createPattern = LockscreenCredential.createPattern(list);
                    Intent intent = new Intent();
                    ConfirmLockPatternFragment confirmLockPatternFragment = ConfirmLockPatternFragment.this;
                    if (confirmLockPatternFragment.mReturnGatekeeperPassword) {
                        if (isInternalActivity()) {
                            startVerifyPattern(createPattern, intent, 1);
                            return;
                        }
                    } else if (!confirmLockPatternFragment.mForceVerifyPath) {
                        startCheckPattern(createPattern, intent);
                        return;
                    } else if (isInternalActivity()) {
                        startVerifyPattern(createPattern, intent, 0);
                        return;
                    }
                    ConfirmLockPatternFragment.this.mCredentialCheckResultTracker.setResult(false, intent, 0, ConfirmLockPatternFragment.this.mEffectiveUserId);
                }
            }

            /* access modifiers changed from: private */
            public boolean isInternalActivity() {
                return ConfirmLockPatternFragment.this.getActivity() instanceof InternalActivity;
            }

            private void startVerifyPattern(LockscreenCredential lockscreenCredential, Intent intent, int i) {
                AsyncTask asyncTask;
                ConfirmLockPatternFragment confirmLockPatternFragment = ConfirmLockPatternFragment.this;
                int i2 = confirmLockPatternFragment.mEffectiveUserId;
                int i3 = confirmLockPatternFragment.mUserId;
                C1221x7a417012 confirmLockPattern$ConfirmLockPatternFragment$3$$ExternalSyntheticLambda0 = new C1221x7a417012(this, i, intent, i2);
                if (i2 == i3) {
                    asyncTask = LockPatternChecker.verifyCredential(confirmLockPatternFragment.mLockPatternUtils, lockscreenCredential, i3, i, confirmLockPattern$ConfirmLockPatternFragment$3$$ExternalSyntheticLambda0);
                } else {
                    asyncTask = LockPatternChecker.verifyTiedProfileChallenge(confirmLockPatternFragment.mLockPatternUtils, lockscreenCredential, i3, i, confirmLockPattern$ConfirmLockPatternFragment$3$$ExternalSyntheticLambda0);
                }
                AsyncTask unused = confirmLockPatternFragment.mPendingLockCheck = asyncTask;
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$startVerifyPattern$0(int i, Intent intent, int i2, VerifyCredentialResponse verifyCredentialResponse, int i3) {
                AsyncTask unused = ConfirmLockPatternFragment.this.mPendingLockCheck = null;
                boolean isMatched = verifyCredentialResponse.isMatched();
                if (isMatched && ConfirmLockPatternFragment.this.mReturnCredentials) {
                    if ((i & 1) != 0) {
                        intent.putExtra("gk_pw_handle", verifyCredentialResponse.getGatekeeperPasswordHandle());
                    } else {
                        intent.putExtra("hw_auth_token", verifyCredentialResponse.getGatekeeperHAT());
                    }
                }
                ConfirmLockPatternFragment.this.mCredentialCheckResultTracker.setResult(isMatched, intent, i3, i2);
            }

            private void startCheckPattern(final LockscreenCredential lockscreenCredential, final Intent intent) {
                if (lockscreenCredential.size() < 4) {
                    ConfirmLockPatternFragment confirmLockPatternFragment = ConfirmLockPatternFragment.this;
                    confirmLockPatternFragment.onPatternChecked(false, intent, 0, confirmLockPatternFragment.mEffectiveUserId, false);
                    return;
                }
                ConfirmLockPatternFragment confirmLockPatternFragment2 = ConfirmLockPatternFragment.this;
                final int i = confirmLockPatternFragment2.mEffectiveUserId;
                AsyncTask unused = confirmLockPatternFragment2.mPendingLockCheck = LockPatternChecker.checkCredential(confirmLockPatternFragment2.mLockPatternUtils, lockscreenCredential, i, new LockPatternChecker.OnCheckCallback() {
                    public void onChecked(boolean z, int i) {
                        AsyncTask unused = ConfirmLockPatternFragment.this.mPendingLockCheck = null;
                        if (z && C12173.this.isInternalActivity() && ConfirmLockPatternFragment.this.mReturnCredentials) {
                            intent.putExtra("type", 2);
                            intent.putExtra("password", lockscreenCredential);
                        }
                        ConfirmLockPatternFragment.this.mCredentialCheckResultTracker.setResult(z, intent, i, i);
                    }
                });
            }
        };
        private CountDownTimer mCountdownTimer;
        /* access modifiers changed from: private */
        public CredentialCheckResultTracker mCredentialCheckResultTracker;
        private CharSequence mDetailsText;
        private DisappearAnimationUtils mDisappearAnimationUtils;
        /* access modifiers changed from: private */
        public boolean mDisappearing = false;
        private GlifLayout mGlifLayout;
        private CharSequence mHeaderText;
        private boolean mIsManagedProfile;
        /* access modifiers changed from: private */
        public LockPatternView mLockPatternView;
        /* access modifiers changed from: private */
        public AsyncTask<?, ?, ?> mPendingLockCheck;

        public int getMetricsCategory() {
            return 31;
        }

        public void onSaveInstanceState(Bundle bundle) {
        }

        /* access modifiers changed from: protected */
        public void onShowError() {
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            View inflate = layoutInflater.inflate(((ConfirmLockPattern) getActivity()).getConfirmCredentialTheme() == ConfirmDeviceCredentialBaseActivity.ConfirmCredentialTheme.NORMAL ? R.layout.confirm_lock_pattern_normal : R.layout.confirm_lock_pattern, viewGroup, false);
            this.mGlifLayout = (GlifLayout) inflate.findViewById(R.id.setup_wizard_layout);
            int rotation = getContext().getDisplay().getRotation();
            if (rotation == 1 || rotation == 3) {
                this.mGlifLayout.setLandscapeHeaderAreaVisible(false);
            }
            this.mLockPatternView = inflate.findViewById(R.id.lockPattern);
            this.mErrorTextView = (TextView) inflate.findViewById(R.id.errorText);
            this.mIsManagedProfile = UserManager.get(getActivity()).isManagedProfile(this.mEffectiveUserId);
            inflate.findViewById(R.id.topLayout).setDefaultTouchRecepient(this.mLockPatternView);
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                this.mHeaderText = intent.getCharSequenceExtra("com.android.settings.ConfirmCredentials.header");
                this.mDetailsText = intent.getCharSequenceExtra("com.android.settings.ConfirmCredentials.details");
            }
            if (TextUtils.isEmpty(this.mHeaderText) && this.mIsManagedProfile) {
                this.mHeaderText = this.mDevicePolicyManager.getOrganizationNameForUser(this.mUserId);
            }
            this.mLockPatternView.setTactileFeedbackEnabled(this.mLockPatternUtils.isTactileFeedbackEnabled());
            this.mLockPatternView.setInStealthMode(!this.mLockPatternUtils.isVisiblePatternEnabled(this.mEffectiveUserId));
            this.mLockPatternView.setOnPatternListener(this.mConfirmExistingLockPatternListener);
            updateStage(Stage.NeedToUnlock);
            if (bundle == null && !this.mFrp && !this.mLockPatternUtils.isLockPatternEnabled(this.mEffectiveUserId)) {
                getActivity().setResult(-1);
                getActivity().finish();
            }
            this.mAppearAnimationUtils = new AppearAnimationUtils(getContext(), 220, 2.0f, 1.3f, AnimationUtils.loadInterpolator(getContext(), 17563662));
            this.mDisappearAnimationUtils = new DisappearAnimationUtils(getContext(), 125, 4.0f, 0.3f, AnimationUtils.loadInterpolator(getContext(), 17563663), new AppearAnimationUtils.RowTranslationScaler() {
                public float getRowTranslationScale(int i, int i2) {
                    return ((float) (i2 - i)) / ((float) i2);
                }
            });
            setAccessibilityTitle(this.mGlifLayout.getHeaderText());
            CredentialCheckResultTracker credentialCheckResultTracker = (CredentialCheckResultTracker) getFragmentManager().findFragmentByTag("check_lock_result");
            this.mCredentialCheckResultTracker = credentialCheckResultTracker;
            if (credentialCheckResultTracker == null) {
                this.mCredentialCheckResultTracker = new CredentialCheckResultTracker();
                getFragmentManager().beginTransaction().add((Fragment) this.mCredentialCheckResultTracker, "check_lock_result").commit();
            }
            return inflate;
        }

        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            Button button = this.mForgotButton;
            if (button != null) {
                button.setText(R.string.lockpassword_forgot_pattern);
            }
        }

        public void onPause() {
            super.onPause();
            CountDownTimer countDownTimer = this.mCountdownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            this.mCredentialCheckResultTracker.setListener((CredentialCheckResultTracker.Listener) null);
        }

        public void onResume() {
            super.onResume();
            long lockoutAttemptDeadline = this.mLockPatternUtils.getLockoutAttemptDeadline(this.mEffectiveUserId);
            if (lockoutAttemptDeadline != 0) {
                this.mCredentialCheckResultTracker.clearResult();
                handleAttemptLockout(lockoutAttemptDeadline);
            } else if (!this.mLockPatternView.isEnabled()) {
                updateStage(Stage.NeedToUnlock);
            }
            this.mCredentialCheckResultTracker.setListener(this);
        }

        public void prepareEnterAnimation() {
            super.prepareEnterAnimation();
            this.mGlifLayout.getHeaderTextView().setAlpha(0.0f);
            this.mCancelButton.setAlpha(0.0f);
            Button button = this.mForgotButton;
            if (button != null) {
                button.setAlpha(0.0f);
            }
            this.mLockPatternView.setAlpha(0.0f);
            this.mGlifLayout.getDescriptionTextView().setAlpha(0.0f);
        }

        private int getDefaultDetails() {
            if (this.mFrp) {
                return R.string.lockpassword_confirm_your_pattern_details_frp;
            }
            boolean isStrongAuthRequired = isStrongAuthRequired();
            return this.mIsManagedProfile ? isStrongAuthRequired ? R.string.lockpassword_strong_auth_required_work_pattern : R.string.lockpassword_confirm_your_pattern_generic_profile : isStrongAuthRequired ? R.string.lockpassword_strong_auth_required_device_pattern : R.string.lockpassword_confirm_your_pattern_generic;
        }

        private Object[][] getActiveViews() {
            ArrayList arrayList = new ArrayList();
            arrayList.add(new ArrayList(Collections.singletonList(this.mGlifLayout.getHeaderTextView())));
            arrayList.add(new ArrayList(Collections.singletonList(this.mGlifLayout.getDescriptionTextView())));
            if (this.mCancelButton.getVisibility() == 0) {
                arrayList.add(new ArrayList(Collections.singletonList(this.mCancelButton)));
            }
            if (this.mForgotButton != null) {
                arrayList.add(new ArrayList(Collections.singletonList(this.mForgotButton)));
            }
            LockPatternView.CellState[][] cellStates = this.mLockPatternView.getCellStates();
            for (int i = 0; i < cellStates.length; i++) {
                ArrayList arrayList2 = new ArrayList();
                for (LockPatternView.CellState add : cellStates[i]) {
                    arrayList2.add(add);
                }
                arrayList.add(arrayList2);
            }
            int size = arrayList.size();
            int[] iArr = new int[2];
            iArr[1] = cellStates[0].length;
            iArr[0] = size;
            Object[][] objArr = (Object[][]) Array.newInstance(Object.class, iArr);
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                ArrayList arrayList3 = (ArrayList) arrayList.get(i2);
                for (int i3 = 0; i3 < arrayList3.size(); i3++) {
                    objArr[i2][i3] = arrayList3.get(i3);
                }
            }
            return objArr;
        }

        public void startEnterAnimation() {
            super.startEnterAnimation();
            this.mLockPatternView.setAlpha(1.0f);
            this.mAppearAnimationUtils.startAnimation2d(getActiveViews(), (Runnable) null, this);
        }

        /* access modifiers changed from: private */
        public void updateStage(Stage stage) {
            int i = C12141.f98xec47707f[stage.ordinal()];
            if (i == 1) {
                CharSequence charSequence = this.mHeaderText;
                if (charSequence != null) {
                    this.mGlifLayout.setHeaderText(charSequence);
                } else {
                    this.mGlifLayout.setHeaderText(getDefaultHeader());
                }
                CharSequence charSequence2 = this.mDetailsText;
                if (charSequence2 != null) {
                    this.mGlifLayout.setDescriptionText(charSequence2);
                } else {
                    this.mGlifLayout.setDescriptionText(getDefaultDetails());
                }
                this.mErrorTextView.setText("");
                updateErrorMessage(this.mLockPatternUtils.getCurrentFailedPasswordAttempts(this.mEffectiveUserId));
                this.mLockPatternView.setEnabled(true);
                this.mLockPatternView.enableInput();
                this.mLockPatternView.clearPattern();
            } else if (i == 2) {
                showError((int) R.string.lockpattern_need_to_unlock_wrong, 3000);
                this.mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                this.mLockPatternView.setEnabled(true);
                this.mLockPatternView.enableInput();
            } else if (i == 3) {
                this.mLockPatternView.clearPattern();
                this.mLockPatternView.setEnabled(false);
            }
            this.mGlifLayout.getHeaderTextView().announceForAccessibility(this.mGlifLayout.getHeaderText());
        }

        private int getDefaultHeader() {
            if (this.mFrp) {
                return R.string.lockpassword_confirm_your_pattern_header_frp;
            }
            return this.mIsManagedProfile ? R.string.lockpassword_confirm_your_work_pattern_header : R.string.lockpassword_confirm_your_pattern_header;
        }

        private void postClearPatternRunnable() {
            this.mLockPatternView.removeCallbacks(this.mClearPatternRunnable);
            this.mLockPatternView.postDelayed(this.mClearPatternRunnable, 3000);
        }

        private void startDisappearAnimation(Intent intent) {
            if (!this.mDisappearing) {
                this.mDisappearing = true;
                ConfirmLockPattern confirmLockPattern = (ConfirmLockPattern) getActivity();
                if (confirmLockPattern != null && !confirmLockPattern.isFinishing()) {
                    if (confirmLockPattern.getConfirmCredentialTheme() == ConfirmDeviceCredentialBaseActivity.ConfirmCredentialTheme.DARK) {
                        this.mLockPatternView.clearPattern();
                        this.mDisappearAnimationUtils.startAnimation2d(getActiveViews(), new C1220x3615d43(confirmLockPattern, intent), this);
                        return;
                    }
                    confirmLockPattern.setResult(-1, intent);
                    confirmLockPattern.finish();
                }
            }
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ void lambda$startDisappearAnimation$0(ConfirmLockPattern confirmLockPattern, Intent intent) {
            confirmLockPattern.setResult(-1, intent);
            confirmLockPattern.finish();
            confirmLockPattern.overridePendingTransition(R.anim.confirm_credential_close_enter, R.anim.confirm_credential_close_exit);
        }

        /* access modifiers changed from: private */
        public void onPatternChecked(boolean z, Intent intent, int i, int i2, boolean z2) {
            this.mLockPatternView.setEnabled(true);
            if (z) {
                if (z2) {
                    ConfirmDeviceCredentialUtils.reportSuccessfulAttempt(this.mLockPatternUtils, this.mUserManager, this.mDevicePolicyManager, this.mEffectiveUserId, true);
                }
                startDisappearAnimation(intent);
                ConfirmDeviceCredentialUtils.checkForPendingIntent(getActivity());
                return;
            }
            if (i > 0) {
                refreshLockScreen();
                handleAttemptLockout(this.mLockPatternUtils.setLockoutAttemptDeadline(i2, i));
            } else {
                updateStage(Stage.NeedToUnlockWrong);
                postClearPatternRunnable();
            }
            if (z2) {
                reportFailedAttempt();
            }
        }

        public void onCredentialChecked(boolean z, Intent intent, int i, int i2, boolean z2) {
            onPatternChecked(z, intent, i, i2, z2);
        }

        /* access modifiers changed from: protected */
        public int getLastTryErrorMessage(int i) {
            if (i == 1) {
                return R.string.lock_last_pattern_attempt_before_wipe_device;
            }
            if (i == 2) {
                return R.string.lock_last_pattern_attempt_before_wipe_profile;
            }
            if (i == 3) {
                return R.string.lock_last_pattern_attempt_before_wipe_user;
            }
            throw new IllegalArgumentException("Unrecognized user type:" + i);
        }

        private void handleAttemptLockout(long j) {
            updateStage(Stage.LockedOut);
            this.mCountdownTimer = new CountDownTimer(j - SystemClock.elapsedRealtime(), 1000) {
                public void onTick(long j) {
                    ConfirmLockPatternFragment confirmLockPatternFragment = ConfirmLockPatternFragment.this;
                    confirmLockPatternFragment.mErrorTextView.setText(confirmLockPatternFragment.getString(R.string.lockpattern_too_many_failed_confirmation_attempts, Integer.valueOf((int) (j / 1000))));
                }

                public void onFinish() {
                    ConfirmLockPatternFragment.this.updateStage(Stage.NeedToUnlock);
                }
            }.start();
        }

        public void createAnimation(Object obj, long j, long j2, float f, boolean z, Interpolator interpolator, Runnable runnable) {
            Object obj2 = obj;
            if (obj2 instanceof LockPatternView.CellState) {
                this.mLockPatternView.startCellStateAnimation((LockPatternView.CellState) obj2, 1.0f, z ? 1.0f : 0.0f, z ? f : 0.0f, z ? 0.0f : f, z ? 0.0f : 1.0f, 1.0f, j, j2, interpolator, runnable);
                return;
            }
            this.mAppearAnimationUtils.createAnimation((View) obj2, j, j2, f, z, interpolator, runnable);
        }
    }

    /* renamed from: com.android.settings.password.ConfirmLockPattern$1 */
    static /* synthetic */ class C12141 {

        /* renamed from: $SwitchMap$com$android$settings$password$ConfirmLockPattern$Stage */
        static final /* synthetic */ int[] f98xec47707f;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                com.android.settings.password.ConfirmLockPattern$Stage[] r0 = com.android.settings.password.ConfirmLockPattern.Stage.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f98xec47707f = r0
                com.android.settings.password.ConfirmLockPattern$Stage r1 = com.android.settings.password.ConfirmLockPattern.Stage.NeedToUnlock     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f98xec47707f     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.settings.password.ConfirmLockPattern$Stage r1 = com.android.settings.password.ConfirmLockPattern.Stage.NeedToUnlockWrong     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f98xec47707f     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.settings.password.ConfirmLockPattern$Stage r1 = com.android.settings.password.ConfirmLockPattern.Stage.LockedOut     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.password.ConfirmLockPattern.C12141.<clinit>():void");
        }
    }
}
