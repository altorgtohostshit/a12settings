package com.android.settings.gestures;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class SwipeBottomToNotificationSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.swipe_bottom_to_notification_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            if (!OneHandedSettingsUtils.isSupportOneHandedMode()) {
                return false;
            }
            return !OneHandedSettingsUtils.isOneHandedModeEnabled(context);
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "SwipeBottomToNotificationSettings";
    }

    public int getMetricsCategory() {
        return 1846;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.swipe_bottom_to_notification_settings;
    }
}
