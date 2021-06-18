package com.android.settingslib.net;

import com.android.settingslib.net.NetworkCycleData;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NetworkCycleChartData extends NetworkCycleData {
    public static final long BUCKET_DURATION_MS = TimeUnit.DAYS.toMillis(1);
    /* access modifiers changed from: private */
    public List<NetworkCycleData> mUsageBuckets;

    private NetworkCycleChartData() {
    }

    public List<NetworkCycleData> getUsageBuckets() {
        return this.mUsageBuckets;
    }

    public static class Builder extends NetworkCycleData.Builder {
        private NetworkCycleChartData mObject = new NetworkCycleChartData();

        public Builder setUsageBuckets(List<NetworkCycleData> list) {
            List unused = getObject().mUsageBuckets = list;
            return this;
        }

        /* access modifiers changed from: protected */
        public NetworkCycleChartData getObject() {
            return this.mObject;
        }

        public NetworkCycleChartData build() {
            return getObject();
        }
    }
}
