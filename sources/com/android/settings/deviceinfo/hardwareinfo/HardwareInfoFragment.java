package com.android.settings.deviceinfo.hardwareinfo;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class HardwareInfoFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.hardware_info) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return context.getResources().getBoolean(R.bool.config_show_device_model);
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "HardwareInfo";
    }

    public int getMetricsCategory() {
        return 862;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.hardware_info;
    }
}
