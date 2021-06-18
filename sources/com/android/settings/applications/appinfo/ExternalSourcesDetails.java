package com.android.settings.applications.appinfo;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.applications.AppInfoWithHeader;
import com.android.settings.applications.AppStateBaseBridge;
import com.android.settings.applications.AppStateInstallAppsBridge;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.applications.ApplicationsState;

public class ExternalSourcesDetails extends AppInfoWithHeader implements Preference.OnPreferenceChangeListener {
    private AppStateInstallAppsBridge mAppBridge;
    private AppOpsManager mAppOpsManager;
    private AppStateInstallAppsBridge.InstallAppsState mInstallAppsState;
    private RestrictedSwitchPreference mSwitchPref;
    private UserManager mUserManager;

    /* access modifiers changed from: protected */
    public AlertDialog createDialog(int i, int i2) {
        return null;
    }

    public int getMetricsCategory() {
        return 808;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentActivity activity = getActivity();
        this.mAppBridge = new AppStateInstallAppsBridge(activity, this.mState, (AppStateBaseBridge.Callback) null);
        this.mAppOpsManager = (AppOpsManager) activity.getSystemService("appops");
        this.mUserManager = UserManager.get(activity);
        addPreferencesFromResource(R.xml.external_sources_details);
        RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) findPreference("external_sources_settings_switch");
        this.mSwitchPref = restrictedSwitchPreference;
        restrictedSwitchPreference.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        int i = 0;
        if (preference != this.mSwitchPref) {
            return false;
        }
        AppStateInstallAppsBridge.InstallAppsState installAppsState = this.mInstallAppsState;
        if (installAppsState == null || booleanValue == installAppsState.canInstallApps()) {
            return true;
        }
        if (Settings.ManageAppExternalSourcesActivity.class.getName().equals(getIntent().getComponent().getClassName())) {
            if (booleanValue) {
                i = -1;
            }
            setResult(i);
        }
        setCanInstallApps(booleanValue);
        refreshUi();
        return true;
    }

    public static CharSequence getPreferenceSummary(Context context, ApplicationsState.AppEntry appEntry) {
        UserHandle userHandleForUid = UserHandle.getUserHandleForUid(appEntry.info.uid);
        UserManager userManager = UserManager.get(context);
        int userRestrictionSource = userManager.getUserRestrictionSource("no_install_unknown_sources_globally", userHandleForUid) | userManager.getUserRestrictionSource("no_install_unknown_sources", userHandleForUid);
        if ((userRestrictionSource & 1) != 0) {
            return context.getString(R.string.disabled_by_admin);
        }
        if (userRestrictionSource != 0) {
            return context.getString(R.string.disabled);
        }
        AppStateInstallAppsBridge appStateInstallAppsBridge = new AppStateInstallAppsBridge(context, (ApplicationsState) null, (AppStateBaseBridge.Callback) null);
        ApplicationInfo applicationInfo = appEntry.info;
        return context.getString(appStateInstallAppsBridge.createInstallAppsStateFor(applicationInfo.packageName, applicationInfo.uid).canInstallApps() ? R.string.app_permission_summary_allowed : R.string.app_permission_summary_not_allowed);
    }

    private void setCanInstallApps(boolean z) {
        this.mAppOpsManager.setMode(66, this.mPackageInfo.applicationInfo.uid, this.mPackageName, z ? 0 : 2);
    }

    /* access modifiers changed from: protected */
    public boolean refreshUi() {
        PackageInfo packageInfo = this.mPackageInfo;
        if (packageInfo == null || packageInfo.applicationInfo == null) {
            return false;
        }
        if (this.mUserManager.hasBaseUserRestriction("no_install_unknown_sources", UserHandle.of(UserHandle.myUserId()))) {
            this.mSwitchPref.setChecked(false);
            this.mSwitchPref.setSummary((int) R.string.disabled);
            this.mSwitchPref.setEnabled(false);
            return true;
        }
        this.mSwitchPref.checkRestrictionAndSetDisabled("no_install_unknown_sources");
        if (!this.mSwitchPref.isDisabledByAdmin()) {
            this.mSwitchPref.checkRestrictionAndSetDisabled("no_install_unknown_sources_globally");
        }
        if (this.mSwitchPref.isDisabledByAdmin()) {
            return true;
        }
        AppStateInstallAppsBridge.InstallAppsState createInstallAppsStateFor = this.mAppBridge.createInstallAppsStateFor(this.mPackageName, this.mPackageInfo.applicationInfo.uid);
        this.mInstallAppsState = createInstallAppsStateFor;
        if (!createInstallAppsStateFor.isPotentialAppSource()) {
            this.mSwitchPref.setEnabled(false);
            return true;
        }
        this.mSwitchPref.setChecked(this.mInstallAppsState.canInstallApps());
        return true;
    }
}
