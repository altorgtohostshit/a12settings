package com.android.settings.password;

import android.content.Intent;
import com.android.settings.password.ConfirmLockPattern;

/* renamed from: com.android.settings.password.ConfirmLockPattern$ConfirmLockPatternFragment$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C1220x3615d43 implements Runnable {
    public final /* synthetic */ ConfirmLockPattern f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ C1220x3615d43(ConfirmLockPattern confirmLockPattern, Intent intent) {
        this.f$0 = confirmLockPattern;
        this.f$1 = intent;
    }

    public final void run() {
        ConfirmLockPattern.ConfirmLockPatternFragment.lambda$startDisappearAnimation$0(this.f$0, this.f$1);
    }
}
