package com.android.settings.notification;

import android.content.Context;
import com.android.settings.Utils;

public class PhoneRingtonePreferenceController extends RingtonePreferenceControllerBase {
    public String getPreferenceKey() {
        return "phone_ringtone";
    }

    public int getRingtoneType() {
        return 1;
    }

    public PhoneRingtonePreferenceController(Context context) {
        super(context);
    }

    public boolean isAvailable() {
        return Utils.isVoiceCapable(this.mContext);
    }
}
