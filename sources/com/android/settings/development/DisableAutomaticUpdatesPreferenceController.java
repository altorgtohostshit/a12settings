package com.android.settings.development;

import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class DisableAutomaticUpdatesPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    static final int DISABLE_UPDATES_SETTING = 1;
    static final int ENABLE_UPDATES_SETTING = 0;

    public String getPreferenceKey() {
        return "ota_disable_automatic_update";
    }

    public DisableAutomaticUpdatesPreferenceController(Context context) {
        super(context);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "ota_disable_automatic_update", ((Boolean) obj).booleanValue() ^ true ? 1 : 0);
        return true;
    }

    public void updateState(Preference preference) {
        boolean z = false;
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "ota_disable_automatic_update", 0);
        SwitchPreference switchPreference = (SwitchPreference) this.mPreference;
        if (i != 1) {
            z = true;
        }
        switchPreference.setChecked(z);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        Settings.Global.putInt(this.mContext.getContentResolver(), "ota_disable_automatic_update", 1);
        ((SwitchPreference) this.mPreference).setChecked(false);
    }
}
