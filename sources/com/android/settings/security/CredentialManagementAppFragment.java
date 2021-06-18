package com.android.settings.security;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class CredentialManagementAppFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.credential_management_app_fragment);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "CredentialManagementApp";
    }

    public int getMetricsCategory() {
        return 1856;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.credential_management_app_fragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((CredentialManagementAppButtonsController) use(CredentialManagementAppButtonsController.class)).setParentFragment(this);
    }
}
