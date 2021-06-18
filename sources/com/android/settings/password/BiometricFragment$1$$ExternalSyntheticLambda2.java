package com.android.settings.password;

import com.android.settings.password.BiometricFragment;

public final /* synthetic */ class BiometricFragment$1$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ BiometricFragment.C11911 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ CharSequence f$2;

    public /* synthetic */ BiometricFragment$1$$ExternalSyntheticLambda2(BiometricFragment.C11911 r1, int i, CharSequence charSequence) {
        this.f$0 = r1;
        this.f$1 = i;
        this.f$2 = charSequence;
    }

    public final void run() {
        this.f$0.lambda$onAuthenticationError$0(this.f$1, this.f$2);
    }
}
