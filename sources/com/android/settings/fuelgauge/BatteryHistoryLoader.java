package com.android.settings.fuelgauge;

import android.content.Context;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.utils.AsyncLoaderCompat;
import java.util.Map;

public class BatteryHistoryLoader extends AsyncLoaderCompat<Map<Long, Map<String, BatteryHistEntry>>> {
    private final Context mContext;

    /* access modifiers changed from: protected */
    public void onDiscardResult(Map<Long, Map<String, BatteryHistEntry>> map) {
    }

    public BatteryHistoryLoader(Context context) {
        super(context);
        this.mContext = context;
    }

    public Map<Long, Map<String, BatteryHistEntry>> loadInBackground() {
        return FeatureFactory.getFactory(this.mContext).getPowerUsageFeatureProvider(this.mContext).getBatteryHistory(this.mContext);
    }
}
