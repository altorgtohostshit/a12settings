package com.android.settings.fuelgauge;

import android.content.Context;

public class BatteryStatusFeatureProviderImpl implements BatteryStatusFeatureProvider {
    protected Context mContext;

    public boolean triggerBatteryStatusUpdate(BatteryPreferenceController batteryPreferenceController, BatteryInfo batteryInfo) {
        return false;
    }

    public BatteryStatusFeatureProviderImpl(Context context) {
        this.mContext = context.getApplicationContext();
    }
}
