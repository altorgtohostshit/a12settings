package com.android.settings.deviceinfo;

import android.content.Context;
import android.os.SystemProperties;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class FccEquipmentIdPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "fcc_equipment_id";
    }

    public FccEquipmentIdPreferenceController(Context context) {
        super(context);
    }

    public boolean isAvailable() {
        return !TextUtils.isEmpty(SystemProperties.get("ro.ril.fccid"));
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference("fcc_equipment_id");
        if (findPreference != null) {
            findPreference.setSummary((CharSequence) SystemProperties.get("ro.ril.fccid", this.mContext.getResources().getString(R.string.device_info_default)));
        }
    }
}
