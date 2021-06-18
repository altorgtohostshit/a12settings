package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.content.Context;
import android.service.notification.ZenPolicy;
import android.util.Pair;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.RadioButtonPreference;

public class ZenRuleDefaultPolicyPreferenceController extends AbstractZenCustomRulePreferenceController {
    private RadioButtonPreference mPreference;

    public /* bridge */ /* synthetic */ boolean isAvailable() {
        return super.isAvailable();
    }

    public /* bridge */ /* synthetic */ void onResume(AutomaticZenRule automaticZenRule, String str) {
        super.onResume(automaticZenRule, str);
    }

    public ZenRuleDefaultPolicyPreferenceController(Context context, Lifecycle lifecycle, String str) {
        super(context, str, lifecycle);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        RadioButtonPreference radioButtonPreference = (RadioButtonPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = radioButtonPreference;
        radioButtonPreference.setOnClickListener(new C1175xe3f1befa(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(RadioButtonPreference radioButtonPreference) {
        this.mRule.setZenPolicy((ZenPolicy) null);
        this.mBackend.updateZenRule(this.mId, this.mRule);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (this.mId != null && this.mRule != null) {
            boolean z = true;
            this.mMetricsFeatureProvider.action(this.mContext, 1606, (Pair<Integer, Object>[]) new Pair[]{Pair.create(1603, this.mId)});
            RadioButtonPreference radioButtonPreference = this.mPreference;
            if (this.mRule.getZenPolicy() != null) {
                z = false;
            }
            radioButtonPreference.setChecked(z);
        }
    }
}
