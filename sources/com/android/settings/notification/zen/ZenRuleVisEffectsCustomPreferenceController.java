package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.RadioButtonPreference;

public class ZenRuleVisEffectsCustomPreferenceController extends AbstractZenCustomRulePreferenceController {
    private RadioButtonPreference mPreference;

    public /* bridge */ /* synthetic */ boolean isAvailable() {
        return super.isAvailable();
    }

    public /* bridge */ /* synthetic */ void onResume(AutomaticZenRule automaticZenRule, String str) {
        super.onResume(automaticZenRule, str);
    }

    public ZenRuleVisEffectsCustomPreferenceController(Context context, Lifecycle lifecycle, String str) {
        super(context, str, lifecycle);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        RadioButtonPreference radioButtonPreference = (RadioButtonPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = radioButtonPreference;
        radioButtonPreference.setOnClickListener(new C1184xbb8ff699(this));
        this.mPreference.setExtraWidgetOnClickListener(new C1183xbb8ff698(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(RadioButtonPreference radioButtonPreference) {
        launchCustomSettings();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$1(View view) {
        launchCustomSettings();
    }

    public void updateState(Preference preference) {
        AutomaticZenRule automaticZenRule;
        super.updateState(preference);
        if (this.mId != null && (automaticZenRule = this.mRule) != null && automaticZenRule.getZenPolicy() != null) {
            this.mPreference.setChecked(!this.mRule.getZenPolicy().shouldHideAllVisualEffects() && !this.mRule.getZenPolicy().shouldShowAllVisualEffects());
        }
    }

    private void launchCustomSettings() {
        this.mMetricsFeatureProvider.action(this.mContext, 1398, (Pair<Integer, Object>[]) new Pair[]{Pair.create(1603, this.mId)});
        new SubSettingLauncher(this.mContext).setDestination(ZenCustomRuleBlockedEffectsSettings.class.getName()).setArguments(createBundle()).setSourceMetricsCategory(1609).launch();
    }
}
