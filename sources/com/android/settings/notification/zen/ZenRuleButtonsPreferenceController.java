package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.notification.zen.ZenDeleteRuleDialog;
import com.android.settings.notification.zen.ZenRuleNameDialog;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.ActionButtonsPreference;

public class ZenRuleButtonsPreferenceController extends AbstractZenModePreferenceController {
    private ActionButtonsPreference mButtonsPref;
    /* access modifiers changed from: private */
    public PreferenceFragmentCompat mFragment;
    /* access modifiers changed from: private */
    public String mId;
    /* access modifiers changed from: private */
    public AutomaticZenRule mRule;

    public ZenRuleButtonsPreferenceController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, Lifecycle lifecycle) {
        super(context, "zen_action_buttons", lifecycle);
        this.mFragment = preferenceFragmentCompat;
    }

    public boolean isAvailable() {
        return this.mRule != null;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        if (isAvailable()) {
            this.mButtonsPref = ((ActionButtonsPreference) preferenceScreen.findPreference("zen_action_buttons")).setButton1Text(R.string.zen_mode_rule_name_edit).setButton1Icon(17302765).setButton1OnClickListener(new EditRuleNameClickListener()).setButton2Text(R.string.zen_mode_delete_rule_button).setButton2Icon(R.drawable.ic_settings_delete).setButton2OnClickListener(new DeleteRuleClickListener());
        }
    }

    public class EditRuleNameClickListener implements View.OnClickListener {
        public EditRuleNameClickListener() {
        }

        public void onClick(View view) {
            ZenRuleNameDialog.show(ZenRuleButtonsPreferenceController.this.mFragment, ZenRuleButtonsPreferenceController.this.mRule.getName(), (Uri) null, new ZenRuleNameDialog.PositiveClickListener() {
                public void onOk(String str, Fragment fragment) {
                    if (!TextUtils.equals(str, ZenRuleButtonsPreferenceController.this.mRule.getName())) {
                        ZenRuleButtonsPreferenceController zenRuleButtonsPreferenceController = ZenRuleButtonsPreferenceController.this;
                        zenRuleButtonsPreferenceController.mMetricsFeatureProvider.action(zenRuleButtonsPreferenceController.mContext, 1267, (Pair<Integer, Object>[]) new Pair[0]);
                        ZenRuleButtonsPreferenceController.this.mRule.setName(str);
                        ZenRuleButtonsPreferenceController.this.mRule.setModified(true);
                        ZenRuleButtonsPreferenceController zenRuleButtonsPreferenceController2 = ZenRuleButtonsPreferenceController.this;
                        zenRuleButtonsPreferenceController2.mBackend.updateZenRule(zenRuleButtonsPreferenceController2.mId, ZenRuleButtonsPreferenceController.this.mRule);
                    }
                }
            });
        }
    }

    public class DeleteRuleClickListener implements View.OnClickListener {
        public DeleteRuleClickListener() {
        }

        public void onClick(View view) {
            ZenDeleteRuleDialog.show(ZenRuleButtonsPreferenceController.this.mFragment, ZenRuleButtonsPreferenceController.this.mRule.getName(), ZenRuleButtonsPreferenceController.this.mId, new ZenDeleteRuleDialog.PositiveClickListener() {
                public void onOk(String str) {
                    Bundle bundle = new Bundle();
                    bundle.putString("DELETE_RULE", str);
                    ZenRuleButtonsPreferenceController zenRuleButtonsPreferenceController = ZenRuleButtonsPreferenceController.this;
                    zenRuleButtonsPreferenceController.mMetricsFeatureProvider.action(zenRuleButtonsPreferenceController.mContext, 175, (Pair<Integer, Object>[]) new Pair[0]);
                    new SubSettingLauncher(ZenRuleButtonsPreferenceController.this.mContext).addFlags(67108864).setDestination(ZenModeAutomationSettings.class.getName()).setSourceMetricsCategory(142).setArguments(bundle).launch();
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onResume(AutomaticZenRule automaticZenRule, String str) {
        this.mRule = automaticZenRule;
        this.mId = str;
    }
}
