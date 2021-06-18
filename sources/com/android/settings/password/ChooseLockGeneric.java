package com.android.settings.password;

import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.service.persistentdata.PersistentDataBlockManager;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockscreenCredential;
import com.android.settings.EncryptionInterstitial;
import com.android.settings.LinkifyUtils;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricEnrollActivity;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.password.ChooseLockGenericController;
import com.android.settings.password.ChooseLockPassword;
import com.android.settings.password.ChooseLockPattern;
import com.android.settings.password.ChooseLockSettingsHelper;
import com.android.settingslib.RestrictedPreference;
import com.google.android.setupcompat.util.WizardManagerHelper;

public class ChooseLockGeneric extends SettingsActivity {

    public static class InternalActivity extends ChooseLockGeneric {
    }

    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        intent.putExtra(":settings:show_fragment", getFragmentClass().getName());
        return intent;
    }

    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return ChooseLockGenericFragment.class.getName().equals(str);
    }

    /* access modifiers changed from: package-private */
    public Class<? extends Fragment> getFragmentClass() {
        return ChooseLockGenericFragment.class;
    }

    public static class ChooseLockGenericFragment extends SettingsPreferenceFragment {
        static final int CHOOSE_LOCK_BEFORE_BIOMETRIC_REQUEST = 103;
        static final int CHOOSE_LOCK_REQUEST = 102;
        static final int CONFIRM_EXISTING_REQUEST = 100;
        static final int ENABLE_ENCRYPTION_REQUEST = 101;
        static final int SKIP_FINGERPRINT_REQUEST = 104;
        private String mCallerAppName = null;
        private ChooseLockGenericController mController;
        private DevicePolicyManager mDpm;
        private FaceManager mFaceManager;
        private FingerprintManager mFingerprintManager;
        protected boolean mForBiometrics = false;
        private boolean mForChangeCredRequiredForBoot = false;
        protected boolean mForFace = false;
        protected boolean mForFingerprint = false;
        private boolean mIsCallingAppAdmin;
        private boolean mIsManagedProfile;
        private boolean mIsSetNewPassword = false;
        private LockPatternUtils mLockPatternUtils;
        private ManagedLockPasswordProvider mManagedPasswordProvider;
        private boolean mOnlyEnforceDevicePasswordRequirement = false;
        private boolean mPasswordConfirmed = false;
        private boolean mRequestGatekeeperPasswordHandle = false;
        private int mRequestedMinComplexity;
        private LockscreenCredential mUnificationProfileCredential;
        private int mUnificationProfileId = -10000;
        private int mUserId;
        private UserManager mUserManager;
        private LockscreenCredential mUserPassword;
        private boolean mWaitingForConfirmation = false;

        /* access modifiers changed from: protected */
        public boolean alwaysHideInsecureScreenLockTypes() {
            return false;
        }

        public int getHelpResource() {
            return R.string.help_url_choose_lockscreen;
        }

        public int getMetricsCategory() {
            return 27;
        }

        public void onCreate(Bundle bundle) {
            String str;
            super.onCreate(bundle);
            FragmentActivity activity = getActivity();
            Bundle arguments = getArguments();
            if (WizardManagerHelper.isDeviceProvisioned(activity) || canRunBeforeDeviceProvisioned()) {
                Intent intent = activity.getIntent();
                String action = intent.getAction();
                this.mFingerprintManager = Utils.getFingerprintManagerOrNull(activity);
                this.mFaceManager = Utils.getFaceManagerOrNull(activity);
                this.mDpm = (DevicePolicyManager) getSystemService("device_policy");
                this.mLockPatternUtils = new LockPatternUtils(activity);
                boolean z = true;
                this.mIsSetNewPassword = "android.app.action.SET_NEW_PARENT_PROFILE_PASSWORD".equals(action) || "android.app.action.SET_NEW_PASSWORD".equals(action);
                boolean booleanExtra = intent.getBooleanExtra("confirm_credentials", true);
                if (activity instanceof InternalActivity) {
                    this.mPasswordConfirmed = !booleanExtra;
                    this.mUserPassword = intent.getParcelableExtra("password");
                } else if (arguments != null) {
                    LockscreenCredential parcelable = arguments.getParcelable("password");
                    this.mUserPassword = parcelable;
                    this.mPasswordConfirmed = parcelable != null;
                }
                this.mRequestGatekeeperPasswordHandle = intent.getBooleanExtra("request_gk_pw_handle", false);
                this.mForFingerprint = intent.getBooleanExtra("for_fingerprint", false);
                this.mForFace = intent.getBooleanExtra("for_face", false);
                this.mForBiometrics = intent.getBooleanExtra("for_biometrics", false);
                this.mRequestedMinComplexity = intent.getIntExtra("requested_min_complexity", 0);
                this.mOnlyEnforceDevicePasswordRequirement = intent.getBooleanExtra("device_password_requirement_only", false);
                this.mIsCallingAppAdmin = intent.getBooleanExtra("is_calling_app_admin", false);
                this.mForChangeCredRequiredForBoot = arguments != null && arguments.getBoolean("for_cred_req_boot");
                this.mUserManager = UserManager.get(activity);
                if (arguments != null) {
                    this.mUnificationProfileCredential = arguments.getParcelable("unification_profile_credential");
                    this.mUnificationProfileId = arguments.getInt("unification_profile_id", -10000);
                }
                if (bundle != null) {
                    this.mPasswordConfirmed = bundle.getBoolean("password_confirmed");
                    this.mWaitingForConfirmation = bundle.getBoolean("waiting_for_confirmation");
                    this.mUserPassword = bundle.getParcelable("password");
                }
                this.mUserId = Utils.getSecureTargetUser(activity.getActivityToken(), UserManager.get(activity), arguments, intent.getExtras()).getIdentifier();
                this.mIsManagedProfile = UserManager.get(getActivity()).isManagedProfile(this.mUserId);
                ChooseLockGenericController build = new ChooseLockGenericController.Builder(getContext(), this.mUserId, this.mLockPatternUtils).setAppRequestedMinComplexity(this.mRequestedMinComplexity).setEnforceDevicePasswordRequirementOnly(this.mOnlyEnforceDevicePasswordRequirement).setProfileToUnify(this.mUnificationProfileId).setHideInsecureScreenLockTypes(alwaysHideInsecureScreenLockTypes() || intent.getBooleanExtra("hide_insecure_options", false)).build();
                this.mController = build;
                if (build.isComplexityProvidedByAdmin()) {
                    str = null;
                } else {
                    str = intent.getStringExtra("caller_app_name");
                }
                this.mCallerAppName = str;
                this.mManagedPasswordProvider = ManagedLockPasswordProvider.get(activity, this.mUserId);
                if (this.mPasswordConfirmed) {
                    if (bundle == null) {
                        z = false;
                    }
                    updatePreferencesOrFinish(z);
                    if (this.mForChangeCredRequiredForBoot) {
                        maybeEnableEncryption(this.mLockPatternUtils.getKeyguardStoredPasswordQuality(this.mUserId), false);
                    }
                } else if (!this.mWaitingForConfirmation) {
                    ChooseLockSettingsHelper.Builder builder = new ChooseLockSettingsHelper.Builder(activity, this);
                    builder.setRequestCode(100).setTitle(getString(R.string.unlock_set_unlock_launch_picker_title)).setReturnCredentials(true).setUserId(this.mUserId);
                    if (((this.mIsManagedProfile && !this.mLockPatternUtils.isSeparateProfileChallengeEnabled(this.mUserId)) && !this.mIsSetNewPassword) || !builder.show()) {
                        this.mPasswordConfirmed = true;
                        if (bundle == null) {
                            z = false;
                        }
                        updatePreferencesOrFinish(z);
                    } else {
                        this.mWaitingForConfirmation = true;
                    }
                }
                addHeaderView();
                return;
            }
            Log.i("ChooseLockGenericFragment", "Refusing to start because device is not provisioned");
            activity.finish();
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            updateActivityTitle();
            return super.onCreateView(layoutInflater, viewGroup, bundle);
        }

        private void updateActivityTitle() {
            LockPatternUtils lockPatternUtils = this.mLockPatternUtils;
            if (lockPatternUtils != null) {
                if (this.mIsManagedProfile) {
                    if (lockPatternUtils.isSeparateProfileChallengeEnabled(this.mUserId)) {
                        getActivity().setTitle(R.string.lock_settings_picker_update_profile_lock_title);
                    } else {
                        getActivity().setTitle(R.string.lock_settings_picker_new_profile_lock_title);
                    }
                } else if (lockPatternUtils.isSecure(this.mUserId)) {
                    getActivity().setTitle(R.string.lock_settings_picker_update_lock_title);
                } else {
                    getActivity().setTitle(R.string.lock_settings_picker_new_lock_title);
                }
            }
        }

        /* access modifiers changed from: protected */
        public boolean canRunBeforeDeviceProvisioned() {
            PersistentDataBlockManager persistentDataBlockManager = (PersistentDataBlockManager) getSystemService("persistent_data_block");
            return persistentDataBlockManager == null || persistentDataBlockManager.getDataBlockSize() == 0;
        }

        /* access modifiers changed from: protected */
        public Class<? extends InternalActivity> getInternalActivityClass() {
            return InternalActivity.class;
        }

        /* access modifiers changed from: protected */
        public void addHeaderView() {
            setHeaderView(R.layout.choose_lock_generic_biometric_header);
            TextView textView = (TextView) getHeaderView().findViewById(R.id.biometric_header_description);
            if (this.mForFingerprint) {
                if (this.mIsSetNewPassword) {
                    textView.setText(R.string.fingerprint_unlock_title);
                } else {
                    textView.setText(R.string.lock_settings_picker_biometric_message);
                }
            } else if (this.mForFace) {
                if (this.mIsSetNewPassword) {
                    textView.setText(R.string.face_unlock_title);
                } else {
                    textView.setText(R.string.lock_settings_picker_biometric_message);
                }
            } else if (this.mForBiometrics) {
                if (this.mIsSetNewPassword) {
                    textView.setText(R.string.biometrics_unlock_title);
                } else {
                    textView.setText(R.string.lock_settings_picker_biometric_message);
                }
            } else if (this.mIsManagedProfile) {
                textView.setText(R.string.lock_settings_picker_profile_message);
            } else {
                int managedProfileId = Utils.getManagedProfileId(this.mUserManager, this.mUserId);
                if (!this.mController.isScreenLockRestrictedByAdmin() || managedProfileId == -10000) {
                    textView.setText("");
                } else {
                    LinkifyUtils.linkify(textView, new StringBuilder(getText(R.string.lock_settings_picker_admin_restricted_personal_message)), new C1193x320961c5(this, managedProfileId));
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$addHeaderView$0(int i) {
            Bundle bundle = new Bundle();
            bundle.putInt("android.intent.extra.USER_ID", i);
            LockscreenCredential lockscreenCredential = this.mUserPassword;
            if (lockscreenCredential != null) {
                bundle.putParcelable("password", lockscreenCredential);
            }
            new SubSettingLauncher(getActivity()).setDestination(ChooseLockGenericFragment.class.getName()).setSourceMetricsCategory(getMetricsCategory()).setArguments(bundle).launch();
            finish();
        }

        public boolean onPreferenceTreeClick(Preference preference) {
            writePreferenceClickMetric(preference);
            String key = preference.getKey();
            if (!isUnlockMethodSecure(key) && this.mLockPatternUtils.isSecure(this.mUserId)) {
                showFactoryResetProtectionWarningDialog(key);
                return true;
            } else if (!"unlock_skip_fingerprint".equals(key) && !"unlock_skip_face".equals(key) && !"unlock_skip_biometrics".equals(key)) {
                return setUnlockMethod(key);
            } else {
                Intent intent = new Intent(getActivity(), getInternalActivityClass());
                intent.setAction(getIntent().getAction());
                intent.putExtra("android.intent.extra.USER_ID", this.mUserId);
                intent.putExtra("confirm_credentials", !this.mPasswordConfirmed);
                intent.putExtra("requested_min_complexity", this.mRequestedMinComplexity);
                intent.putExtra("device_password_requirement_only", this.mOnlyEnforceDevicePasswordRequirement);
                intent.putExtra("caller_app_name", this.mCallerAppName);
                LockscreenCredential lockscreenCredential = this.mUserPassword;
                if (lockscreenCredential != null) {
                    intent.putExtra("password", lockscreenCredential);
                }
                startActivityForResult(intent, 104);
                return true;
            }
        }

        private void maybeEnableEncryption(int i, boolean z) {
            DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService("device_policy");
            if (UserManager.get(getActivity()).isAdminUser() && this.mUserId == UserHandle.myUserId() && LockPatternUtils.isDeviceEncryptionEnabled() && !LockPatternUtils.isFileEncryptionEnabled() && !devicePolicyManager.getDoNotAskCredentialsOnBoot()) {
                Intent intentForUnlockMethod = getIntentForUnlockMethod(i);
                intentForUnlockMethod.putExtra("for_cred_req_boot", this.mForChangeCredRequiredForBoot);
                FragmentActivity activity = getActivity();
                Intent encryptionInterstitialIntent = getEncryptionInterstitialIntent(activity, i, this.mLockPatternUtils.isCredentialRequiredToDecrypt(!AccessibilityManager.getInstance(activity).isEnabled()), intentForUnlockMethod);
                encryptionInterstitialIntent.putExtra("for_fingerprint", this.mForFingerprint);
                encryptionInterstitialIntent.putExtra("for_face", this.mForFace);
                encryptionInterstitialIntent.putExtra("for_biometrics", this.mForBiometrics);
                startActivityForResult(encryptionInterstitialIntent, (!this.mIsSetNewPassword || !this.mRequestGatekeeperPasswordHandle) ? 101 : 103);
            } else if (this.mForChangeCredRequiredForBoot) {
                finish();
            } else {
                updateUnlockMethodAndFinish(i, z, false);
            }
        }

        public void onActivityResult(int i, int i2, Intent intent) {
            super.onActivityResult(i, i2, intent);
            this.mWaitingForConfirmation = false;
            if (i == 100 && i2 == -1) {
                this.mPasswordConfirmed = true;
                this.mUserPassword = intent != null ? (LockscreenCredential) intent.getParcelableExtra("password") : null;
                updatePreferencesOrFinish(false);
                if (this.mForChangeCredRequiredForBoot) {
                    LockscreenCredential lockscreenCredential = this.mUserPassword;
                    if (lockscreenCredential == null || lockscreenCredential.isNone()) {
                        finish();
                    } else {
                        maybeEnableEncryption(this.mLockPatternUtils.getKeyguardStoredPasswordQuality(this.mUserId), false);
                    }
                }
            } else if (i == 102 || i == 101) {
                if (i2 != 0 || this.mForChangeCredRequiredForBoot) {
                    getActivity().setResult(i2, intent);
                    finish();
                } else if (getIntent().getIntExtra("lockscreen.password_type", -1) != -1) {
                    getActivity().setResult(0, intent);
                    finish();
                }
            } else if (i == 103 && i2 == 1) {
                Intent biometricEnrollIntent = getBiometricEnrollIntent(getActivity());
                if (intent != null) {
                    biometricEnrollIntent.putExtras(intent.getExtras());
                }
                biometricEnrollIntent.putExtra("android.intent.extra.USER_ID", this.mUserId);
                startActivity(biometricEnrollIntent);
                finish();
            } else if (i == 104) {
                if (i2 != 0) {
                    FragmentActivity activity = getActivity();
                    if (i2 == 1) {
                        i2 = -1;
                    }
                    activity.setResult(i2, intent);
                    finish();
                }
            } else if (i != 501) {
                getActivity().setResult(0);
                finish();
            } else {
                return;
            }
            if (i == 0 && this.mForChangeCredRequiredForBoot) {
                finish();
            }
        }

        /* access modifiers changed from: protected */
        public Intent getBiometricEnrollIntent(Context context) {
            Intent intent = new Intent(context, BiometricEnrollActivity.InternalActivity.class);
            intent.putExtra("skip_intro", true);
            return intent;
        }

        public void onSaveInstanceState(Bundle bundle) {
            super.onSaveInstanceState(bundle);
            bundle.putBoolean("password_confirmed", this.mPasswordConfirmed);
            bundle.putBoolean("waiting_for_confirmation", this.mWaitingForConfirmation);
            LockscreenCredential lockscreenCredential = this.mUserPassword;
            if (lockscreenCredential != null) {
                bundle.putParcelable("password", lockscreenCredential.duplicate());
            }
        }

        /* access modifiers changed from: package-private */
        public void updatePreferencesOrFinish(boolean z) {
            int i;
            Intent intent = getActivity().getIntent();
            if (StorageManager.isFileEncryptedNativeOrEmulated()) {
                i = intent.getIntExtra("lockscreen.password_type", -1);
            } else {
                Log.i("ChooseLockGenericFragment", "Ignoring PASSWORD_TYPE_KEY because device is not file encrypted");
                i = -1;
            }
            if (i == -1) {
                PreferenceScreen preferenceScreen = getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceScreen.removeAll();
                }
                addPreferences();
                disableUnusablePreferences();
                updatePreferenceText();
                updateCurrentPreference();
                updatePreferenceSummaryIfNeeded();
            } else if (!z) {
                updateUnlockMethodAndFinish(i, false, true);
            }
        }

        /* access modifiers changed from: protected */
        public void addPreferences() {
            addPreferencesFromResource(R.xml.security_settings_picker);
            Preference findPreference = findPreference("lock_settings_footer");
            if (TextUtils.isEmpty(this.mCallerAppName) || this.mIsCallingAppAdmin) {
                findPreference.setVisible(false);
            } else {
                findPreference.setVisible(true);
                findPreference.setTitle((CharSequence) getFooterString());
            }
            findPreference(ScreenLockType.NONE.preferenceKey).setViewId(R.id.lock_none);
            findPreference("unlock_skip_fingerprint").setViewId(R.id.lock_none);
            findPreference("unlock_skip_face").setViewId(R.id.lock_none);
            findPreference("unlock_skip_biometrics").setViewId(R.id.lock_none);
            findPreference(ScreenLockType.PIN.preferenceKey).setViewId(R.id.lock_pin);
            findPreference(ScreenLockType.PASSWORD.preferenceKey).setViewId(R.id.lock_password);
        }

        private String getFooterString() {
            int aggregatedPasswordComplexity = this.mController.getAggregatedPasswordComplexity();
            return getResources().getString(aggregatedPasswordComplexity != 65536 ? aggregatedPasswordComplexity != 196608 ? aggregatedPasswordComplexity != 327680 ? R.string.unlock_footer_none_complexity_requested : R.string.unlock_footer_high_complexity_requested : R.string.unlock_footer_medium_complexity_requested : R.string.unlock_footer_low_complexity_requested, new Object[]{this.mCallerAppName});
        }

        private void updatePreferenceText() {
            if (this.mForFingerprint) {
                setPreferenceTitle(ScreenLockType.PATTERN, (int) R.string.fingerprint_unlock_set_unlock_pattern);
                setPreferenceTitle(ScreenLockType.PIN, (int) R.string.fingerprint_unlock_set_unlock_pin);
                setPreferenceTitle(ScreenLockType.PASSWORD, (int) R.string.fingerprint_unlock_set_unlock_password);
            } else if (this.mForFace) {
                setPreferenceTitle(ScreenLockType.PATTERN, (int) R.string.face_unlock_set_unlock_pattern);
                setPreferenceTitle(ScreenLockType.PIN, (int) R.string.face_unlock_set_unlock_pin);
                setPreferenceTitle(ScreenLockType.PASSWORD, (int) R.string.face_unlock_set_unlock_password);
            } else if (this.mForBiometrics) {
                setPreferenceTitle(ScreenLockType.PATTERN, (int) R.string.biometrics_unlock_set_unlock_pattern);
                setPreferenceTitle(ScreenLockType.PIN, (int) R.string.biometrics_unlock_set_unlock_pin);
                setPreferenceTitle(ScreenLockType.PASSWORD, (int) R.string.biometrics_unlock_set_unlock_password);
            }
            if (this.mManagedPasswordProvider.isSettingManagedPasswordSupported()) {
                setPreferenceTitle(ScreenLockType.MANAGED, this.mManagedPasswordProvider.getPickerOptionTitle(this.mForFingerprint));
            } else {
                removePreference(ScreenLockType.MANAGED.preferenceKey);
            }
            if (!this.mForFingerprint || !this.mIsSetNewPassword) {
                removePreference("unlock_skip_fingerprint");
            }
            if (!this.mForFace || !this.mIsSetNewPassword) {
                removePreference("unlock_skip_face");
            }
            if (!this.mForBiometrics || !this.mIsSetNewPassword) {
                removePreference("unlock_skip_biometrics");
            }
        }

        private void setPreferenceTitle(ScreenLockType screenLockType, int i) {
            Preference findPreference = findPreference(screenLockType.preferenceKey);
            if (findPreference != null) {
                findPreference.setTitle(i);
            }
        }

        private void setPreferenceTitle(ScreenLockType screenLockType, CharSequence charSequence) {
            Preference findPreference = findPreference(screenLockType.preferenceKey);
            if (findPreference != null) {
                findPreference.setTitle(charSequence);
            }
        }

        private void setPreferenceSummary(ScreenLockType screenLockType, int i) {
            Preference findPreference = findPreference(screenLockType.preferenceKey);
            if (findPreference != null) {
                findPreference.setSummary(i);
            }
        }

        private void updateCurrentPreference() {
            Preference findPreference = findPreference(getKeyForCurrent());
            if (findPreference != null) {
                findPreference.setSummary((int) R.string.current_screen_lock);
            }
        }

        private String getKeyForCurrent() {
            int credentialOwnerProfile = UserManager.get(getContext()).getCredentialOwnerProfile(this.mUserId);
            if (this.mLockPatternUtils.isLockScreenDisabled(credentialOwnerProfile)) {
                return ScreenLockType.NONE.preferenceKey;
            }
            ScreenLockType fromQuality = ScreenLockType.fromQuality(this.mLockPatternUtils.getKeyguardStoredPasswordQuality(credentialOwnerProfile));
            if (fromQuality != null) {
                return fromQuality.preferenceKey;
            }
            return null;
        }

        private void disableUnusablePreferences() {
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            for (ScreenLockType screenLockType : ScreenLockType.values()) {
                Preference findPreference = findPreference(screenLockType.preferenceKey);
                if (findPreference instanceof RestrictedPreference) {
                    boolean isScreenLockVisible = this.mController.isScreenLockVisible(screenLockType);
                    boolean isScreenLockEnabled = this.mController.isScreenLockEnabled(screenLockType);
                    if (!isScreenLockVisible) {
                        preferenceScreen.removePreference(findPreference);
                    } else if (!isScreenLockEnabled) {
                        findPreference.setEnabled(false);
                    }
                }
            }
        }

        private void updatePreferenceSummaryIfNeeded() {
            if (StorageManager.isBlockEncrypted() && !StorageManager.isNonDefaultBlockEncrypted() && !AccessibilityManager.getInstance(getActivity()).getEnabledAccessibilityServiceList(-1).isEmpty()) {
                setPreferenceSummary(ScreenLockType.PATTERN, R.string.secure_lock_encryption_warning);
                setPreferenceSummary(ScreenLockType.PIN, R.string.secure_lock_encryption_warning);
                setPreferenceSummary(ScreenLockType.PASSWORD, R.string.secure_lock_encryption_warning);
                setPreferenceSummary(ScreenLockType.MANAGED, R.string.secure_lock_encryption_warning);
            }
        }

        /* access modifiers changed from: protected */
        public Intent getLockManagedPasswordIntent(LockscreenCredential lockscreenCredential) {
            return this.mManagedPasswordProvider.createIntent(false, lockscreenCredential);
        }

        /* access modifiers changed from: protected */
        public Intent getLockPasswordIntent(int i) {
            ChooseLockPassword.IntentBuilder requestGatekeeperPasswordHandle = new ChooseLockPassword.IntentBuilder(getContext()).setPasswordType(i).setPasswordRequirement(this.mController.getAggregatedPasswordComplexity(), this.mController.getAggregatedPasswordMetrics()).setForFingerprint(this.mForFingerprint).setForFace(this.mForFace).setForBiometrics(this.mForBiometrics).setUserId(this.mUserId).setRequestGatekeeperPasswordHandle(this.mRequestGatekeeperPasswordHandle);
            LockscreenCredential lockscreenCredential = this.mUserPassword;
            if (lockscreenCredential != null) {
                requestGatekeeperPasswordHandle.setPassword(lockscreenCredential);
            }
            int i2 = this.mUnificationProfileId;
            if (i2 != -10000) {
                requestGatekeeperPasswordHandle.setProfileToUnify(i2, this.mUnificationProfileCredential);
            }
            return requestGatekeeperPasswordHandle.build();
        }

        /* access modifiers changed from: protected */
        public Intent getLockPatternIntent() {
            ChooseLockPattern.IntentBuilder requestGatekeeperPasswordHandle = new ChooseLockPattern.IntentBuilder(getContext()).setForFingerprint(this.mForFingerprint).setForFace(this.mForFace).setForBiometrics(this.mForBiometrics).setUserId(this.mUserId).setRequestGatekeeperPasswordHandle(this.mRequestGatekeeperPasswordHandle);
            LockscreenCredential lockscreenCredential = this.mUserPassword;
            if (lockscreenCredential != null) {
                requestGatekeeperPasswordHandle.setPattern(lockscreenCredential);
            }
            int i = this.mUnificationProfileId;
            if (i != -10000) {
                requestGatekeeperPasswordHandle.setProfileToUnify(i, this.mUnificationProfileCredential);
            }
            return requestGatekeeperPasswordHandle.build();
        }

        /* access modifiers changed from: protected */
        public Intent getEncryptionInterstitialIntent(Context context, int i, boolean z, Intent intent) {
            return EncryptionInterstitial.createStartIntent(context, i, z, intent);
        }

        /* access modifiers changed from: package-private */
        public void updateUnlockMethodAndFinish(int i, boolean z, boolean z2) {
            if (this.mPasswordConfirmed) {
                int upgradeQuality = this.mController.upgradeQuality(i);
                Intent intentForUnlockMethod = getIntentForUnlockMethod(upgradeQuality);
                if (intentForUnlockMethod != null) {
                    if (getIntent().getBooleanExtra("show_options_button", false)) {
                        intentForUnlockMethod.putExtra("show_options_button", z2);
                    }
                    intentForUnlockMethod.putExtra("choose_lock_generic_extras", getIntent().getExtras());
                    startActivityForResult(intentForUnlockMethod, (!this.mIsSetNewPassword || !this.mRequestGatekeeperPasswordHandle) ? 102 : 103);
                } else if (upgradeQuality == 0) {
                    if (this.mUserPassword != null) {
                        this.mLockPatternUtils.setLockCredential(LockscreenCredential.createNone(), this.mUserPassword, this.mUserId);
                    }
                    this.mLockPatternUtils.setLockScreenDisabled(z, this.mUserId);
                    getActivity().setResult(-1);
                    finish();
                }
            } else {
                throw new IllegalStateException("Tried to update password without confirming it");
            }
        }

        private Intent getIntentForUnlockMethod(int i) {
            if (i >= 524288) {
                return getLockManagedPasswordIntent(this.mUserPassword);
            }
            if (i >= 131072) {
                return getLockPasswordIntent(i);
            }
            if (i == 65536) {
                return getLockPatternIntent();
            }
            return null;
        }

        public void onDestroy() {
            super.onDestroy();
            LockscreenCredential lockscreenCredential = this.mUserPassword;
            if (lockscreenCredential != null) {
                lockscreenCredential.zeroize();
            }
            System.gc();
            System.runFinalization();
            System.gc();
        }

        private int getResIdForFactoryResetProtectionWarningTitle() {
            return this.mIsManagedProfile ? R.string.unlock_disable_frp_warning_title_profile : R.string.unlock_disable_frp_warning_title;
        }

        private int getResIdForFactoryResetProtectionWarningMessage() {
            FingerprintManager fingerprintManager = this.mFingerprintManager;
            boolean z = false;
            boolean hasEnrolledFingerprints = (fingerprintManager == null || !fingerprintManager.isHardwareDetected()) ? false : this.mFingerprintManager.hasEnrolledFingerprints(this.mUserId);
            FaceManager faceManager = this.mFaceManager;
            if (faceManager != null && faceManager.isHardwareDetected()) {
                z = this.mFaceManager.hasEnrolledTemplates(this.mUserId);
            }
            int keyguardStoredPasswordQuality = this.mLockPatternUtils.getKeyguardStoredPasswordQuality(this.mUserId);
            if (keyguardStoredPasswordQuality != 65536) {
                if (keyguardStoredPasswordQuality == 131072 || keyguardStoredPasswordQuality == 196608) {
                    if (hasEnrolledFingerprints && z) {
                        return R.string.unlock_disable_frp_warning_content_pin_face_fingerprint;
                    }
                    if (hasEnrolledFingerprints) {
                        return R.string.unlock_disable_frp_warning_content_pin_fingerprint;
                    }
                    return z ? R.string.unlock_disable_frp_warning_content_pin_face : R.string.unlock_disable_frp_warning_content_pin;
                } else if (keyguardStoredPasswordQuality == 262144 || keyguardStoredPasswordQuality == 327680 || keyguardStoredPasswordQuality == 393216 || keyguardStoredPasswordQuality == 524288) {
                    if (hasEnrolledFingerprints && z) {
                        return R.string.unlock_disable_frp_warning_content_password_face_fingerprint;
                    }
                    if (hasEnrolledFingerprints) {
                        return R.string.unlock_disable_frp_warning_content_password_fingerprint;
                    }
                    return z ? R.string.unlock_disable_frp_warning_content_password_face : R.string.unlock_disable_frp_warning_content_password;
                } else if (hasEnrolledFingerprints && z) {
                    return R.string.unlock_disable_frp_warning_content_unknown_face_fingerprint;
                } else {
                    if (hasEnrolledFingerprints) {
                        return R.string.unlock_disable_frp_warning_content_unknown_fingerprint;
                    }
                    return z ? R.string.unlock_disable_frp_warning_content_unknown_face : R.string.unlock_disable_frp_warning_content_unknown;
                }
            } else if (hasEnrolledFingerprints && z) {
                return R.string.unlock_disable_frp_warning_content_pattern_face_fingerprint;
            } else {
                if (hasEnrolledFingerprints) {
                    return R.string.unlock_disable_frp_warning_content_pattern_fingerprint;
                }
                return z ? R.string.unlock_disable_frp_warning_content_pattern_face : R.string.unlock_disable_frp_warning_content_pattern;
            }
        }

        private boolean isUnlockMethodSecure(String str) {
            return !ScreenLockType.SWIPE.preferenceKey.equals(str) && !ScreenLockType.NONE.preferenceKey.equals(str);
        }

        /* access modifiers changed from: private */
        public boolean setUnlockMethod(String str) {
            EventLog.writeEvent(90200, str);
            ScreenLockType fromKey = ScreenLockType.fromKey(str);
            if (fromKey != null) {
                switch (C11921.$SwitchMap$com$android$settings$password$ScreenLockType[fromKey.ordinal()]) {
                    case 1:
                    case 2:
                        updateUnlockMethodAndFinish(fromKey.defaultQuality, fromKey == ScreenLockType.NONE, false);
                        return true;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        maybeEnableEncryption(fromKey.defaultQuality, false);
                        return true;
                }
            }
            Log.e("ChooseLockGenericFragment", "Encountered unknown unlock method to set: " + str);
            return false;
        }

        private void showFactoryResetProtectionWarningDialog(String str) {
            FactoryResetProtectionWarningDialog.newInstance(getResIdForFactoryResetProtectionWarningTitle(), getResIdForFactoryResetProtectionWarningMessage(), str).show(getChildFragmentManager(), "frp_warning_dialog");
        }

        public static class FactoryResetProtectionWarningDialog extends InstrumentedDialogFragment {
            public int getMetricsCategory() {
                return 528;
            }

            public static FactoryResetProtectionWarningDialog newInstance(int i, int i2, String str) {
                FactoryResetProtectionWarningDialog factoryResetProtectionWarningDialog = new FactoryResetProtectionWarningDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("titleRes", i);
                bundle.putInt("messageRes", i2);
                bundle.putString("unlockMethodToSet", str);
                factoryResetProtectionWarningDialog.setArguments(bundle);
                return factoryResetProtectionWarningDialog;
            }

            public void show(FragmentManager fragmentManager, String str) {
                if (fragmentManager.findFragmentByTag(str) == null) {
                    super.show(fragmentManager, str);
                }
            }

            public Dialog onCreateDialog(Bundle bundle) {
                Bundle arguments = getArguments();
                return new AlertDialog.Builder(getActivity()).setTitle(arguments.getInt("titleRes")).setMessage(arguments.getInt("messageRes")).setPositiveButton((int) R.string.unlock_disable_frp_warning_ok, (DialogInterface.OnClickListener) new C1195x10535b08(this, arguments)).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) new C1194x10535b07(this)).create();
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onCreateDialog$0(Bundle bundle, DialogInterface dialogInterface, int i) {
                boolean unused = ((ChooseLockGenericFragment) getParentFragment()).setUnlockMethod(bundle.getString("unlockMethodToSet"));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        }
    }

    /* renamed from: com.android.settings.password.ChooseLockGeneric$1 */
    static /* synthetic */ class C11921 {
        static final /* synthetic */ int[] $SwitchMap$com$android$settings$password$ScreenLockType;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.settings.password.ScreenLockType[] r0 = com.android.settings.password.ScreenLockType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$settings$password$ScreenLockType = r0
                com.android.settings.password.ScreenLockType r1 = com.android.settings.password.ScreenLockType.NONE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$settings$password$ScreenLockType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.settings.password.ScreenLockType r1 = com.android.settings.password.ScreenLockType.SWIPE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$settings$password$ScreenLockType     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.settings.password.ScreenLockType r1 = com.android.settings.password.ScreenLockType.PATTERN     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$android$settings$password$ScreenLockType     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.settings.password.ScreenLockType r1 = com.android.settings.password.ScreenLockType.PIN     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$android$settings$password$ScreenLockType     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.settings.password.ScreenLockType r1 = com.android.settings.password.ScreenLockType.PASSWORD     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$android$settings$password$ScreenLockType     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.android.settings.password.ScreenLockType r1 = com.android.settings.password.ScreenLockType.MANAGED     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.password.ChooseLockGeneric.C11921.<clinit>():void");
        }
    }
}
