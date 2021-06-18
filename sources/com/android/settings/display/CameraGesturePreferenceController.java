package com.android.settings.display;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class CameraGesturePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    public String getPreferenceKey() {
        return "camera_gesture";
    }

    public CameraGesturePreferenceController(Context context) {
        super(context);
    }

    public void updateState(Preference preference) {
        boolean z = false;
        SwitchPreference switchPreference = (SwitchPreference) preference;
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "camera_gesture_disabled", 0) == 0) {
            z = true;
        }
        switchPreference.setChecked(z);
    }

    public boolean isAvailable() {
        if (!(this.mContext.getResources().getInteger(17694760) != -1) || SystemProperties.getBoolean("gesture.disable_camera_launch", false)) {
            return false;
        }
        return true;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "camera_gesture_disabled", ((Boolean) obj).booleanValue() ^ true ? 1 : 0);
        return true;
    }
}
