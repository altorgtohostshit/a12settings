package com.android.settings.notification.zen;

import android.content.Context;
import com.android.settings.R;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class ZenCustomRuleNotificationsSettings extends ZenCustomRuleSettingsBase {
    public int getMetricsCategory() {
        return 1608;
    }

    /* access modifiers changed from: package-private */
    public String getPreferenceCategoryKey() {
        return "restrict_category";
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.zen_mode_restrict_notifications_settings;
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
        arrayList.add(new ZenRuleVisEffectsAllPreferenceController(context, getSettingsLifecycle(), "zen_mute_notifications"));
        this.mControllers.add(new ZenRuleVisEffectsNonePreferenceController(context, getSettingsLifecycle(), "zen_hide_notifications"));
        this.mControllers.add(new ZenRuleVisEffectsCustomPreferenceController(context, getSettingsLifecycle(), "zen_custom"));
        this.mControllers.add(new ZenRuleNotifFooterPreferenceController(context, getSettingsLifecycle(), "footer_preference"));
        return this.mControllers;
    }
}
