package com.android.settings.gestures;

import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;

public class TapScreenGestureSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.tap_screen_gesture_settings);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "TapScreenGestureSettings";
    }

    public int getMetricsCategory() {
        return 1626;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.tap_screen_gesture_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        FeatureFactory.getFactory(context).getSuggestionFeatureProvider(context).getSharedPrefs(context).edit().putBoolean("pref_tap_gesture_suggestion_complete", true).apply();
        ((TapScreenGesturePreferenceController) use(TapScreenGesturePreferenceController.class)).setConfig(new AmbientDisplayConfiguration(context));
    }
}
