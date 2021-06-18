package com.android.settings.notification.zen;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.notification.zen.ZenModeSettings;
import com.android.settingslib.core.AbstractPreferenceController;

public class ZenModeAutomationPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final ZenModeSettings.SummaryBuilder mSummaryBuilder;

    public String getPreferenceKey() {
        return "zen_mode_automation_settings";
    }

    public boolean isAvailable() {
        return true;
    }

    public ZenModeAutomationPreferenceController(Context context) {
        super(context);
        this.mSummaryBuilder = new ZenModeSettings.SummaryBuilder(context);
    }

    public void updateState(Preference preference) {
        preference.setSummary((CharSequence) this.mSummaryBuilder.getAutomaticRulesSummary());
    }
}
