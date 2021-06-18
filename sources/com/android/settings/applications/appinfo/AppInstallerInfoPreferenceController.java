package com.android.settings.applications.appinfo;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserManager;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.applications.AppStoreUtil;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.applications.AppUtils;

public class AppInstallerInfoPreferenceController extends AppInfoPreferenceControllerBase {
    private CharSequence mInstallerLabel;
    private String mInstallerPackage;
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

    public AppInstallerInfoPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        if (!UserManager.get(this.mContext).isManagedProfile() && !AppUtils.isMainlineModule(this.mContext.getPackageManager(), this.mPackageName) && this.mInstallerLabel != null) {
            return 0;
        }
        return 4;
    }

    public void updateState(Preference preference) {
        preference.setSummary((CharSequence) this.mContext.getString(AppUtils.isInstant(this.mParent.getPackageInfo().applicationInfo) ? R.string.instant_app_details_summary : R.string.app_install_details_summary, new Object[]{this.mInstallerLabel}));
        Intent appStoreLink = AppStoreUtil.getAppStoreLink(this.mContext, this.mInstallerPackage, this.mPackageName);
        if (appStoreLink != null) {
            preference.setIntent(appStoreLink);
        } else {
            preference.setEnabled(false);
        }
    }

    public void setPackageName(String str) {
        this.mPackageName = str;
        String installerPackageName = AppStoreUtil.getInstallerPackageName(this.mContext, str);
        this.mInstallerPackage = installerPackageName;
        this.mInstallerLabel = Utils.getApplicationLabel(this.mContext, installerPackageName);
    }
}
