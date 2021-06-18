package com.google.android.settings.security;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.security.ChangeScreenLockPreferenceController;

public class ChangeScreenLockGooglePreferenceController extends ChangeScreenLockPreferenceController {
    public ChangeScreenLockGooglePreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment) {
        super(context, settingsPreferenceFragment);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference.isEnabled()) {
            preference.setIcon((this.mLockPatternUtils.isSecure(this.mUserId) ? SecurityLevel.INFO : SecurityLevel.RECOMMENDATION).getIconResId());
        }
    }
}
