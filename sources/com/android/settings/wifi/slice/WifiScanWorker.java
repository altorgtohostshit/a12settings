package com.android.settings.wifi.slice;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.wifi.WifiPickerTrackerHelper;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.util.ArrayList;
import java.util.List;

public class WifiScanWorker extends SliceBackgroundWorker<WifiSliceItem> implements WifiPickerTracker.WifiPickerTrackerCallback, LifecycleOwner, WifiEntry.WifiEntryCallback {
    final LifecycleRegistry mLifecycleRegistry;
    protected WifiPickerTracker mWifiPickerTracker;
    protected WifiPickerTrackerHelper mWifiPickerTrackerHelper;

    /* access modifiers changed from: protected */
    public int getApRowCount() {
        return 3;
    }

    public void onNumSavedNetworksChanged() {
    }

    public void onNumSavedSubscriptionsChanged() {
    }

    public WifiScanWorker(Context context, Uri uri) {
        super(context, uri);
        LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
        this.mLifecycleRegistry = lifecycleRegistry;
        WifiPickerTrackerHelper wifiPickerTrackerHelper = new WifiPickerTrackerHelper(lifecycleRegistry, context, this);
        this.mWifiPickerTrackerHelper = wifiPickerTrackerHelper;
        this.mWifiPickerTracker = wifiPickerTrackerHelper.getWifiPickerTracker();
        lifecycleRegistry.markState(Lifecycle.State.INITIALIZED);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

    /* access modifiers changed from: protected */
    public void onSlicePinned() {
        this.mLifecycleRegistry.markState(Lifecycle.State.STARTED);
        this.mLifecycleRegistry.markState(Lifecycle.State.RESUMED);
        updateResults();
    }

    /* access modifiers changed from: protected */
    public void onSliceUnpinned() {
        this.mLifecycleRegistry.markState(Lifecycle.State.STARTED);
        this.mLifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

    public void close() {
        this.mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
    }

    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    public void onWifiStateChanged() {
        notifySliceChange();
    }

    public void onWifiEntriesChanged() {
        updateResults();
    }

    public void onUpdated() {
        updateResults();
    }

    public WifiEntry getWifiEntry(String str) {
        WifiEntry connectedWifiEntry = this.mWifiPickerTracker.getConnectedWifiEntry();
        if (connectedWifiEntry != null && TextUtils.equals(str, connectedWifiEntry.getKey())) {
            return connectedWifiEntry;
        }
        for (WifiEntry next : this.mWifiPickerTracker.getWifiEntries()) {
            if (TextUtils.equals(str, next.getKey())) {
                return next;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void updateResults() {
        if (this.mWifiPickerTracker.getWifiState() == 3 && this.mLifecycleRegistry.getCurrentState() == Lifecycle.State.RESUMED) {
            ArrayList arrayList = new ArrayList();
            WifiEntry connectedWifiEntry = this.mWifiPickerTracker.getConnectedWifiEntry();
            if (connectedWifiEntry != null) {
                connectedWifiEntry.setListener(this);
                arrayList.add(new WifiSliceItem(getContext(), connectedWifiEntry));
            }
            for (WifiEntry next : this.mWifiPickerTracker.getWifiEntries()) {
                if (arrayList.size() >= getApRowCount()) {
                    break;
                } else if (next.getLevel() != -1) {
                    next.setListener(this);
                    arrayList.add(new WifiSliceItem(getContext(), next));
                }
            }
            super.updateResults(arrayList);
            return;
        }
        super.updateResults((List) null);
    }

    public void setCarrierNetworkEnabledIfNeeded(boolean z, int i) {
        if (!this.mWifiPickerTrackerHelper.isCarrierNetworkProvisionEnabled(i)) {
            this.mWifiPickerTrackerHelper.setCarrierNetworkEnabled(z);
        }
    }

    public void connectCarrierNetwork() {
        this.mWifiPickerTrackerHelper.connectCarrierNetwork((WifiEntry.ConnectCallback) null);
    }
}
