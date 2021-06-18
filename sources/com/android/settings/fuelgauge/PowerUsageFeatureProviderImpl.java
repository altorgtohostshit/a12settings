package com.android.settings.fuelgauge;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.SparseIntArray;
import com.android.internal.util.ArrayUtils;
import com.android.settingslib.fuelgauge.Estimate;
import java.util.Map;

public class PowerUsageFeatureProviderImpl implements PowerUsageFeatureProvider {
    private static final String[] PACKAGES_SYSTEM = {"com.android.providers.media", "com.android.providers.calendar", "com.android.systemui"};
    protected Context mContext;
    protected PackageManager mPackageManager;

    public String getAdvancedUsageScreenInfoString() {
        return null;
    }

    public Map<Long, Map<String, BatteryHistEntry>> getBatteryHistory(Context context) {
        return null;
    }

    public boolean getEarlyWarningSignal(Context context, String str) {
        return false;
    }

    public Estimate getEnhancedBatteryPrediction(Context context) {
        return null;
    }

    public SparseIntArray getEnhancedBatteryPredictionCurve(Context context, long j) {
        return null;
    }

    public boolean isChartGraphEnabled(Context context) {
        return false;
    }

    public boolean isChartGraphSlotsEnabled(Context context) {
        return false;
    }

    public boolean isEnhancedBatteryPredictionEnabled(Context context) {
        return false;
    }

    public PowerUsageFeatureProviderImpl(Context context) {
        this.mPackageManager = context.getPackageManager();
        this.mContext = context.getApplicationContext();
    }

    public boolean isTypeSystem(int i, String[] strArr) {
        if (i >= 0 && i < 10000) {
            return true;
        }
        if (strArr != null) {
            for (String contains : strArr) {
                if (ArrayUtils.contains(PACKAGES_SYSTEM, contains)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSmartBatterySupported() {
        return this.mContext.getResources().getBoolean(17891632);
    }
}
