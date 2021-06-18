package com.android.settings.development;

import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class ArtVerifierPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    static final int SETTING_VALUE_OFF = 0;
    static final int SETTING_VALUE_ON = 1;

    public String getPreferenceKey() {
        return "art_verifier_for_debuggable";
    }

    public ArtVerifierPreferenceController(Context context) {
        super(context);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "art_verifier_verify_debuggable", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }

    public void updateState(Preference preference) {
        boolean z = true;
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "art_verifier_verify_debuggable", 1);
        SwitchPreference switchPreference = (SwitchPreference) this.mPreference;
        if (i == 0) {
            z = false;
        }
        switchPreference.setChecked(z);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        Settings.Global.putInt(this.mContext.getContentResolver(), "art_verifier_verify_debuggable", 1);
        ((SwitchPreference) this.mPreference).setChecked(true);
    }
}
