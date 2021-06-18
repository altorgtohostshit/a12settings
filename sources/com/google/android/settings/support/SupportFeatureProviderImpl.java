package com.google.android.settings.support;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.android.settings.overlay.SupportFeatureProvider;

public class SupportFeatureProviderImpl implements SupportFeatureProvider {
    static final String ACCOUNT_TYPE = "com.google";

    public SupportFeatureProviderImpl(Context context) {
    }

    /* access modifiers changed from: package-private */
    public Account[] getSupportEligibleAccounts(Context context) {
        return AccountManager.get(context).getAccountsByType(ACCOUNT_TYPE);
    }

    public void startSupport(Activity activity) {
        if (activity != null) {
            Account[] supportEligibleAccounts = getSupportEligibleAccounts(activity);
            Account account = null;
            if (supportEligibleAccounts.length != 0) {
                account = supportEligibleAccounts[0];
            }
            Intent intent = new Intent("com.android.settings.action.LAUNCH_HELP");
            intent.putExtra("isYoungDevice", PsdValuesLoader.getDeviceAgeInDays(activity.getContentResolver()) <= 30 ? "1" : "0");
            PsdBundle makePsdBundle = PsdValuesLoader.makePsdBundle(activity, 0);
            intent.putExtra("sendPackageName", activity.getResources().getBoolean(17891328));
            intent.putExtra("psdKeys", makePsdBundle.getKeys());
            intent.putExtra("psdValues", makePsdBundle.getValues());
            intent.putExtra("account", account);
            activity.startActivityForResult(intent, 0);
        }
    }
}
