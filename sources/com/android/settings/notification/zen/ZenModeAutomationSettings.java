package com.android.settings.notification.zen;

import android.app.AlertDialog;
import android.app.AutomaticZenRule;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.utils.ManagedServiceSettings;
import com.android.settings.utils.ZenServiceListing;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZenModeAutomationSettings extends ZenModeSettingsBase {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.zen_mode_automation_settings) {
        public List<String> getNonIndexableKeys(Context context) {
            List<String> nonIndexableKeys = super.getNonIndexableKeys(context);
            nonIndexableKeys.add("zen_mode_add_automatic_rule");
            nonIndexableKeys.add("zen_mode_automatic_rules");
            return nonIndexableKeys;
        }

        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return ZenModeAutomationSettings.buildPreferenceControllers(context, (Fragment) null, (ZenServiceListing) null, (Lifecycle) null);
        }
    };
    protected final ManagedServiceSettings.Config CONFIG = getConditionProviderConfig();
    private final int DELETE_RULES = 1;
    /* access modifiers changed from: private */
    public boolean[] mDeleteDialogChecked;
    /* access modifiers changed from: private */
    public String[] mDeleteDialogRuleIds;
    private CharSequence[] mDeleteDialogRuleNames;

    public int getMetricsCategory() {
        return 142;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.zen_mode_automation_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("DELETE_RULE")) {
            this.mBackend.removeZenRule(arguments.getString("DELETE_RULE"));
            arguments.remove("DELETE_RULE");
        }
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ZenServiceListing zenServiceListing = new ZenServiceListing(getContext(), this.CONFIG);
        zenServiceListing.reloadApprovedServices();
        return buildPreferenceControllers(context, this, zenServiceListing, getSettingsLifecycle());
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Fragment fragment, ZenServiceListing zenServiceListing, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ZenModeAddAutomaticRulePreferenceController(context, fragment, zenServiceListing, lifecycle));
        arrayList.add(new ZenModeAutomaticRulesPreferenceController(context, fragment, lifecycle));
        return arrayList;
    }

    protected static ManagedServiceSettings.Config getConditionProviderConfig() {
        return new ManagedServiceSettings.Config.Builder().setTag("ZenModeSettings").setIntentAction("android.service.notification.ConditionProviderService").setConfigurationIntentAction("android.app.action.AUTOMATIC_ZEN_RULE").setPermission("android.permission.BIND_CONDITION_PROVIDER_SERVICE").setNoun("condition provider").build();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.add(0, 1, 0, R.string.zen_mode_delete_automatic_rules);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 1) {
            return super.onOptionsItemSelected(menuItem);
        }
        final Map.Entry[] automaticZenRules = this.mBackend.getAutomaticZenRules();
        this.mDeleteDialogRuleNames = new CharSequence[automaticZenRules.length];
        this.mDeleteDialogRuleIds = new String[automaticZenRules.length];
        this.mDeleteDialogChecked = new boolean[automaticZenRules.length];
        for (int i = 0; i < automaticZenRules.length; i++) {
            this.mDeleteDialogRuleNames[i] = ((AutomaticZenRule) automaticZenRules[i].getValue()).getName();
            this.mDeleteDialogRuleIds[i] = (String) automaticZenRules[i].getKey();
        }
        new AlertDialog.Builder(this.mContext).setTitle(R.string.zen_mode_delete_automatic_rules).setMultiChoiceItems(this.mDeleteDialogRuleNames, (boolean[]) null, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialogInterface, int i, boolean z) {
                ZenModeAutomationSettings.this.mDeleteDialogChecked[i] = z;
            }
        }).setPositiveButton(R.string.zen_mode_schedule_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int i2 = 0; i2 < automaticZenRules.length; i2++) {
                    if (ZenModeAutomationSettings.this.mDeleteDialogChecked[i2]) {
                        ZenModeAutomationSettings zenModeAutomationSettings = ZenModeAutomationSettings.this;
                        zenModeAutomationSettings.mBackend.removeZenRule(zenModeAutomationSettings.mDeleteDialogRuleIds[i2]);
                    }
                }
            }
        }).show();
        return true;
    }
}
