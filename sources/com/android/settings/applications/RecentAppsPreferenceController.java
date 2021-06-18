package com.android.settings.applications;

import android.app.Application;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.IntentFilter;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.applications.RecentAppStatsMixin;
import com.android.settings.applications.appinfo.AppInfoDashboardFragment;
import com.android.settings.applications.manageapplications.ManageApplications;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.Utils;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.utils.StringUtil;
import com.android.settingslib.widget.AppEntitiesHeaderController;
import com.android.settingslib.widget.AppEntityInfo;
import com.android.settingslib.widget.LayoutPreference;
import java.util.List;

public class RecentAppsPreferenceController extends BasePreferenceController implements RecentAppStatsMixin.RecentAppStatsListener {
    static final String KEY_DIVIDER = "recent_apps_divider";
    AppEntitiesHeaderController mAppEntitiesController;
    private final ApplicationsState mApplicationsState = ApplicationsState.getInstance((Application) this.mContext.getApplicationContext());
    Preference mDivider;
    private Fragment mHost;
    private final MetricsFeatureProvider mMetricsFeatureProvider = FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider();
    private List<UsageStats> mRecentApps;
    LayoutPreference mRecentAppsPreference;
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

    public RecentAppsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void setFragment(Fragment fragment) {
        this.mHost = fragment;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mDivider = preferenceScreen.findPreference(KEY_DIVIDER);
        LayoutPreference layoutPreference = (LayoutPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mRecentAppsPreference = layoutPreference;
        this.mAppEntitiesController = AppEntitiesHeaderController.newInstance(this.mContext, layoutPreference.findViewById(R.id.app_entities_header)).setHeaderTitleRes(R.string.recent_app_category_title).setHeaderDetailsClickListener(new RecentAppsPreferenceController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(View view) {
        this.mMetricsFeatureProvider.logClickedPreference(this.mRecentAppsPreference, getMetricsCategory());
        new SubSettingLauncher(this.mContext).setDestination(ManageApplications.class.getName()).setArguments((Bundle) null).setTitleRes(R.string.application_info_label).setSourceMetricsCategory(getMetricsCategory()).launch();
    }

    public void onReloadDataCompleted(List<UsageStats> list) {
        this.mRecentApps = list;
        refreshUi();
        Context context = this.mContext;
        new InstalledAppCounter(context, -1, context.getPackageManager()) {
            /* access modifiers changed from: protected */
            public void onCountComplete(int i) {
                RecentAppsPreferenceController recentAppsPreferenceController = RecentAppsPreferenceController.this;
                recentAppsPreferenceController.mAppEntitiesController.setHeaderDetails(recentAppsPreferenceController.mContext.getResources().getQuantityString(R.plurals.see_all_apps_title, i, new Object[]{Integer.valueOf(i)}));
                RecentAppsPreferenceController.this.mAppEntitiesController.apply();
            }
        }.execute(new Void[0]);
    }

    private void refreshUi() {
        if (!this.mRecentApps.isEmpty()) {
            displayRecentApps();
            this.mRecentAppsPreference.setVisible(true);
            this.mDivider.setVisible(true);
            return;
        }
        this.mDivider.setVisible(false);
        this.mRecentAppsPreference.setVisible(false);
    }

    private void displayRecentApps() {
        int i = 0;
        for (UsageStats createAppEntity : this.mRecentApps) {
            AppEntityInfo createAppEntity2 = createAppEntity(createAppEntity);
            if (createAppEntity2 != null) {
                this.mAppEntitiesController.setAppEntity(i, createAppEntity2);
                i++;
            }
            if (i == 3) {
                return;
            }
        }
    }

    private AppEntityInfo createAppEntity(UsageStats usageStats) {
        String packageName = usageStats.getPackageName();
        ApplicationsState.AppEntry entry = this.mApplicationsState.getEntry(packageName, this.mUserId);
        if (entry == null) {
            return null;
        }
        return new AppEntityInfo.Builder().setIcon(Utils.getBadgedIcon(this.mContext, entry.info)).setTitle(entry.label).setSummary(StringUtil.formatRelativeTime(this.mContext, (double) (System.currentTimeMillis() - usageStats.getLastTimeUsed()), false, RelativeDateTimeFormatter.Style.SHORT)).setOnClickListener(new RecentAppsPreferenceController$$ExternalSyntheticLambda1(this, packageName, entry)).build();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createAppEntity$1(String str, ApplicationsState.AppEntry appEntry, View view) {
        this.mMetricsFeatureProvider.logClickedPreference(this.mRecentAppsPreference, getMetricsCategory());
        AppInfoBase.startAppInfoFragment(AppInfoDashboardFragment.class, R.string.application_info_label, str, appEntry.info.uid, this.mHost, 1001, getMetricsCategory());
    }
}
