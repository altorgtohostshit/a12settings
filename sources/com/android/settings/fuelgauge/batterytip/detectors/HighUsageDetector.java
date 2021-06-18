package com.android.settings.fuelgauge.batterytip.detectors;

import android.content.Context;
import android.os.BatteryUsageStats;
import android.os.UidBatteryConsumer;
import com.android.settings.fuelgauge.BatteryInfo;
import com.android.settings.fuelgauge.BatteryUtils;
import com.android.settings.fuelgauge.batterytip.AppInfo;
import com.android.settings.fuelgauge.batterytip.BatteryTipPolicy;
import com.android.settings.fuelgauge.batterytip.HighUsageDataParser;
import com.android.settings.fuelgauge.batterytip.tips.BatteryTip;
import com.android.settings.fuelgauge.batterytip.tips.HighUsageTip;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HighUsageDetector {
    private final BatteryInfo mBatteryInfo;
    private BatteryUsageStats mBatteryUsageStats;
    BatteryUtils mBatteryUtils;
    HighUsageDataParser mDataParser;
    boolean mDischarging;
    private List<AppInfo> mHighUsageAppList = new ArrayList();
    private BatteryTipPolicy mPolicy;

    public HighUsageDetector(Context context, BatteryTipPolicy batteryTipPolicy, BatteryUsageStats batteryUsageStats, BatteryInfo batteryInfo) {
        this.mPolicy = batteryTipPolicy;
        this.mBatteryUsageStats = batteryUsageStats;
        this.mBatteryInfo = batteryInfo;
        this.mBatteryUtils = BatteryUtils.getInstance(context);
        BatteryTipPolicy batteryTipPolicy2 = this.mPolicy;
        this.mDataParser = new HighUsageDataParser(batteryTipPolicy2.highUsagePeriodMs, batteryTipPolicy2.highUsageBatteryDraining);
        this.mDischarging = batteryInfo.discharging;
    }

    public BatteryTip detect() {
        long calculateLastFullChargeTime = this.mBatteryUtils.calculateLastFullChargeTime(this.mBatteryUsageStats, System.currentTimeMillis());
        if (this.mPolicy.highUsageEnabled && this.mDischarging) {
            parseBatteryData();
            if (this.mDataParser.isDeviceHeavilyUsed() || this.mPolicy.testHighUsageTip) {
                double consumedPower = this.mBatteryUsageStats.getConsumedPower();
                int dischargePercentage = this.mBatteryUsageStats.getDischargePercentage();
                List<UidBatteryConsumer> uidBatteryConsumers = this.mBatteryUsageStats.getUidBatteryConsumers();
                uidBatteryConsumers.sort(HighUsageDetector$$ExternalSyntheticLambda0.INSTANCE);
                for (UidBatteryConsumer uidBatteryConsumer : uidBatteryConsumers) {
                    if (this.mBatteryUtils.calculateBatteryPercent(uidBatteryConsumer.getConsumedPower(), consumedPower, dischargePercentage) + 0.5d >= 1.0d && !this.mBatteryUtils.shouldHideUidBatteryConsumer(uidBatteryConsumer)) {
                        this.mHighUsageAppList.add(new AppInfo.Builder().setUid(uidBatteryConsumer.getUid()).setPackageName(this.mBatteryUtils.getPackageName(uidBatteryConsumer.getUid())).build());
                        if (this.mHighUsageAppList.size() >= this.mPolicy.highUsageAppCount) {
                            break;
                        }
                    }
                }
                if (this.mPolicy.testHighUsageTip && this.mHighUsageAppList.isEmpty()) {
                    this.mHighUsageAppList.add(new AppInfo.Builder().setPackageName("com.android.settings").setScreenOnTimeMs(TimeUnit.HOURS.toMillis(3)).build());
                }
            }
        }
        return new HighUsageTip(calculateLastFullChargeTime, this.mHighUsageAppList);
    }

    /* access modifiers changed from: package-private */
    public void parseBatteryData() {
        this.mBatteryInfo.parseBatteryHistory(this.mDataParser);
    }
}
