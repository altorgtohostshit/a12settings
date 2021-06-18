package com.android.settings.fuelgauge.batterytip.tips;

import android.app.AppOpsManager;
import android.content.Context;
import com.android.settings.fuelgauge.batterytip.AppInfo;
import java.util.function.Predicate;

public class AppRestrictionPredicate implements Predicate<AppInfo> {
    private static AppRestrictionPredicate sInstance;
    private AppOpsManager mAppOpsManager;

    public static AppRestrictionPredicate getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppRestrictionPredicate(context.getApplicationContext());
        }
        return sInstance;
    }

    private AppRestrictionPredicate(Context context) {
        this.mAppOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
    }

    public boolean test(AppInfo appInfo) {
        return this.mAppOpsManager.checkOpNoThrow(70, appInfo.uid, appInfo.packageName) == 1;
    }
}
