package com.android.settings.location;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.applications.appinfo.AppInfoDashboardFragment;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.location.RecentLocationApps;
import com.android.settingslib.widget.AppPreference;
import java.util.ArrayList;

public class RecentLocationRequestPreferenceController extends LocationBasePreferenceController {
    public static final int MAX_APPS = 3;
    private PreferenceCategory mCategoryRecentLocationRequests;
    RecentLocationApps mRecentLocationApps;
    private int mType = 3;

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

    static class PackageEntryClickedListener implements Preference.OnPreferenceClickListener {
        private final DashboardFragment mFragment;
        private final String mPackage;
        private final UserHandle mUserHandle;

        public PackageEntryClickedListener(DashboardFragment dashboardFragment, String str, UserHandle userHandle) {
            this.mFragment = dashboardFragment;
            this.mPackage = str;
            this.mUserHandle = userHandle;
        }

        public boolean onPreferenceClick(Preference preference) {
            Bundle bundle = new Bundle();
            bundle.putString("package", this.mPackage);
            new SubSettingLauncher(this.mFragment.getContext()).setDestination(AppInfoDashboardFragment.class.getName()).setArguments(bundle).setTitleRes(R.string.application_info_label).setUserHandle(this.mUserHandle).setSourceMetricsCategory(this.mFragment.getMetricsCategory()).launch();
            return true;
        }
    }

    public RecentLocationRequestPreferenceController(Context context, String str) {
        super(context, str);
        this.mRecentLocationApps = new RecentLocationApps(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mCategoryRecentLocationRequests = preferenceCategory;
        Context context = preferenceCategory.getContext();
        ArrayList<RecentLocationApps.Request> arrayList = new ArrayList<>();
        UserManager userManager = UserManager.get(this.mContext);
        for (RecentLocationApps.Request next : this.mRecentLocationApps.getAppListSorted(false)) {
            if (isRequestMatchesProfileType(userManager, next, this.mType)) {
                arrayList.add(next);
                if (arrayList.size() == 3) {
                    break;
                }
            }
        }
        if (arrayList.size() > 0) {
            for (RecentLocationApps.Request createAppPreference : arrayList) {
                this.mCategoryRecentLocationRequests.addPreference(createAppPreference(context, createAppPreference, this.mFragment));
            }
            return;
        }
        AppPreference appPreference = new AppPreference(context);
        appPreference.setTitle((int) R.string.location_no_recent_apps);
        appPreference.setSelectable(false);
        this.mCategoryRecentLocationRequests.addPreference(appPreference);
    }

    public void onLocationModeChanged(int i, boolean z) {
        this.mCategoryRecentLocationRequests.setEnabled(this.mLocationEnabler.isEnabled(i));
    }

    public void setProfileType(int i) {
        this.mType = i;
    }

    public static AppPreference createAppPreference(Context context, RecentLocationApps.Request request, DashboardFragment dashboardFragment) {
        AppPreference appPreference = new AppPreference(context);
        appPreference.setIcon(request.icon);
        appPreference.setTitle(request.label);
        appPreference.setOnPreferenceClickListener(new PackageEntryClickedListener(dashboardFragment, request.packageName, request.userHandle));
        return appPreference;
    }

    public static boolean isRequestMatchesProfileType(UserManager userManager, RecentLocationApps.Request request, int i) {
        boolean isManagedProfile = userManager.isManagedProfile(request.userHandle.getIdentifier());
        if (!isManagedProfile || (i & 2) == 0) {
            return !isManagedProfile && (i & 1) != 0;
        }
        return true;
    }
}
