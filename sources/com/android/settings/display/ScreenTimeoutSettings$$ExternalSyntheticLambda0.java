package com.android.settings.display;

import android.hardware.SensorPrivacyManager;

public final /* synthetic */ class ScreenTimeoutSettings$$ExternalSyntheticLambda0 implements SensorPrivacyManager.OnSensorPrivacyChangedListener {
    public final /* synthetic */ ScreenTimeoutSettings f$0;

    public /* synthetic */ ScreenTimeoutSettings$$ExternalSyntheticLambda0(ScreenTimeoutSettings screenTimeoutSettings) {
        this.f$0 = screenTimeoutSettings;
    }

    public final void onSensorPrivacyChanged(int i, boolean z) {
        this.f$0.lambda$onAttach$0(i, z);
    }
}
