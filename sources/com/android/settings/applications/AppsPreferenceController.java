package com.android.settings.applications;

import android.app.Application;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.IntentFilter;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.applications.appinfo.AppInfoDashboardFragment;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.Utils;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.utils.StringUtil;
import com.android.settingslib.widget.AppPreference;
import java.util.List;

public class AppsPreferenceController extends BasePreferenceController {
    static final String KEY_ALL_APP_INFO = "all_app_infos";
    static final String KEY_GENERAL_CATEGORY = "general_category";
    static final String KEY_RECENT_APPS_CATEGORY = "recent_apps_category";
    static final String KEY_SEE_ALL = "see_all_apps";
    public static final int SHOW_RECENT_APP_COUNT = 4;
    Preference mAllAppsInfoPref;
    private final ApplicationsState mApplicationsState = ApplicationsState.getInstance((Application) this.mContext.getApplicationContext());
    PreferenceCategory mGeneralCategory;
    private Fragment mHost;
    List<UsageStats> mRecentApps;
    PreferenceCategory mRecentAppsCategory;
    Preference mSeeAllPref;
    private final int mUserId = UserHandle.myUserId();

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 1;
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

    public AppsPreferenceController(Context context) {
        super(context, KEY_RECENT_APPS_CATEGORY);
    }

    public void setFragment(Fragment fragment) {
        this.mHost = fragment;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        initPreferences(preferenceScreen);
        refreshUi();
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        refreshUi();
    }

    /* access modifiers changed from: package-private */
    public void refreshUi() {
        loadAllAppsCount();
        List<UsageStats> loadRecentApps = loadRecentApps();
        this.mRecentApps = loadRecentApps;
        if (!loadRecentApps.isEmpty()) {
            displayRecentApps();
            this.mRecentAppsCategory.setVisible(true);
            this.mGeneralCategory.setVisible(true);
            this.mSeeAllPref.setVisible(true);
            return;
        }
        this.mAllAppsInfoPref.setVisible(true);
    }

    /* access modifiers changed from: package-private */
    public void loadAllAppsCount() {
        Context context = this.mContext;
        new InstalledAppCounter(context, -1, context.getPackageManager()) {
            /* access modifiers changed from: protected */
            public void onCountComplete(int i) {
                if (!AppsPreferenceController.this.mRecentApps.isEmpty()) {
                    AppsPreferenceController appsPreferenceController = AppsPreferenceController.this;
                    appsPreferenceController.mSeeAllPref.setTitle((CharSequence) appsPreferenceController.mContext.getResources().getQuantityString(R.plurals.see_all_apps_title, i, new Object[]{Integer.valueOf(i)}));
                    return;
                }
                AppsPreferenceController appsPreferenceController2 = AppsPreferenceController.this;
                appsPreferenceController2.mAllAppsInfoPref.setSummary((CharSequence) appsPreferenceController2.mContext.getString(R.string.apps_summary, new Object[]{Integer.valueOf(i)}));
            }
        }.execute(new Void[0]);
    }

    /* access modifiers changed from: package-private */
    public List<UsageStats> loadRecentApps() {
        RecentAppStatsMixin recentAppStatsMixin = new RecentAppStatsMixin(this.mContext, 4);
        recentAppStatsMixin.loadDisplayableRecentApps(4);
        return recentAppStatsMixin.mRecentApps;
    }

    private void initPreferences(PreferenceScreen preferenceScreen) {
        this.mRecentAppsCategory = (PreferenceCategory) preferenceScreen.findPreference(KEY_RECENT_APPS_CATEGORY);
        this.mGeneralCategory = (PreferenceCategory) preferenceScreen.findPreference(KEY_GENERAL_CATEGORY);
        this.mAllAppsInfoPref = preferenceScreen.findPreference(KEY_ALL_APP_INFO);
        this.mSeeAllPref = preferenceScreen.findPreference(KEY_SEE_ALL);
        this.mRecentAppsCategory.setVisible(false);
        this.mGeneralCategory.setVisible(false);
        this.mAllAppsInfoPref.setVisible(false);
        this.mSeeAllPref.setVisible(false);
    }

    private void displayRecentApps() {
        boolean z;
        if (this.mRecentAppsCategory != null) {
            ArrayMap arrayMap = new ArrayMap();
            int preferenceCount = this.mRecentAppsCategory.getPreferenceCount();
            for (int i = 0; i < preferenceCount; i++) {
                Preference preference = this.mRecentAppsCategory.getPreference(i);
                String key = preference.getKey();
                if (!TextUtils.equals(key, KEY_SEE_ALL)) {
                    arrayMap.put(key, preference);
                }
            }
            int i2 = 0;
            for (UsageStats next : this.mRecentApps) {
                String packageName = next.getPackageName();
                ApplicationsState.AppEntry entry = this.mApplicationsState.getEntry(packageName, this.mUserId);
                if (entry != null) {
                    Preference preference2 = (Preference) arrayMap.remove(packageName);
                    if (preference2 == null) {
                        preference2 = new AppPreference(this.mContext);
                        z = false;
                    } else {
                        z = true;
                    }
                    preference2.setKey(packageName);
                    preference2.setTitle((CharSequence) entry.label);
                    preference2.setIcon(Utils.getBadgedIcon(this.mContext, entry.info));
                    preference2.setSummary(StringUtil.formatRelativeTime(this.mContext, (double) (System.currentTimeMillis() - next.getLastTimeUsed()), false, RelativeDateTimeFormatter.Style.SHORT));
                    int i3 = i2 + 1;
                    preference2.setOrder(i2);
                    preference2.setOnPreferenceClickListener(new AppsPreferenceController$$ExternalSyntheticLambda0(this, packageName, entry));
                    if (!z) {
                        this.mRecentAppsCategory.addPreference(preference2);
                    }
                    i2 = i3;
                }
            }
            for (Preference removePreference : arrayMap.values()) {
                this.mRecentAppsCategory.removePreference(removePreference);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$displayRecentApps$0(String str, ApplicationsState.AppEntry appEntry, Preference preference) {
        AppInfoBase.startAppInfoFragment(AppInfoDashboardFragment.class, R.string.application_info_label, str, appEntry.info.uid, this.mHost, 1001, getMetricsCategory());
        return true;
    }
}
