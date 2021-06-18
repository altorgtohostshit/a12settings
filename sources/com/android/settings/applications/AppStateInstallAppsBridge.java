package com.android.settings.applications;

import android.app.AppGlobals;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.ArrayUtils;
import com.android.settings.applications.AppStateBaseBridge;
import com.android.settingslib.applications.ApplicationsState;
import java.util.ArrayList;

public class AppStateInstallAppsBridge extends AppStateBaseBridge {
    public static final ApplicationsState.AppFilter FILTER_APP_SOURCES = new ApplicationsState.AppFilter() {
        public void init() {
        }

        public boolean filterApp(ApplicationsState.AppEntry appEntry) {
            Object obj = appEntry.extraInfo;
            if (obj == null || !(obj instanceof InstallAppsState)) {
                return false;
            }
            return ((InstallAppsState) obj).isPotentialAppSource();
        }
    };
    private static final String TAG = "AppStateInstallAppsBridge";
    private final AppOpsManager mAppOpsManager;
    private final IPackageManager mIpm = AppGlobals.getPackageManager();

    public AppStateInstallAppsBridge(Context context, ApplicationsState applicationsState, AppStateBaseBridge.Callback callback) {
        super(applicationsState, callback);
        this.mAppOpsManager = (AppOpsManager) context.getSystemService("appops");
    }

    /* access modifiers changed from: protected */
    public void updateExtraInfo(ApplicationsState.AppEntry appEntry, String str, int i) {
        appEntry.extraInfo = createInstallAppsStateFor(str, i);
    }

    /* access modifiers changed from: protected */
    public void loadAllExtraInfo() {
        ArrayList<ApplicationsState.AppEntry> allApps = this.mAppSession.getAllApps();
        for (int i = 0; i < allApps.size(); i++) {
            ApplicationsState.AppEntry appEntry = allApps.get(i);
            ApplicationInfo applicationInfo = appEntry.info;
            updateExtraInfo(appEntry, applicationInfo.packageName, applicationInfo.uid);
        }
    }

    private boolean hasRequestedAppOpPermission(String str, String str2) {
        try {
            return ArrayUtils.contains(this.mIpm.getAppOpPermissionPackages(str), str2);
        } catch (RemoteException unused) {
            Log.e(TAG, "PackageManager dead. Cannot get permission info");
            return false;
        }
    }

    private int getAppOpMode(int i, int i2, String str) {
        return this.mAppOpsManager.checkOpNoThrow(i, i2, str);
    }

    public InstallAppsState createInstallAppsStateFor(String str, int i) {
        InstallAppsState installAppsState = new InstallAppsState();
        installAppsState.permissionRequested = hasRequestedAppOpPermission("android.permission.REQUEST_INSTALL_PACKAGES", str);
        installAppsState.appOpMode = getAppOpMode(66, i, str);
        return installAppsState;
    }

    public static class InstallAppsState {
        int appOpMode = 3;
        boolean permissionRequested;

        public boolean canInstallApps() {
            return this.appOpMode == 0;
        }

        public boolean isPotentialAppSource() {
            return this.appOpMode != 3 || this.permissionRequested;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[permissionRequested: " + this.permissionRequested);
            sb.append(", appOpMode: " + this.appOpMode);
            sb.append("]");
            return sb.toString();
        }
    }
}
