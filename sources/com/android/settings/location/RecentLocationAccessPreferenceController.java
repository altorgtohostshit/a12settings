package com.android.settings.location;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.UserHandle;
import android.os.UserManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.location.RecentLocationAccesses;
import com.android.settingslib.utils.StringUtil;
import com.android.settingslib.widget.AppPreference;
import java.util.ArrayList;

public class RecentLocationAccessPreferenceController extends LocationBasePreferenceController {
    public static final int MAX_APPS = 3;
    private PreferenceCategory mCategoryRecentLocationRequests;
    RecentLocationAccesses mRecentLocationApps;
    private int mType;

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

    private static class PackageEntryClickedListener implements Preference.OnPreferenceClickListener {
        private final Context mContext;
        private final String mPackage;
        private final UserHandle mUserHandle;

        PackageEntryClickedListener(Context context, String str, UserHandle userHandle) {
            this.mContext = context;
            this.mPackage = str;
            this.mUserHandle = userHandle;
        }

        public boolean onPreferenceClick(Preference preference) {
            Intent intent = new Intent("android.intent.action.MANAGE_APP_PERMISSION");
            intent.putExtra("android.intent.extra.PERMISSION_GROUP_NAME", "android.permission-group.LOCATION");
            intent.putExtra("android.intent.extra.PACKAGE_NAME", this.mPackage);
            intent.putExtra("android.intent.extra.USER", this.mUserHandle);
            this.mContext.startActivity(intent);
            return true;
        }
    }

    public RecentLocationAccessPreferenceController(Context context, String str) {
        this(context, str, new RecentLocationAccesses(context));
    }

    public RecentLocationAccessPreferenceController(Context context, String str, RecentLocationAccesses recentLocationAccesses) {
        super(context, str);
        this.mType = 3;
        this.mRecentLocationApps = recentLocationAccesses;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mCategoryRecentLocationRequests = preferenceCategory;
        Context context = preferenceCategory.getContext();
        ArrayList<RecentLocationAccesses.Access> arrayList = new ArrayList<>();
        UserManager userManager = UserManager.get(this.mContext);
        for (RecentLocationAccesses.Access next : this.mRecentLocationApps.getAppListSorted(false)) {
            if (isRequestMatchesProfileType(userManager, next, this.mType)) {
                arrayList.add(next);
                if (arrayList.size() == 3) {
                    break;
                }
            }
        }
        if (arrayList.size() > 0) {
            for (RecentLocationAccesses.Access createAppPreference : arrayList) {
                this.mCategoryRecentLocationRequests.addPreference(createAppPreference(context, createAppPreference, this.mFragment));
            }
            return;
        }
        AppPreference appPreference = new AppPreference(context);
        appPreference.setTitle((int) R.string.location_no_recent_accesses);
        appPreference.setSelectable(false);
        this.mCategoryRecentLocationRequests.addPreference(appPreference);
    }

    public void onLocationModeChanged(int i, boolean z) {
        this.mCategoryRecentLocationRequests.setVisible(this.mLocationEnabler.isEnabled(i));
    }

    public void setProfileType(int i) {
        this.mType = i;
    }

    public static AppPreference createAppPreference(Context context, RecentLocationAccesses.Access access, DashboardFragment dashboardFragment) {
        AppPreference appPreference = new AppPreference(context);
        appPreference.setIcon(access.icon);
        appPreference.setTitle(access.label);
        appPreference.setSummary(StringUtil.formatRelativeTime(context, (double) (System.currentTimeMillis() - access.accessFinishTime), false, RelativeDateTimeFormatter.Style.SHORT));
        appPreference.setOnPreferenceClickListener(new PackageEntryClickedListener(dashboardFragment.getContext(), access.packageName, access.userHandle));
        return appPreference;
    }

    public static boolean isRequestMatchesProfileType(UserManager userManager, RecentLocationAccesses.Access access, int i) {
        boolean isManagedProfile = userManager.isManagedProfile(access.userHandle.getIdentifier());
        if (!isManagedProfile || (i & 2) == 0) {
            return !isManagedProfile && (i & 1) != 0;
        }
        return true;
    }
}
