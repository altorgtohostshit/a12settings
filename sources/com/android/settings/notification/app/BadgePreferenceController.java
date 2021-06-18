package com.android.settings.notification.app;

import android.app.NotificationChannel;
import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.RestrictedSwitchPreference;

public class BadgePreferenceController extends NotificationPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    public String getPreferenceKey() {
        return "badge";
    }

    public BadgePreferenceController(Context context, NotificationBackend notificationBackend) {
        super(context, notificationBackend);
    }

    public boolean isAvailable() {
        if (!super.isAvailable()) {
            return false;
        }
        if ((this.mAppRow == null && this.mChannel == null) || Settings.Secure.getInt(this.mContext.getContentResolver(), "notification_badging", 1) == 0) {
            return false;
        }
        if (this.mChannel == null || isDefaultChannel()) {
            return true;
        }
        NotificationBackend.AppRow appRow = this.mAppRow;
        if (appRow == null) {
            return false;
        }
        return appRow.showBadge;
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return this.mPreferenceFilter.contains("launcher");
    }

    public void updateState(Preference preference) {
        if (this.mAppRow != null) {
            RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preference;
            restrictedSwitchPreference.setDisabledByAdmin(this.mAdmin);
            NotificationChannel notificationChannel = this.mChannel;
            if (notificationChannel != null) {
                restrictedSwitchPreference.setChecked(notificationChannel.canShowBadge());
                restrictedSwitchPreference.setEnabled(!restrictedSwitchPreference.isDisabledByAdmin());
                return;
            }
            restrictedSwitchPreference.setChecked(this.mAppRow.showBadge);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        NotificationChannel notificationChannel = this.mChannel;
        if (notificationChannel != null) {
            notificationChannel.setShowBadge(booleanValue);
            saveChannel();
            return true;
        }
        NotificationBackend.AppRow appRow = this.mAppRow;
        if (appRow == null) {
            return true;
        }
        appRow.showBadge = booleanValue;
        this.mBackend.setShowBadge(appRow.pkg, appRow.uid, booleanValue);
        return true;
    }
}
