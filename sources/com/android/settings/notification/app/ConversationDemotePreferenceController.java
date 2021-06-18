package com.android.settings.notification.app;

import android.app.NotificationChannel;
import android.content.Context;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.notification.NotificationBackend;

public class ConversationDemotePreferenceController extends NotificationPreferenceController implements PreferenceControllerMixin {
    SettingsPreferenceFragment mHostFragment;

    public String getPreferenceKey() {
        return "demote";
    }

    public ConversationDemotePreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, NotificationBackend notificationBackend) {
        super(context, notificationBackend);
        this.mHostFragment = settingsPreferenceFragment;
    }

    public boolean isAvailable() {
        NotificationChannel notificationChannel;
        if (super.isAvailable() && this.mAppRow != null && (notificationChannel = this.mChannel) != null && !TextUtils.isEmpty(notificationChannel.getConversationId()) && !this.mChannel.isDemoted()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return this.mPreferenceFilter.contains("conversation");
    }

    public void updateState(Preference preference) {
        preference.setEnabled(this.mAdmin == null);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!"demote".equals(preference.getKey())) {
            return false;
        }
        this.mChannel.setDemoted(true);
        saveChannel();
        this.mHostFragment.getActivity().finish();
        return true;
    }
}
