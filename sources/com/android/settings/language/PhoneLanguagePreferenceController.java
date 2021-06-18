package com.android.settings.language;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.localepicker.LocaleListEditor;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.List;

public class PhoneLanguagePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "phone_language";
    }

    public PhoneLanguagePreferenceController(Context context) {
        super(context);
    }

    public boolean isAvailable() {
        if (!this.mContext.getResources().getBoolean(R.bool.config_show_phone_language) || this.mContext.getAssets().getLocales().length <= 1) {
            return false;
        }
        return true;
    }

    public void updateState(Preference preference) {
        if (preference != null) {
            preference.setSummary((CharSequence) FeatureFactory.getFactory(this.mContext).getLocaleFeatureProvider().getLocaleNames());
        }
    }

    public void updateNonIndexableKeys(List<String> list) {
        list.add(getPreferenceKey());
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!"phone_language".equals(preference.getKey())) {
            return false;
        }
        new SubSettingLauncher(this.mContext).setDestination(LocaleListEditor.class.getName()).setSourceMetricsCategory(750).setTitleRes(R.string.language_picker_title).launch();
        return true;
    }
}
