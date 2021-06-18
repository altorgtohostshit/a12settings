package com.android.settings.password;

import android.content.Intent;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.settings.password.ConfirmLockPattern;

/* renamed from: com.android.settings.password.ConfirmLockPattern$ConfirmLockPatternFragment$3$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C1221x7a417012 implements LockPatternChecker.OnVerifyCallback {
    public final /* synthetic */ ConfirmLockPattern.ConfirmLockPatternFragment.C12173 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ Intent f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ C1221x7a417012(ConfirmLockPattern.ConfirmLockPatternFragment.C12173 r1, int i, Intent intent, int i2) {
        this.f$0 = r1;
        this.f$1 = i;
        this.f$2 = intent;
        this.f$3 = i2;
    }

    public final void onVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
        this.f$0.lambda$startVerifyPattern$0(this.f$1, this.f$2, this.f$3, verifyCredentialResponse, i);
    }
}
