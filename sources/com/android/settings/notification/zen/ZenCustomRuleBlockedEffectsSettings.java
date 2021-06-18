package com.android.settings.notification.zen;

import android.content.Context;
import android.os.Bundle;
import com.android.settings.R;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class ZenCustomRuleBlockedEffectsSettings extends ZenCustomRuleSettingsBase {
    public int getMetricsCategory() {
        return 1609;
    }

    /* access modifiers changed from: package-private */
    public String getPreferenceCategoryKey() {
        return null;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.zen_mode_block_settings;
    }

    public /* bridge */ /* synthetic */ int getHelpResource() {
        return super.getHelpResource();
    }

    public /* bridge */ /* synthetic */ void onAttach(Context context) {
        super.onAttach(context);
    }

    public /* bridge */ /* synthetic */ void onResume() {
        super.onResume();
    }

    public /* bridge */ /* synthetic */ void onZenModeConfigChanged() {
        super.onZenModeConfigChanged();
    }

    public /* bridge */ /* synthetic */ void updatePreferences() {
        super.updatePreferences();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        this.mControllers = arrayList;
        Context context2 = context;
        arrayList.add(new ZenRuleVisEffectPreferenceController(context2, getSettingsLifecycle(), "zen_effect_intent", 0, 1332, (int[]) null));
        this.mControllers.add(new ZenRuleVisEffectPreferenceController(context2, getSettingsLifecycle(), "zen_effect_light", 1, 1333, (int[]) null));
        this.mControllers.add(new ZenRuleVisEffectPreferenceController(context2, getSettingsLifecycle(), "zen_effect_peek", 2, 1334, (int[]) null));
        Context context3 = context;
        this.mControllers.add(new ZenRuleVisEffectPreferenceController(context3, getSettingsLifecycle(), "zen_effect_status", 3, 1335, new int[]{6}));
        this.mControllers.add(new ZenRuleVisEffectPreferenceController(context3, getSettingsLifecycle(), "zen_effect_badge", 4, 1336, (int[]) null));
        this.mControllers.add(new ZenRuleVisEffectPreferenceController(context3, getSettingsLifecycle(), "zen_effect_ambient", 5, 1337, (int[]) null));
        this.mControllers.add(new ZenRuleVisEffectPreferenceController(context3, getSettingsLifecycle(), "zen_effect_list", 6, 1338, (int[]) null));
        return this.mControllers;
    }
}
