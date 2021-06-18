package com.android.settings.enterprise;

import android.content.Context;
import java.util.Date;

public class BugReportsPreferenceController extends AdminActionPreferenceControllerBase {
    public String getPreferenceKey() {
        return "bug_reports";
    }

    public BugReportsPreferenceController(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public Date getAdminActionTimestamp() {
        return this.mFeatureProvider.getLastBugReportRequestTime();
    }
}
