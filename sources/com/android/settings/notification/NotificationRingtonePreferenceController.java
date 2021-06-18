package com.android.settings.notification;

import android.content.Context;
import com.android.settings.R;

public class NotificationRingtonePreferenceController extends RingtonePreferenceControllerBase {
    public String getPreferenceKey() {
        return "notification_ringtone";
    }

    public int getRingtoneType() {
        return 2;
    }

    public NotificationRingtonePreferenceController(Context context) {
        super(context);
    }

    public boolean isAvailable() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_notification_ringtone);
    }
}
