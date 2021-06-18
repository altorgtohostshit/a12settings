package com.android.settings.applications;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.apphibernation.AppHibernationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.DeviceConfig;
import android.util.ArrayMap;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class HibernatedAppsPreferenceController extends BasePreferenceController {
    private static final long DEFAULT_UNUSED_THRESHOLD_MS = TimeUnit.DAYS.toMillis(90);
    private static final String PROPERTY_HIBERNATION_UNUSED_THRESHOLD_MILLIS = "auto_revoke_unused_threshold_millis2";
    private static final String TAG = "HibernatedAppsPrefController";

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

    public HibernatedAppsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return (!isHibernationEnabled() || getNumHibernated() <= 0) ? 2 : 0;
    }

    public CharSequence getSummary() {
        int numHibernated = getNumHibernated();
        return this.mContext.getResources().getQuantityString(R.plurals.unused_apps_summary, numHibernated, new Object[]{Integer.valueOf(numHibernated)});
    }

    private int getNumHibernated() {
        String[] strArr;
        PackageManager packageManager = this.mContext.getPackageManager();
        List hibernatingPackagesForUser = ((AppHibernationManager) this.mContext.getSystemService(AppHibernationManager.class)).getHibernatingPackagesForUser();
        int size = hibernatingPackagesForUser.size();
        long currentTimeMillis = System.currentTimeMillis();
        long j = DeviceConfig.getLong("permissions", PROPERTY_HIBERNATION_UNUSED_THRESHOLD_MILLIS, DEFAULT_UNUSED_THRESHOLD_MS);
        List<UsageStats> queryUsageStats = ((UsageStatsManager) this.mContext.getSystemService(UsageStatsManager.class)).queryUsageStats(2, currentTimeMillis - j, currentTimeMillis);
        ArrayMap arrayMap = new ArrayMap();
        for (UsageStats next : queryUsageStats) {
            arrayMap.put(next.mPackageName, next);
        }
        for (PackageInfo next2 : packageManager.getInstalledPackages(4608)) {
            String str = next2.packageName;
            UsageStats usageStats = (UsageStats) arrayMap.get(str);
            int i = 0;
            boolean z = usageStats != null && (currentTimeMillis - usageStats.getLastTimeAnyComponentUsed() < j || currentTimeMillis - usageStats.getLastTimeVisible() < j);
            if (!hibernatingPackagesForUser.contains(str) && (strArr = next2.requestedPermissions) != null && !z) {
                int length = strArr.length;
                while (true) {
                    if (i >= length) {
                        break;
                    } else if ((packageManager.getPermissionFlags(strArr[i], str, this.mContext.getUser()) & 131072) != 0) {
                        size++;
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }
        return size;
    }

    private static boolean isHibernationEnabled() {
        return DeviceConfig.getBoolean("app_hibernation", "app_hibernation_enabled", false);
    }
}
