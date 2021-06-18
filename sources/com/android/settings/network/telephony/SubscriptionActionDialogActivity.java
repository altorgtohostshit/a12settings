package com.android.settings.network.telephony;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import com.android.settings.network.telephony.ProgressDialogFragment;

public class SubscriptionActionDialogActivity extends Activity {
    protected SubscriptionManager mSubscriptionManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSubscriptionManager = (SubscriptionManager) getSystemService(SubscriptionManager.class);
    }

    /* access modifiers changed from: protected */
    public void showProgressDialog(String str) {
        ProgressDialogFragment.show(getFragmentManager(), str, (ProgressDialogFragment.OnDismissCallback) null);
    }

    /* access modifiers changed from: protected */
    public void dismissProgressDialog() {
        ProgressDialogFragment.dismiss(getFragmentManager());
    }

    /* access modifiers changed from: protected */
    public void showErrorDialog(String str, String str2) {
        AlertDialogFragment.show(this, str, str2);
    }
}
