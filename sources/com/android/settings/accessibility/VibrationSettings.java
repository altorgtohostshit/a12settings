package com.android.settings.accessibility;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class VibrationSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.accessibility_vibration_settings);

    public int getHelpResource() {
        return R.string.help_uri_accessibility_vibration;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "VibrationSettings";
    }

    public int getMetricsCategory() {
        return 1292;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.accessibility_vibration_settings;
    }
}
