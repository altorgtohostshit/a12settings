package com.android.settings.fuelgauge;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import java.time.Duration;

public class BatteryHistEntry {
    public final String mAppLabel;
    public final long mBackgroundUsageTimeInMs;
    public final int mBatteryHealth;
    public final int mBatteryLevel;
    public final int mBatteryStatus;
    public final long mBootTimestamp;
    public final double mConsumePower;
    public final int mConsumerType;
    public final int mDrainType;
    public final long mForegroundUsageTimeInMs;
    public final boolean mIsHidden;
    private boolean mIsValidEntry = true;
    private String mKey = null;
    public final String mPackageName;
    public final double mPercentOfTotal;
    public final long mTimestamp;
    public final double mTotalPower;
    public final long mUid;
    public final long mUserId;
    public final String mZoneId;

    private static double interpolate(double d, double d2, double d3) {
        return d + (d3 * (d2 - d));
    }

    public BatteryHistEntry(ContentValues contentValues) {
        this.mUid = getLong(contentValues, "uid");
        this.mUserId = getLong(contentValues, "userId");
        this.mAppLabel = getString(contentValues, "appLabel");
        this.mPackageName = getString(contentValues, "packageName");
        this.mIsHidden = getBoolean(contentValues, "isHidden");
        this.mBootTimestamp = getLong(contentValues, "bootTimestamp");
        this.mTimestamp = getLong(contentValues, "timestamp");
        this.mZoneId = getString(contentValues, "zoneId");
        this.mTotalPower = getDouble(contentValues, "totalPower");
        this.mConsumePower = getDouble(contentValues, "consumePower");
        this.mPercentOfTotal = getDouble(contentValues, "percentOfTotal");
        this.mForegroundUsageTimeInMs = getLong(contentValues, "foregroundUsageTimeInMs");
        this.mBackgroundUsageTimeInMs = getLong(contentValues, "backgroundUsageTimeInMs");
        this.mDrainType = getInteger(contentValues, "drainType");
        this.mConsumerType = getInteger(contentValues, "consumerType");
        this.mBatteryLevel = getInteger(contentValues, "batteryLevel");
        this.mBatteryStatus = getInteger(contentValues, "batteryStatus");
        this.mBatteryHealth = getInteger(contentValues, "batteryHealth");
    }

    public BatteryHistEntry(Cursor cursor) {
        this.mUid = getLong(cursor, "uid");
        this.mUserId = getLong(cursor, "userId");
        this.mAppLabel = getString(cursor, "appLabel");
        this.mPackageName = getString(cursor, "packageName");
        this.mIsHidden = getBoolean(cursor, "isHidden");
        this.mBootTimestamp = getLong(cursor, "bootTimestamp");
        this.mTimestamp = getLong(cursor, "timestamp");
        this.mZoneId = getString(cursor, "zoneId");
        this.mTotalPower = getDouble(cursor, "totalPower");
        this.mConsumePower = getDouble(cursor, "consumePower");
        this.mPercentOfTotal = getDouble(cursor, "percentOfTotal");
        this.mForegroundUsageTimeInMs = getLong(cursor, "foregroundUsageTimeInMs");
        this.mBackgroundUsageTimeInMs = getLong(cursor, "backgroundUsageTimeInMs");
        this.mDrainType = getInteger(cursor, "drainType");
        this.mConsumerType = getInteger(cursor, "consumerType");
        this.mBatteryLevel = getInteger(cursor, "batteryLevel");
        this.mBatteryStatus = getInteger(cursor, "batteryStatus");
        this.mBatteryHealth = getInteger(cursor, "batteryHealth");
    }

