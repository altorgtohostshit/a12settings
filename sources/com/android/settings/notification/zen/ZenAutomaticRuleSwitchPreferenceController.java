package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.content.Context;
import android.widget.Switch;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class ZenAutomaticRuleSwitchPreferenceController extends AbstractZenModeAutomaticRulePreferenceController implements OnMainSwitchChangeListener {
    private String mId;
    private AutomaticZenRule mRule;
    private MainSwitchPreference mSwitchBar;

    public String getPreferenceKey() {
        return "zen_automatic_rule_switch";
    }

    public ZenAutomaticRuleSwitchPreferenceController(Context context, Fragment fragment, Lifecycle lifecycle) {
        super(context, "zen_automatic_rule_switch", fragment, lifecycle);
    }

    public boolean isAvailable() {
        return (this.mRule == null || this.mId == null) ? false : true;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference("zen_automatic_rule_switch");
        MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) findPreference;
        this.mSwitchBar = mainSwitchPreference;
        if (mainSwitchPreference != null) {
            mainSwitchPreference.setTitle(this.mContext.getString(R.string.zen_mode_use_automatic_rule));
            try {
                findPreference.setOnPreferenceClickListener(new C1121x7484d15e(this));
                this.mSwitchBar.addOnSwitchChangeListener(this);
            } catch (IllegalStateException unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$displayPreference$0(Preference preference) {
        AutomaticZenRule automaticZenRule = this.mRule;
        automaticZenRule.setEnabled(!automaticZenRule.isEnabled());
        this.mBackend.updateZenRule(this.mId, this.mRule);
        return true;
    }

    public void onResume(AutomaticZenRule automaticZenRule, String str) {
        this.mRule = automaticZenRule;
        this.mId = str;
    }

    public void updateState(Preference preference) {
        AutomaticZenRule automaticZenRule = this.mRule;
        if (automaticZenRule != null) {
            this.mSwitchBar.updateStatus(automaticZenRule.isEnabled());
        }
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        if (z != this.mRule.isEnabled()) {
            this.mRule.setEnabled(z);
            this.mBackend.updateZenRule(this.mId, this.mRule);
        }
    }
}
