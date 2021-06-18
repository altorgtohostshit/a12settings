package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.content.Context;
import android.service.notification.ZenPolicy;
import android.util.Log;
import android.util.Pair;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class ZenRuleCustomSwitchPreferenceController extends AbstractZenCustomRulePreferenceController implements Preference.OnPreferenceChangeListener {
    private int mCategory;
    private int mMetricsCategory;

    public /* bridge */ /* synthetic */ boolean isAvailable() {
        return super.isAvailable();
    }

    public /* bridge */ /* synthetic */ void onResume(AutomaticZenRule automaticZenRule, String str) {
        super.onResume(automaticZenRule, str);
    }

    public ZenRuleCustomSwitchPreferenceController(Context context, Lifecycle lifecycle, String str, int i, int i2) {
        super(context, str, lifecycle);
        this.mCategory = i;
        this.mMetricsCategory = i2;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        AutomaticZenRule automaticZenRule = this.mRule;
        if (automaticZenRule != null && automaticZenRule.getZenPolicy() != null) {
            ((SwitchPreference) preference).setChecked(this.mRule.getZenPolicy().isCategoryAllowed(this.mCategory, false));
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        if (ZenModeSettingsBase.DEBUG) {
            Log.d("PrefControllerMixin", this.KEY + " onPrefChange mRule=" + this.mRule + " mCategory=" + this.mCategory + " allow=" + booleanValue);
        }
        this.mMetricsFeatureProvider.action(this.mContext, this.mMetricsCategory, (Pair<Integer, Object>[]) new Pair[]{Pair.create(1602, Integer.valueOf(booleanValue ? 1 : 0)), Pair.create(1603, this.mId)});
        this.mRule.setZenPolicy(new ZenPolicy.Builder(this.mRule.getZenPolicy()).allowCategory(this.mCategory, booleanValue).build());
        this.mBackend.updateZenRule(this.mId, this.mRule);
        return true;
    }
}
