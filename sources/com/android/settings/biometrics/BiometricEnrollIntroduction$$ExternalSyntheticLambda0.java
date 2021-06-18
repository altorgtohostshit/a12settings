package com.android.settings.biometrics;

import android.content.Intent;
import com.android.settings.biometrics.BiometricEnrollIntroduction;

public final /* synthetic */ class BiometricEnrollIntroduction$$ExternalSyntheticLambda0 implements BiometricEnrollIntroduction.GenerateChallengeCallback {
    public final /* synthetic */ BiometricEnrollIntroduction f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ BiometricEnrollIntroduction$$ExternalSyntheticLambda0(BiometricEnrollIntroduction biometricEnrollIntroduction, Intent intent) {
        this.f$0 = biometricEnrollIntroduction;
        this.f$1 = intent;
    }

    public final void onChallengeGenerated(int i, int i2, long j) {
        this.f$0.lambda$onActivityResult$1(this.f$1, i, i2, j);
    }
}
