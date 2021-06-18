package com.android.settings.applications.specialaccess.zenaccess;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.applications.AppInfoWithHeader;
import com.android.settings.applications.specialaccess.zenaccess.ZenAccessSettingObserverMixin;

public class ZenAccessDetails extends AppInfoWithHeader implements ZenAccessSettingObserverMixin.Listener {
    /* access modifiers changed from: protected */
    public AlertDialog createDialog(int i, int i2) {
        return null;
    }

    public int getMetricsCategory() {
        return 1692;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.zen_access_permission_details);
        getSettingsLifecycle().addObserver(new ZenAccessSettingObserverMixin(getContext(), this));
    }

    /* access modifiers changed from: protected */
    public boolean refreshUi() {
        Context context = getContext();
        if (!ZenAccessController.getPackagesRequestingNotificationPolicyAccess().contains(this.mPackageName)) {
            return false;
        }
        updatePreference(context, (SwitchPreference) findPreference("zen_access_switch"));
        return true;
    }

    public void updatePreference(Context context, SwitchPreference switchPreference) {
        CharSequence loadLabel = this.mPackageInfo.applicationInfo.loadLabel(this.mPm);
        if (ZenAccessController.getAutoApprovedPackages(context).contains(this.mPackageName)) {
            switchPreference.setEnabled(false);
            switchPreference.setSummary((CharSequence) getString(R.string.zen_access_disabled_package_warning));
            return;
        }
        switchPreference.setChecked(ZenAccessController.hasAccess(context, this.mPackageName));
        switchPreference.setOnPreferenceChangeListener(new ZenAccessDetails$$ExternalSyntheticLambda0(this, loadLabel));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updatePreference$0(CharSequence charSequence, Preference preference, Object obj) {
        if (((Boolean) obj).booleanValue()) {
            new ScaryWarningDialogFragment().setPkgInfo(this.mPackageName, charSequence, this).show(getFragmentManager(), "dialog");
            return false;
        }
        new FriendlyWarningDialogFragment().setPkgInfo(this.mPackageName, charSequence, this).show(getFragmentManager(), "dialog");
        return false;
    }

    public void onZenAccessPolicyChanged() {
        refreshUi();
    }
}
