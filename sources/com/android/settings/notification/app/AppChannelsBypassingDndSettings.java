package com.android.settings.notification.app;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class AppChannelsBypassingDndSettings extends NotificationSettings {
    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AppChannelsBypassingDndSettings";
    }

    public int getMetricsCategory() {
        return 1840;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.app_channels_bypassing_dnd_settings;
    }

    public void onResume() {
        super.onResume();
        if (this.mUid < 0 || TextUtils.isEmpty(this.mPkg) || this.mPkgInfo == null) {
            Log.w("AppChannelsBypassingDndSettings", "Missing package or uid or packageinfo");
            finish();
            return;
        }
        for (NotificationPreferenceController next : this.mControllers) {
            next.onResume(this.mAppRow, (NotificationChannel) null, (NotificationChannelGroup) null, (Drawable) null, (ShortcutInfo) null, this.mSuspendedAppsAdmin, (List<String>) null);
            next.displayPreference(getPreferenceScreen());
        }
        updatePreferenceStates();
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        this.mControllers = arrayList;
        arrayList.add(new HeaderPreferenceController(context, this));
        this.mControllers.add(new AppChannelsBypassingDndPreferenceController(context, new NotificationBackend()));
        return new ArrayList(this.mControllers);
    }
}
