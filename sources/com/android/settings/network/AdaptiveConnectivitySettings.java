package com.android.settings.network;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class AdaptiveConnectivitySettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.adaptive_connectivity_settings);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AdaptiveConnectivitySettings";
    }

    public int getMetricsCategory() {
        return 1850;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.adaptive_connectivity_settings;
    }
}
