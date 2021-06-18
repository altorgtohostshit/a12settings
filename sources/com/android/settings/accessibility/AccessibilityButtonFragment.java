package com.android.settings.accessibility;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class AccessibilityButtonFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.accessibility_button_settings);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AccessibilityButtonFragment";
    }

    public int getMetricsCategory() {
        return 1870;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.accessibility_button_settings;
    }
}
