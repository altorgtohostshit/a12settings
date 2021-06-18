package com.android.settings.applications.appinfo;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.applications.ApplicationFeatureProvider;
import com.android.settings.core.LiveDataController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.List;

public class TimeSpentInAppPreferenceController extends LiveDataController {
    static final Intent SEE_TIME_IN_APP_TEMPLATE = new Intent("android.settings.action.APP_USAGE_SETTINGS");
    private final ApplicationFeatureProvider mAppFeatureProvider;
    private Intent mIntent;
    private final PackageManager mPackageManager;
    private String mPackageName;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public TimeSpentInAppPreferenceController(Context context, String str) {
        super(context, str);
        this.mPackageManager = context.getPackageManager();
        this.mAppFeatureProvider = FeatureFactory.getFactory(context).getApplicationFeatureProvider(context);
    }

    public void setPackageName(String str) {
        this.mPackageName = str;
        this.mIntent = new Intent(SEE_TIME_IN_APP_TEMPLATE).putExtra("android.intent.extra.PACKAGE_NAME", this.mPackageName);
    }

    public int getAvailabilityStatus() {
        List<ResolveInfo> queryIntentActivities;
        if (!TextUtils.isEmpty(this.mPackageName) && (queryIntentActivities = this.mPackageManager.queryIntentActivities(this.mIntent, 0)) != null && !queryIntentActivities.isEmpty()) {
            for (ResolveInfo isSystemApp : queryIntentActivities) {
                if (isSystemApp(isSystemApp)) {
                    return 0;
                }
            }
        }
        return 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        if (findPreference != null) {
            findPreference.setIntent(this.mIntent);
        }
    }

    /* access modifiers changed from: protected */
    public CharSequence getSummaryTextInBackground() {
        return this.mAppFeatureProvider.getTimeSpentInApp(this.mPackageName);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0007, code lost:
        r1 = (r1 = r1.activityInfo).applicationInfo;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isSystemApp(android.content.pm.ResolveInfo r1) {
        /*
            r0 = this;
            r0 = 1
            if (r1 == 0) goto L_0x0011
            android.content.pm.ActivityInfo r1 = r1.activityInfo
            if (r1 == 0) goto L_0x0011
            android.content.pm.ApplicationInfo r1 = r1.applicationInfo
            if (r1 == 0) goto L_0x0011
            int r1 = r1.flags
            r1 = r1 & r0
            if (r1 == 0) goto L_0x0011
            goto L_0x0012
        L_0x0011:
            r0 = 0
        L_0x0012:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.appinfo.TimeSpentInAppPreferenceController.isSystemApp(android.content.pm.ResolveInfo):boolean");
    }
}
