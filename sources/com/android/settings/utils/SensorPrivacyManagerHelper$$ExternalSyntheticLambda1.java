package com.android.settings.utils;

import com.android.settings.utils.SensorPrivacyManagerHelper;

public final /* synthetic */ class SensorPrivacyManagerHelper$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ SensorPrivacyManagerHelper.Callback f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ SensorPrivacyManagerHelper$$ExternalSyntheticLambda1(SensorPrivacyManagerHelper.Callback callback, int i, boolean z) {
        this.f$0 = callback;
        this.f$1 = i;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.onSensorPrivacyChanged(this.f$1, this.f$2);
    }
}
