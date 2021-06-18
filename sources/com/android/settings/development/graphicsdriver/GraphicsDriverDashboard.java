package com.android.settings.development.graphicsdriver;

import android.content.Context;
import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.MainSwitchBarController;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settingslib.development.DevelopmentSettingsEnabler;

public class GraphicsDriverDashboard extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.graphics_driver_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return DevelopmentSettingsEnabler.isDevelopmentSettingsEnabled(context);
        }
    };

    public int getHelpResource() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "GraphicsDriverDashboard";
    }

    public int getMetricsCategory() {
        return 1613;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.graphics_driver_settings;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        SettingsMainSwitchBar switchBar = settingsActivity.getSwitchBar();
        getSettingsLifecycle().addObserver(new GraphicsDriverGlobalSwitchBarController(settingsActivity, new MainSwitchBarController(switchBar)));
        switchBar.setTitle(getContext().getString(R.string.graphics_driver_main_switch_title));
        switchBar.show();
    }
}
