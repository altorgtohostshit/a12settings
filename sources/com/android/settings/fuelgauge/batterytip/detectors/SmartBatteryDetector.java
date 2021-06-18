package com.android.settings.fuelgauge.batterytip.detectors;

import android.content.ContentResolver;
import android.content.Context;
import android.os.PowerManager;
import android.provider.Settings;
import com.android.settings.fuelgauge.BatteryInfo;
import com.android.settings.fuelgauge.batterytip.BatteryTipPolicy;
import com.android.settings.fuelgauge.batterytip.tips.BatteryTip;
import com.android.settings.fuelgauge.batterytip.tips.SmartBatteryTip;

public class SmartBatteryDetector {
    private BatteryInfo mBatteryInfo;
    private ContentResolver mContentResolver;
    private BatteryTipPolicy mPolicy;
    private PowerManager mPowerManager;

    public SmartBatteryDetector(Context context, BatteryTipPolicy batteryTipPolicy, BatteryInfo batteryInfo, ContentResolver contentResolver) {
        this.mPolicy = batteryTipPolicy;
        this.mBatteryInfo = batteryInfo;
        this.mContentResolver = contentResolver;
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
    }

    public BatteryTip detect() {
        boolean z = true;
        int i = 0;
        boolean z2 = Settings.Global.getInt(this.mContentResolver, "adaptive_battery_management_enabled", 1) == 0;
        boolean z3 = this.mBatteryInfo.batteryLevel <= 30;
        if ((!z2 || this.mPowerManager.isPowerSaveMode() || !z3) && !this.mPolicy.testSmartBatteryTip) {
            z = false;
        }
        if (!z) {
            i = 2;
        }
        return new SmartBatteryTip(i);
    }
}
