package com.android.settings.fuelgauge.batterytip;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.StatsDimensionsValue;
import android.os.UserManager;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.fuelgauge.BatteryUtils;
import com.android.settings.fuelgauge.PowerUsageFeatureProvider;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.fuelgauge.PowerAllowlistBackend;
import com.android.settingslib.utils.ThreadUtils;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AnomalyDetectionJobService extends JobService {
    static final long MAX_DELAY_MS = TimeUnit.MINUTES.toMillis(30);
    static final int STATSD_UID_FILED = 1;
    static final int UID_NULL = -1;
    boolean mIsJobCanceled = false;
    private final Object mLock = new Object();

    public static void scheduleAnomalyDetection(Context context, Intent intent) {
        if (((JobScheduler) context.getSystemService(JobScheduler.class)).enqueue(new JobInfo.Builder(R.integer.job_anomaly_detection, new ComponentName(context, AnomalyDetectionJobService.class)).setOverrideDeadline(MAX_DELAY_MS).build(), new JobWorkItem(intent)) != 1) {
            Log.i("AnomalyDetectionService", "Anomaly detection job service enqueue failed.");
        }
    }

    public boolean onStartJob(JobParameters jobParameters) {
        synchronized (this.mLock) {
            this.mIsJobCanceled = false;
        }
        ThreadUtils.postOnBackgroundThread((Runnable) new AnomalyDetectionJobService$$ExternalSyntheticLambda0(this, jobParameters));
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartJob$0(JobParameters jobParameters) {
        BatteryDatabaseManager instance = BatteryDatabaseManager.getInstance(this);
        BatteryTipPolicy batteryTipPolicy = new BatteryTipPolicy(this);
        BatteryUtils instance2 = BatteryUtils.getInstance(this);
        ContentResolver contentResolver = getContentResolver();
        UserManager userManager = (UserManager) getSystemService(UserManager.class);
        PowerAllowlistBackend instance3 = PowerAllowlistBackend.getInstance(this);
        PowerUsageFeatureProvider powerUsageFeatureProvider = FeatureFactory.getFactory(this).getPowerUsageFeatureProvider(this);
        MetricsFeatureProvider metricsFeatureProvider = FeatureFactory.getFactory(this).getMetricsFeatureProvider();
        JobWorkItem dequeueWork = dequeueWork(jobParameters);
        while (dequeueWork != null) {
            BatteryDatabaseManager batteryDatabaseManager = instance;
            saveAnomalyToDatabase(this, userManager, instance, instance2, batteryTipPolicy, instance3, contentResolver, powerUsageFeatureProvider, metricsFeatureProvider, dequeueWork.getIntent().getExtras());
            completeWork(jobParameters, dequeueWork);
            dequeueWork = dequeueWork(jobParameters);
            instance = batteryDatabaseManager;
        }
    }

    public boolean onStopJob(JobParameters jobParameters) {
        synchronized (this.mLock) {
            this.mIsJobCanceled = true;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0068, code lost:
        if (android.provider.Settings.Global.getInt(r1, "app_auto_restriction_enabled", 1) == 1) goto L_0x006a;
     */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x008f A[Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x00a7 A[Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveAnomalyToDatabase(android.content.Context r14, android.os.UserManager r15, com.android.settings.fuelgauge.batterytip.BatteryDatabaseManager r16, com.android.settings.fuelgauge.BatteryUtils r17, com.android.settings.fuelgauge.batterytip.BatteryTipPolicy r18, com.android.settingslib.fuelgauge.PowerAllowlistBackend r19, android.content.ContentResolver r20, com.android.settings.fuelgauge.PowerUsageFeatureProvider r21, com.android.settingslib.core.instrumentation.MetricsFeatureProvider r22, android.os.Bundle r23) {
        /*
            r13 = this;
            r0 = r17
            r1 = r20
            r2 = r23
            java.lang.String r3 = "android.app.extra.STATS_DIMENSIONS_VALUE"
            android.os.Parcelable r3 = r2.getParcelable(r3)
            android.os.StatsDimensionsValue r3 = (android.os.StatsDimensionsValue) r3
            long r4 = java.lang.System.currentTimeMillis()
            java.lang.String r6 = "key_anomaly_timestamp"
            long r4 = r2.getLong(r6, r4)
            java.lang.String r6 = "android.app.extra.STATS_BROADCAST_SUBSCRIBER_COOKIES"
            java.util.ArrayList r2 = r2.getStringArrayList(r6)
            com.android.settings.fuelgauge.batterytip.AnomalyInfo r6 = new com.android.settings.fuelgauge.batterytip.AnomalyInfo
            boolean r7 = com.android.internal.util.ArrayUtils.isEmpty(r2)
            r8 = 0
            if (r7 != 0) goto L_0x002e
            java.lang.Object r2 = r2.get(r8)
            java.lang.String r2 = (java.lang.String) r2
            goto L_0x0030
        L_0x002e:
            java.lang.String r2 = ""
        L_0x0030:
            r6.<init>(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r7 = "Extra stats value: "
            r2.append(r7)
            java.lang.String r7 = r3.toString()
            r2.append(r7)
            java.lang.String r2 = r2.toString()
            java.lang.String r7 = "AnomalyDetectionService"
            android.util.Log.i(r7, r2)
            r2 = r13
            int r2 = r13.extractUidFromStatsDimensionsValue(r3)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            boolean r3 = r21.isSmartBatterySupported()     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            r9 = 1
            if (r3 == 0) goto L_0x0062
            java.lang.String r3 = "adaptive_battery_management_enabled"
            int r1 = android.provider.Settings.Global.getInt(r1, r3, r9)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            if (r1 != r9) goto L_0x006b
            goto L_0x006a
        L_0x0062:
            java.lang.String r3 = "app_auto_restriction_enabled"
            int r1 = android.provider.Settings.Global.getInt(r1, r3, r9)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            if (r1 != r9) goto L_0x006b
        L_0x006a:
            r8 = r9
        L_0x006b:
            java.lang.String r1 = r0.getPackageName(r2)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            long r10 = r0.getAppLongVersionCode(r1)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            r3.append(r1)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            java.lang.String r12 = "/"
            r3.append(r12)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            r3.append(r10)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            r10 = r19
            boolean r10 = r0.shouldHideAnomaly(r10, r2, r6)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            if (r10 == 0) goto L_0x00a7
            r0 = 0
            r1 = 1387(0x56b, float:1.944E-42)
            r2 = 0
            java.lang.Integer r4 = r6.anomalyType     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            int r4 = r4.intValue()     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            r13 = r22
            r14 = r0
            r15 = r1
            r16 = r2
            r17 = r3
            r18 = r4
            r13.action(r14, r15, r16, r17, r18)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            goto L_0x00f7
        L_0x00a7:
            if (r8 == 0) goto L_0x00c5
            boolean r8 = r6.autoRestriction     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            if (r8 == 0) goto L_0x00c5
            r0.setForceAppStandby(r2, r1, r9)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            java.lang.Integer r0 = r6.anomalyType     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            int r0 = r0.intValue()     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            r8 = 2
            r13 = r16
            r14 = r2
            r15 = r1
            r16 = r0
            r17 = r8
            r18 = r4
            r13.insertAnomaly(r14, r15, r16, r17, r18)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            goto L_0x00d9
        L_0x00c5:
            java.lang.Integer r0 = r6.anomalyType     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            int r0 = r0.intValue()     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            r8 = 0
            r13 = r16
            r14 = r2
            r15 = r1
            r16 = r0
            r17 = r8
            r18 = r4
            r13.insertAnomaly(r14, r15, r16, r17, r18)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
        L_0x00d9:
            r0 = 0
            r1 = 1367(0x557, float:1.916E-42)
            r2 = 0
            java.lang.Integer r4 = r6.anomalyType     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            int r4 = r4.intValue()     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            r13 = r22
            r14 = r0
            r15 = r1
            r16 = r2
            r17 = r3
            r18 = r4
            r13.action(r14, r15, r16, r17, r18)     // Catch:{ IndexOutOfBoundsException | NullPointerException -> 0x00f1 }
            goto L_0x00f7
        L_0x00f1:
            r0 = move-exception
            java.lang.String r1 = "Parse stats dimensions value error."
            android.util.Log.e(r7, r1, r0)
        L_0x00f7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.fuelgauge.batterytip.AnomalyDetectionJobService.saveAnomalyToDatabase(android.content.Context, android.os.UserManager, com.android.settings.fuelgauge.batterytip.BatteryDatabaseManager, com.android.settings.fuelgauge.BatteryUtils, com.android.settings.fuelgauge.batterytip.BatteryTipPolicy, com.android.settingslib.fuelgauge.PowerAllowlistBackend, android.content.ContentResolver, com.android.settings.fuelgauge.PowerUsageFeatureProvider, com.android.settingslib.core.instrumentation.MetricsFeatureProvider, android.os.Bundle):void");
    }

    /* access modifiers changed from: package-private */
    public int extractUidFromStatsDimensionsValue(StatsDimensionsValue statsDimensionsValue) {
        if (statsDimensionsValue == null) {
            return UID_NULL;
        }
        if (statsDimensionsValue.isValueType(3) && statsDimensionsValue.getField() == 1) {
            return statsDimensionsValue.getIntValue();
        }
        if (statsDimensionsValue.isValueType(7)) {
            List tupleValueList = statsDimensionsValue.getTupleValueList();
            int size = tupleValueList.size();
            for (int i = 0; i < size; i++) {
                int extractUidFromStatsDimensionsValue = extractUidFromStatsDimensionsValue((StatsDimensionsValue) tupleValueList.get(i));
                if (extractUidFromStatsDimensionsValue != UID_NULL) {
                    return extractUidFromStatsDimensionsValue;
                }
            }
        }
        return UID_NULL;
    }

    /* access modifiers changed from: package-private */
    public JobWorkItem dequeueWork(JobParameters jobParameters) {
        synchronized (this.mLock) {
            if (this.mIsJobCanceled) {
                return null;
            }
            JobWorkItem dequeueWork = jobParameters.dequeueWork();
            return dequeueWork;
        }
    }

    /* access modifiers changed from: package-private */
    public void completeWork(JobParameters jobParameters, JobWorkItem jobWorkItem) {
        synchronized (this.mLock) {
            if (!this.mIsJobCanceled) {
                jobParameters.completeWork(jobWorkItem);
            }
        }
    }
}
