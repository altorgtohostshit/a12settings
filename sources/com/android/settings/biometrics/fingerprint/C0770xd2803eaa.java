package com.android.settings.biometrics.fingerprint;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import com.android.settings.biometrics.fingerprint.FingerprintSettings;

/* renamed from: com.android.settings.biometrics.fingerprint.FingerprintSettings$FingerprintSettingsFragment$$ExternalSyntheticLambda1 */
public final /* synthetic */ class C0770xd2803eaa implements FingerprintManager.GenerateChallengeCallback {
    public final /* synthetic */ FingerprintSettings.FingerprintSettingsFragment f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ C0770xd2803eaa(FingerprintSettings.FingerprintSettingsFragment fingerprintSettingsFragment, Intent intent) {
        this.f$0 = fingerprintSettingsFragment;
        this.f$1 = intent;
    }

    public final void onChallengeGenerated(int i, int i2, long j) {
        this.f$0.lambda$onActivityResult$2(this.f$1, i, i2, j);
    }
}
