package com.android.settings.notification.zen;

import android.content.Context;
import android.icu.text.MessageFormat;
import com.android.settings.R;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.HashMap;
import java.util.Locale;

public class ZenModeDurationPreferenceController extends AbstractZenModePreferenceController {
    public String getPreferenceKey() {
        return "zen_mode_duration_settings";
    }

    public boolean isAvailable() {
        return true;
    }

    public ZenModeDurationPreferenceController(Context context, Lifecycle lifecycle) {
        super(context, "zen_mode_duration_settings", lifecycle);
    }

    public CharSequence getSummary() {
        int zenDuration = getZenDuration();
        if (zenDuration < 0) {
            return this.mContext.getString(R.string.zen_mode_duration_summary_always_prompt);
        }
        if (zenDuration == 0) {
            return this.mContext.getString(R.string.zen_mode_duration_summary_forever);
        }
        if (zenDuration >= 60) {
            MessageFormat messageFormat = new MessageFormat(this.mContext.getString(R.string.zen_mode_duration_summary_time_hours), Locale.getDefault());
            HashMap hashMap = new HashMap();
            hashMap.put("count", Integer.valueOf(zenDuration / 60));
            return messageFormat.format(hashMap);
        }
        MessageFormat messageFormat2 = new MessageFormat(this.mContext.getString(R.string.zen_mode_duration_summary_time_minutes), Locale.getDefault());
        HashMap hashMap2 = new HashMap();
        hashMap2.put("count", Integer.valueOf(zenDuration));
        return messageFormat2.format(hashMap2);
    }
}
