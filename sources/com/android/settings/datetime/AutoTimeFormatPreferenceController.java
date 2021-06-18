package com.android.settings.datetime;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.Locale;

public class AutoTimeFormatPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "auto_24hour";
    }

    public boolean isAvailable() {
        return true;
    }

    public AutoTimeFormatPreferenceController(Context context, UpdateTimeAndDateCallback updateTimeAndDateCallback) {
        super(context);
    }

    public void updateState(Preference preference) {
        if (preference instanceof SwitchPreference) {
            ((SwitchPreference) preference).setChecked(isAutoTimeFormatSelection(this.mContext));
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        Boolean bool;
        if (!(preference instanceof TwoStatePreference) || !TextUtils.equals("auto_24hour", preference.getKey())) {
            return false;
        }
        if (((SwitchPreference) preference).isChecked()) {
            bool = null;
        } else {
            bool = Boolean.valueOf(is24HourLocale(this.mContext.getResources().getConfiguration().locale));
        }
        TimeFormatPreferenceController.update24HourFormat(this.mContext, bool);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean is24HourLocale(Locale locale) {
        return DateFormat.is24HourLocale(locale);
    }

    static boolean isAutoTimeFormatSelection(Context context) {
        return Settings.System.getString(context.getContentResolver(), "time_12_24") == null;
    }
}
