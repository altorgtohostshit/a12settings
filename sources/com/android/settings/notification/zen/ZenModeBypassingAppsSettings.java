package com.android.settings.notification.zen;

import android.app.Application;
import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class ZenModeBypassingAppsSettings extends ZenModeSettingsBase {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.zen_mode_bypassing_apps) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return ZenModeBypassingAppsSettings.buildPreferenceControllers(context, (Application) null, (Fragment) null, (NotificationBackend) null);
        }
    };
    private final String TAG = "ZenBypassingApps";

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ZenBypassingApps";
    }

    public int getMetricsCategory() {
        return 1588;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.zen_mode_bypassing_apps;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        FragmentActivity activity = getActivity();
        return buildPreferenceControllers(context, activity != null ? activity.getApplication() : null, this, new NotificationBackend());
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Application application, Fragment fragment, NotificationBackend notificationBackend) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ZenModeAllBypassingAppsPreferenceController(context, application, fragment, notificationBackend));
        arrayList.add(new ZenModeAddBypassingAppsPreferenceController(context, application, fragment, notificationBackend));
        return arrayList;
    }
}