    private BatteryHistEntry(BatteryHistEntry batteryHistEntry, long j, long j2, double d, double d2, long j3, long j4, int i) {
        BatteryHistEntry batteryHistEntry2 = batteryHistEntry;
        this.mUid = batteryHistEntry2.mUid;
        this.mUserId = batteryHistEntry2.mUserId;
        this.mAppLabel = batteryHistEntry2.mAppLabel;
        this.mPackageName = batteryHistEntry2.mPackageName;
        this.mIsHidden = batteryHistEntry2.mIsHidden;
        this.mBootTimestamp = j;
        this.mTimestamp = j2;
        this.mZoneId = batteryHistEntry2.mZoneId;
        this.mTotalPower = d;
        this.mConsumePower = d2;
        this.mPercentOfTotal = batteryHistEntry2.mPercentOfTotal;
        this.mForegroundUsageTimeInMs = j3;
        this.mBackgroundUsageTimeInMs = j4;
        this.mDrainType = batteryHistEntry2.mDrainType;
        this.mConsumerType = batteryHistEntry2.mConsumerType;
        this.mBatteryLevel = i;
        this.mBatteryStatus = batteryHistEntry2.mBatteryStatus;
        this.mBatteryHealth = batteryHistEntry2.mBatteryHealth;
    }

    public boolean isUserEntry() {
        return this.mConsumerType == 2;
    }

    public boolean isAppEntry() {
        return this.mConsumerType == 1;
    }

    public String getKey() {
        if (this.mKey == null) {
            int i = this.mConsumerType;
            if (i == 1) {
                this.mKey = Long.toString(this.mUid);
            } else if (i == 2) {
                this.mKey = "U|" + this.mUserId;
            } else if (i == 3) {
                this.mKey = "S|" + this.mDrainType;
            }
        }
        return this.mKey;
    }

    public String toString() {
        String utcToLocalTime = ConvertUtils.utcToLocalTime(this.mTimestamp);
        return "\nBatteryHistEntry{" + String.format("\n\tpackage=%s|label=%s|uid=%d|userId=%d|isHidden=%b", new Object[]{this.mPackageName, this.mAppLabel, Long.valueOf(this.mUid), Long.valueOf(this.mUserId), Boolean.valueOf(this.mIsHidden)}) + String.format("\n\ttimestamp=%s|zoneId=%s|bootTimestamp=%d", new Object[]{utcToLocalTime, this.mZoneId, Long.valueOf(Duration.ofMillis(this.mBootTimestamp).getSeconds())}) + String.format("\n\tusage=%f|total=%f|consume=%f|elapsedTime=%d|%d", new Object[]{Double.valueOf(this.mPercentOfTotal), Double.valueOf(this.mTotalPower), Double.valueOf(this.mConsumePower), Long.valueOf(Duration.ofMillis(this.mForegroundUsageTimeInMs).getSeconds()), Long.valueOf(Duration.ofMillis(this.mBackgroundUsageTimeInMs).getSeconds())}) + String.format("\n\tdrainType=%d|consumerType=%d", new Object[]{Integer.valueOf(this.mDrainType), Integer.valueOf(this.mConsumerType)}) + String.format("\n\tbattery=%d|status=%d|health=%d\n}", new Object[]{Integer.valueOf(this.mBatteryLevel), Integer.valueOf(this.mBatteryStatus), Integer.valueOf(this.mBatteryHealth)});
    }

    private int getInteger(ContentValues contentValues, String str) {
        if (contentValues != null && contentValues.containsKey(str)) {
            return contentValues.getAsInteger(str).intValue();
        }
        this.mIsValidEntry = false;
        return 0;
    }

    private int getInteger(Cursor cursor, String str) {
        int columnIndex = cursor.getColumnIndex(str);
        if (columnIndex >= 0) {
            return cursor.getInt(columnIndex);
        }
        this.mIsValidEntry = false;
        return 0;
    }

    private long getLong(ContentValues contentValues, String str) {
        if (contentValues != null && contentValues.containsKey(str)) {
            return contentValues.getAsLong(str).longValue();
        }
        this.mIsValidEntry = false;
        return 0;
    }

    private long getLong(Cursor cursor, String str) {
        int columnIndex = cursor.getColumnIndex(str);
        if (columnIndex >= 0) {
            return cursor.getLong(columnIndex);
        }
        this.mIsValidEntry = false;
        return 0;
    }

