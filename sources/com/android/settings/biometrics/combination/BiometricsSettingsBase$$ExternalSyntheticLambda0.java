package com.android.settings.biometrics.combination;

import android.hardware.face.FaceManager;
import androidx.preference.Preference;

public final /* synthetic */ class BiometricsSettingsBase$$ExternalSyntheticLambda0 implements FaceManager.GenerateChallengeCallback {
    public final /* synthetic */ BiometricsSettingsBase f$0;
    public final /* synthetic */ Preference f$1;

    public /* synthetic */ BiometricsSettingsBase$$ExternalSyntheticLambda0(BiometricsSettingsBase biometricsSettingsBase, Preference preference) {
        this.f$0 = biometricsSettingsBase;
        this.f$1 = preference;
    }

    public final void onGenerateChallengeResult(int i, int i2, long j) {
        this.f$0.lambda$onPreferenceTreeClick$0(this.f$1, i, i2, j);
    }
}
