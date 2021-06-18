package com.android.settings.datausage;

import com.android.settings.datausage.ChartDataUsagePreference;
import java.util.function.ToLongFunction;

public final /* synthetic */ class ChartDataUsagePreference$$ExternalSyntheticLambda3 implements ToLongFunction {
    public static final /* synthetic */ ChartDataUsagePreference$$ExternalSyntheticLambda3 INSTANCE = new ChartDataUsagePreference$$ExternalSyntheticLambda3();

    private /* synthetic */ ChartDataUsagePreference$$ExternalSyntheticLambda3() {
    }

    public final long applyAsLong(Object obj) {
        return ((ChartDataUsagePreference.DataUsageSummaryNode) obj).getEndTime();
    }
}
