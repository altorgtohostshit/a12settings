package com.google.android.settings.security;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class SecurityHubDashboard extends DashboardFragment {
    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "SecuritySettingsHub";
    }

    public int getMetricsCategory() {
        return 87;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.security_hub_dashboard;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ChangeScreenLockGooglePreferenceController(context, this));
        return arrayList;
    }
}
