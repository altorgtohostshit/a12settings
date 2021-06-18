package com.android.settingslib.net;

import com.android.settingslib.net.NetworkCycleData;

public class NetworkCycleDataForUid extends NetworkCycleData {
    /* access modifiers changed from: private */
    public long mBackgroudUsage;
    /* access modifiers changed from: private */
    public long mForegroudUsage;

    private NetworkCycleDataForUid() {
    }

    public long getBackgroudUsage() {
        return this.mBackgroudUsage;
    }

    public long getForegroudUsage() {
        return this.mForegroudUsage;
    }

    public static class Builder extends NetworkCycleData.Builder {
        private NetworkCycleDataForUid mObject = new NetworkCycleDataForUid();

        public Builder setBackgroundUsage(long j) {
            long unused = getObject().mBackgroudUsage = j;
            return this;
        }

        public Builder setForegroundUsage(long j) {
            long unused = getObject().mForegroudUsage = j;
            return this;
        }

        public NetworkCycleDataForUid getObject() {
            return this.mObject;
        }

        public NetworkCycleDataForUid build() {
            return getObject();
        }
    }
}
