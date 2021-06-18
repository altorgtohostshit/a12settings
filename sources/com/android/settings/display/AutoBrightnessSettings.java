package com.android.settings.display;

import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class AutoBrightnessSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.auto_brightness_detail);

    public int getHelpResource() {
        return R.string.help_url_auto_brightness;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AutoBrightnessSettings";
    }

    public int getMetricsCategory() {
        return 1381;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.auto_brightness_detail;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }
}
