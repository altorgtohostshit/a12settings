package com.android.settings.accessibility;

import android.content.Context;
import android.content.res.Resources;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.accessibility.ToggleAutoclickPreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ToggleAutoclickPreferenceFragment extends DashboardFragment implements ToggleAutoclickPreferenceController.OnChangeListener {
    private static final int[] AUTOCLICK_PREFERENCE_SUMMARIES = {R.plurals.accessibilty_autoclick_preference_subtitle_short_delay, R.plurals.accessibilty_autoclick_preference_subtitle_medium_delay, R.plurals.accessibilty_autoclick_preference_subtitle_long_delay};
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.accessibility_autoclick_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return ToggleAutoclickPreferenceFragment.buildPreferenceControllers(context, (Lifecycle) null);
        }
    };
    private static final List<AbstractPreferenceController> sControllers = new ArrayList();

    public int getHelpResource() {
        return R.string.help_url_autoclick;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AutoclickPrefFragment";
    }

    public int getMetricsCategory() {
        return 335;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.accessibility_autoclick_settings;
    }

    static CharSequence getAutoclickPreferenceSummary(Resources resources, int i) {
        int autoclickPreferenceSummaryIndex = getAutoclickPreferenceSummaryIndex(i);
        int i2 = i == 1000 ? 1 : 3;
        float f = ((float) i) / 1000.0f;
        return resources.getQuantityString(AUTOCLICK_PREFERENCE_SUMMARIES[autoclickPreferenceSummaryIndex], i2, new Object[]{String.format(f == 1.0f ? "%.0f" : "%.1f", new Object[]{Float.valueOf(f)})});
    }

    private static int getAutoclickPreferenceSummaryIndex(int i) {
        if (i <= 200) {
            return 0;
        }
        if (i >= 1000) {
            return AUTOCLICK_PREFERENCE_SUMMARIES.length - 1;
        }
        return (i - 200) / (800 / (AUTOCLICK_PREFERENCE_SUMMARIES.length - 1));
    }

    public void onResume() {
        super.onResume();
        Iterator<AbstractPreferenceController> it = sControllers.iterator();
        while (it.hasNext()) {
            ((ToggleAutoclickPreferenceController) it.next()).setOnChangeListener(this);
        }
    }

    public void onPause() {
        super.onPause();
        Iterator<AbstractPreferenceController> it = sControllers.iterator();
        while (it.hasNext()) {
            ((ToggleAutoclickPreferenceController) it.next()).setOnChangeListener((ToggleAutoclickPreferenceController.OnChangeListener) null);
        }
    }

    public void onCheckedChanged(Preference preference) {
        for (AbstractPreferenceController updateState : sControllers) {
            updateState.updateState(preference);
        }
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle());
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle) {
        for (String toggleAutoclickPreferenceController : context.getResources().getStringArray(R.array.accessibility_autoclick_control_selector_keys)) {
            sControllers.add(new ToggleAutoclickPreferenceController(context, lifecycle, toggleAutoclickPreferenceController));
        }
        return sControllers;
    }
}
