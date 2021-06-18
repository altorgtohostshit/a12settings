package com.android.settings.security.trustagent;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class TrustAgentSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.trust_agent_settings);

    public int getHelpResource() {
        return R.string.help_url_trust_agent;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "TrustAgentSettings";
    }

    public int getMetricsCategory() {
        return 91;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.trust_agent_settings;
    }
}
