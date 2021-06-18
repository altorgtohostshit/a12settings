package com.android.settings.accounts;

import android.accounts.Account;
import android.content.Context;

public class AccountFeatureProviderImpl implements AccountFeatureProvider {
    public String getAccountType() {
        return null;
    }

    public Account[] getAccounts(Context context) {
        return new Account[0];
    }
}
