package com.android.settings.notification.zen;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class ZenModeConversationsSettings extends ZenModeSettingsBase {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.zen_mode_conversations_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return ZenModeConversationsSettings.buildPreferenceControllers(context, (Lifecycle) null, (NotificationBackend) null);
        }
    };
    private final NotificationBackend mNotificationBackend = new NotificationBackend();

    public int getMetricsCategory() {
        return 1837;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.zen_mode_conversations_settings;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle(), this.mNotificationBackend);
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle, NotificationBackend notificationBackend) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ZenModeConversationsImagePreferenceController(context, "zen_mode_conversations_image", lifecycle, notificationBackend));
        arrayList.add(new ZenModePriorityConversationsPreferenceController(context, "zen_mode_conversations_radio_buttons", lifecycle, notificationBackend));
        return arrayList;
    }
}
