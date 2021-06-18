package com.android.settings.deviceinfo;

import com.android.settingslib.DeviceInfoUtils;
import java.util.concurrent.Callable;

public final /* synthetic */ class HardwareInfoPreferenceController$$ExternalSyntheticLambda0 implements Callable {
    public static final /* synthetic */ HardwareInfoPreferenceController$$ExternalSyntheticLambda0 INSTANCE = new HardwareInfoPreferenceController$$ExternalSyntheticLambda0();

    private /* synthetic */ HardwareInfoPreferenceController$$ExternalSyntheticLambda0() {
    }

    public final Object call() {
        return DeviceInfoUtils.getMsvSuffix();
    }
}
