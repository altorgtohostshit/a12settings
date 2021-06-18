package com.android.settings.inputmethod;

import android.content.Context;
import android.view.textservice.SpellCheckerInfo;
import android.view.textservice.TextServicesManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.inputmethod.InputMethodAndSubtypeUtilCompat;

public class SpellCheckerPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final TextServicesManager mTextServicesManager;

    public String getPreferenceKey() {
        return "spellcheckers_settings";
    }

    public SpellCheckerPreferenceController(Context context) {
        super(context);
        this.mTextServicesManager = (TextServicesManager) context.getSystemService("textservices");
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference("spellcheckers_settings");
        if (findPreference != null) {
            InputMethodAndSubtypeUtilCompat.removeUnnecessaryNonPersistentPreference(findPreference);
        }
    }

    public boolean isAvailable() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_spellcheckers_settings);
    }

    public void updateState(Preference preference) {
        if (preference != null) {
            if (!this.mTextServicesManager.isSpellCheckerEnabled()) {
                preference.setSummary((int) R.string.switch_off_text);
                return;
            }
            SpellCheckerInfo currentSpellChecker = this.mTextServicesManager.getCurrentSpellChecker();
            if (currentSpellChecker != null) {
                preference.setSummary(currentSpellChecker.loadLabel(this.mContext.getPackageManager()));
            } else {
                preference.setSummary((int) R.string.spell_checker_not_selected);
            }
        }
    }
}
