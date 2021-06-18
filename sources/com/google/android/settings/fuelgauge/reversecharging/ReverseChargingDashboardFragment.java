package com.google.android.settings.fuelgauge.reversecharging;

import com.android.settings.R;
import com.android.settings.dashboard.RestrictedDashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class ReverseChargingDashboardFragment extends RestrictedDashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.reverse_charging);

    public int getHelpResource() {
        return R.string.help_uri_smart_battery_settings;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ReverseChargingDashboardFragment";
    }

    public int getMetricsCategory() {
        return 1842;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.reverse_charging;
    }

    public ReverseChargingDashboardFragment() {
        super("ReverseChargingDashboardFragment");
    }
}
