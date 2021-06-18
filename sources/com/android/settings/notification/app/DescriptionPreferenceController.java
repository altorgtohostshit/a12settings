package com.android.settings.notification.app;

import android.app.NotificationChannel;
import android.content.Context;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.notification.NotificationBackend;

public class DescriptionPreferenceController extends NotificationPreferenceController implements PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "desc";
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return false;
    }

    public DescriptionPreferenceController(Context context) {
        super(context, (NotificationBackend) null);
    }

    public boolean isAvailable() {
        if (!super.isAvailable()) {
            return false;
        }
        if (this.mChannel == null && !hasValidGroup()) {
            return false;
        }
        NotificationChannel notificationChannel = this.mChannel;
        if (notificationChannel != null && !TextUtils.isEmpty(notificationChannel.getDescription())) {
            return true;
        }
        if (!hasValidGroup() || TextUtils.isEmpty(this.mChannelGroup.getDescription())) {
            return false;
        }
        return true;
    }

    public void updateState(Preference preference) {
        if (this.mAppRow != null) {
            NotificationChannel notificationChannel = this.mChannel;
            if (notificationChannel != null) {
                preference.setTitle((CharSequence) notificationChannel.getDescription());
            } else if (hasValidGroup()) {
                preference.setTitle((CharSequence) this.mChannelGroup.getDescription());
            }
        }
        preference.setEnabled(false);
        preference.setSelectable(false);
    }
}
