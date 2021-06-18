package com.android.settings.fuelgauge;

import android.content.Context;
import android.os.BatteryStatsManager;
import android.os.BatteryUsageStats;
import android.os.BatteryUsageStatsQuery;
import com.android.settingslib.utils.AsyncLoaderCompat;

public class BatteryUsageStatsLoader extends AsyncLoaderCompat<BatteryUsageStats> {
    private final BatteryStatsManager mBatteryStatsManager;
    private final boolean mIncludeBatteryHistory;

    /* access modifiers changed from: protected */
    public void onDiscardResult(BatteryUsageStats batteryUsageStats) {
    }

    public BatteryUsageStatsLoader(Context context, boolean z) {
        super(context);
        this.mBatteryStatsManager = (BatteryStatsManager) context.getSystemService(BatteryStatsManager.class);
        this.mIncludeBatteryHistory = z;
    }

    public BatteryUsageStats loadInBackground() {
        BatteryUsageStatsQuery.Builder builder = new BatteryUsageStatsQuery.Builder();
        if (this.mIncludeBatteryHistory) {
            builder.includeBatteryHistory();
        }
        return this.mBatteryStatsManager.getBatteryUsageStats(builder.build());
    }
}
