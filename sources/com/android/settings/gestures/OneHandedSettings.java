package com.android.settings.gestures;

import android.content.Context;
import android.os.UserHandle;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class OneHandedSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.one_handed_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return OneHandedSettingsUtils.isSupportOneHandedMode();
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "OneHandedSettings";
    }

    public int getMetricsCategory() {
        return 1841;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.one_handed_settings;
    }

    /* access modifiers changed from: protected */
    public void updatePreferenceStates() {
        OneHandedSettingsUtils.setUserId(UserHandle.myUserId());
        super.updatePreferenceStates();
    }
}
