package com.android.settings.notification.zen;

import android.content.Context;
import com.android.settings.R;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class ZenCustomRuleSettings extends ZenCustomRuleSettingsBase {
    public int getMetricsCategory() {
        return 1604;
    }

    /* access modifiers changed from: package-private */
    public String getPreferenceCategoryKey() {
        return "zen_custom_rule_category";
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.zen_mode_custom_rule_settings;
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

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        this.mControllers = arrayList;
        arrayList.add(new ZenRuleDefaultPolicyPreferenceController(context, getSettingsLifecycle(), "zen_custom_rule_setting_default"));
        this.mControllers.add(new ZenRuleCustomPolicyPreferenceController(context, getSettingsLifecycle(), "zen_custom_rule_setting"));
        return this.mControllers;
    }
}
