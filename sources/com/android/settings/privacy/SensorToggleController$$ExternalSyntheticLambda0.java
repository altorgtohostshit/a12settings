package com.android.settings.privacy;

import androidx.preference.PreferenceScreen;
import com.android.settings.utils.SensorPrivacyManagerHelper;

public final /* synthetic */ class SensorToggleController$$ExternalSyntheticLambda0 implements SensorPrivacyManagerHelper.Callback {
    public final /* synthetic */ SensorToggleController f$0;
    public final /* synthetic */ PreferenceScreen f$1;

    public /* synthetic */ SensorToggleController$$ExternalSyntheticLambda0(SensorToggleController sensorToggleController, PreferenceScreen preferenceScreen) {
        this.f$0 = sensorToggleController;
        this.f$1 = preferenceScreen;
    }

    public final void onSensorPrivacyChanged(int i, boolean z) {
        this.f$0.lambda$displayPreference$0(this.f$1, i, z);
    }
}
