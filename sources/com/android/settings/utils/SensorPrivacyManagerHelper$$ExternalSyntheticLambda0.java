package com.android.settings.utils;

import android.hardware.SensorPrivacyManager;

public final /* synthetic */ class SensorPrivacyManagerHelper$$ExternalSyntheticLambda0 implements SensorPrivacyManager.OnSensorPrivacyChangedListener {
    public final /* synthetic */ SensorPrivacyManagerHelper f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ SensorPrivacyManagerHelper$$ExternalSyntheticLambda0(SensorPrivacyManagerHelper sensorPrivacyManagerHelper, int i) {
        this.f$0 = sensorPrivacyManagerHelper;
        this.f$1 = i;
    }

    public final void onSensorPrivacyChanged(int i, boolean z) {
        this.f$0.lambda$registerCurrentUserListenerIfNeeded$1(this.f$1, i, z);
    }
}
