package com.android.settings.emergency;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class EmergencyGestureSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.emergency_gesture_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            EmergencyGestureEntrypointPreferenceController emergencyGestureEntrypointPreferenceController = new EmergencyGestureEntrypointPreferenceController(context, "dummy_emergency_gesture_pref_key");
            return !emergencyGestureEntrypointPreferenceController.isAvailable() || emergencyGestureEntrypointPreferenceController.shouldSuppressFromSearch();
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "EmergencyGestureSetting";
    }

    public int getMetricsCategory() {
        return 1847;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.emergency_gesture_settings;
    }
}
