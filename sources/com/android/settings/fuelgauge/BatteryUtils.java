package com.android.settings.fuelgauge;

import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.BatteryConsumer;
import android.os.BatteryStats;
import android.os.BatteryStatsManager;
import android.os.BatteryUsageStats;
import android.os.BatteryUsageStatsQuery;
import android.os.SystemClock;
import android.os.UidBatteryConsumer;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.util.ArrayUtils;
import com.android.settings.fuelgauge.batterytip.AnomalyInfo;
import com.android.settings.fuelgauge.batterytip.BatteryDatabaseManager;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.fuelgauge.Estimate;
import com.android.settingslib.fuelgauge.PowerAllowlistBackend;
import com.android.settingslib.utils.PowerUtil;
import com.android.settingslib.utils.ThreadUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class BatteryUtils {
    private static BatteryUtils sInstance;
    private AppOpsManager mAppOpsManager;
    private Context mContext;
    private PackageManager mPackageManager;
    PowerUsageFeatureProvider mPowerUsageFeatureProvider;

    public double calculateBatteryPercent(double d, double d2, int i) {
        if (d2 == 0.0d) {
            return 0.0d;
        }
        return (d / d2) * ((double) i);
    }

    public static BatteryUtils getInstance(Context context) {
        BatteryUtils batteryUtils = sInstance;
        if (batteryUtils == null || batteryUtils.isDataCorrupted()) {
            sInstance = new BatteryUtils(context.getApplicationContext());
        }
        return sInstance;
    }

    public BatteryUtils(Context context) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mAppOpsManager = (AppOpsManager) context.getSystemService("appops");
        this.mPowerUsageFeatureProvider = FeatureFactory.getFactory(context).getPowerUsageFeatureProvider(context);
    }

    public boolean shouldHideUidBatteryConsumer(UidBatteryConsumer uidBatteryConsumer) {
        return shouldHideUidBatteryConsumer(uidBatteryConsumer, this.mPackageManager.getPackagesForUid(uidBatteryConsumer.getUid()));
    }

    public boolean shouldHideUidBatteryConsumer(UidBatteryConsumer uidBatteryConsumer, String[] strArr) {
        return uidBatteryConsumer.getConsumedPower() < 0.002d || this.mPowerUsageFeatureProvider.isTypeSystem(uidBatteryConsumer.getUid(), strArr) || shouldHideUidBatteryConsumerUnconditionally(uidBatteryConsumer, strArr);
    }

    /* access modifiers changed from: package-private */
    public boolean shouldHideUidBatteryConsumerUnconditionally(UidBatteryConsumer uidBatteryConsumer, String[] strArr) {
        return uidBatteryConsumer.getUid() < 0 || isHiddenSystemModule(strArr);
    }

    public boolean shouldHideDevicePowerComponent(BatteryConsumer batteryConsumer, int i) {
        return i == 0 || i == 2 || i == 8 || i == 11 || i == 16 || batteryConsumer.getConsumedPower(i) < 0.002d;
    }

    public boolean shouldHideCustomDevicePowerComponent(BatteryConsumer batteryConsumer, int i) {
        return batteryConsumer.getConsumedPowerForCustomComponent(i) < 0.002d;
    }

    public boolean isHiddenSystemModule(String[] strArr) {
        if (strArr != null) {
            for (String isHiddenSystemModule : strArr) {
                if (AppUtils.isHiddenSystemModule(this.mContext, isHiddenSystemModule)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getPackageName(int i) {
        String[] packagesForUid = this.mPackageManager.getPackagesForUid(i);
        if (ArrayUtils.isEmpty(packagesForUid)) {
            return null;
        }
        return packagesForUid[0];
    }

    public long calculateLastFullChargeTime(BatteryUsageStats batteryUsageStats, long j) {
        return j - batteryUsageStats.getStatsStartTimestamp();
    }

    public static void logRuntime(String str, String str2, long j) {
        Log.d(str, str2 + ": " + (System.currentTimeMillis() - j) + "ms");
    }

    public static boolean isBatteryDefenderOn(BatteryInfo batteryInfo) {
        return batteryInfo.isOverheated && !batteryInfo.discharging;
    }

    public int getPackageUid(String str) {
        if (str == null) {
            return -1;
        }
        try {
            return this.mPackageManager.getPackageUid(str, 128);
        } catch (PackageManager.NameNotFoundException unused) {
            return -1;
        }
    }

    public void setForceAppStandby(int i, String str, int i2) {
        if (isPreOApp(str)) {
            this.mAppOpsManager.setMode(63, i, str, i2);
        }
        this.mAppOpsManager.setMode(70, i, str, i2);
        ThreadUtils.postOnBackgroundThread((Runnable) new BatteryUtils$$ExternalSyntheticLambda0(this, i2, i, str));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setForceAppStandby$0(int i, int i2, String str) {
        BatteryDatabaseManager instance = BatteryDatabaseManager.getInstance(this.mContext);
        if (i == 1) {
            instance.insertAction(0, i2, str, System.currentTimeMillis());
        } else if (i == 0) {
            instance.deleteAction(0, i2, str);
        }
    }

    public boolean isForceAppStandbyEnabled(int i, String str) {
        return this.mAppOpsManager.checkOpNoThrow(70, i, str) == 1;
    }

    public boolean clearForceAppStandby(String str) {
        int packageUid = getPackageUid(str);
        if (packageUid == -1 || !isForceAppStandbyEnabled(packageUid, str)) {
            return false;
        }
        setForceAppStandby(packageUid, str, 0);
        return true;
    }

    public BatteryInfo getBatteryInfo(String str) {
        BatteryUsageStats batteryUsageStats = ((BatteryStatsManager) this.mContext.getSystemService(BatteryStatsManager.class)).getBatteryUsageStats(new BatteryUsageStatsQuery.Builder().includeBatteryHistory().build());
        long currentTimeMillis = System.currentTimeMillis();
        Intent registerReceiver = this.mContext.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        long convertMsToUs = PowerUtil.convertMsToUs(SystemClock.elapsedRealtime());
        Estimate enhancedEstimate = getEnhancedEstimate();
        if (enhancedEstimate == null) {
            enhancedEstimate = new Estimate(batteryUsageStats.getBatteryTimeRemainingMs(), false, -1);
        }
        logRuntime(str, "BatteryInfoLoader post query", currentTimeMillis);
        BatteryInfo batteryInfo = BatteryInfo.getBatteryInfo(this.mContext, registerReceiver, batteryUsageStats, enhancedEstimate, convertMsToUs, false);
        logRuntime(str, "BatteryInfoLoader.loadInBackground", currentTimeMillis);
        return batteryInfo;
    }

    /* access modifiers changed from: package-private */
    public Estimate getEnhancedEstimate() {
        if (Duration.between(Estimate.getLastCacheUpdateTime(this.mContext), Instant.now()).compareTo(Duration.ofSeconds(10)) < 0) {
            return Estimate.getCachedEstimateIfAvailable(this.mContext);
        }
        PowerUsageFeatureProvider powerUsageFeatureProvider = this.mPowerUsageFeatureProvider;
        if (powerUsageFeatureProvider == null || !powerUsageFeatureProvider.isEnhancedBatteryPredictionEnabled(this.mContext)) {
            return null;
        }
        Estimate enhancedBatteryPrediction = this.mPowerUsageFeatureProvider.getEnhancedBatteryPrediction(this.mContext);
        if (enhancedBatteryPrediction != null) {
            Estimate.storeCachedEstimate(this.mContext, enhancedBatteryPrediction);
        }
        return enhancedBatteryPrediction;
    }

    private boolean isDataCorrupted() {
        return this.mPackageManager == null || this.mAppOpsManager == null;
    }

    /* access modifiers changed from: package-private */
    public long getForegroundActivityTotalTimeUs(BatteryStats.Uid uid, long j) {
        BatteryStats.Timer foregroundActivityTimer = uid.getForegroundActivityTimer();
        if (foregroundActivityTimer != null) {
            return foregroundActivityTimer.getTotalTimeLocked(j, 0);
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public long getForegroundServiceTotalTimeUs(BatteryStats.Uid uid, long j) {
        BatteryStats.Timer foregroundServiceTimer = uid.getForegroundServiceTimer();
        if (foregroundServiceTimer != null) {
            return foregroundServiceTimer.getTotalTimeLocked(j, 0);
        }
        return 0;
    }

    public boolean isPreOApp(String str) {
        try {
            if (this.mPackageManager.getApplicationInfo(str, 128).targetSdkVersion < 26) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("BatteryUtils", "Cannot find package: " + str, e);
            return false;
        }
    }

    public boolean isPreOApp(String[] strArr) {
        if (ArrayUtils.isEmpty(strArr)) {
            return false;
        }
        for (String isPreOApp : strArr) {
            if (isPreOApp(isPreOApp)) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldHideAnomaly(PowerAllowlistBackend powerAllowlistBackend, int i, AnomalyInfo anomalyInfo) {
        String[] packagesForUid = this.mPackageManager.getPackagesForUid(i);
        if (ArrayUtils.isEmpty(packagesForUid) || isSystemUid(i) || powerAllowlistBackend.isAllowlisted(packagesForUid)) {
            return true;
        }
        if (isSystemApp(this.mPackageManager, packagesForUid) && !hasLauncherEntry(packagesForUid)) {
            return true;
        }
        if (!isExcessiveBackgroundAnomaly(anomalyInfo) || isPreOApp(packagesForUid)) {
            return false;
        }
        return true;
    }

    private boolean isExcessiveBackgroundAnomaly(AnomalyInfo anomalyInfo) {
        return anomalyInfo.anomalyType.intValue() == 4;
    }

    private boolean isSystemUid(int i) {
        int appId = UserHandle.getAppId(i);
        return appId >= 0 && appId < 10000;
    }

    private boolean isSystemApp(PackageManager packageManager, String[] strArr) {
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            String str = strArr[i];
            try {
                if ((packageManager.getApplicationInfo(str, 0).flags & 1) != 0) {
                    return true;
                }
                i++;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("BatteryUtils", "Package not found: " + str, e);
            }
        }
        return false;
    }

    private boolean hasLauncherEntry(String[] strArr) {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> queryIntentActivities = this.mPackageManager.queryIntentActivities(intent, 1835520);
        int size = queryIntentActivities.size();
        for (int i = 0; i < size; i++) {
            if (ArrayUtils.contains(strArr, queryIntentActivities.get(i).activityInfo.packageName)) {
                return true;
            }
        }
        return false;
    }

    public long getAppLongVersionCode(String str) {
        try {
            return this.mPackageManager.getPackageInfo(str, 0).getLongVersionCode();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("BatteryUtils", "Cannot find package: " + str, e);
            return -1;
        }
    }
}
