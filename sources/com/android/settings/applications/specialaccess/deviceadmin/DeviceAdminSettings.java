package com.android.settings.applications.specialaccess.deviceadmin;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class DeviceAdminSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.device_admin_settings);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "DeviceAdminSettings";
    }

    public int getMetricsCategory() {
        return 516;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.device_admin_settings;
    }
}
