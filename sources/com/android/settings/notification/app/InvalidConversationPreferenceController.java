package com.android.settings.notification.app;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.RestrictedSwitchPreference;

public class InvalidConversationPreferenceController extends NotificationPreferenceController implements Preference.OnPreferenceChangeListener {
    public String getPreferenceKey() {
        return "invalid_conversation_switch";
    }

    public InvalidConversationPreferenceController(Context context, NotificationBackend notificationBackend) {
        super(context, notificationBackend);
    }

    public boolean isAvailable() {
        NotificationBackend.AppRow appRow = this.mAppRow;
        if (appRow == null || appRow.banned) {
            return false;
        }
        if (this.mPreferenceFilter != null && !isIncludedInFilter()) {
            return false;
        }
        NotificationBackend notificationBackend = this.mBackend;
        NotificationBackend.AppRow appRow2 = this.mAppRow;
        return notificationBackend.isInInvalidMsgState(appRow2.pkg, appRow2.uid);
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return this.mPreferenceFilter.contains("conversation");
    }

    public void updateState(Preference preference) {
        if (this.mAppRow != null) {
            RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preference;
            restrictedSwitchPreference.setDisabledByAdmin(this.mAdmin);
            restrictedSwitchPreference.setEnabled(!restrictedSwitchPreference.isDisabledByAdmin());
            NotificationBackend notificationBackend = this.mBackend;
            NotificationBackend.AppRow appRow = this.mAppRow;
            restrictedSwitchPreference.setChecked(!notificationBackend.hasUserDemotedInvalidMsgApp(appRow.pkg, appRow.uid));
            preference.setSummary((CharSequence) this.mContext.getString(R.string.conversation_section_switch_summary));
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        NotificationBackend.AppRow appRow = this.mAppRow;
        if (appRow == null) {
            return false;
        }
        this.mBackend.setInvalidMsgAppDemoted(appRow.pkg, appRow.uid, !((Boolean) obj).booleanValue());
        return true;
    }
}
