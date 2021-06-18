package com.android.settings.notification.zen;

import android.content.Context;
import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class ZenModeBlockedEffectsSettings extends ZenModeSettingsBase {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.zen_mode_block_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return ZenModeBlockedEffectsSettings.buildPreferenceControllers(context, (Lifecycle) null);
        }
    };

    public int getMetricsCategory() {
        return 1339;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.zen_mode_block_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle());
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ZenModeVisEffectPreferenceController(context, lifecycle, "zen_effect_intent", 4, 1332, (int[]) null));
        Context context2 = context;
        Lifecycle lifecycle2 = lifecycle;
        arrayList.add(new ZenModeVisEffectPreferenceController(context2, lifecycle2, "zen_effect_light", 8, 1333, (int[]) null));
        Lifecycle lifecycle3 = lifecycle;
        arrayList.add(new ZenModeVisEffectPreferenceController(context, lifecycle3, "zen_effect_peek", 16, 1334, (int[]) null));
        arrayList.add(new ZenModeVisEffectPreferenceController(context2, lifecycle2, "zen_effect_status", 32, 1335, new int[]{256}));
        Context context3 = context;
        arrayList.add(new ZenModeVisEffectPreferenceController(context3, lifecycle3, "zen_effect_badge", 64, 1336, (int[]) null));
        arrayList.add(new ZenModeVisEffectPreferenceController(context2, lifecycle2, "zen_effect_ambient", 128, 1337, (int[]) null));
        arrayList.add(new ZenModeVisEffectPreferenceController(context3, lifecycle3, "zen_effect_list", 256, 1338, (int[]) null));
        return arrayList;
    }
}
