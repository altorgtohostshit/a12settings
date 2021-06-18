package com.android.settingslib.net;

import android.content.Context;
import android.util.Log;
import com.android.settingslib.net.NetworkCycleDataForUid;
import com.android.settingslib.net.NetworkCycleDataLoader;
import java.util.ArrayList;
import java.util.List;

public class NetworkCycleDataForUidLoader extends NetworkCycleDataLoader<List<NetworkCycleDataForUid>> {
    private final List<NetworkCycleDataForUid> mData;
    private final boolean mRetrieveDetail;
    private final List<Integer> mUids;

    private NetworkCycleDataForUidLoader(Builder builder) {
        super(builder);
        this.mUids = builder.mUids;
        this.mRetrieveDetail = builder.mRetrieveDetail;
        this.mData = new ArrayList();
    }

    /* access modifiers changed from: package-private */
    public void recordUsage(long j, long j2) {
        try {
            long j3 = 0;
            long j4 = 0;
            for (Integer intValue : this.mUids) {
                int intValue2 = intValue.intValue();
                long totalUsage = getTotalUsage(this.mNetworkStatsManager.queryDetailsForUid(this.mNetworkTemplate, j, j2, intValue2));
                if (totalUsage > 0) {
                    long j5 = j3 + totalUsage;
                    if (this.mRetrieveDetail) {
                        j4 += getForegroundUsage(j, j2, intValue2);
                    }
                    j3 = j5;
                }
            }
            if (j3 > 0) {
                NetworkCycleDataForUid.Builder builder = new NetworkCycleDataForUid.Builder();
                builder.setStartTime(j).setEndTime(j2).setTotalUsage(j3);
                if (this.mRetrieveDetail) {
                    builder.setBackgroundUsage(j3 - j4).setForegroundUsage(j4);
                }
                this.mData.add(builder.build());
            }
        } catch (Exception e) {
            Log.e("NetworkDataForUidLoader", "Exception querying network detail.", e);
        }
    }

    /* access modifiers changed from: package-private */
    public List<NetworkCycleDataForUid> getCycleUsage() {
        return this.mData;
    }

    public static Builder<?> builder(Context context) {
        return new Builder<NetworkCycleDataForUidLoader>(context) {
            public NetworkCycleDataForUidLoader build() {
                return new NetworkCycleDataForUidLoader(this);
            }
        };
    }

    public List<Integer> getUids() {
        return this.mUids;
    }

    private long getForegroundUsage(long j, long j2, int i) {
        return getTotalUsage(this.mNetworkStatsManager.queryDetailsForUidTagState(this.mNetworkTemplate, j, j2, i, 0, 2));
    }

    public static abstract class Builder<T extends NetworkCycleDataForUidLoader> extends NetworkCycleDataLoader.Builder<T> {
        /* access modifiers changed from: private */
        public boolean mRetrieveDetail = true;
        /* access modifiers changed from: private */
        public final List<Integer> mUids = new ArrayList();

        public Builder(Context context) {
            super(context);
        }

        public Builder<T> addUid(int i) {
            this.mUids.add(Integer.valueOf(i));
            return this;
        }

        public Builder<T> setRetrieveDetail(boolean z) {
            this.mRetrieveDetail = z;
            return this;
        }
    }
}
