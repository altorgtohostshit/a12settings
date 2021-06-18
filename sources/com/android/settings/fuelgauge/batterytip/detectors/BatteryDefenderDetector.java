package com.android.settings.fuelgauge.batterytip.detectors;

import com.android.settings.fuelgauge.BatteryInfo;
import com.android.settings.fuelgauge.BatteryUtils;
import com.android.settings.fuelgauge.batterytip.tips.BatteryDefenderTip;
import com.android.settings.fuelgauge.batterytip.tips.BatteryTip;

public class BatteryDefenderDetector {
    private BatteryInfo mBatteryInfo;

    public BatteryDefenderDetector(BatteryInfo batteryInfo) {
        this.mBatteryInfo = batteryInfo;
    }

    public BatteryTip detect() {
        return new BatteryDefenderTip(BatteryUtils.isBatteryDefenderOn(this.mBatteryInfo) ? 0 : 2);
    }
}
