package com.android.settings.enterprise;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class EnterpriseSetDefaultAppsListFragment extends DashboardFragment {
    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "EnterprisePrivacySettings";
    }

    public int getMetricsCategory() {
        return 940;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.enterprise_set_default_apps_settings;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new EnterpriseSetDefaultAppsListPreferenceController(context, this, context.getPackageManager()));
        return arrayList;
    }
}
