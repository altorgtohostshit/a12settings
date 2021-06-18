package com.android.settings;

import android.os.Bundle;
import android.os.UserManager;
import androidx.preference.PreferenceScreen;

public class TestingSettings extends SettingsPreferenceFragment {
    public int getMetricsCategory() {
        return 89;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.testing_settings);
        if (!UserManager.get(getContext()).isAdminUser()) {
            getPreferenceScreen().removePreference((PreferenceScreen) findPreference("radio_info_settings"));
        }
    }
}
