package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.content.Context;
import android.service.notification.ZenPolicy;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.RadioButtonPreference;

public class ZenRuleCustomPolicyPreferenceController extends AbstractZenCustomRulePreferenceController {
    private RadioButtonPreference mPreference;

    public /* bridge */ /* synthetic */ boolean isAvailable() {
        return super.isAvailable();
    }

    public /* bridge */ /* synthetic */ void onResume(AutomaticZenRule automaticZenRule, String str) {
        super.onResume(automaticZenRule, str);
    }

    public ZenRuleCustomPolicyPreferenceController(Context context, Lifecycle lifecycle, String str) {
        super(context, str, lifecycle);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        RadioButtonPreference radioButtonPreference = (RadioButtonPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = radioButtonPreference;
        radioButtonPreference.setExtraWidgetOnClickListener(new C1173xdfd2fb48(this));
        this.mPreference.setOnClickListener(new C1174xdfd2fb49(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(View view) {
        setCustomPolicy();
        launchCustomSettings();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$1(RadioButtonPreference radioButtonPreference) {
        setCustomPolicy();
        launchCustomSettings();
    }

    public void updateState(Preference preference) {
        AutomaticZenRule automaticZenRule;
        super.updateState(preference);
        if (this.mId != null && (automaticZenRule = this.mRule) != null) {
            this.mPreference.setChecked(automaticZenRule.getZenPolicy() != null);
        }
    }

    private void setCustomPolicy() {
        if (this.mRule.getZenPolicy() == null) {
            this.mRule.setZenPolicy(this.mBackend.setDefaultZenPolicy(new ZenPolicy()));
            this.mBackend.updateZenRule(this.mId, this.mRule);
        }
    }

    private void launchCustomSettings() {
        new SubSettingLauncher(this.mContext).setDestination(ZenCustomRuleConfigSettings.class.getName()).setArguments(createBundle()).setSourceMetricsCategory(1605).launch();
    }
}
