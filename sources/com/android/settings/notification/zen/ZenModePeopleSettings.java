package com.android.settings.notification.zen;

import android.app.Application;
import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.android.settings.R;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class ZenModePeopleSettings extends ZenModeSettingsBase {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.zen_mode_people_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return ZenModePeopleSettings.buildPreferenceControllers(context, (Lifecycle) null, (Application) null, (Fragment) null, (FragmentManager) null, (NotificationBackend) null);
        }
    };

    public int getMetricsCategory() {
        return 1823;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.zen_mode_people_settings;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        FragmentActivity activity = getActivity();
        return buildPreferenceControllers(context, getSettingsLifecycle(), activity != null ? activity.getApplication() : null, this, getFragmentManager(), new NotificationBackend());
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle, Application application, Fragment fragment, FragmentManager fragmentManager, NotificationBackend notificationBackend) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ZenModeConversationsImagePreferenceController(context, "zen_mode_conversations_image", lifecycle, notificationBackend));
        arrayList.add(new ZenModeConversationsPreferenceController(context, "zen_mode_conversations", lifecycle));
        arrayList.add(new ZenModeCallsPreferenceController(context, lifecycle, "zen_mode_people_calls"));
        arrayList.add(new ZenModeMessagesPreferenceController(context, lifecycle, "zen_mode_people_messages"));
        arrayList.add(new ZenModeSettingsFooterPreferenceController(context, lifecycle, fragmentManager));
        return arrayList;
    }
}
