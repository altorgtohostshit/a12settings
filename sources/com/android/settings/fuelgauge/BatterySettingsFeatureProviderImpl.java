package com.android.settings.fuelgauge;

import android.content.Context;

public class BatterySettingsFeatureProviderImpl implements BatterySettingsFeatureProvider {
    protected Context mContext;

    public BatterySettingsFeatureProviderImpl(Context context) {
        this.mContext = context.getApplicationContext();
    }
}
