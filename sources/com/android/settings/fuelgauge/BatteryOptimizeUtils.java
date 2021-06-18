package com.android.settings.fuelgauge;

import android.app.AppOpsManager;
import android.content.Context;
import android.util.Log;
import com.android.settingslib.fuelgauge.PowerAllowlistBackend;

public class BatteryOptimizeUtils {
    private boolean mAllowListed;
    AppOpsManager mAppOpsManager;
    BatteryUtils mBatteryUtils;
    private int mMode;
    private final String mPackageName;
    PowerAllowlistBackend mPowerAllowListBackend;
    private final int mUid;

    public enum AppUsageState {
        UNKNOWN,
        RESTRICTED,
        UNRESTRICTED,
        OPTIMIZED
    }

    public BatteryOptimizeUtils(Context context, int i, String str) {
        this.mUid = i;
        this.mPackageName = str;
        this.mAppOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mBatteryUtils = BatteryUtils.getInstance(context);
        this.mPowerAllowListBackend = PowerAllowlistBackend.getInstance(context);
        this.mMode = this.mAppOpsManager.checkOpNoThrow(70, i, str);
        this.mAllowListed = this.mPowerAllowListBackend.isAllowlisted(str);
    }

    public AppUsageState getAppUsageState() {
        refreshState();
        boolean z = this.mAllowListed;
        if (!z && this.mMode == 1) {
            return AppUsageState.RESTRICTED;
        }
        if (z && this.mMode == 0) {
            return AppUsageState.UNRESTRICTED;
        }
        if (!z && this.mMode == 0) {
            return AppUsageState.OPTIMIZED;
        }
        Log.d("BatteryOptimizeUtils", "get unknown app usage state.");
        return AppUsageState.UNKNOWN;
    }

    /* renamed from: com.android.settings.fuelgauge.BatteryOptimizeUtils$1 */
    static /* synthetic */ class C09241 {

        /* renamed from: $SwitchMap$com$android$settings$fuelgauge$BatteryOptimizeUtils$AppUsageState */
        static final /* synthetic */ int[] f65x8b0ce43e;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                com.android.settings.fuelgauge.BatteryOptimizeUtils$AppUsageState[] r0 = com.android.settings.fuelgauge.BatteryOptimizeUtils.AppUsageState.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f65x8b0ce43e = r0
                com.android.settings.fuelgauge.BatteryOptimizeUtils$AppUsageState r1 = com.android.settings.fuelgauge.BatteryOptimizeUtils.AppUsageState.RESTRICTED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f65x8b0ce43e     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.settings.fuelgauge.BatteryOptimizeUtils$AppUsageState r1 = com.android.settings.fuelgauge.BatteryOptimizeUtils.AppUsageState.UNRESTRICTED     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f65x8b0ce43e     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.settings.fuelgauge.BatteryOptimizeUtils$AppUsageState r1 = com.android.settings.fuelgauge.BatteryOptimizeUtils.AppUsageState.OPTIMIZED     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.fuelgauge.BatteryOptimizeUtils.C09241.<clinit>():void");
        }
    }

    public void setAppUsageState(AppUsageState appUsageState) {
        int i = C09241.f65x8b0ce43e[appUsageState.ordinal()];
        if (i == 1) {
            this.mBatteryUtils.setForceAppStandby(this.mUid, this.mPackageName, 1);
            this.mPowerAllowListBackend.removeApp(this.mPackageName);
        } else if (i == 2) {
            this.mBatteryUtils.setForceAppStandby(this.mUid, this.mPackageName, 0);
            this.mPowerAllowListBackend.addApp(this.mPackageName);
        } else if (i != 3) {
            Log.d("BatteryOptimizeUtils", "set unknown app usage state.");
        } else {
            this.mBatteryUtils.setForceAppStandby(this.mUid, this.mPackageName, 0);
            this.mPowerAllowListBackend.removeApp(this.mPackageName);
        }
    }

    public boolean isValidPackageName() {
        return this.mBatteryUtils.getPackageUid(this.mPackageName) != -1;
    }

    public boolean isSystemOrDefaultApp() {
        this.mPowerAllowListBackend.refreshList();
        return this.mPowerAllowListBackend.isSysAllowlisted(this.mPackageName) || this.mPowerAllowListBackend.isDefaultActiveApp(this.mPackageName);
    }

    private void refreshState() {
        this.mPowerAllowListBackend.refreshList();
        this.mAllowListed = this.mPowerAllowListBackend.isAllowlisted(this.mPackageName);
        this.mMode = this.mAppOpsManager.checkOpNoThrow(70, this.mUid, this.mPackageName);
        Log.d("BatteryOptimizeUtils", String.format("refresh %s state, allowlisted = %s, mode = %d", new Object[]{this.mPackageName, Boolean.valueOf(this.mAllowListed), Integer.valueOf(this.mMode)}));
    }
}
