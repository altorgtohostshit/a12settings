package com.android.settings.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import androidx.lifecycle.LifecycleObserver;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.applications.autofill.PasswordsPreferenceController;
import com.android.settings.applications.defaultapps.DefaultAutofillPreferenceController;
import com.android.settings.applications.defaultapps.DefaultWorkAutofillPreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.users.AutoSyncDataPreferenceController;
import com.android.settings.users.AutoSyncPersonalDataPreferenceController;
import com.android.settings.users.AutoSyncWorkDataPreferenceController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.search.SearchIndexableRaw;
import java.util.ArrayList;
import java.util.List;

public class AccountDashboardFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.accounts_dashboard_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            ArrayList arrayList = new ArrayList();
            AccountDashboardFragment.buildAccountPreferenceControllers(context, (SettingsPreferenceFragment) null, (String[]) null, arrayList);
            AccountDashboardFragment.buildAutofillPreferenceControllers(context, arrayList);
            return arrayList;
        }

        public List<SearchIndexableRaw> getDynamicRawDataToIndex(Context context, boolean z) {
            ArrayList arrayList = new ArrayList();
            for (UserInfo isManagedProfile : ((UserManager) context.getSystemService("user")).getProfiles(UserHandle.myUserId())) {
                if (isManagedProfile.isManagedProfile()) {
                    return arrayList;
                }
            }
            for (Account account : AccountManager.get(context).getAccounts()) {
                SearchIndexableRaw searchIndexableRaw = new SearchIndexableRaw(context);
                searchIndexableRaw.key = AccountTypePreference.buildKey(account);
                searchIndexableRaw.title = account.name;
                arrayList.add(searchIndexableRaw);
            }
            return arrayList;
        }
    };

    public int getHelpResource() {
        return R.string.help_url_user_and_account_dashboard;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AccountDashboardFrag";
    }

    public int getMetricsCategory() {
        return 8;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.accounts_dashboard_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        getSettingsLifecycle().addObserver((LifecycleObserver) use(PasswordsPreferenceController.class));
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        buildAutofillPreferenceControllers(context, arrayList);
        buildAccountPreferenceControllers(context, this, getIntent().getStringArrayExtra("authorities"), arrayList);
        return arrayList;
    }

    static void buildAutofillPreferenceControllers(Context context, List<AbstractPreferenceController> list) {
        list.add(new DefaultAutofillPreferenceController(context));
        list.add(new DefaultWorkAutofillPreferenceController(context));
    }

    /* access modifiers changed from: private */
    public static void buildAccountPreferenceControllers(Context context, SettingsPreferenceFragment settingsPreferenceFragment, String[] strArr, List<AbstractPreferenceController> list) {
        AccountPreferenceController accountPreferenceController = new AccountPreferenceController(context, settingsPreferenceFragment, strArr, 3);
        if (settingsPreferenceFragment != null) {
            settingsPreferenceFragment.getSettingsLifecycle().addObserver(accountPreferenceController);
        }
        list.add(accountPreferenceController);
        list.add(new AutoSyncDataPreferenceController(context, settingsPreferenceFragment));
        list.add(new AutoSyncPersonalDataPreferenceController(context, settingsPreferenceFragment));
        list.add(new AutoSyncWorkDataPreferenceController(context, settingsPreferenceFragment));
    }
}
