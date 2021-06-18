package com.android.settings.fuelgauge;

import android.content.Context;
import android.util.SparseIntArray;
import com.android.settingslib.fuelgauge.Estimate;
import java.util.Map;

public interface PowerUsageFeatureProvider {
    String getAdvancedUsageScreenInfoString();

    Map<Long, Map<String, BatteryHistEntry>> getBatteryHistory(Context context);

    boolean getEarlyWarningSignal(Context context, String str);

    Estimate getEnhancedBatteryPrediction(Context context);

    SparseIntArray getEnhancedBatteryPredictionCurve(Context context, long j);

    boolean isChartGraphEnabled(Context context);

    boolean isChartGraphSlotsEnabled(Context context);

    boolean isEnhancedBatteryPredictionEnabled(Context context);

    boolean isSmartBatterySupported();

    boolean isTypeSystem(int i, String[] strArr);
}
