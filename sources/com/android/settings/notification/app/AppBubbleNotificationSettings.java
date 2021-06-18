package com.android.settings.notification.app;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.notification.AppBubbleListPreferenceController;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.notification.app.GlobalBubblePermissionObserverMixin;
import com.android.settings.notification.app.NotificationSettings;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class AppBubbleNotificationSettings extends NotificationSettings implements GlobalBubblePermissionObserverMixin.Listener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return false;
        }

        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return new ArrayList(AppBubbleNotificationSettings.getPreferenceControllers(context, (AppBubbleNotificationSettings) null, (NotificationSettings.DependentFieldListener) null));
        }
    };
    private GlobalBubblePermissionObserverMixin mObserverMixin;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AppBubNotiSettings";
    }

    public int getMetricsCategory() {
        return 1700;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.app_bubble_notification_settings;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        this.mControllers = getPreferenceControllers(context, this, this.mDependentFieldListener);
        return new ArrayList(this.mControllers);
    }

    protected static List<NotificationPreferenceController> getPreferenceControllers(Context context, AppBubbleNotificationSettings appBubbleNotificationSettings, NotificationSettings.DependentFieldListener dependentFieldListener) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new HeaderPreferenceController(context, appBubbleNotificationSettings));
        arrayList.add(new BubblePreferenceController(context, appBubbleNotificationSettings != null ? appBubbleNotificationSettings.getChildFragmentManager() : null, new NotificationBackend(), true, dependentFieldListener));
        arrayList.add(new AppBubbleListPreferenceController(context, new NotificationBackend()));
        return arrayList;
    }

    public void onGlobalBubblePermissionChanged() {
        updatePreferenceStates();
    }

    public void onResume() {
        super.onResume();
        if (this.mUid < 0 || TextUtils.isEmpty(this.mPkg) || this.mPkgInfo == null) {
            Log.w("AppBubNotiSettings", "Missing package or uid or packageinfo");
            finish();
            return;
        }
        for (NotificationPreferenceController next : this.mControllers) {
            next.onResume(this.mAppRow, (NotificationChannel) null, (NotificationChannelGroup) null, (Drawable) null, (ShortcutInfo) null, this.mSuspendedAppsAdmin, (List<String>) null);
            next.displayPreference(getPreferenceScreen());
        }
        updatePreferenceStates();
        GlobalBubblePermissionObserverMixin globalBubblePermissionObserverMixin = new GlobalBubblePermissionObserverMixin(getContext(), this);
        this.mObserverMixin = globalBubblePermissionObserverMixin;
        globalBubblePermissionObserverMixin.onStart();
    }

    public void onPause() {
        this.mObserverMixin.onStop();
        super.onPause();
    }
}
