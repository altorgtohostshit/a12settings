package com.android.settings.notification;

import android.content.Context;

public class AlarmRingtonePreferenceController extends RingtonePreferenceControllerBase {
    public String getPreferenceKey() {
        return "alarm_ringtone";
    }

    public int getRingtoneType() {
        return 4;
    }

    public AlarmRingtonePreferenceController(Context context) {
        super(context);
    }
}
