package com.android.settings.development;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class SelectDebugAppPreferenceController extends DeveloperOptionsPreferenceController implements PreferenceControllerMixin, OnActivityResultListener {
    private final DevelopmentSettingsDashboardFragment mFragment;
    private final PackageManager mPackageManager = this.mContext.getPackageManager();

    public String getPreferenceKey() {
        return "debug_app";
    }

    public SelectDebugAppPreferenceController(Context context, DevelopmentSettingsDashboardFragment developmentSettingsDashboardFragment) {
        super(context);
        this.mFragment = developmentSettingsDashboardFragment;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!"debug_app".equals(preference.getKey())) {
            return false;
        }
        Intent activityStartIntent = getActivityStartIntent();
        activityStartIntent.putExtra("com.android.settings.extra.DEBUGGABLE", true);
        this.mFragment.startActivityForResult(activityStartIntent, 1);
        return true;
    }

    public void updateState(Preference preference) {
        updatePreferenceSummary();
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (i != 1 || i2 != -1) {
            return false;
        }
        Settings.Global.putString(this.mContext.getContentResolver(), "debug_app", intent.getAction());
        updatePreferenceSummary();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        this.mPreference.setSummary((CharSequence) this.mContext.getResources().getString(R.string.debug_app_not_set));
    }

    /* access modifiers changed from: package-private */
    public Intent getActivityStartIntent() {
        return new Intent(this.mContext, AppPicker.class);
    }

    private void updatePreferenceSummary() {
        String string = Settings.Global.getString(this.mContext.getContentResolver(), "debug_app");
        if (string == null || string.length() <= 0) {
            this.mPreference.setSummary((CharSequence) this.mContext.getResources().getString(R.string.debug_app_not_set));
            return;
        }
        this.mPreference.setSummary((CharSequence) this.mContext.getResources().getString(R.string.debug_app_set, new Object[]{getAppLabel(string)}));
    }

    private String getAppLabel(String str) {
        try {
            CharSequence applicationLabel = this.mPackageManager.getApplicationLabel(this.mPackageManager.getApplicationInfo(str, 512));
            return applicationLabel != null ? applicationLabel.toString() : str;
        } catch (PackageManager.NameNotFoundException unused) {
            return str;
        }
    }
}
