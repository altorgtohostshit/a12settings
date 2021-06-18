package com.android.settings.deviceinfo.legal;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;

public class ModuleLicensesDashboard extends DashboardFragment {
    public int getHelpResource() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ModuleLicensesDashboard";
    }

    public int getMetricsCategory() {
        return 1746;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.module_licenses;
    }
}
