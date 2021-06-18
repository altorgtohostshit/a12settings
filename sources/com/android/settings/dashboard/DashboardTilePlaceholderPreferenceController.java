package com.android.settings.dashboard;

import android.content.Context;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

class DashboardTilePlaceholderPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private int mOrder = Integer.MAX_VALUE;

    public String getPreferenceKey() {
        return "dashboard_tile_placeholder";
    }

    public boolean isAvailable() {
        return false;
    }

    public DashboardTilePlaceholderPreferenceController(Context context) {
        super(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        if (findPreference != null) {
            this.mOrder = findPreference.getOrder();
            preferenceScreen.removePreference(findPreference);
        }
    }

    public int getOrder() {
        return this.mOrder;
    }
}
