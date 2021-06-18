package com.android.settingslib.net;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.NetworkTemplate;
import android.os.RemoteException;
import android.util.Log;
import androidx.loader.content.AsyncTaskLoader;

public class NetworkStatsSummaryLoader extends AsyncTaskLoader<NetworkStats> {
    private final long mEnd;
    private final NetworkStatsManager mNetworkStatsManager;
    private final NetworkTemplate mNetworkTemplate;
    private final long mStart;

    private NetworkStatsSummaryLoader(Builder builder) {
        super(builder.mContext);
        this.mStart = builder.mStart;
        this.mEnd = builder.mEnd;
        this.mNetworkTemplate = builder.mNetworkTemplate;
        this.mNetworkStatsManager = (NetworkStatsManager) builder.mContext.getSystemService("netstats");
    }

    /* access modifiers changed from: protected */
    public void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public NetworkStats loadInBackground() {
        try {
            return this.mNetworkStatsManager.querySummary(this.mNetworkTemplate, this.mStart, this.mEnd);
        } catch (RemoteException e) {
            Log.e("NetworkDetailLoader", "Exception querying network detail.", e);
            return null;
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

    public static class Builder {
        /* access modifiers changed from: private */
        public final Context mContext;
        /* access modifiers changed from: private */
        public long mEnd;
        /* access modifiers changed from: private */
        public NetworkTemplate mNetworkTemplate;
        /* access modifiers changed from: private */
        public long mStart;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setStartTime(long j) {
            this.mStart = j;
            return this;
        }

        public Builder setEndTime(long j) {
            this.mEnd = j;
            return this;
        }

        public Builder setNetworkTemplate(NetworkTemplate networkTemplate) {
            this.mNetworkTemplate = networkTemplate;
            return this;
        }

        public NetworkStatsSummaryLoader build() {
            return new NetworkStatsSummaryLoader(this);
        }
    }
}
