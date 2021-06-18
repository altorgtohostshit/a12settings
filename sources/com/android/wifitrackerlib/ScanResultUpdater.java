package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import android.util.Pair;
import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanResultUpdater {
    private final Clock mClock;
    private final Object mLock = new Object();
    private final long mMaxScanAgeMillis;
    private Map<Pair<String, String>, ScanResult> mScanResultsBySsidAndBssid = new HashMap();

    public ScanResultUpdater(Clock clock, long j) {
        this.mMaxScanAgeMillis = j;
        this.mClock = clock;
    }

    public void update(List<ScanResult> list) {
        synchronized (this.mLock) {
            evictOldScans();
            for (ScanResult next : list) {
                Pair pair = new Pair(next.SSID, next.BSSID);
                ScanResult scanResult = this.mScanResultsBySsidAndBssid.get(pair);
                if (scanResult == null || scanResult.timestamp < next.timestamp) {
                    this.mScanResultsBySsidAndBssid.put(pair, next);
                }
            }
        }
    }

    public List<ScanResult> getScanResults() {
        return getScanResults(this.mMaxScanAgeMillis);
    }

    public List<ScanResult> getScanResults(long j) throws IllegalArgumentException {
        ArrayList arrayList;
        if (j <= this.mMaxScanAgeMillis) {
            synchronized (this.mLock) {
                arrayList = new ArrayList();
                for (ScanResult next : this.mScanResultsBySsidAndBssid.values()) {
                    if (this.mClock.millis() - (next.timestamp / 1000) <= j) {
                        arrayList.add(next);
                    }
                }
            }
            return arrayList;
        }
        throw new IllegalArgumentException("maxScanAgeMillis argument cannot be greater than mMaxScanAgeMillis!");
    }

    private void evictOldScans() {
        synchronized (this.mLock) {
            this.mScanResultsBySsidAndBssid.entrySet().removeIf(new ScanResultUpdater$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$evictOldScans$0(Map.Entry entry) {
        return this.mClock.millis() - (((ScanResult) entry.getValue()).timestamp / 1000) > this.mMaxScanAgeMillis;
    }
}
