package com.android.settings.fuelgauge;

import android.content.Context;
import com.android.settingslib.utils.AsyncLoaderCompat;

public class BatteryInfoLoader extends AsyncLoaderCompat<BatteryInfo> {
    BatteryUtils mBatteryUtils;

    /* access modifiers changed from: protected */
    public void onDiscardResult(BatteryInfo batteryInfo) {
    }

    public BatteryInfoLoader(Context context) {
        super(context);
        this.mBatteryUtils = BatteryUtils.getInstance(context);
    }

    public BatteryInfo loadInBackground() {
        return this.mBatteryUtils.getBatteryInfo("BatteryInfoLoader");
    }
}
