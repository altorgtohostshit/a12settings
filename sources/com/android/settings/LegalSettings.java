package com.android.settings;

import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class LegalSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.about_legal);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "LegalSettings";
    }

    public int getMetricsCategory() {
        return 225;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.about_legal;
    }
}
