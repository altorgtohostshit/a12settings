package com.android.settings.development;

import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class OverlaySettingsPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "overlay_settings";
    }

    public boolean isAvailable() {
        return true;
    }

    public OverlaySettingsPreferenceController(Context context) {
        super(context);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        setOverlaySettingsEnabled(this.mContext, ((Boolean) obj).booleanValue());
        return true;
    }

    public void updateState(Preference preference) {
        ((SwitchPreference) preference).setChecked(isOverlaySettingsEnabled(this.mContext));
    }

    static boolean isOverlaySettingsEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "secure_overlay_settings", 0) != 0;
    }

    static void setOverlaySettingsEnabled(Context context, boolean z) {
        Settings.Secure.putInt(context.getContentResolver(), "secure_overlay_settings", z ? 1 : 0);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        setOverlaySettingsEnabled(this.mContext, false);
    }
}
