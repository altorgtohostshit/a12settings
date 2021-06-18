package com.android.settingslib.location;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.PermissionChecker;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.IconDrawableFactory;
import android.util.Log;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class RecentLocationAccesses {
    static final String ANDROID_SYSTEM_PACKAGE_NAME = "android";
    static final int[] LOCATION_OPS = {1, 0};
    private static final String TAG = "RecentLocationAccesses";
    private final Clock mClock;
    private final Context mContext;
    private final IconDrawableFactory mDrawableFactory;
    private final PackageManager mPackageManager;

    public RecentLocationAccesses(Context context) {
        this(context, Clock.systemDefaultZone());
    }

    RecentLocationAccesses(Context context, Clock clock) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mDrawableFactory = IconDrawableFactory.newInstance(context);
        this.mClock = clock;
    }

    /* access modifiers changed from: package-private */
    public List<Access> getAppList(boolean z) {
        List<UserHandle> list;
        PackageManager packageManager;
        List list2;
        int i;
        boolean z2;
        Access accessFromOps;
        PackageManager packageManager2 = this.mContext.getPackageManager();
        List packagesForOps = ((AppOpsManager) this.mContext.getSystemService("appops")).getPackagesForOps(LOCATION_OPS);
        int size = packagesForOps != null ? packagesForOps.size() : 0;
        ArrayList arrayList = new ArrayList(size);
        long millis = this.mClock.millis();
        List<UserHandle> userProfiles = ((UserManager) this.mContext.getSystemService("user")).getUserProfiles();
        int i2 = 0;
        while (i2 < size) {
            AppOpsManager.PackageOps packageOps = (AppOpsManager.PackageOps) packagesForOps.get(i2);
            String packageName = packageOps.getPackageName();
            int uid = packageOps.getUid();
            UserHandle userHandleForUid = UserHandle.getUserHandleForUid(uid);
            if (!userProfiles.contains(userHandleForUid)) {
                packageManager = packageManager2;
                list2 = packagesForOps;
                i = size;
                list = userProfiles;
            } else {
                if (!z) {
                    int[] iArr = LOCATION_OPS;
                    int length = iArr.length;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= length) {
                            break;
                        }
                        list2 = packagesForOps;
                        String opToPermission = AppOpsManager.opToPermission(iArr[i3]);
                        i = size;
                        int permissionFlags = packageManager2.getPermissionFlags(opToPermission, packageName, userHandleForUid);
                        packageManager = packageManager2;
                        list = userProfiles;
                        if (PermissionChecker.checkPermissionForPreflight(this.mContext, opToPermission, -1, uid, packageName) == 0) {
                            if ((permissionFlags & 256) == 0) {
                                break;
                            }
                            i3++;
                            size = i;
                            packagesForOps = list2;
                            packageManager2 = packageManager;
                            userProfiles = list;
                        } else if ((permissionFlags & 512) == 0) {
                            break;
                        } else {
                            i3++;
                            size = i;
                            packagesForOps = list2;
                            packageManager2 = packageManager;
                            userProfiles = list;
                        }
                    }
                    z2 = false;
                    if (z2 && (accessFromOps = getAccessFromOps(millis, packageOps)) != null) {
                        arrayList.add(accessFromOps);
                    }
                }
                packageManager = packageManager2;
                list2 = packagesForOps;
                i = size;
                list = userProfiles;
                z2 = true;
                arrayList.add(accessFromOps);
            }
            i2++;
            size = i;
            packagesForOps = list2;
            packageManager2 = packageManager;
            userProfiles = list;
        }
        return arrayList;
    }

    public List<Access> getAppListSorted(boolean z) {
        List<Access> appList = getAppList(z);
        Collections.sort(appList, Collections.reverseOrder(new Comparator<Access>() {
            public int compare(Access access, Access access2) {
                return Long.compare(access.accessFinishTime, access2.accessFinishTime);
            }
        }));
        return appList;
    }

    private Access getAccessFromOps(long j, AppOpsManager.PackageOps packageOps) {
        long j2;
        String packageName = packageOps.getPackageName();
        long j3 = j - 86400000;
        Iterator it = packageOps.getOps().iterator();
        long j4 = 0;
        loop0:
        while (true) {
            j2 = j4;
            while (true) {
                if (!it.hasNext()) {
                    break loop0;
                }
                j4 = ((AppOpsManager.OpEntry) it.next()).getLastAccessTime(13);
                if (j4 > j2) {
                }
            }
        }
        if (j2 < j3) {
            return null;
        }
        int userId = UserHandle.getUserId(packageOps.getUid());
        try {
            ApplicationInfo applicationInfoAsUser = this.mPackageManager.getApplicationInfoAsUser(packageName, 128, userId);
            if (applicationInfoAsUser == null) {
                String str = TAG;
                Log.w(str, "Null application info retrieved for package " + packageName + ", userId " + userId);
                return null;
            }
            UserHandle userHandle = new UserHandle(userId);
            Drawable badgedIcon = this.mDrawableFactory.getBadgedIcon(applicationInfoAsUser, userId);
            CharSequence applicationLabel = this.mPackageManager.getApplicationLabel(applicationInfoAsUser);
            CharSequence userBadgedLabel = this.mPackageManager.getUserBadgedLabel(applicationLabel, userHandle);
            return new Access(packageName, userHandle, badgedIcon, applicationLabel, applicationLabel.toString().contentEquals(userBadgedLabel) ? null : userBadgedLabel, j2);
        } catch (PackageManager.NameNotFoundException unused) {
            String str2 = TAG;
            Log.w(str2, "package name not found for " + packageName + ", userId " + userId);
            return null;
        }
    }

    public static class Access {
        public final long accessFinishTime;
        public final CharSequence contentDescription;
        public final Drawable icon;
        public final CharSequence label;
        public final String packageName;
        public final UserHandle userHandle;

        public Access(String str, UserHandle userHandle2, Drawable drawable, CharSequence charSequence, CharSequence charSequence2, long j) {
            this.packageName = str;
            this.userHandle = userHandle2;
            this.icon = drawable;
            this.label = charSequence;
            this.contentDescription = charSequence2;
            this.accessFinishTime = j;
        }
    }
}
