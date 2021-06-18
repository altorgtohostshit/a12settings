package com.android.settings.fuelgauge;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.settings.fuelgauge.BatteryEntry;
import com.android.settingslib.utils.StringUtil;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BatteryDiffEntry {
    public static final Comparator<BatteryDiffEntry> COMPARATOR = BatteryDiffEntry$$ExternalSyntheticLambda0.INSTANCE;
    static Locale sCurrentLocale;
    static final Map<String, BatteryEntry.NameAndIcon> sResourceCache = new HashMap();
    Drawable mAppIcon = null;
    int mAppIconId;
    String mAppLabel = null;
    public long mBackgroundUsageTimeInMs;
    public final BatteryHistEntry mBatteryHistEntry;
    public double mConsumePower;
    private Context mContext;
    private String mDefaultPackageName = null;
    public long mForegroundUsageTimeInMs;
    boolean mIsLoaded = false;
    private double mPercentOfTotal;
    private double mTotalConsumePower;
    private UserManager mUserManager;

    public BatteryDiffEntry(Context context, long j, long j2, double d, BatteryHistEntry batteryHistEntry) {
        this.mContext = context;
        this.mConsumePower = d;
        this.mForegroundUsageTimeInMs = j;
        this.mBackgroundUsageTimeInMs = j2;
        this.mBatteryHistEntry = batteryHistEntry;
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
    }

    public void setTotalConsumePower(double d) {
        this.mTotalConsumePower = d;
        double d2 = 0.0d;
        if (d != 0.0d) {
            d2 = (this.mConsumePower / d) * 100.0d;
        }
        this.mPercentOfTotal = d2;
    }

    public double getPercentOfTotal() {
        return this.mPercentOfTotal;
    }

    public BatteryDiffEntry clone() {
        return new BatteryDiffEntry(this.mContext, this.mForegroundUsageTimeInMs, this.mBackgroundUsageTimeInMs, this.mConsumePower, this.mBatteryHistEntry);
    }

    public String getAppLabel() {
        loadLabelAndIcon();
        String str = this.mAppLabel;
        if (str == null || str.length() == 0) {
            return this.mBatteryHistEntry.mAppLabel;
        }
        return this.mAppLabel;
    }

    public Drawable getAppIcon() {
        loadLabelAndIcon();
        return this.mAppIcon;
    }

    public int getAppIconId() {
        loadLabelAndIcon();
        return this.mAppIconId;
    }

    public String getPackageName() {
        String str = this.mDefaultPackageName;
        return str != null ? str : this.mBatteryHistEntry.mPackageName;
    }

    public boolean isSystemEntry() {
        BatteryHistEntry batteryHistEntry = this.mBatteryHistEntry;
        int i = batteryHistEntry.mConsumerType;
        if (i != 1) {
            return i == 2 || i == 3;
        }
        if (isSystemUid((int) batteryHistEntry.mUid) || this.mBatteryHistEntry.mIsHidden) {
            return true;
        }
        return false;
    }

    private void loadLabelAndIcon() {
        BatteryEntry.NameAndIcon nameAndIconFromPowerComponent;
        if (!this.mIsLoaded) {
            this.mIsLoaded = true;
            BatteryHistEntry batteryHistEntry = this.mBatteryHistEntry;
            int i = batteryHistEntry.mConsumerType;
            if (i == 1) {
                BatteryEntry.NameAndIcon cache = getCache();
                if (cache != null) {
                    this.mAppLabel = cache.name;
                    this.mAppIcon = cache.icon;
                    return;
                }
                loadNameAndIconForUid();
                if (this.mAppIcon == null) {
                    this.mAppIcon = this.mContext.getPackageManager().getDefaultActivityIcon();
                }
                Drawable badgeIconForUser = getBadgeIconForUser(this.mAppIcon);
                this.mAppIcon = badgeIconForUser;
                if (this.mAppLabel != null || badgeIconForUser != null) {
                    sResourceCache.put(this.mBatteryHistEntry.getKey(), new BatteryEntry.NameAndIcon(this.mAppLabel, this.mAppIcon, 0));
                }
            } else if (i == 2) {
                BatteryEntry.NameAndIcon nameAndIconFromUserId = BatteryEntry.getNameAndIconFromUserId(this.mContext, (int) batteryHistEntry.mUserId);
                if (nameAndIconFromUserId != null) {
                    this.mAppIcon = nameAndIconFromUserId.icon;
                    this.mAppLabel = nameAndIconFromUserId.name;
                }
            } else if (i == 3 && (nameAndIconFromPowerComponent = BatteryEntry.getNameAndIconFromPowerComponent(this.mContext, batteryHistEntry.mDrainType)) != null) {
                this.mAppLabel = nameAndIconFromPowerComponent.name;
                int i2 = nameAndIconFromPowerComponent.iconId;
                if (i2 != 0) {
                    this.mAppIconId = i2;
                    this.mAppIcon = this.mContext.getDrawable(i2);
                }
            }
        }
    }

    private BatteryEntry.NameAndIcon getCache() {
        Locale locale = Locale.getDefault();
        Locale locale2 = sCurrentLocale;
        if (locale2 != locale) {
            Log.d("BatteryDiffEntry", String.format("clearCache() locale is changed from %s to %s", new Object[]{locale2, locale}));
            sCurrentLocale = locale;
            clearCache();
        }
        return sResourceCache.get(this.mBatteryHistEntry.getKey());
    }

    private void loadNameAndIconForUid() {
        String str = this.mBatteryHistEntry.mPackageName;
        PackageManager packageManager = this.mContext.getPackageManager();
        if (!(str == null || str.length() == 0)) {
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
                if (applicationInfo != null) {
                    this.mAppLabel = packageManager.getApplicationLabel(applicationInfo).toString();
                    this.mAppIcon = packageManager.getApplicationIcon(applicationInfo);
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Log.e("BatteryDiffEntry", "failed to retrieve ApplicationInfo for: " + str);
                this.mAppLabel = str;
            }
        }
        if (this.mAppLabel == null || this.mAppIcon == null) {
            int i = (int) this.mBatteryHistEntry.mUid;
            String[] packagesForUid = packageManager.getPackagesForUid(i);
            if (packagesForUid == null || packagesForUid.length == 0) {
                BatteryEntry.NameAndIcon nameAndIconFromUid = BatteryEntry.getNameAndIconFromUid(this.mContext, this.mAppLabel, i);
                this.mAppLabel = nameAndIconFromUid.name;
                this.mAppIcon = nameAndIconFromUid.icon;
            }
            BatteryEntry.NameAndIcon loadNameAndIcon = BatteryEntry.loadNameAndIcon(this.mContext, i, (Handler) null, (BatteryEntry) null, str, this.mAppLabel, this.mAppIcon);
            BatteryEntry.clearUidCache();
            if (loadNameAndIcon != null) {
                this.mAppLabel = loadNameAndIcon.name;
                this.mAppIcon = loadNameAndIcon.icon;
                String str2 = loadNameAndIcon.packageName;
                this.mDefaultPackageName = str2;
                if (str2 != null && !str2.equals(str2)) {
                    Log.w("BatteryDiffEntry", String.format("found different package: %s | %s", new Object[]{this.mDefaultPackageName, loadNameAndIcon.packageName}));
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BatteryDiffEntry{");
        sb.append("\n\tname=" + getAppLabel());
        sb.append(String.format("\n\tconsume=%.2f%% %f/%f", new Object[]{Double.valueOf(this.mPercentOfTotal), Double.valueOf(this.mConsumePower), Double.valueOf(this.mTotalConsumePower)}));
        sb.append(String.format("\n\tforeground:%s background:%s", new Object[]{StringUtil.formatElapsedTime(this.mContext, (double) this.mForegroundUsageTimeInMs, true, false), StringUtil.formatElapsedTime(this.mContext, (double) this.mBackgroundUsageTimeInMs, true, false)}));
        sb.append(String.format("\n\tpackage:%s|%s uid:%d userId:%d", new Object[]{this.mBatteryHistEntry.mPackageName, getPackageName(), Long.valueOf(this.mBatteryHistEntry.mUid), Long.valueOf(this.mBatteryHistEntry.mUserId)}));
        return sb.toString();
    }

    static void clearCache() {
        sResourceCache.clear();
    }

    private Drawable getBadgeIconForUser(Drawable drawable) {
        return this.mUserManager.getBadgedIconForUser(drawable, new UserHandle(UserHandle.getUserId((int) this.mBatteryHistEntry.mUid)));
    }

    private static boolean isSystemUid(int i) {
        int appId = UserHandle.getAppId(i);
        return appId >= 1000 && appId < 10000;
    }
}
