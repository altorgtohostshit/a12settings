package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.content.Context;
import android.service.notification.ZenPolicy;
import android.util.Log;
import android.util.Pair;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class ZenRuleRepeatCallersPreferenceController extends AbstractZenCustomRulePreferenceController implements Preference.OnPreferenceChangeListener {
    private final int mRepeatCallersThreshold;

    public /* bridge */ /* synthetic */ boolean isAvailable() {
        return super.isAvailable();
    }

    public /* bridge */ /* synthetic */ void onResume(AutomaticZenRule automaticZenRule, String str) {
        super.onResume(automaticZenRule, str);
    }

    public ZenRuleRepeatCallersPreferenceController(Context context, String str, Lifecycle lifecycle, int i) {
        super(context, str, lifecycle);
        this.mRepeatCallersThreshold = i;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        setRepeatCallerSummary(preferenceScreen.findPreference(this.KEY));
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        AutomaticZenRule automaticZenRule = this.mRule;
        if (automaticZenRule != null && automaticZenRule.getZenPolicy() != null) {
            SwitchPreference switchPreference = (SwitchPreference) preference;
            boolean z = false;
            if (this.mRule.getZenPolicy().getPriorityCallSenders() == 1) {
                switchPreference.setEnabled(false);
                switchPreference.setChecked(true);
                return;
            }
            switchPreference.setEnabled(true);
            if (this.mRule.getZenPolicy().getPriorityCategoryRepeatCallers() == 1) {
                z = true;
            }
            switchPreference.setChecked(z);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        if (ZenModeSettingsBase.DEBUG) {
            Log.d("PrefControllerMixin", this.KEY + " onPrefChange mRule=" + this.mRule + " mCategory=" + 4 + " allow=" + booleanValue);
        }
        this.mMetricsFeatureProvider.action(this.mContext, 171, (Pair<Integer, Object>[]) new Pair[]{Pair.create(1602, Integer.valueOf(booleanValue ? 1 : 0)), Pair.create(1603, this.mId)});
        this.mRule.setZenPolicy(new ZenPolicy.Builder(this.mRule.getZenPolicy()).allowRepeatCallers(booleanValue).build());
        this.mBackend.updateZenRule(this.mId, this.mRule);
        return true;
    }

    private void setRepeatCallerSummary(Preference preference) {
        preference.setSummary((CharSequence) this.mContext.getString(R.string.zen_mode_repeat_callers_summary, new Object[]{Integer.valueOf(this.mRepeatCallersThreshold)}));
    }
}
