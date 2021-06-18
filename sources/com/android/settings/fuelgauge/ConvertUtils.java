package com.android.settings.fuelgauge;

import android.content.ContentValues;
import android.os.BatteryUsageStats;
import android.os.UserHandle;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public final class ConvertUtils {
    private static final BatteryHistEntry EMPTY_BATTERY_HIST_ENTRY = new BatteryHistEntry(new ContentValues());
    private static final Map<String, BatteryHistEntry> EMPTY_BATTERY_MAP = new HashMap();
    static double PERCENTAGE_OF_TOTAL_THRESHOLD = 1.0d;
    static SimpleDateFormat sSimpleDateFormat;
    static SimpleDateFormat sSimpleDateFormatForHour;
    private static String sZoneId;
    private static String sZoneIdForHour;

    private static double getDiffValue(double d, double d2, double d3) {
        double d4 = 0.0d;
        double d5 = d2 > d ? d2 - d : 0.0d;
        if (d3 > d2) {
            d4 = d3 - d2;
        }
        return d5 + d4;
    }

    private static long getDiffValue(long j, long j2, long j3) {
        long j4 = 0;
        long j5 = j2 > j ? j2 - j : 0;
        if (j3 > j2) {
            j4 = j3 - j2;
        }
        return j5 + j4;
    }

    public static ContentValues convert(BatteryEntry batteryEntry, BatteryUsageStats batteryUsageStats, int i, int i2, int i3, long j, long j2) {
        ContentValues contentValues = new ContentValues();
        if (batteryEntry == null || batteryUsageStats == null) {
            contentValues.put("packageName", "fake_package");
        } else {
            contentValues.put("uid", Long.valueOf((long) batteryEntry.getUid()));
            contentValues.put("userId", Long.valueOf((long) UserHandle.getUserId(batteryEntry.getUid())));
            contentValues.put("appLabel", batteryEntry.getLabel());
            contentValues.put("packageName", batteryEntry.getDefaultPackageName());
            contentValues.put("isHidden", Boolean.valueOf(batteryEntry.isHidden()));
            contentValues.put("totalPower", Double.valueOf(batteryUsageStats.getConsumedPower()));
            contentValues.put("consumePower", Double.valueOf(batteryEntry.getConsumedPower()));
            contentValues.put("percentOfTotal", Double.valueOf(batteryEntry.percent));
            contentValues.put("foregroundUsageTimeInMs", Long.valueOf(batteryEntry.getTimeInForegroundMs()));
            contentValues.put("backgroundUsageTimeInMs", Long.valueOf(batteryEntry.getTimeInBackgroundMs()));
            contentValues.put("drainType", Integer.valueOf(batteryEntry.getPowerComponentId()));
            contentValues.put("consumerType", Integer.valueOf(batteryEntry.getConsumerType()));
        }
        contentValues.put("bootTimestamp", Long.valueOf(j));
        contentValues.put("timestamp", Long.valueOf(j2));
        contentValues.put("zoneId", TimeZone.getDefault().getID());
        contentValues.put("batteryLevel", Integer.valueOf(i));
        contentValues.put("batteryStatus", Integer.valueOf(i2));
        contentValues.put("batteryHealth", Integer.valueOf(i3));
        return contentValues;
    }

    public static String utcToLocalTime(long j) {
        String id = TimeZone.getDefault().getID();
        if (!id.equals(sZoneId) || sSimpleDateFormat == null) {
            sZoneId = id;
            sSimpleDateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss", Locale.ENGLISH);
        }
        return sSimpleDateFormat.format(new Date(j));
    }

    public static String utcToLocalTimeHour(long j) {
        String id = TimeZone.getDefault().getID();
        if (!id.equals(sZoneIdForHour) || sSimpleDateFormatForHour == null) {
            sZoneIdForHour = id;
            sSimpleDateFormatForHour = new SimpleDateFormat("h aa", Locale.ENGLISH);
        }
        return sSimpleDateFormatForHour.format(new Date(j)).toLowerCase(Locale.getDefault());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00f1, code lost:
        if (r5 == 0.0d) goto L_0x00fc;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map<java.lang.Integer, java.util.List<com.android.settings.fuelgauge.BatteryDiffEntry>> getIndexedUsageMap(android.content.Context r39, int r40, long[] r41, java.util.Map<java.lang.Long, java.util.Map<java.lang.String, com.android.settings.fuelgauge.BatteryHistEntry>> r42, boolean r43) {
        /*
            r0 = r42
            if (r0 == 0) goto L_0x01cc
            boolean r1 = r42.isEmpty()
            if (r1 == 0) goto L_0x000c
            goto L_0x01cc
        L_0x000c:
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            r3 = r40
            r4 = 0
        L_0x0014:
            if (r4 >= r3) goto L_0x01c1
            int r5 = r4 * 2
            r6 = r41[r5]
            java.lang.Long r6 = java.lang.Long.valueOf(r6)
            int r7 = r5 + 1
            r7 = r41[r7]
            java.lang.Long r7 = java.lang.Long.valueOf(r7)
            r8 = 2
            int r5 = r5 + r8
            r9 = r41[r5]
            java.lang.Long r5 = java.lang.Long.valueOf(r9)
            java.util.Map<java.lang.String, com.android.settings.fuelgauge.BatteryHistEntry> r9 = EMPTY_BATTERY_MAP
            java.lang.Object r6 = r0.getOrDefault(r6, r9)
            java.util.Map r6 = (java.util.Map) r6
            java.lang.Object r7 = r0.getOrDefault(r7, r9)
            java.util.Map r7 = (java.util.Map) r7
            java.lang.Object r5 = r0.getOrDefault(r5, r9)
            java.util.Map r5 = (java.util.Map) r5
            boolean r9 = r6.isEmpty()
            if (r9 != 0) goto L_0x01a5
            boolean r9 = r7.isEmpty()
            if (r9 != 0) goto L_0x01a5
            boolean r9 = r5.isEmpty()
            if (r9 == 0) goto L_0x0056
            goto L_0x01a5
        L_0x0056:
            java.util.HashSet r9 = new java.util.HashSet
            r9.<init>()
            java.util.Set r10 = r6.keySet()
            r9.addAll(r10)
            java.util.Set r10 = r7.keySet()
            r9.addAll(r10)
            java.util.Set r10 = r5.keySet()
            r9.addAll(r10)
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            java.lang.Integer r11 = java.lang.Integer.valueOf(r4)
            r1.put(r11, r10)
            java.util.Iterator r9 = r9.iterator()
            r13 = 0
        L_0x0082:
            boolean r15 = r9.hasNext()
            if (r15 == 0) goto L_0x0184
            java.lang.Object r15 = r9.next()
            java.lang.String r15 = (java.lang.String) r15
            com.android.settings.fuelgauge.BatteryHistEntry r8 = EMPTY_BATTERY_HIST_ENTRY
            java.lang.Object r16 = r6.getOrDefault(r15, r8)
            r2 = r16
            com.android.settings.fuelgauge.BatteryHistEntry r2 = (com.android.settings.fuelgauge.BatteryHistEntry) r2
            java.lang.Object r16 = r7.getOrDefault(r15, r8)
            r11 = r16
            com.android.settings.fuelgauge.BatteryHistEntry r11 = (com.android.settings.fuelgauge.BatteryHistEntry) r11
            java.lang.Object r8 = r5.getOrDefault(r15, r8)
            com.android.settings.fuelgauge.BatteryHistEntry r8 = (com.android.settings.fuelgauge.BatteryHistEntry) r8
            r15 = r5
            r12 = r6
            long r5 = r2.mForegroundUsageTimeInMs
            r16 = r1
            long r0 = r11.mForegroundUsageTimeInMs
            r25 = r4
            long r3 = r8.mForegroundUsageTimeInMs
            r19 = r5
            r21 = r0
            r23 = r3
            long r0 = getDiffValue((long) r19, (long) r21, (long) r23)
            long r3 = r2.mBackgroundUsageTimeInMs
            long r5 = r11.mBackgroundUsageTimeInMs
            r27 = r9
            r26 = r10
            long r9 = r8.mBackgroundUsageTimeInMs
            r19 = r3
            r21 = r5
            r23 = r9
            long r3 = getDiffValue((long) r19, (long) r21, (long) r23)
            double r5 = r2.mConsumePower
            double r9 = r11.mConsumePower
            r28 = r13
            r14 = r12
            double r12 = r8.mConsumePower
            r19 = r5
            r21 = r9
            r23 = r12
            double r5 = getDiffValue((double) r19, (double) r21, (double) r23)
            r9 = 0
            int r12 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1))
            if (r12 != 0) goto L_0x00f4
            int r9 = (r3 > r9 ? 1 : (r3 == r9 ? 0 : -1))
            if (r9 != 0) goto L_0x00f4
            r9 = 0
            int r12 = (r5 > r9 ? 1 : (r5 == r9 ? 0 : -1))
            if (r12 != 0) goto L_0x00f6
            goto L_0x00fc
        L_0x00f4:
            r9 = 0
        L_0x00f6:
            com.android.settings.fuelgauge.BatteryHistEntry r38 = selectBatteryHistEntry(r2, r11, r8)
            if (r38 != 0) goto L_0x010f
        L_0x00fc:
            r3 = r40
            r0 = r42
            r6 = r14
            r5 = r15
            r1 = r16
            r4 = r25
            r10 = r26
            r9 = r27
            r13 = r28
            r8 = 2
            goto L_0x0082
        L_0x010f:
            long r11 = r0 + r3
            float r8 = (float) r11
            r11 = 1255913984(0x4adbba00, float:7200000.0)
            int r12 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1))
            if (r12 <= 0) goto L_0x015b
            float r11 = r11 / r8
            r8 = 3
            java.lang.Object[] r8 = new java.lang.Object[r8]
            java.time.Duration r12 = java.time.Duration.ofMillis(r0)
            long r12 = r12.getSeconds()
            java.lang.Long r12 = java.lang.Long.valueOf(r12)
            r13 = 0
            r8[r13] = r12
            java.time.Duration r12 = java.time.Duration.ofMillis(r3)
            long r17 = r12.getSeconds()
            java.lang.Long r12 = java.lang.Long.valueOf(r17)
            r17 = 1
            r8[r17] = r12
            r12 = 2
            r8[r12] = r2
            java.lang.String r2 = "abnormal usage time %d|%d for:\n%s"
            java.lang.String r2 = java.lang.String.format(r2, r8)
            java.lang.String r8 = "ConvertUtils"
            android.util.Log.w(r8, r2)
            float r0 = (float) r0
            float r0 = r0 * r11
            int r0 = java.lang.Math.round(r0)
            long r0 = (long) r0
            float r2 = (float) r3
            float r2 = r2 * r11
            int r2 = java.lang.Math.round(r2)
            long r3 = (long) r2
            double r9 = (double) r11
            double r5 = r5 * r9
            goto L_0x015d
        L_0x015b:
            r12 = 2
            r13 = 0
        L_0x015d:
            r32 = r0
            r34 = r3
            r36 = r5
            double r0 = r28 + r36
            com.android.settings.fuelgauge.BatteryDiffEntry r2 = new com.android.settings.fuelgauge.BatteryDiffEntry
            r30 = r2
            r31 = r39
            r30.<init>(r31, r32, r34, r36, r38)
            r3 = r26
            r3.add(r2)
            r10 = r3
            r8 = r12
            r6 = r14
            r5 = r15
            r4 = r25
            r9 = r27
            r3 = r40
            r13 = r0
            r1 = r16
            r0 = r42
            goto L_0x0082
        L_0x0184:
            r16 = r1
            r25 = r4
            r3 = r10
            r28 = r13
            r13 = 0
            java.util.Iterator r0 = r3.iterator()
        L_0x0190:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x01a2
            java.lang.Object r1 = r0.next()
            com.android.settings.fuelgauge.BatteryDiffEntry r1 = (com.android.settings.fuelgauge.BatteryDiffEntry) r1
            r11 = r28
            r1.setTotalConsumePower(r11)
            goto L_0x0190
        L_0x01a2:
            r2 = r16
            goto L_0x01b8
        L_0x01a5:
            r16 = r1
            r25 = r4
            r13 = 0
            java.lang.Integer r0 = java.lang.Integer.valueOf(r25)
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r2 = r16
            r2.put(r0, r1)
        L_0x01b8:
            int r4 = r25 + 1
            r3 = r40
            r0 = r42
            r1 = r2
            goto L_0x0014
        L_0x01c1:
            r2 = r1
            r0 = -1
            insert24HoursData(r0, r2)
            if (r43 == 0) goto L_0x01cb
            purgeLowPercentageAndFakeData(r2)
        L_0x01cb:
            return r2
        L_0x01cc:
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.fuelgauge.ConvertUtils.getIndexedUsageMap(android.content.Context, int, long[], java.util.Map, boolean):java.util.Map");
    }

    private static void insert24HoursData(int i, Map<Integer, List<BatteryDiffEntry>> map) {
        HashMap hashMap = new HashMap();
        double d = 0.0d;
        for (List<BatteryDiffEntry> it : map.values()) {
            for (BatteryDiffEntry batteryDiffEntry : it) {
                String key = batteryDiffEntry.mBatteryHistEntry.getKey();
                BatteryDiffEntry batteryDiffEntry2 = (BatteryDiffEntry) hashMap.get(key);
                if (batteryDiffEntry2 == null) {
                    hashMap.put(key, batteryDiffEntry.clone());
                } else {
                    batteryDiffEntry2.mForegroundUsageTimeInMs += batteryDiffEntry.mForegroundUsageTimeInMs;
                    batteryDiffEntry2.mBackgroundUsageTimeInMs += batteryDiffEntry.mBackgroundUsageTimeInMs;
                    batteryDiffEntry2.mConsumePower += batteryDiffEntry.mConsumePower;
                }
                d += batteryDiffEntry.mConsumePower;
            }
        }
        ArrayList<BatteryDiffEntry> arrayList = new ArrayList<>(hashMap.values());
        for (BatteryDiffEntry totalConsumePower : arrayList) {
            totalConsumePower.setTotalConsumePower(d);
        }
        map.put(Integer.valueOf(i), arrayList);
    }

    private static void purgeLowPercentageAndFakeData(Map<Integer, List<BatteryDiffEntry>> map) {
        for (List<BatteryDiffEntry> it : map.values()) {
            Iterator it2 = it.iterator();
            while (it2.hasNext()) {
                BatteryDiffEntry batteryDiffEntry = (BatteryDiffEntry) it2.next();
                if (batteryDiffEntry.getPercentOfTotal() < PERCENTAGE_OF_TOTAL_THRESHOLD || "fake_package".equals(batteryDiffEntry.getPackageName())) {
                    it2.remove();
                }
            }
        }
    }

    private static BatteryHistEntry selectBatteryHistEntry(BatteryHistEntry batteryHistEntry, BatteryHistEntry batteryHistEntry2, BatteryHistEntry batteryHistEntry3) {
        if (batteryHistEntry != null && batteryHistEntry != EMPTY_BATTERY_HIST_ENTRY) {
            return batteryHistEntry;
        }
        if (batteryHistEntry2 != null && batteryHistEntry2 != EMPTY_BATTERY_HIST_ENTRY) {
            return batteryHistEntry2;
        }
        if (batteryHistEntry3 == null || batteryHistEntry3 == EMPTY_BATTERY_HIST_ENTRY) {
            return null;
        }
        return batteryHistEntry3;
    }
}
