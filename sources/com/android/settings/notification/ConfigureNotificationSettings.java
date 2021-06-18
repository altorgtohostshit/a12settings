package com.android.settings.notification;

import android.app.Application;
import android.app.usage.IUsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.FeatureFlagUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.RingtonePreference;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class ConfigureNotificationSettings extends DashboardFragment implements OnActivityResultListener {
    static final String KEY_SWIPE_DOWN = "gesture_swipe_down_fingerprint_notifications";
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.configure_notification_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return ConfigureNotificationSettings.buildPreferenceControllers(context, (Application) null, (Fragment) null);
        }

        public List<String> getNonIndexableKeys(Context context) {
            List<String> nonIndexableKeys = super.getNonIndexableKeys(context);
            nonIndexableKeys.add(ConfigureNotificationSettings.KEY_SWIPE_DOWN);
            return nonIndexableKeys;
        }
    };
    private RingtonePreference mRequestPreference;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ConfigNotiSettings";
    }

    public int getMetricsCategory() {
        return 337;
    }

    /* access modifiers changed from: protected */
    public boolean isParalleledControllers() {
        return true;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return FeatureFlagUtils.isEnabled(getContext(), "settings_silky_home") ? R.xml.configure_notification_settings_v2 : R.xml.configure_notification_settings;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        FragmentActivity activity = getActivity();
        return buildPreferenceControllers(context, activity != null ? activity.getApplication() : null, this);
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Application application, Fragment fragment) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new RecentNotifyingAppsPreferenceController(context, new NotificationBackend(), IUsageStatsManager.Stub.asInterface(ServiceManager.getService("usagestats")), (UserManager) context.getSystemService(UserManager.class), application, fragment));
        arrayList.add(new ShowOnLockScreenNotificationPreferenceController(context, "lock_screen_notifications"));
        arrayList.add(new NotificationRingtonePreferenceController(context) {
            public String getPreferenceKey() {
                return "notification_default_ringtone";
            }
        });
        arrayList.add(new NotificationAssistantPreferenceController(context, new NotificationBackend(), fragment, "notification_assistant"));
        if (FeatureFlagUtils.isEnabled(context, "settings_silky_home")) {
            arrayList.add(new EmergencyBroadcastPreferenceController(context, "app_and_notif_cell_broadcast_settings"));
        }
        return arrayList;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!FeatureFlagUtils.isEnabled(getContext(), "settings_silky_home")) {
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            Bundle arguments = getArguments();
            if (preferenceScreen != null && arguments != null && !TextUtils.isEmpty(arguments.getString(":settings:fragment_args_key"))) {
                PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference("configure_notifications_advanced");
                preferenceCategory.setInitialExpandedChildrenCount(Integer.MAX_VALUE);
                scrollToPreference(preferenceCategory);
            }
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (!(preference instanceof RingtonePreference)) {
            return super.onPreferenceTreeClick(preference);
        }
        writePreferenceClickMetric(preference);
        RingtonePreference ringtonePreference = (RingtonePreference) preference;
        this.mRequestPreference = ringtonePreference;
        ringtonePreference.onPrepareRingtonePickerIntent(ringtonePreference.getIntent());
        getActivity().startActivityForResultAsUser(this.mRequestPreference.getIntent(), 200, (Bundle) null, UserHandle.of(this.mRequestPreference.getUserId()));
        return true;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        RingtonePreference ringtonePreference = this.mRequestPreference;
        if (ringtonePreference != null) {
            ringtonePreference.onActivityResult(i, i2, intent);
            this.mRequestPreference = null;
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        RingtonePreference ringtonePreference = this.mRequestPreference;
        if (ringtonePreference != null) {
            bundle.putString("selected_preference", ringtonePreference.getKey());
        }
    }

    /* access modifiers changed from: protected */
    public void enableNAS(ComponentName componentName) {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        NotificationAssistantPreferenceController notificationAssistantPreferenceController = (NotificationAssistantPreferenceController) use(NotificationAssistantPreferenceController.class);
        notificationAssistantPreferenceController.setNotificationAssistantGranted(componentName);
        notificationAssistantPreferenceController.updateState(preferenceScreen.findPreference(notificationAssistantPreferenceController.getPreferenceKey()));
    }
}
