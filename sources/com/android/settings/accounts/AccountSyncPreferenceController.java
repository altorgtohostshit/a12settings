package com.android.settings.accounts;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncAdapterType;
import android.os.Bundle;
import android.os.UserHandle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.accounts.AuthenticatorHelper;
import com.android.settingslib.core.AbstractPreferenceController;

public class AccountSyncPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, AuthenticatorHelper.OnAccountsUpdateListener {
    private Account mAccount;
    private Preference mPreference;
    private UserHandle mUserHandle;

    public String getPreferenceKey() {
        return "account_sync";
    }

    public boolean isAvailable() {
        return true;
    }

    public AccountSyncPreferenceController(Context context) {
        super(context);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!"account_sync".equals(preference.getKey())) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("account", this.mAccount);
        bundle.putParcelable("android.intent.extra.USER", this.mUserHandle);
        new SubSettingLauncher(this.mContext).setDestination(AccountSyncSettings.class.getName()).setArguments(bundle).setSourceMetricsCategory(8).setTitleRes(R.string.account_sync_title).launch();
        return true;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        updateSummary(preference);
    }

    public void onAccountsUpdate(UserHandle userHandle) {
        updateSummary(this.mPreference);
    }

    public void init(Account account, UserHandle userHandle) {
        this.mAccount = account;
        this.mUserHandle = userHandle;
    }

    /* access modifiers changed from: package-private */
    public void updateSummary(Preference preference) {
        int i;
        int i2;
        if (this.mAccount != null) {
            int identifier = this.mUserHandle.getIdentifier();
            SyncAdapterType[] syncAdapterTypesAsUser = ContentResolver.getSyncAdapterTypesAsUser(identifier);
            if (syncAdapterTypesAsUser != null) {
                i2 = 0;
                i = 0;
                for (SyncAdapterType syncAdapterType : syncAdapterTypesAsUser) {
                    if (syncAdapterType.accountType.equals(this.mAccount.type) && syncAdapterType.isUserVisible() && ContentResolver.getIsSyncableAsUser(this.mAccount, syncAdapterType.authority, identifier) > 0) {
                        i2++;
                        boolean syncAutomaticallyAsUser = ContentResolver.getSyncAutomaticallyAsUser(this.mAccount, syncAdapterType.authority, identifier);
                        if ((!ContentResolver.getMasterSyncAutomaticallyAsUser(identifier)) || syncAutomaticallyAsUser) {
                            i++;
                        }
                    }
                }
            } else {
                i2 = 0;
                i = 0;
            }
            if (i == 0) {
                preference.setSummary((int) R.string.account_sync_summary_all_off);
            } else if (i == i2) {
                preference.setSummary((int) R.string.account_sync_summary_all_on);
            } else {
                preference.setSummary((CharSequence) this.mContext.getString(R.string.account_sync_summary_some_on, new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}));
            }
        }
    }
}
