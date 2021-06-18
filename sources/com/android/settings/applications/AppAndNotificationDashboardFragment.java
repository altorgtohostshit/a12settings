package com.android.settings.applications;

import android.app.usage.UsageStats;
import android.content.Context;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.util.FeatureFlagUtils;
import android.view.View;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.applications.RecentAppStatsMixin;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.notification.EmergencyBroadcastPreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppAndNotificationDashboardFragment extends DashboardFragment implements RecentAppStatsMixin.RecentAppStatsListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            SearchIndexableResource searchIndexableResource = new SearchIndexableResource(context);
            searchIndexableResource.xmlResId = R.xml.app_and_notification;
            return Arrays.asList(new SearchIndexableResource[]{searchIndexableResource});
        }

        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return AppAndNotificationDashboardFragment.buildPreferenceControllers(context);
        }

        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return !FeatureFlagUtils.isEnabled(context, "settings_silky_home");
        }
    };
    private AllAppsInfoPreferenceController mAllAppsInfoPreferenceController;
    private RecentAppStatsMixin mRecentAppStatsMixin;
    private RecentAppsPreferenceController mRecentAppsPreferenceController;

    public int getHelpResource() {
        return R.string.help_url_apps_and_notifications;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AppAndNotifDashboard";
    }

    public int getMetricsCategory() {
        return 748;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.app_and_notification;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((SpecialAppAccessPreferenceController) use(SpecialAppAccessPreferenceController.class)).setSession(getSettingsLifecycle());
        this.mRecentAppStatsMixin = new RecentAppStatsMixin(context, 3);
        getSettingsLifecycle().addObserver(this.mRecentAppStatsMixin);
        this.mRecentAppStatsMixin.addListener(this);
        RecentAppsPreferenceController recentAppsPreferenceController = (RecentAppsPreferenceController) use(RecentAppsPreferenceController.class);
        this.mRecentAppsPreferenceController = recentAppsPreferenceController;
        recentAppsPreferenceController.setFragment(this);
        this.mRecentAppStatsMixin.addListener(this.mRecentAppsPreferenceController);
        AllAppsInfoPreferenceController allAppsInfoPreferenceController = (AllAppsInfoPreferenceController) use(AllAppsInfoPreferenceController.class);
        this.mAllAppsInfoPreferenceController = allAppsInfoPreferenceController;
        this.mRecentAppStatsMixin.addListener(allAppsInfoPreferenceController);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setPinnedHeaderView((int) R.layout.progress_header);
        showPinnedHeader(false);
    }

    public void onStart() {
        super.onStart();
        showPinnedHeader(true);
    }

    public void onReloadDataCompleted(List<UsageStats> list) {
        showPinnedHeader(false);
        if (!list.isEmpty()) {
            Utils.setActionBarShadowAnimation(getActivity(), getSettingsLifecycle(), getListView());
        }
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context);
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new EmergencyBroadcastPreferenceController(context, "app_and_notif_cell_broadcast_settings"));
        return arrayList;
    }
}
