package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.Map;
import java.util.Objects;

public class ZenModeAutomaticRulesPreferenceController extends AbstractZenModeAutomaticRulePreferenceController {
    protected PreferenceCategory mPreferenceCategory;

    public String getPreferenceKey() {
        return "zen_mode_automatic_rules";
    }

    public boolean isAvailable() {
        return true;
    }

    public ZenModeAutomaticRulesPreferenceController(Context context, Fragment fragment, Lifecycle lifecycle) {
        super(context, "zen_mode_automatic_rules", fragment, lifecycle);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreferenceCategory = preferenceCategory;
        preferenceCategory.setPersistent(false);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        Map.Entry[] rules = getRules();
        if (this.mPreferenceCategory.getPreferenceCount() == rules.length) {
            int i = 0;
            while (i < rules.length) {
                ZenRulePreference zenRulePreference = (ZenRulePreference) this.mPreferenceCategory.getPreference(i);
                if (Objects.equals(zenRulePreference.mId, rules[i].getKey())) {
                    zenRulePreference.updatePreference((AutomaticZenRule) rules[i].getValue());
                    i++;
                } else {
                    reloadAllRules(rules);
                    return;
                }
            }
            return;
        }
        reloadAllRules(rules);
    }

    /* access modifiers changed from: package-private */
    public void reloadAllRules(Map.Entry<String, AutomaticZenRule>[] entryArr) {
        this.mPreferenceCategory.removeAll();
        for (Map.Entry<String, AutomaticZenRule> createZenRulePreference : entryArr) {
            this.mPreferenceCategory.addPreference(createZenRulePreference(createZenRulePreference));
        }
    }

    /* access modifiers changed from: package-private */
    public ZenRulePreference createZenRulePreference(Map.Entry<String, AutomaticZenRule> entry) {
        return new ZenRulePreference(this.mPreferenceCategory.getContext(), entry, this.mParent, this.mMetricsFeatureProvider);
    }
}
