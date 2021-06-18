package com.android.settings.accessibility;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.accessibility.AccessibilityTimeoutController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class AccessibilityControlTimeoutPreferenceFragment extends DashboardFragment implements AccessibilityTimeoutController.OnChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.accessibility_control_timeout_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return AccessibilityControlTimeoutPreferenceFragment.buildPreferenceControllers(context, (Lifecycle) null);
        }
    };
    private static final List<AbstractPreferenceController> sControllers = new ArrayList();

    public int getHelpResource() {
        return R.string.help_url_timeout;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AccessibilityControlTimeoutPreferenceFragment";
    }

    public int getMetricsCategory() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.accessibility_control_timeout_settings;
    }

    public void onCheckedChanged(Preference preference) {
        for (AbstractPreferenceController updateState : sControllers) {
            updateState.updateState(preference);
        }
    }

    public void onResume() {
        super.onResume();
        Iterator<AbstractPreferenceController> it = buildPreferenceControllers(getPrefContext(), getSettingsLifecycle()).iterator();
        while (it.hasNext()) {
            ((AccessibilityTimeoutController) it.next()).setOnChangeListener(this);
        }
    }

    public void onPause() {
        super.onPause();
        Iterator<AbstractPreferenceController> it = buildPreferenceControllers(getPrefContext(), getSettingsLifecycle()).iterator();
        while (it.hasNext()) {
            ((AccessibilityTimeoutController) it.next()).setOnChangeListener((AccessibilityTimeoutController.OnChangeListener) null);
        }
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle());
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle) {
        if (sControllers.size() == 0) {
            String[] stringArray = context.getResources().getStringArray(R.array.accessibility_timeout_control_selector_keys);
            for (String accessibilityTimeoutController : stringArray) {
                sControllers.add(new AccessibilityTimeoutController(context, lifecycle, accessibilityTimeoutController));
            }
        }
        return sControllers;
    }
}
