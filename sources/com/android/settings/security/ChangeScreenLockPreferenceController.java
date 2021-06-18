package com.android.settings.security;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.password.ChooseLockGeneric;
import com.android.settings.security.screenlock.ScreenLockSettings;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class ChangeScreenLockPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, GearPreference.OnGearClickListener {
    protected final DevicePolicyManager mDPM;
    protected final SettingsPreferenceFragment mHost;
    protected final LockPatternUtils mLockPatternUtils;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    protected RestrictedPreference mPreference;
    protected final int mProfileChallengeUserId;
    protected final UserManager mUm;
    protected final int mUserId;

    public String getPreferenceKey() {
        return "unlock_set_or_change";
    }

    public ChangeScreenLockPreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment) {
        super(context);
        int myUserId = UserHandle.myUserId();
        this.mUserId = myUserId;
        UserManager userManager = (UserManager) context.getSystemService("user");
        this.mUm = userManager;
        this.mDPM = (DevicePolicyManager) context.getSystemService("device_policy");
        this.mLockPatternUtils = FeatureFactory.getFactory(context).getSecurityFeatureProvider().getLockPatternUtils(context);
        this.mHost = settingsPreferenceFragment;
        this.mProfileChallengeUserId = Utils.getManagedProfileId(userManager, myUserId);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public boolean isAvailable() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_unlock_set_or_change);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (RestrictedPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        RestrictedPreference restrictedPreference = this.mPreference;
        if (restrictedPreference != null && (restrictedPreference instanceof GearPreference)) {
            if (this.mLockPatternUtils.isSecure(this.mUserId)) {
                ((GearPreference) this.mPreference).setOnGearClickListener(this);
            } else {
                ((GearPreference) this.mPreference).setOnGearClickListener((GearPreference.OnGearClickListener) null);
            }
        }
        updateSummary(preference, this.mUserId);
        disableIfPasswordQualityManaged(this.mUserId);
        if (!this.mLockPatternUtils.isSeparateProfileChallengeEnabled(this.mProfileChallengeUserId)) {
            disableIfPasswordQualityManaged(this.mProfileChallengeUserId);
        }
    }

    public void onGearClick(GearPreference gearPreference) {
        if (TextUtils.equals(gearPreference.getKey(), getPreferenceKey())) {
            this.mMetricsFeatureProvider.logClickedPreference(gearPreference, gearPreference.getExtras().getInt("category"));
            new SubSettingLauncher(this.mContext).setDestination(ScreenLockSettings.class.getName()).setSourceMetricsCategory(this.mHost.getMetricsCategory()).launch();
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return super.handlePreferenceTreeClick(preference);
        }
        int i = this.mProfileChallengeUserId;
        if (i != -10000 && !this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i) && StorageManager.isFileEncryptedNativeOnly() && Utils.startQuietModeDialogIfNecessary(this.mContext, this.mUm, this.mProfileChallengeUserId)) {
            return false;
        }
        new SubSettingLauncher(this.mContext).setDestination(ChooseLockGeneric.ChooseLockGenericFragment.class.getName()).setSourceMetricsCategory(this.mHost.getMetricsCategory()).launch();
        return true;
    }

    /* access modifiers changed from: protected */
    public void updateSummary(Preference preference, int i) {
        if (this.mLockPatternUtils.isSecure(i)) {
            int keyguardStoredPasswordQuality = this.mLockPatternUtils.getKeyguardStoredPasswordQuality(i);
            if (keyguardStoredPasswordQuality == 65536) {
                preference.setSummary((int) R.string.unlock_set_unlock_mode_pattern);
            } else if (keyguardStoredPasswordQuality == 131072 || keyguardStoredPasswordQuality == 196608) {
                preference.setSummary((int) R.string.unlock_set_unlock_mode_pin);
            } else if (keyguardStoredPasswordQuality == 262144 || keyguardStoredPasswordQuality == 327680 || keyguardStoredPasswordQuality == 393216 || keyguardStoredPasswordQuality == 524288) {
                preference.setSummary((int) R.string.unlock_set_unlock_mode_password);
            }
        } else if (i == this.mProfileChallengeUserId || this.mLockPatternUtils.isLockScreenDisabled(i)) {
            preference.setSummary((int) R.string.unlock_set_unlock_mode_off);
        } else {
            preference.setSummary((int) R.string.unlock_set_unlock_mode_none);
        }
        this.mPreference.setEnabled(true);
    }

    /* access modifiers changed from: package-private */
    public void disableIfPasswordQualityManaged(int i) {
        RestrictedLockUtils.EnforcedAdmin checkIfPasswordQualityIsSet = RestrictedLockUtilsInternal.checkIfPasswordQualityIsSet(this.mContext, i);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        if (checkIfPasswordQualityIsSet != null && devicePolicyManager.getPasswordQuality(checkIfPasswordQualityIsSet.component, i) == 524288) {
            this.mPreference.setDisabledByAdmin(checkIfPasswordQualityIsSet);
        }
    }
}
