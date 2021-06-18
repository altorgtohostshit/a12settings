package com.android.settings.fuelgauge.batterytip.detectors;

import android.content.Context;
import android.os.PowerManager;
import com.android.settings.fuelgauge.BatteryInfo;
import com.android.settings.fuelgauge.batterytip.BatteryTipPolicy;

public class LowBatteryDetector {
    private BatteryInfo mBatteryInfo;
    private BatteryTipPolicy mPolicy;
    private PowerManager mPowerManager;
    private int mWarningLevel;

    public LowBatteryDetector(Context context, BatteryTipPolicy batteryTipPolicy, BatteryInfo batteryInfo) {
        this.mPolicy = batteryTipPolicy;
        this.mBatteryInfo = batteryInfo;
        this.mPowerManager = (PowerManager) context.getSystemService("power");
        this.mWarningLevel = context.getResources().getInteger(17694838);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0029, code lost:
        if (r1 < java.util.concurrent.TimeUnit.HOURS.toMicros((long) r8.mPolicy.lowBatteryHour)) goto L_0x002e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.settings.fuelgauge.batterytip.tips.BatteryTip detect() {
        /*
            r8 = this;
            android.os.PowerManager r0 = r8.mPowerManager
            boolean r0 = r0.isPowerSaveMode()
            com.android.settings.fuelgauge.BatteryInfo r1 = r8.mBatteryInfo
            int r2 = r1.batteryLevel
            int r3 = r8.mWarningLevel
            r4 = 1
            r5 = 0
            if (r2 <= r3) goto L_0x002e
            boolean r2 = r1.discharging
            if (r2 == 0) goto L_0x002c
            long r1 = r1.remainingTimeUs
            r6 = 0
            int r3 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r3 == 0) goto L_0x002c
            java.util.concurrent.TimeUnit r3 = java.util.concurrent.TimeUnit.HOURS
            com.android.settings.fuelgauge.batterytip.BatteryTipPolicy r6 = r8.mPolicy
            int r6 = r6.lowBatteryHour
            long r6 = (long) r6
            long r6 = r3.toMicros(r6)
            int r1 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r1 >= 0) goto L_0x002c
            goto L_0x002e
        L_0x002c:
            r1 = r5
            goto L_0x002f
        L_0x002e:
            r1 = r4
        L_0x002f:
            com.android.settings.fuelgauge.batterytip.BatteryTipPolicy r2 = r8.mPolicy
            boolean r3 = r2.lowBatteryEnabled
            if (r3 == 0) goto L_0x0039
            if (r0 != 0) goto L_0x0039
            r3 = r4
            goto L_0x003a
        L_0x0039:
            r3 = r5
        L_0x003a:
            boolean r2 = r2.testLowBatteryTip
            if (r2 != 0) goto L_0x0048
            com.android.settings.fuelgauge.BatteryInfo r8 = r8.mBatteryInfo
            boolean r8 = r8.discharging
            if (r8 == 0) goto L_0x0047
            if (r1 == 0) goto L_0x0047
            goto L_0x0048
        L_0x0047:
            r4 = r5
        L_0x0048:
            r8 = 2
            if (r3 == 0) goto L_0x004e
            if (r4 == 0) goto L_0x004e
            goto L_0x004f
        L_0x004e:
            r5 = r8
        L_0x004f:
            com.android.settings.fuelgauge.batterytip.tips.LowBatteryTip r8 = new com.android.settings.fuelgauge.batterytip.tips.LowBatteryTip
            r8.<init>(r5, r0)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.fuelgauge.batterytip.detectors.LowBatteryDetector.detect():com.android.settings.fuelgauge.batterytip.tips.BatteryTip");
    }
}
