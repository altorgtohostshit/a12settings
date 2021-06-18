package com.android.settings.enterprise;

import android.content.Context;
import java.util.Date;

public class SecurityLogsPreferenceController extends AdminActionPreferenceControllerBase {
    public String getPreferenceKey() {
        return "security_logs";
    }

    public SecurityLogsPreferenceController(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public Date getAdminActionTimestamp() {
        return this.mFeatureProvider.getLastSecurityLogRetrievalTime();
    }

    public boolean isAvailable() {
        return this.mFeatureProvider.isSecurityLoggingEnabled() || this.mFeatureProvider.getLastSecurityLogRetrievalTime() != null;
    }
}
