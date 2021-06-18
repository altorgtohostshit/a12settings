package com.android.settings.sound;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class MediaControlsSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.media_controls_settings);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "MediaControlsSettings";
    }

    public int getMetricsCategory() {
        return 1845;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.media_controls_settings;
    }
}
