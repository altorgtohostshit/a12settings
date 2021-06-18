package com.google.android.settings.fuelgauge;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryUsageStats;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.SystemClock;
import android.util.Log;
import com.android.settings.fuelgauge.BatteryEntry;
import com.android.settings.fuelgauge.BatteryHistEntry;
import com.android.settings.fuelgauge.ConvertUtils;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class DatabaseUtils {
    public static final Uri BATTERY_CONTENT_URI;
    private static final Uri CONTENT_BASE_URI;
    public static final Uri SI_BATTERY_SETTINGS_URI;

    static {
        Uri build = new Uri.Builder().scheme("content").authority("com.google.android.settings.intelligence.modules.battery.provider").build();
        CONTENT_BASE_URI = build;
        BATTERY_CONTENT_URI = Uri.withAppendedPath(build, "BatteryState");
        SI_BATTERY_SETTINGS_URI = Uri.withAppendedPath(build, "BatterySettings");
    }

    static boolean isContentProviderEnabled(Context context) {
        return context.getPackageManager().getComponentEnabledSetting(new ComponentName("com.google.android.settings.intelligence", "com.google.android.settings.intelligence.modules.battery.impl.BatterySettingsContentProvider")) == 1;
    }

    static List<ContentValues> sendBatteryEntryData(Context context, List<BatteryEntry> list, BatteryUsageStats batteryUsageStats) {
        ArrayList arrayList;
        String str;
        ArrayList arrayList2;
        int i;
        long currentTimeMillis = System.currentTimeMillis();
        Intent batteryIntet = getBatteryIntet(context);
        if (batteryIntet == null) {
            Log.e("DatabaseUtils", "sendBatteryEntryData(): cannot fetch battery intent");
            return null;
        }
        int batteryLevel = getBatteryLevel(batteryIntet);
        int intExtra = batteryIntet.getIntExtra("status", 1);
        int intExtra2 = batteryIntet.getIntExtra("health", 1);
        long millis = Clock.systemUTC().millis();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        ArrayList arrayList3 = new ArrayList();
        if (list != null) {
            Stream filter = list.stream().filter(DatabaseUtils$$ExternalSyntheticLambda1.INSTANCE);
            DatabaseUtils$$ExternalSyntheticLambda0 databaseUtils$$ExternalSyntheticLambda0 = r4;
            str = "DatabaseUtils";
            arrayList = arrayList3;
            DatabaseUtils$$ExternalSyntheticLambda0 databaseUtils$$ExternalSyntheticLambda02 = new DatabaseUtils$$ExternalSyntheticLambda0(arrayList3, batteryUsageStats, batteryLevel, intExtra, intExtra2, elapsedRealtime, millis);
            filter.forEach(databaseUtils$$ExternalSyntheticLambda0);
        } else {
            str = "DatabaseUtils";
            arrayList = arrayList3;
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (!arrayList.isEmpty()) {
            ContentValues[] contentValuesArr = new ContentValues[arrayList.size()];
            arrayList2 = arrayList;
            arrayList2.toArray(contentValuesArr);
            i = contentResolver.bulkInsert(BATTERY_CONTENT_URI, contentValuesArr);
        } else {
            arrayList2 = arrayList;
            ContentValues convert = ConvertUtils.convert((BatteryEntry) null, (BatteryUsageStats) null, batteryLevel, intExtra, intExtra2, elapsedRealtime, millis);
            contentResolver.insert(BATTERY_CONTENT_URI, convert);
            arrayList2.add(convert);
            i = 1;
        }
        String str2 = str;
        Log.d(str2, String.format("sendBatteryEntryData() size=%d in %d/ms", new Object[]{Integer.valueOf(i), Long.valueOf(System.currentTimeMillis() - currentTimeMillis)}));
        return arrayList2;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$sendBatteryEntryData$0(BatteryEntry batteryEntry) {
        long timeInForegroundMs = batteryEntry.getTimeInForegroundMs();
        long timeInBackgroundMs = batteryEntry.getTimeInBackgroundMs();
        if (batteryEntry.getConsumedPower() == 0.0d && !(timeInForegroundMs == 0 && timeInBackgroundMs == 0)) {
            Log.w("DatabaseUtils", String.format("no consumed power but has running time for %s time=%d|%d", new Object[]{batteryEntry.getLabel(), Long.valueOf(timeInForegroundMs), Long.valueOf(timeInBackgroundMs)}));
        }
        if (batteryEntry.getConsumedPower() == 0.0d && timeInForegroundMs == 0 && timeInBackgroundMs == 0) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendBatteryEntryData$1(List list, BatteryUsageStats batteryUsageStats, int i, int i2, int i3, long j, long j2, BatteryEntry batteryEntry) {
        List list2 = list;
        list.add(ConvertUtils.convert(batteryEntry, batteryUsageStats, i, i2, i3, j, j2));
    }

    static Map<Long, Map<String, BatteryHistEntry>> getHistoryMap(Context context, Clock clock, boolean z) {
        if (!isContentProviderEnabled(context)) {
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        HashMap hashMap = new HashMap();
        Cursor query = context.getContentResolver().query(BATTERY_CONTENT_URI, (String[]) null, (Bundle) null, (CancellationSignal) null);
        if (query != null) {
            try {
                if (query.getCount() != 0) {
                    while (query.moveToNext()) {
                        BatteryHistEntry batteryHistEntry = new BatteryHistEntry(query);
                        long j = batteryHistEntry.mTimestamp;
                        String key = batteryHistEntry.getKey();
                        Map map = (Map) hashMap.get(Long.valueOf(j));
                        if (map == null) {
                            map = new HashMap();
                            hashMap.put(Long.valueOf(j), map);
                        }
                        map.put(key, batteryHistEntry);
                    }
                    query.close();
                    Log.d("DatabaseUtils", String.format("getHistoryMap() size=%d in %d/ms", new Object[]{Integer.valueOf(hashMap.size()), Long.valueOf(System.currentTimeMillis() - currentTimeMillis)}));
                    if (!hashMap.isEmpty() && z) {
                        long[] timestampSlots = getTimestampSlots(clock);
                        interpolateHistory(clock, timestampSlots, hashMap);
                        for (Long l : new ArrayList(hashMap.keySet())) {
                            if (!contains(timestampSlots, l.longValue())) {
                                hashMap.remove(l);
                            }
                        }
                        Log.d("DatabaseUtils", String.format("interpolateHistory() size=%d in %d/ms", new Object[]{Integer.valueOf(hashMap.size()), Long.valueOf(System.currentTimeMillis() - currentTimeMillis)}));
                    }
                    return hashMap;
                }
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        }
        if (query != null) {
            query.close();
        }
        return hashMap;
        throw th;
    }

    static void interpolateHistory(Clock clock, long[] jArr, Map<Long, Map<String, BatteryHistEntry>> map) {
        long[] jArr2 = jArr;
        Map<Long, Map<String, BatteryHistEntry>> map2 = map;
        ArrayList arrayList = new ArrayList(map.keySet());
        Collections.sort(arrayList);
        for (long j : jArr2) {
            long[] findNearestTimestamp = findNearestTimestamp(arrayList, j);
            long j2 = findNearestTimestamp[0];
            long j3 = findNearestTimestamp[1];
            int i = (j3 > 0 ? 1 : (j3 == 0 ? 0 : -1));
            if (i == 0) {
                Log.d("DatabaseUtils", String.format("%s job scheduler is delayed", new Object[]{ConvertUtils.utcToLocalTime(j)}));
                map2.put(Long.valueOf(j), new HashMap());
            } else if (j3 - j < 5000 && i != 0) {
                Log.d("DatabaseUtils", String.format("%s force align into the nearnest slot", new Object[]{ConvertUtils.utcToLocalTime(j)}));
                map2.put(Long.valueOf(j), map2.get(Long.valueOf(j3)));
            } else if (j2 == 0) {
                Log.d("DatabaseUtils", String.format("%s no lower timestamp slot data", new Object[]{ConvertUtils.utcToLocalTime(j)}));
                map2.put(Long.valueOf(j), new HashMap());
            } else {
                interpolateHistory(j, j2, j3, map);
            }
        }
    }

    static void interpolateHistory(long j, long j2, long j3, Map<Long, Map<String, BatteryHistEntry>> map) {
        BatteryHistEntry batteryHistEntry;
        Map<Long, Map<String, BatteryHistEntry>> map2 = map;
        Map map3 = map2.get(Long.valueOf(j2));
        Map map4 = map2.get(Long.valueOf(j3));
        BatteryHistEntry batteryHistEntry2 = (BatteryHistEntry) map4.values().stream().findFirst().get();
        if (j2 >= batteryHistEntry2.mTimestamp - batteryHistEntry2.mBootTimestamp) {
            Log.d("DatabaseUtils", String.format("%s apply interpolation arithmetic", new Object[]{ConvertUtils.utcToLocalTime(j)}));
            HashMap hashMap = new HashMap();
            double d = (double) (j3 - j2);
            double d2 = (double) (j - j2);
            for (String str : map4.keySet()) {
                BatteryHistEntry batteryHistEntry3 = (BatteryHistEntry) map3.get(str);
                BatteryHistEntry batteryHistEntry4 = (BatteryHistEntry) map4.get(str);
                if (batteryHistEntry3 != null) {
                    String str2 = str;
                    boolean z = batteryHistEntry3.mForegroundUsageTimeInMs > batteryHistEntry4.mForegroundUsageTimeInMs;
                    batteryHistEntry = batteryHistEntry3;
                    boolean z2 = batteryHistEntry3.mBackgroundUsageTimeInMs > batteryHistEntry4.mBackgroundUsageTimeInMs;
                    if (z || z2) {
                        hashMap.put(str2, batteryHistEntry4);
                        Log.w("DatabaseUtils", String.format("%s abnormal reset condition is found:\n%s", new Object[]{ConvertUtils.utcToLocalTime(j), batteryHistEntry4}));
                    } else {
                        str = str2;
                    }
                } else {
                    batteryHistEntry = batteryHistEntry3;
                }
                BatteryHistEntry batteryHistEntry5 = batteryHistEntry4;
                Map map5 = map3;
                double d3 = d2;
                double d4 = d;
                BatteryHistEntry interpolate = BatteryHistEntry.interpolate(j, j3, d2 / d, batteryHistEntry, batteryHistEntry5);
                hashMap.put(str, interpolate);
                if (batteryHistEntry == null) {
                    Log.w("DatabaseUtils", String.format("%s cannot find lower entry data\n%s\n%s", new Object[]{ConvertUtils.utcToLocalTime(j), batteryHistEntry5, interpolate}));
                    d = d4;
                    map3 = map5;
                    d2 = d3;
                } else {
                    d = d4;
                    map3 = map5;
                    d2 = d3;
                }
            }
            map2.put(Long.valueOf(j), hashMap);
        } else if (j3 - j < 600000) {
            Log.d("DatabaseUtils", String.format("%s force align into the nearnest slot", new Object[]{ConvertUtils.utcToLocalTime(j)}));
            map2.put(Long.valueOf(j), map4);
        } else {
            Log.d("DatabaseUtils", String.format("%s in the different booting section", new Object[]{ConvertUtils.utcToLocalTime(j)}));
            map2.put(Long.valueOf(j), new HashMap());
        }
    }

    static long[] getTimestampSlots(Clock clock) {
        long[] jArr = new long[25];
        long millis = (clock.millis() / 3600000) * 3600000;
        for (int i = 0; i < 25; i++) {
            jArr[i] = millis - (((long) i) * 3600000);
        }
        return jArr;
    }

    static long[] findNearestTimestamp(List<Long> list, long j) {
        long[] jArr = {Long.MIN_VALUE, Long.MAX_VALUE};
        for (Long longValue : list) {
            long longValue2 = longValue.longValue();
            int i = (longValue2 > j ? 1 : (longValue2 == j ? 0 : -1));
            if (i <= 0 && longValue2 > jArr[0]) {
                jArr[0] = longValue2;
            }
            if (i >= 0 && longValue2 < jArr[1]) {
                jArr[1] = longValue2;
            }
        }
        long j2 = 0;
        jArr[0] = jArr[0] == Long.MIN_VALUE ? 0 : jArr[0];
        if (jArr[1] != Long.MAX_VALUE) {
            j2 = jArr[1];
        }
        jArr[1] = j2;
        return jArr;
    }

    static boolean contains(long[] jArr, long j) {
        for (long j2 : jArr) {
            if (j2 == j) {
                return true;
            }
        }
        return false;
    }

    private static Intent getBatteryIntet(Context context) {
        return context.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }

    private static int getBatteryLevel(Intent intent) {
        int intExtra = intent.getIntExtra("level", -1);
        int intExtra2 = intent.getIntExtra("scale", 0);
        if (intExtra2 == 0) {
            return -1;
        }
        return Math.round((((float) intExtra) / ((float) intExtra2)) * 100.0f);
    }
}
