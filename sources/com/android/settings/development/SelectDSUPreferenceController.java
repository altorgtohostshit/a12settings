package com.android.settings.development;

import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

class SelectDSUPreferenceController extends DeveloperOptionsPreferenceController implements PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "dsu_loader";
    }

    SelectDSUPreferenceController(Context context) {
        super(context);
    }

    private boolean isDSURunning() {
        return SystemProperties.getBoolean("ro.gsid.image_running", false);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!"dsu_loader".equals(preference.getKey())) {
            return false;
        }
        if (isDSURunning()) {
            return true;
        }
        this.mContext.startActivity(new Intent(this.mContext, DSULoader.class));
        return true;
    }

    public void updateState(Preference preference) {
        preference.setSummary((CharSequence) this.mContext.getResources().getString(isDSURunning() ? R.string.dsu_is_running : R.string.dsu_loader_description));
    }
}
