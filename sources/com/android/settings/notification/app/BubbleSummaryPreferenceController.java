package com.android.settings.notification.app;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.notification.NotificationBackend;

public class BubbleSummaryPreferenceController extends NotificationPreferenceController {

    /* renamed from: ON */
    static final int f94ON = 1;

    public String getPreferenceKey() {
        return "bubble_pref_link";
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return false;
    }

    public BubbleSummaryPreferenceController(Context context, NotificationBackend notificationBackend) {
        super(context, notificationBackend);
    }

    public boolean isAvailable() {
        if (!super.isAvailable() || this.mAppRow == null) {
            return false;
        }
        if (this.mChannel != null) {
            if (!isGloballyEnabled()) {
                return false;
            }
            if (isDefaultChannel()) {
                return true;
            }
            if (this.mAppRow != null) {
                return true;
            }
            return false;
        } else if (!isGloballyEnabled()) {
            return false;
        } else {
            NotificationBackend notificationBackend = this.mBackend;
            NotificationBackend.AppRow appRow = this.mAppRow;
            if (notificationBackend.hasSentValidMsg(appRow.pkg, appRow.uid)) {
                return true;
            }
            return false;
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (this.mAppRow != null) {
            Intent intent = new Intent("android.settings.APP_NOTIFICATION_BUBBLE_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", this.mAppRow.pkg);
            intent.putExtra("app_uid", this.mAppRow.uid);
            preference.setIntent(intent);
        }
    }

    public CharSequence getSummary() {
        NotificationBackend.AppRow appRow = this.mAppRow;
        if (appRow == null) {
            return null;
        }
        int i = appRow.bubblePreference;
        Resources resources = this.mContext.getResources();
        if (i == 0 || !isGloballyEnabled()) {
            return resources.getString(R.string.bubble_app_setting_none);
        }
        if (i == 1) {
            return resources.getString(R.string.bubble_app_setting_all);
        }
        return resources.getString(R.string.bubble_app_setting_selected);
    }

    private boolean isGloballyEnabled() {
        return !((ActivityManager) this.mContext.getSystemService(ActivityManager.class)).isLowRamDevice() && Settings.Secure.getInt(this.mContext.getContentResolver(), "notification_bubbles", 1) == 1;
    }
}
