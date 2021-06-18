package com.android.settings.notification.app;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.notification.NotificationBackend;

public class InvalidConversationInfoPreferenceController extends NotificationPreferenceController {
    public String getPreferenceKey() {
        return "invalid_conversation_info";
    }

    public InvalidConversationInfoPreferenceController(Context context, NotificationBackend notificationBackend) {
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
        NotificationBackend.AppRow appRow = this.mAppRow;
        if (appRow != null) {
            preference.setSummary((CharSequence) this.mContext.getString(R.string.convo_not_supported_summary, new Object[]{appRow.label}));
        }
    }
}
