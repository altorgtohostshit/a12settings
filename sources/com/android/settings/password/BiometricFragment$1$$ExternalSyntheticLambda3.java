package com.android.settings.password;

import android.hardware.biometrics.BiometricPrompt;
import com.android.settings.password.BiometricFragment;

public final /* synthetic */ class BiometricFragment$1$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ BiometricFragment.C11911 f$0;
    public final /* synthetic */ BiometricPrompt.AuthenticationResult f$1;

    public /* synthetic */ BiometricFragment$1$$ExternalSyntheticLambda3(BiometricFragment.C11911 r1, BiometricPrompt.AuthenticationResult authenticationResult) {
        this.f$0 = r1;
        this.f$1 = authenticationResult;
    }

    public final void run() {
        this.f$0.lambda$onAuthenticationSucceeded$1(this.f$1);
    }
}
