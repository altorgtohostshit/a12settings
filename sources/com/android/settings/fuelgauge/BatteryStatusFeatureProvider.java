package com.android.settings.fuelgauge;

public interface BatteryStatusFeatureProvider {
    boolean triggerBatteryStatusUpdate(BatteryPreferenceController batteryPreferenceController, BatteryInfo batteryInfo);
}
