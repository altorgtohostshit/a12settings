package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.SubSettingLauncher;

public abstract class ZenModeRuleSettingsBase extends ZenModeSettingsBase {
    protected static final boolean DEBUG = ZenModeSettingsBase.DEBUG;
    private final String CUSTOM_BEHAVIOR_KEY = "zen_custom_setting";
    protected ZenRuleButtonsPreferenceController mActionButtons;
    protected Context mContext;
    protected Preference mCustomBehaviorPreference;
    protected boolean mDisableListeners;
    protected ZenAutomaticRuleHeaderPreferenceController mHeader;
    protected String mId;
    protected AutomaticZenRule mRule;
    protected ZenAutomaticRuleSwitchPreferenceController mSwitch;

    public int getHelpResource() {
        return R.string.help_uri_interruptions;
    }

    /* access modifiers changed from: protected */
    public abstract void onCreateInternal();

    /* access modifiers changed from: protected */
    public abstract boolean setRule(AutomaticZenRule automaticZenRule);

    /* access modifiers changed from: protected */
    public abstract void updateControlsInternal();

    public void onCreate(Bundle bundle) {
        this.mContext = getActivity();
        Intent intent = getActivity().getIntent();
        boolean z = DEBUG;
        if (z) {
            Log.d("ZenModeSettings", "onCreate getIntent()=" + intent);
        }
        if (intent == null) {
            Log.w("ZenModeSettings", "No intent");
            toastAndFinish();
            return;
        }
        String stringExtra = intent.getStringExtra("android.service.notification.extra.RULE_ID");
        this.mId = stringExtra;
        if (stringExtra == null) {
            Log.w("ZenModeSettings", "rule id is null");
            toastAndFinish();
            return;
        }
        if (z) {
            Log.d("ZenModeSettings", "mId=" + this.mId);
        }
        if (!refreshRuleOrFinish()) {
            super.onCreate(bundle);
            Preference findPreference = getPreferenceScreen().findPreference("zen_custom_setting");
            this.mCustomBehaviorPreference = findPreference;
            findPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Bundle bundle = new Bundle();
                    bundle.putString("RULE_ID", ZenModeRuleSettingsBase.this.mId);
                    new SubSettingLauncher(ZenModeRuleSettingsBase.this.mContext).setDestination(ZenCustomRuleSettings.class.getName()).setArguments(bundle).setSourceMetricsCategory(0).launch();
                    return true;
                }
            });
            onCreateInternal();
        }
    }

    public void onResume() {
        super.onResume();
        if (!isUiRestricted() && !refreshRuleOrFinish()) {
            updateControls();
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Utils.setActionBarShadowAnimation(getActivity(), getSettingsLifecycle(), getListView());
    }

    /* access modifiers changed from: protected */
    public void updateHeader() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        this.mSwitch.onResume(this.mRule, this.mId);
        this.mSwitch.displayPreference(preferenceScreen);
        updatePreference(this.mSwitch);
        this.mHeader.onResume(this.mRule, this.mId);
        this.mHeader.displayPreference(preferenceScreen);
        updatePreference(this.mHeader);
        this.mActionButtons.onResume(this.mRule, this.mId);
        this.mActionButtons.displayPreference(preferenceScreen);
        updatePreference(this.mActionButtons);
    }

    /* access modifiers changed from: protected */
    public void updateRule(Uri uri) {
        this.mRule.setConditionId(uri);
        this.mBackend.updateZenRule(this.mId, this.mRule);
    }

    /* access modifiers changed from: protected */
    public void onZenModeConfigChanged() {
        super.onZenModeConfigChanged();
        if (!refreshRuleOrFinish()) {
            updateControls();
        }
    }

    private boolean refreshRuleOrFinish() {
        this.mRule = getZenRule();
        if (DEBUG) {
            Log.d("ZenModeSettings", "mRule=" + this.mRule);
        }
        if (setRule(this.mRule)) {
            return false;
        }
        toastAndFinish();
        return true;
    }

    private void toastAndFinish() {
        Toast.makeText(this.mContext, R.string.zen_mode_rule_not_found_text, 0).show();
        getActivity().finish();
    }

    private AutomaticZenRule getZenRule() {
        return NotificationManager.from(this.mContext).getAutomaticZenRule(this.mId);
    }

    private void updateControls() {
        this.mDisableListeners = true;
        updateControlsInternal();
        updateHeader();
        if (this.mRule.getZenPolicy() == null) {
            this.mCustomBehaviorPreference.setSummary((int) R.string.zen_mode_custom_behavior_summary_default);
        } else {
            this.mCustomBehaviorPreference.setSummary((int) R.string.zen_mode_custom_behavior_summary);
        }
        this.mDisableListeners = false;
    }
}
