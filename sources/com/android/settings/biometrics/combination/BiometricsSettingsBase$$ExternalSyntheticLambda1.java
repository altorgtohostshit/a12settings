package com.android.settings.biometrics.combination;

import android.hardware.fingerprint.FingerprintManager;
import androidx.preference.Preference;

public final /* synthetic */ class BiometricsSettingsBase$$ExternalSyntheticLambda1 implements FingerprintManager.GenerateChallengeCallback {
    public final /* synthetic */ BiometricsSettingsBase f$0;
    public final /* synthetic */ Preference f$1;

    public /* synthetic */ BiometricsSettingsBase$$ExternalSyntheticLambda1(BiometricsSettingsBase biometricsSettingsBase, Preference preference) {
        this.f$0 = biometricsSettingsBase;
        this.f$1 = preference;
    }

    public final void onChallengeGenerated(int i, int i2, long j) {
        this.f$0.lambda$onPreferenceTreeClick$1(this.f$1, i, i2, j);
    }
}
