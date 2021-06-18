package com.android.settings.applications;

import android.content.Intent;
import com.android.settings.SettingsActivity;
import com.android.settings.applications.intentpicker.AppLaunchSettings;

public class InstalledAppOpenByDefaultActivity extends SettingsActivity {
    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        intent.putExtra(":settings:show_fragment", AppLaunchSettings.class.getName());
        return intent;
    }

    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return AppLaunchSettings.class.getName().equals(str);
    }
}
