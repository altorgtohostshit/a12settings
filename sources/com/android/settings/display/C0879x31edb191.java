package com.android.settings.display;

import android.hardware.SensorPrivacyManager;

/* renamed from: com.android.settings.display.AdaptiveSleepCameraStatePreferenceController$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C0879x31edb191 implements SensorPrivacyManager.OnSensorPrivacyChangedListener {
    public final /* synthetic */ AdaptiveSleepCameraStatePreferenceController f$0;

    public /* synthetic */ C0879x31edb191(AdaptiveSleepCameraStatePreferenceController adaptiveSleepCameraStatePreferenceController) {
        this.f$0 = adaptiveSleepCameraStatePreferenceController;
    }

    public final void onSensorPrivacyChanged(int i, boolean z) {
        this.f$0.lambda$new$0(i, z);
    }
}
