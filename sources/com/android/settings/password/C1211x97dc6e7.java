package com.android.settings.password;

import android.content.Intent;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.settings.password.ConfirmLockPassword;

/* renamed from: com.android.settings.password.ConfirmLockPassword$ConfirmLockPasswordFragment$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C1211x97dc6e7 implements LockPatternChecker.OnVerifyCallback {
    public final /* synthetic */ ConfirmLockPassword.ConfirmLockPasswordFragment f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ Intent f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ C1211x97dc6e7(ConfirmLockPassword.ConfirmLockPasswordFragment confirmLockPasswordFragment, int i, Intent intent, int i2) {
        this.f$0 = confirmLockPasswordFragment;
        this.f$1 = i;
        this.f$2 = intent;
        this.f$3 = i2;
    }

    public final void onVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
        this.f$0.lambda$startVerifyPassword$0(this.f$1, this.f$2, this.f$3, verifyCredentialResponse, i);
    }
}
