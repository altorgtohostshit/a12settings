package com.android.settings.notification.app;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.notification.NotificationBackend;

public class NotificationsOffPreferenceController extends NotificationPreferenceController implements PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "block_desc";
    }

    public NotificationsOffPreferenceController(Context context) {
        super(context, (NotificationBackend) null);
    }

    public boolean isAvailable() {
        if (this.mAppRow == null) {
            return false;
        }
        if (this.mPreferenceFilter == null || isIncludedInFilter()) {
            return !super.isAvailable();
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return this.mPreferenceFilter.contains("importance");
    }

    public void updateState(Preference preference) {
        if (this.mAppRow != null) {
            if (this.mChannel != null) {
                preference.setTitle((int) R.string.channel_notifications_off_desc);
            } else if (this.mChannelGroup != null) {
                preference.setTitle((int) R.string.channel_group_notifications_off_desc);
            } else {
                preference.setTitle((int) R.string.app_notifications_off_desc);
            }
        }
        preference.setSelectable(false);
    }
}
