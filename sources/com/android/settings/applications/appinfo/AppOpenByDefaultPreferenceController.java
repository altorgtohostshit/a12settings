package com.android.settings.applications.appinfo;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.verify.domain.DomainVerificationManager;
import android.content.pm.verify.domain.DomainVerificationUserState;
import android.os.UserHandle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.applications.intentpicker.AppLaunchSettings;
import com.android.settings.applications.intentpicker.IntentPickerUtils;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.applications.ApplicationsState;

public class AppOpenByDefaultPreferenceController extends AppInfoPreferenceControllerBase {
    private final DomainVerificationManager mDomainVerificationManager;
    private String mPackageName;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AppOpenByDefaultPreferenceController(Context context, String str) {
        super(context, str);
        this.mDomainVerificationManager = (DomainVerificationManager) context.getSystemService(DomainVerificationManager.class);
    }

    public AppOpenByDefaultPreferenceController setPackageName(String str) {
        this.mPackageName = str;
        return this;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        ApplicationInfo applicationInfo;
        super.displayPreference(preferenceScreen);
        ApplicationsState.AppEntry appEntry = this.mParent.getAppEntry();
        if (appEntry == null || (applicationInfo = appEntry.info) == null) {
            this.mPreference.setEnabled(false);
        } else if ((applicationInfo.flags & 8388608) == 0 || !applicationInfo.enabled) {
            this.mPreference.setEnabled(false);
        }
    }

    public void updateState(Preference preference) {
        PackageInfo packageInfo = this.mParent.getPackageInfo();
        if (packageInfo == null || AppUtils.isInstant(packageInfo.applicationInfo) || AppUtils.isBrowserApp(this.mContext, packageInfo.packageName, UserHandle.myUserId())) {
            preference.setVisible(false);
            return;
        }
        preference.setVisible(true);
        preference.setSummary(getSubtext());
    }

    /* access modifiers changed from: protected */
    public Class<? extends SettingsPreferenceFragment> getDetailFragmentClass() {
        return AppLaunchSettings.class;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getSubtext() {
        return this.mContext.getText(isLinkHandlingAllowed() ? R.string.app_link_open_always : R.string.app_link_open_never);
    }

    /* access modifiers changed from: package-private */
    public boolean isLinkHandlingAllowed() {
        DomainVerificationUserState domainVerificationUserState = IntentPickerUtils.getDomainVerificationUserState(this.mDomainVerificationManager, this.mPackageName);
        if (domainVerificationUserState == null) {
            return false;
        }
        return domainVerificationUserState.isLinkHandlingAllowed();
    }
}
