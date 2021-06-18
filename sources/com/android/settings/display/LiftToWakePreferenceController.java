package com.android.settings.display;

import android.content.Context;
import android.hardware.SensorManager;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class LiftToWakePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    public String getPreferenceKey() {
        return "lift_to_wake";
    }

    public LiftToWakePreferenceController(Context context) {
        super(context);
    }

    public boolean isAvailable() {
        SensorManager sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        return (sensorManager == null || sensorManager.getDefaultSensor(23) == null) ? false : true;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "wake_gesture_enabled", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }

    public void updateState(Preference preference) {
        boolean z = false;
        SwitchPreference switchPreference = (SwitchPreference) preference;
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "wake_gesture_enabled", 0) != 0) {
            z = true;
        }
        switchPreference.setChecked(z);
    }
}
