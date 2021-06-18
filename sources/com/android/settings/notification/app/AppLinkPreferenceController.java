package com.android.settings.notification.app;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.notification.NotificationBackend;

public class AppLinkPreferenceController extends NotificationPreferenceController implements PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "app_link";
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return false;
    }

    public AppLinkPreferenceController(Context context) {
        super(context, (NotificationBackend) null);
    }

    public boolean isAvailable() {
        if (super.isAvailable() && this.mAppRow.settingsIntent != null) {
            return true;
        }
        return false;
    }

    public void updateState(Preference preference) {
        NotificationBackend.AppRow appRow = this.mAppRow;
        if (appRow != null) {
            preference.setIntent(appRow.settingsIntent);
        }
    }
}
