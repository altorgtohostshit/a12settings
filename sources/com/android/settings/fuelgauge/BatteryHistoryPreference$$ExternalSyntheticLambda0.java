package com.android.settings.fuelgauge;

import com.android.settings.fuelgauge.BatteryInfo;

public final /* synthetic */ class BatteryHistoryPreference$$ExternalSyntheticLambda0 implements BatteryInfo.Callback {
    public final /* synthetic */ BatteryHistoryPreference f$0;

    public /* synthetic */ BatteryHistoryPreference$$ExternalSyntheticLambda0(BatteryHistoryPreference batteryHistoryPreference) {
        this.f$0 = batteryHistoryPreference;
    }

    public final void onBatteryInfoLoaded(BatteryInfo batteryInfo) {
        this.f$0.lambda$setBatteryUsageStats$0(batteryInfo);
    }
}
