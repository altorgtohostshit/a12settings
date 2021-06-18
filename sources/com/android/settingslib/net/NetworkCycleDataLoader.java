package com.android.settingslib.net;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.INetworkStatsService;
import android.net.INetworkStatsSession;
import android.net.NetworkPolicy;
import android.net.NetworkPolicyManager;
import android.net.NetworkStatsHistory;
import android.net.NetworkTemplate;
import android.net.TrafficStats;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Pair;
import androidx.loader.content.AsyncTaskLoader;
import com.android.settingslib.NetworkPolicyEditor;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class NetworkCycleDataLoader<D> extends AsyncTaskLoader<D> {
    private final ArrayList<Long> mCycles;
    protected final NetworkStatsManager mNetworkStatsManager;
    final INetworkStatsService mNetworkStatsService = INetworkStatsService.Stub.asInterface(ServiceManager.getService("netstats"));
    protected final NetworkTemplate mNetworkTemplate;
    private final NetworkPolicy mPolicy;

    /* access modifiers changed from: package-private */
    public abstract D getCycleUsage();

    /* access modifiers changed from: package-private */
    public abstract void recordUsage(long j, long j2);

    protected NetworkCycleDataLoader(Builder<?> builder) {
        super(builder.mContext);
        NetworkTemplate access$100 = builder.mNetworkTemplate;
        this.mNetworkTemplate = access$100;
        this.mCycles = builder.mCycles;
        this.mNetworkStatsManager = (NetworkStatsManager) builder.mContext.getSystemService("netstats");
        NetworkPolicyEditor networkPolicyEditor = new NetworkPolicyEditor(NetworkPolicyManager.from(builder.mContext));
        networkPolicyEditor.read();
        this.mPolicy = networkPolicyEditor.getPolicy(access$100);
    }

    /* access modifiers changed from: protected */
    public void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public D loadInBackground() {
        ArrayList<Long> arrayList = this.mCycles;
        if (arrayList != null && arrayList.size() > 1) {
            loadDataForSpecificCycles();
        } else if (this.mPolicy == null) {
            loadFourWeeksData();
        } else {
            loadPolicyData();
        }
        return getCycleUsage();
    }

    /* access modifiers changed from: package-private */
    public void loadPolicyData() {
        Iterator cycleIterator = NetworkPolicyManager.cycleIterator(this.mPolicy);
        while (cycleIterator.hasNext()) {
            Pair pair = (Pair) cycleIterator.next();
            recordUsage(((ZonedDateTime) pair.first).toInstant().toEpochMilli(), ((ZonedDateTime) pair.second).toInstant().toEpochMilli());
        }
    }

    /* access modifiers changed from: protected */
    public void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    /* access modifiers changed from: protected */
    public void onReset() {
        super.onReset();
        cancelLoad();
    }

    /* access modifiers changed from: package-private */
    public void loadFourWeeksData() {
        try {
            INetworkStatsSession openSession = this.mNetworkStatsService.openSession();
            NetworkStatsHistory historyForNetwork = openSession.getHistoryForNetwork(this.mNetworkTemplate, 10);
            long start = historyForNetwork.getStart();
            long end = historyForNetwork.getEnd();
            while (end > start) {
                long j = end - 2419200000L;
                recordUsage(j, end);
                end = j;
            }
            TrafficStats.closeQuietly(openSession);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: package-private */
    public void loadDataForSpecificCycles() {
        long longValue = this.mCycles.get(0).longValue();
        int i = 1;
        int size = this.mCycles.size() - 1;
        while (i <= size) {
            long longValue2 = this.mCycles.get(i).longValue();
            recordUsage(longValue2, longValue);
            i++;
            longValue = longValue2;
        }
    }

    /* access modifiers changed from: protected */
    public long getTotalUsage(NetworkStats networkStats) {
        long j = 0;
        if (networkStats != null) {
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            while (networkStats.hasNextBucket() && networkStats.getNextBucket(bucket)) {
                j += bucket.getRxBytes() + bucket.getTxBytes();
            }
            networkStats.close();
        }
        return j;
    }

    public ArrayList<Long> getCycles() {
        return this.mCycles;
    }

    public static abstract class Builder<T extends NetworkCycleDataLoader> {
        /* access modifiers changed from: private */
        public final Context mContext;
        /* access modifiers changed from: private */
        public ArrayList<Long> mCycles;
        /* access modifiers changed from: private */
        public NetworkTemplate mNetworkTemplate;

        public abstract T build();

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder<T> setNetworkTemplate(NetworkTemplate networkTemplate) {
            this.mNetworkTemplate = networkTemplate;
            return this;
        }

        public Builder<T> setCycles(ArrayList<Long> arrayList) {
            this.mCycles = arrayList;
            return this;
        }
    }
}
