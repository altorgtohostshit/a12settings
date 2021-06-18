package com.android.settings.enterprise;

import android.content.Context;
import java.util.Date;

public class NetworkLogsPreferenceController extends AdminActionPreferenceControllerBase {
    public String getPreferenceKey() {
        return "network_logs";
    }

    public NetworkLogsPreferenceController(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public Date getAdminActionTimestamp() {
        return this.mFeatureProvider.getLastNetworkLogRetrievalTime();
    }

    public boolean isAvailable() {
        return this.mFeatureProvider.isNetworkLoggingEnabled() || this.mFeatureProvider.getLastNetworkLogRetrievalTime() != null;
    }
}