    private double getDouble(ContentValues contentValues, String str) {
        if (contentValues != null && contentValues.containsKey(str)) {
            return contentValues.getAsDouble(str).doubleValue();
        }
        this.mIsValidEntry = false;
        return 0.0d;
    }

    private double getDouble(Cursor cursor, String str) {
        int columnIndex = cursor.getColumnIndex(str);
        if (columnIndex >= 0) {
            return cursor.getDouble(columnIndex);
        }
        this.mIsValidEntry = false;
        return 0.0d;
    }

    private String getString(ContentValues contentValues, String str) {
        if (contentValues != null && contentValues.containsKey(str)) {
            return contentValues.getAsString(str);
        }
        this.mIsValidEntry = false;
        return null;
    }

    private String getString(Cursor cursor, String str) {
        int columnIndex = cursor.getColumnIndex(str);
        if (columnIndex >= 0) {
            return cursor.getString(columnIndex);
        }
        this.mIsValidEntry = false;
        return null;
    }

    private boolean getBoolean(ContentValues contentValues, String str) {
        if (contentValues != null && contentValues.containsKey(str)) {
            return contentValues.getAsBoolean(str).booleanValue();
        }
        this.mIsValidEntry = false;
        return false;
    }

    private boolean getBoolean(Cursor cursor, String str) {
        int columnIndex = cursor.getColumnIndex(str);
        if (columnIndex < 0) {
            this.mIsValidEntry = false;
            return false;
        } else if (cursor.getInt(columnIndex) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static BatteryHistEntry interpolate(long j, long j2, double d, BatteryHistEntry batteryHistEntry, BatteryHistEntry batteryHistEntry2) {
        double d2;
        double d3;
        double d4;
        BatteryHistEntry batteryHistEntry3 = batteryHistEntry;
        BatteryHistEntry batteryHistEntry4 = batteryHistEntry2;
        double d5 = 0.0d;
        double interpolate = interpolate(batteryHistEntry3 == null ? 0.0d : batteryHistEntry3.mTotalPower, batteryHistEntry4.mTotalPower, d);
        if (batteryHistEntry3 == null) {
            d2 = 0.0d;
        } else {
            d2 = batteryHistEntry3.mConsumePower;
        }
        double interpolate2 = interpolate(d2, batteryHistEntry4.mConsumePower, d);
        if (batteryHistEntry3 == null) {
            d3 = 0.0d;
        } else {
            d3 = (double) batteryHistEntry3.mForegroundUsageTimeInMs;
        }
        double interpolate3 = interpolate(d3, (double) batteryHistEntry4.mForegroundUsageTimeInMs, d);
        if (batteryHistEntry3 != null) {
            d5 = (double) batteryHistEntry3.mBackgroundUsageTimeInMs;
        }
        double interpolate4 = interpolate(d5, (double) batteryHistEntry4.mBackgroundUsageTimeInMs, d);
        if (batteryHistEntry4.mConsumePower < interpolate2 || ((double) batteryHistEntry4.mForegroundUsageTimeInMs) < interpolate3 || ((double) batteryHistEntry4.mBackgroundUsageTimeInMs) < interpolate4) {
            Log.w("BatteryHistEntry", String.format("abnormal interpolation:\nupper:%s\nlower:%s", new Object[]{batteryHistEntry4, batteryHistEntry3}));
        }
        if (batteryHistEntry3 == null) {
            d4 = (double) batteryHistEntry4.mBatteryLevel;
        } else {
            d4 = interpolate((double) batteryHistEntry3.mBatteryLevel, (double) batteryHistEntry4.mBatteryLevel, d);
        }
        return new BatteryHistEntry(batteryHistEntry2, batteryHistEntry4.mBootTimestamp - (j2 - j), j, interpolate, interpolate2, Math.round(interpolate3), Math.round(interpolate4), (int) Math.round(d4));
    }
}
