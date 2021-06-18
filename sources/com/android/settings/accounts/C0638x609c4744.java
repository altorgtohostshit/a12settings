package com.android.settings.accounts;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import com.android.settings.accounts.RemoveAccountPreferenceController;

/* renamed from: com.android.settings.accounts.RemoveAccountPreferenceController$ConfirmRemoveAccountDialog$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C0638x609c4744 implements AccountManagerCallback {
    public final /* synthetic */ RemoveAccountPreferenceController.ConfirmRemoveAccountDialog f$0;

    public /* synthetic */ C0638x609c4744(RemoveAccountPreferenceController.ConfirmRemoveAccountDialog confirmRemoveAccountDialog) {
        this.f$0 = confirmRemoveAccountDialog;
    }

    public final void run(AccountManagerFuture accountManagerFuture) {
        this.f$0.lambda$onClick$0(accountManagerFuture);
    }
}
