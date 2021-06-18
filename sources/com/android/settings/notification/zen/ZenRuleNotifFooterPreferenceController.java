package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class ZenRuleNotifFooterPreferenceController extends AbstractZenCustomRulePreferenceController {
    public /* bridge */ /* synthetic */ void onResume(AutomaticZenRule automaticZenRule, String str) {
        super.onResume(automaticZenRule, str);
    }

    public ZenRuleNotifFooterPreferenceController(Context context, Lifecycle lifecycle, String str) {
        super(context, str, lifecycle);
    }

    public boolean isAvailable() {
        if (!super.isAvailable() || this.mRule.getZenPolicy() == null) {
            return false;
        }
        if (this.mRule.getZenPolicy().shouldHideAllVisualEffects() || this.mRule.getZenPolicy().shouldShowAllVisualEffects()) {
            return true;
        }
        return false;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        AutomaticZenRule automaticZenRule = this.mRule;
        if (automaticZenRule != null && automaticZenRule.getZenPolicy() != null) {
            if (this.mRule.getZenPolicy().shouldShowAllVisualEffects()) {
                preference.setTitle((int) R.string.zen_mode_restrict_notifications_mute_footer);
            } else if (this.mRule.getZenPolicy().shouldHideAllVisualEffects()) {
                preference.setTitle((int) R.string.zen_mode_restrict_notifications_hide_footer);
            } else {
                preference.setTitle((CharSequence) null);
            }
        }
    }
}
