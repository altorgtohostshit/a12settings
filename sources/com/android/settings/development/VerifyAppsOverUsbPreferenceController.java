package com.android.settings.development;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class VerifyAppsOverUsbPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, AdbOnChangeListener, PreferenceControllerMixin {
    static final int SETTING_VALUE_OFF = 0;
    static final int SETTING_VALUE_ON = 1;
    private final PackageManager mPackageManager;
    private final RestrictedLockUtilsDelegate mRestrictedLockUtils = new RestrictedLockUtilsDelegate();

    public String getPreferenceKey() {
        return "verify_apps_over_usb";
    }

    class RestrictedLockUtilsDelegate {
        RestrictedLockUtilsDelegate() {
        }

        public RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced(Context context, String str, int i) {
            return RestrictedLockUtilsInternal.checkIfRestrictionEnforced(context, str, i);
        }
    }

    public VerifyAppsOverUsbPreferenceController(Context context) {
        super(context);
        this.mPackageManager = context.getPackageManager();
    }

    public boolean isAvailable() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "verifier_setting_visible", 1) > 0;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "verifier_verify_adb_installs", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }

    public void updateState(Preference preference) {
        RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preference;
        boolean z = false;
        if (!shouldBeEnabled()) {
            restrictedSwitchPreference.setChecked(false);
            restrictedSwitchPreference.setDisabledByAdmin((RestrictedLockUtils.EnforcedAdmin) null);
            restrictedSwitchPreference.setEnabled(false);
            return;
        }
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = this.mRestrictedLockUtils.checkIfRestrictionEnforced(this.mContext, "ensure_verify_apps", UserHandle.myUserId());
        if (checkIfRestrictionEnforced != null) {
            restrictedSwitchPreference.setChecked(true);
            restrictedSwitchPreference.setDisabledByAdmin(checkIfRestrictionEnforced);
            return;
        }
        restrictedSwitchPreference.setEnabled(true);
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "verifier_verify_adb_installs", 1) != 0) {
            z = true;
        }
        restrictedSwitchPreference.setChecked(z);
    }

    public void onAdbSettingChanged() {
        if (isAvailable()) {
            updateState(this.mPreference);
        }
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchEnabled() {
        super.onDeveloperOptionsSwitchEnabled();
        updateState(this.mPreference);
    }

    private boolean shouldBeEnabled() {
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "adb_enabled", 0) == 0) {
            return false;
        }
        Intent intent = new Intent("android.intent.action.PACKAGE_NEEDS_VERIFICATION");
        intent.setType("application/vnd.android.package-archive");
        intent.addFlags(1);
        return !this.mPackageManager.queryBroadcastReceivers(intent, 0).isEmpty();
    }
}
