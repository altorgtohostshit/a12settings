package com.android.settings.wifi.slice;

import android.content.Context;
import android.net.Uri;

public class ContextualWifiScanWorker extends WifiScanWorker {
    public ContextualWifiScanWorker(Context context, Uri uri) {
        super(context, uri);
    }

    /* access modifiers changed from: protected */
    public int getApRowCount() {
        return ContextualWifiSlice.getApRowCount();
    }
}
