package com.android.settings.security;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockscreenCredential;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.password.ChooseLockGeneric;
import com.android.settings.password.ChooseLockSettingsHelper;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.core.AbstractPreferenceController;

public class LockUnificationPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    private static final int MY_USER_ID = UserHandle.myUserId();
    private LockscreenCredential mCurrentDevicePassword = LockscreenCredential.createNone();
    private LockscreenCredential mCurrentProfilePassword = LockscreenCredential.createNone();
    private final DevicePolicyManager mDpm;
    private final SettingsPreferenceFragment mHost;
    private final LockPatternUtils mLockPatternUtils;
    private final int mProfileUserId;
    private boolean mRequireNewDevicePassword;
    private final UserManager mUm;
    private RestrictedSwitchPreference mUnifyProfile;

    public String getPreferenceKey() {
        return "unification";
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mUnifyProfile = (RestrictedSwitchPreference) preferenceScreen.findPreference("unification");
    }

    public LockUnificationPreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment) {
        super(context);
        this.mHost = settingsPreferenceFragment;
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        this.mUm = userManager;
        this.mDpm = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        this.mLockPatternUtils = FeatureFactory.getFactory(context).getSecurityFeatureProvider().getLockPatternUtils(context);
        this.mProfileUserId = Utils.getManagedProfileId(userManager, MY_USER_ID);
    }

    public boolean isAvailable() {
        int i = this.mProfileUserId;
        return i != -10000 && this.mLockPatternUtils.isSeparateProfileChallengeAllowed(i);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (Utils.startQuietModeDialogIfNecessary(this.mContext, this.mUm, this.mProfileUserId)) {
            return false;
        }
        if (((Boolean) obj).booleanValue()) {
            this.mRequireNewDevicePassword = !this.mDpm.isPasswordSufficientAfterProfileUnification(UserHandle.myUserId(), this.mProfileUserId);
            startUnification();
        } else {
            if (!new ChooseLockSettingsHelper.Builder(this.mHost.getActivity(), this.mHost).setRequestCode(130).setTitle(this.mContext.getString(R.string.unlock_set_unlock_launch_picker_title)).setReturnCredentials(true).setUserId(MY_USER_ID).show()) {
                ununifyLocks();
            }
        }
        return true;
    }

    public void updateState(Preference preference) {
        if (this.mUnifyProfile != null) {
            boolean isSeparateProfileChallengeEnabled = this.mLockPatternUtils.isSeparateProfileChallengeEnabled(this.mProfileUserId);
            this.mUnifyProfile.setChecked(!isSeparateProfileChallengeEnabled);
            if (isSeparateProfileChallengeEnabled) {
                this.mUnifyProfile.setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, "no_unified_password", this.mProfileUserId));
            }
        }
    }

    public boolean handleActivityResult(int i, int i2, Intent intent) {
        if (i == 130 && i2 == -1) {
            this.mCurrentDevicePassword = intent.getParcelableExtra("password");
            ununifyLocks();
            return true;
        } else if (i != 129 || i2 != -1) {
            return false;
        } else {
            this.mCurrentProfilePassword = intent.getParcelableExtra("password");
            unifyLocks();
            return true;
        }
    }

    private void ununifyLocks() {
        Bundle bundle = new Bundle();
        bundle.putInt("android.intent.extra.USER_ID", this.mProfileUserId);
        bundle.putParcelable("password", this.mCurrentDevicePassword);
        new SubSettingLauncher(this.mContext).setDestination(ChooseLockGeneric.ChooseLockGenericFragment.class.getName()).setSourceMetricsCategory(this.mHost.getMetricsCategory()).setArguments(bundle).launch();
    }

    public void startUnification() {
        if (!new ChooseLockSettingsHelper.Builder(this.mHost.getActivity(), this.mHost).setRequestCode(129).setTitle(this.mContext.getString(R.string.unlock_set_unlock_launch_picker_title_profile)).setReturnCredentials(true).setUserId(this.mProfileUserId).show()) {
            unifyLocks();
        }
    }

    private void unifyLocks() {
        if (this.mRequireNewDevicePassword) {
            promptForNewDeviceLockAndThenUnify();
        } else {
            unifyKeepingDeviceLock();
        }
        LockscreenCredential lockscreenCredential = this.mCurrentDevicePassword;
        if (lockscreenCredential != null) {
            lockscreenCredential.zeroize();
            this.mCurrentDevicePassword = null;
        }
        LockscreenCredential lockscreenCredential2 = this.mCurrentProfilePassword;
        if (lockscreenCredential2 != null) {
            lockscreenCredential2.zeroize();
            this.mCurrentProfilePassword = null;
        }
    }

    private void unifyKeepingDeviceLock() {
        this.mLockPatternUtils.setSeparateProfileChallengeEnabled(this.mProfileUserId, false, this.mCurrentProfilePassword);
    }

    private void promptForNewDeviceLockAndThenUnify() {
        Bundle bundle = new Bundle();
        bundle.putInt("unification_profile_id", this.mProfileUserId);
        bundle.putParcelable("unification_profile_credential", this.mCurrentProfilePassword);
        new SubSettingLauncher(this.mContext).setDestination(ChooseLockGeneric.ChooseLockGenericFragment.class.getName()).setTitleRes(R.string.lock_settings_picker_title).setSourceMetricsCategory(this.mHost.getMetricsCategory()).setArguments(bundle).launch();
    }
}
