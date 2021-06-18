package com.android.settings.gestures;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class PreventRingingGestureSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.prevent_ringing_gesture_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return PreventRingingGestureSettings.buildPreferenceControllers(context, (Lifecycle) null);
        }
    };

    public int getHelpResource() {
        return R.string.help_uri_prevent_ringing_gesture;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "RingingGestureSettings";
    }

    public int getMetricsCategory() {
        return 1360;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.prevent_ringing_gesture_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle());
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PreventRingingGesturePreferenceController(context, lifecycle));
        arrayList.add(new PreventRingingSwitchPreferenceController(context));
        return arrayList;
    }
}
