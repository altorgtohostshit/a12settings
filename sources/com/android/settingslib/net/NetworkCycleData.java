package com.android.settingslib.net;

public class NetworkCycleData {
    /* access modifiers changed from: private */
    public long mEndTime;
    /* access modifiers changed from: private */
    public long mStartTime;
    /* access modifiers changed from: private */
    public long mTotalUsage;

    protected NetworkCycleData() {
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public long getEndTime() {
        return this.mEndTime;
    }

    public long getTotalUsage() {
        return this.mTotalUsage;
    }

    public static class Builder {
        private NetworkCycleData mObject = new NetworkCycleData();

        public Builder setStartTime(long j) {
            long unused = getObject().mStartTime = j;
            return this;
        }

        public Builder setEndTime(long j) {
            long unused = getObject().mEndTime = j;
            return this;
        }

        public Builder setTotalUsage(long j) {
            long unused = getObject().mTotalUsage = j;
            return this;
        }

        /* access modifiers changed from: protected */
        public NetworkCycleData getObject() {
            return this.mObject;
        }

        public NetworkCycleData build() {
            return getObject();
        }
    }
}
