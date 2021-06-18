package com.google.android.settings.accessibility;

import android.content.Context;
import android.content.SharedPreferences;
import com.android.settings.R;

public class HapticsSharedPreferences {
    private Context mContext;

    public HapticsSharedPreferences(Context context) {
        this.mContext = context;
    }

    public SharedPreferences getSharedPreferences() {
        Context context = this.mContext;
        return context.getSharedPreferences(context.getString(R.string.accessibility_vibration_preferences_file), 0);
    }

    public void savePrimarySwitchOffTriggerReason(int i) {
        getSharedPreferences().edit().putInt("PRIMARY_SWITCH_OFF_TRIGGER", i).apply();
    }

    public int getPrimarySwitchOffTriggerReason() {
        return getSharedPreferences().getInt("PRIMARY_SWITCH_OFF_TRIGGER", 0);
    }

    public boolean isSwitchOffTriggerReasonDependencies() {
        int i = getSharedPreferences().getInt("PRIMARY_SWITCH_OFF_TRIGGER", 0);
        return i == 2 || i == 3;
    }

    public void setAckFlag(String str, boolean z) {
        getSharedPreferences().edit().putBoolean(str, z).apply();
    }

    public boolean isTriggerReasonAcknowledged(String str) {
        return getSharedPreferences().getBoolean(str, false);
    }
}
