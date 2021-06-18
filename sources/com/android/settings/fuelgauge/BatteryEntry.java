package com.android.settings.fuelgauge;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.BatteryConsumer;
import android.os.Handler;
import android.os.UidBatteryConsumer;
import android.os.UserBatteryConsumer;
import android.os.UserManager;
import android.util.DebugUtils;
import android.util.Log;
import com.android.settings.R;
import com.android.settingslib.Utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class BatteryEntry {
    public static final Comparator<BatteryEntry> COMPARATOR = BatteryEntry$$ExternalSyntheticLambda0.INSTANCE;
    private static NameAndIconLoader mRequestThread;
    static Locale sCurrentLocale = null;
    static Handler sHandler;
    static final ArrayList<BatteryEntry> sRequestQueue = new ArrayList<>();
    static final HashMap<String, UidToDetail> sUidCache = new HashMap<>();
    public Drawable icon;
    public int iconId;
    private final BatteryConsumer mBatteryConsumer;
    private double mConsumedPower;
    private final int mConsumerType;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public String mDefaultPackageName;
    private final boolean mIsHidden;
    private final int mPowerComponentId;
    private long mUsageDurationMs;
    public String name;
    public double percent;

    public static final class NameAndIcon {
        public final Drawable icon;
        public final int iconId;
        public final String name;
        public final String packageName;

        public NameAndIcon(String str, Drawable drawable, int i) {
            this(str, (String) null, drawable, i);
        }

        public NameAndIcon(String str, String str2, Drawable drawable, int i) {
            this.name = str;
            this.icon = drawable;
            this.iconId = i;
            this.packageName = str2;
        }
    }

    private static class NameAndIconLoader extends Thread {
        private boolean mAbort = false;

        public NameAndIconLoader() {
            super("BatteryUsage Icon Loader");
        }

        public void abort() {
            this.mAbort = true;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0016, code lost:
            r0 = com.android.settings.fuelgauge.BatteryEntry.loadNameAndIcon(com.android.settings.fuelgauge.BatteryEntry.access$000(r1), r1.getUid(), com.android.settings.fuelgauge.BatteryEntry.sHandler, r1, com.android.settings.fuelgauge.BatteryEntry.access$100(r1), r1.name, r1.icon);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x002d, code lost:
            if (r0 == null) goto L_0x0000;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x002f, code lost:
            r1.icon = r0.icon;
            r1.name = r0.name;
            com.android.settings.fuelgauge.BatteryEntry.access$102(r1, r0.packageName);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r9 = this;
            L_0x0000:
                java.util.ArrayList<com.android.settings.fuelgauge.BatteryEntry> r0 = com.android.settings.fuelgauge.BatteryEntry.sRequestQueue
                monitor-enter(r0)
                boolean r1 = r0.isEmpty()     // Catch:{ all -> 0x0047 }
                if (r1 != 0) goto L_0x003d
                boolean r1 = r9.mAbort     // Catch:{ all -> 0x0047 }
                if (r1 == 0) goto L_0x000e
                goto L_0x003d
            L_0x000e:
                r1 = 0
                java.lang.Object r1 = r0.remove(r1)     // Catch:{ all -> 0x0047 }
                com.android.settings.fuelgauge.BatteryEntry r1 = (com.android.settings.fuelgauge.BatteryEntry) r1     // Catch:{ all -> 0x0047 }
                monitor-exit(r0)     // Catch:{ all -> 0x0047 }
                android.content.Context r2 = r1.mContext
                int r3 = r1.getUid()
                android.os.Handler r4 = com.android.settings.fuelgauge.BatteryEntry.sHandler
                java.lang.String r6 = r1.mDefaultPackageName
                java.lang.String r7 = r1.name
                android.graphics.drawable.Drawable r8 = r1.icon
                r5 = r1
                com.android.settings.fuelgauge.BatteryEntry$NameAndIcon r0 = com.android.settings.fuelgauge.BatteryEntry.loadNameAndIcon(r2, r3, r4, r5, r6, r7, r8)
                if (r0 == 0) goto L_0x0000
                android.graphics.drawable.Drawable r2 = r0.icon
                r1.icon = r2
                java.lang.String r2 = r0.name
                r1.name = r2
                java.lang.String r0 = r0.packageName
                java.lang.String unused = r1.mDefaultPackageName = r0
                goto L_0x0000
            L_0x003d:
                android.os.Handler r9 = com.android.settings.fuelgauge.BatteryEntry.sHandler     // Catch:{ all -> 0x0047 }
                if (r9 == 0) goto L_0x0045
                r1 = 2
                r9.sendEmptyMessage(r1)     // Catch:{ all -> 0x0047 }
            L_0x0045:
                monitor-exit(r0)     // Catch:{ all -> 0x0047 }
                return
            L_0x0047:
                r9 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0047 }
                throw r9
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.fuelgauge.BatteryEntry.NameAndIconLoader.run():void");
        }
    }

    public static void startRequestQueue() {
        if (sHandler != null) {
            ArrayList<BatteryEntry> arrayList = sRequestQueue;
            synchronized (arrayList) {
                if (!arrayList.isEmpty()) {
                    NameAndIconLoader nameAndIconLoader = mRequestThread;
                    if (nameAndIconLoader != null) {
                        nameAndIconLoader.abort();
                    }
                    NameAndIconLoader nameAndIconLoader2 = new NameAndIconLoader();
                    mRequestThread = nameAndIconLoader2;
                    nameAndIconLoader2.setPriority(1);
                    mRequestThread.start();
                    arrayList.notify();
                }
            }
        }
    }

    public static void stopRequestQueue() {
        ArrayList<BatteryEntry> arrayList = sRequestQueue;
        synchronized (arrayList) {
            NameAndIconLoader nameAndIconLoader = mRequestThread;
            if (nameAndIconLoader != null) {
                nameAndIconLoader.abort();
                mRequestThread = null;
                arrayList.clear();
                sHandler = null;
            }
        }
    }

    public static void clearUidCache() {
        sUidCache.clear();
    }

    static class UidToDetail {
        Drawable icon;
        String name;
        String packageName;

        UidToDetail() {
        }
    }

    public BatteryEntry(Context context, Handler handler, UserManager userManager, BatteryConsumer batteryConsumer, boolean z, String[] strArr, String str) {
        this(context, handler, userManager, batteryConsumer, z, strArr, str, true);
    }

    public BatteryEntry(Context context, Handler handler, UserManager userManager, BatteryConsumer batteryConsumer, boolean z, String[] strArr, String str, boolean z2) {
        sHandler = handler;
        this.mContext = context;
        this.mBatteryConsumer = batteryConsumer;
        this.mIsHidden = z;
        this.mDefaultPackageName = str;
        this.mPowerComponentId = -1;
        if (batteryConsumer instanceof UidBatteryConsumer) {
            this.mConsumerType = 1;
            this.mConsumedPower = batteryConsumer.getConsumedPower();
            UidBatteryConsumer uidBatteryConsumer = (UidBatteryConsumer) batteryConsumer;
            int uid = uidBatteryConsumer.getUid();
            if (this.mDefaultPackageName == null) {
                if (strArr == null || strArr.length != 1) {
                    this.mDefaultPackageName = uidBatteryConsumer.getPackageWithHighestDrain();
                } else {
                    this.mDefaultPackageName = strArr[0];
                }
            }
            if (this.mDefaultPackageName != null) {
                PackageManager packageManager = context.getPackageManager();
                try {
                    this.name = packageManager.getApplicationLabel(packageManager.getApplicationInfo(this.mDefaultPackageName, 0)).toString();
                } catch (PackageManager.NameNotFoundException unused) {
                    Log.d("BatteryEntry", "PackageManager failed to retrieve ApplicationInfo for: " + this.mDefaultPackageName);
                    this.name = this.mDefaultPackageName;
                }
            }
            getQuickNameIconForUid(uid, strArr, z2);
        } else if (batteryConsumer instanceof UserBatteryConsumer) {
            this.mConsumerType = 2;
            this.mConsumedPower = batteryConsumer.getConsumedPower();
            NameAndIcon nameAndIconFromUserId = getNameAndIconFromUserId(context, ((UserBatteryConsumer) batteryConsumer).getUserId());
            this.icon = nameAndIconFromUserId.icon;
            this.name = nameAndIconFromUserId.name;
        } else {
            throw new IllegalArgumentException("Unsupported battery consumer: " + batteryConsumer);
        }
    }

    public BatteryEntry(Context context, int i, double d, double d2, long j) {
        this.mContext = context;
        this.mBatteryConsumer = null;
        this.mIsHidden = false;
        this.mPowerComponentId = i;
        this.mConsumedPower = d - d2;
        this.mUsageDurationMs = j;
        this.mConsumerType = 3;
        NameAndIcon nameAndIconFromPowerComponent = getNameAndIconFromPowerComponent(context, i);
        int i2 = nameAndIconFromPowerComponent.iconId;
        this.iconId = i2;
        this.name = nameAndIconFromPowerComponent.name;
        if (i2 != 0) {
            this.icon = context.getDrawable(i2);
        }
    }

    public BatteryEntry(Context context, int i, String str, double d, double d2) {
        this.mContext = context;
        this.mBatteryConsumer = null;
        this.mIsHidden = false;
        this.mPowerComponentId = i;
        this.iconId = R.drawable.ic_power_system;
        this.icon = context.getDrawable(R.drawable.ic_power_system);
        this.name = str;
        this.mConsumedPower = d - d2;
        this.mConsumerType = 3;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public String getLabel() {
        return this.name;
    }

    public int getConsumerType() {
        return this.mConsumerType;
    }

    public int getPowerComponentId() {
        return this.mPowerComponentId;
    }

    /* access modifiers changed from: package-private */
    public void getQuickNameIconForUid(int i, String[] strArr, boolean z) {
        Locale locale = Locale.getDefault();
        if (sCurrentLocale != locale) {
            clearUidCache();
            sCurrentLocale = locale;
        }
        String num = Integer.toString(i);
        HashMap<String, UidToDetail> hashMap = sUidCache;
        if (hashMap.containsKey(num)) {
            UidToDetail uidToDetail = hashMap.get(num);
            this.mDefaultPackageName = uidToDetail.packageName;
            this.name = uidToDetail.name;
            this.icon = uidToDetail.icon;
            return;
        }
        if (strArr == null || strArr.length == 0) {
            NameAndIcon nameAndIconFromUid = getNameAndIconFromUid(this.mContext, this.name, i);
            this.icon = nameAndIconFromUid.icon;
            this.name = nameAndIconFromUid.name;
        } else {
            this.icon = this.mContext.getPackageManager().getDefaultActivityIcon();
        }
        if (sHandler != null && z) {
            ArrayList<BatteryEntry> arrayList = sRequestQueue;
            synchronized (arrayList) {
                arrayList.add(this);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00ee, code lost:
        r6 = r0;
        r12 = r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.android.settings.fuelgauge.BatteryEntry.NameAndIcon loadNameAndIcon(android.content.Context r16, int r17, android.os.Handler r18, com.android.settings.fuelgauge.BatteryEntry r19, java.lang.String r20, java.lang.String r21, android.graphics.drawable.Drawable r22) {
        /*
            r1 = r17
            r2 = r18
            if (r1 == 0) goto L_0x014c
            r0 = -1
            if (r1 != r0) goto L_0x000b
            goto L_0x014c
        L_0x000b:
            android.content.pm.PackageManager r3 = r16.getPackageManager()
            r0 = 1000(0x3e8, float:1.401E-42)
            if (r1 != r0) goto L_0x001a
            java.lang.String r0 = "android"
            java.lang.String[] r0 = new java.lang.String[]{r0}
            goto L_0x001e
        L_0x001a:
            java.lang.String[] r0 = r3.getPackagesForUid(r1)
        L_0x001e:
            r4 = r0
            r5 = 1
            r6 = 0
            if (r4 == 0) goto L_0x0116
            int r7 = r4.length
            java.lang.String[] r8 = new java.lang.String[r7]
            int r0 = r4.length
            java.lang.System.arraycopy(r4, r6, r8, r6, r0)
            android.content.pm.IPackageManager r9 = android.app.AppGlobals.getPackageManager()
            int r10 = android.os.UserHandle.getUserId(r17)
            r11 = r20
            r12 = r6
        L_0x0035:
            java.lang.String r13 = ", user "
            java.lang.String r14 = "BatteryEntry"
            if (r12 >= r7) goto L_0x0099
            r0 = r8[r12]     // Catch:{ RemoteException -> 0x0078 }
            android.content.pm.ApplicationInfo r0 = r9.getApplicationInfo(r0, r6, r10)     // Catch:{ RemoteException -> 0x0078 }
            if (r0 != 0) goto L_0x0060
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x0078 }
            r0.<init>()     // Catch:{ RemoteException -> 0x0078 }
            java.lang.String r15 = "Retrieving null app info for package "
            r0.append(r15)     // Catch:{ RemoteException -> 0x0078 }
            r15 = r8[r12]     // Catch:{ RemoteException -> 0x0078 }
            r0.append(r15)     // Catch:{ RemoteException -> 0x0078 }
            r0.append(r13)     // Catch:{ RemoteException -> 0x0078 }
            r0.append(r10)     // Catch:{ RemoteException -> 0x0078 }
            java.lang.String r0 = r0.toString()     // Catch:{ RemoteException -> 0x0078 }
            android.util.Log.d(r14, r0)     // Catch:{ RemoteException -> 0x0078 }
            goto L_0x0095
        L_0x0060:
            java.lang.CharSequence r15 = r0.loadLabel(r3)     // Catch:{ RemoteException -> 0x0078 }
            if (r15 == 0) goto L_0x006c
            java.lang.String r15 = r15.toString()     // Catch:{ RemoteException -> 0x0078 }
            r8[r12] = r15     // Catch:{ RemoteException -> 0x0078 }
        L_0x006c:
            int r15 = r0.icon     // Catch:{ RemoteException -> 0x0078 }
            if (r15 == 0) goto L_0x0095
            r11 = r4[r12]     // Catch:{ RemoteException -> 0x0078 }
            android.graphics.drawable.Drawable r0 = r0.loadIcon(r3)     // Catch:{ RemoteException -> 0x0078 }
            r6 = r0
            goto L_0x009b
        L_0x0078:
            r0 = move-exception
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r6 = "Error while retrieving app info for package "
            r15.append(r6)
            r6 = r8[r12]
            r15.append(r6)
            r15.append(r13)
            r15.append(r10)
            java.lang.String r6 = r15.toString()
            android.util.Log.d(r14, r6, r0)
        L_0x0095:
            int r12 = r12 + 1
            r6 = 0
            goto L_0x0035
        L_0x0099:
            r6 = r22
        L_0x009b:
            if (r7 != r5) goto L_0x00a3
            r7 = 0
            r0 = r8[r7]
            r12 = r0
            goto L_0x011c
        L_0x00a3:
            r7 = 0
            int r8 = r4.length
            r12 = r21
            r15 = r7
        L_0x00a8:
            if (r15 >= r8) goto L_0x011c
            r5 = r4[r15]
            android.content.pm.PackageInfo r0 = r9.getPackageInfo(r5, r7, r10)     // Catch:{ RemoteException -> 0x00f4 }
            if (r0 != 0) goto L_0x00cd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x00f4 }
            r0.<init>()     // Catch:{ RemoteException -> 0x00f4 }
            java.lang.String r7 = "Retrieving null package info for package "
            r0.append(r7)     // Catch:{ RemoteException -> 0x00f4 }
            r0.append(r5)     // Catch:{ RemoteException -> 0x00f4 }
            r0.append(r13)     // Catch:{ RemoteException -> 0x00f4 }
            r0.append(r10)     // Catch:{ RemoteException -> 0x00f4 }
            java.lang.String r0 = r0.toString()     // Catch:{ RemoteException -> 0x00f4 }
            android.util.Log.d(r14, r0)     // Catch:{ RemoteException -> 0x00f4 }
            goto L_0x010f
        L_0x00cd:
            int r7 = r0.sharedUserLabel     // Catch:{ RemoteException -> 0x00f4 }
            if (r7 == 0) goto L_0x010f
            android.content.pm.ApplicationInfo r1 = r0.applicationInfo     // Catch:{ RemoteException -> 0x00f4 }
            java.lang.CharSequence r1 = r3.getText(r5, r7, r1)     // Catch:{ RemoteException -> 0x00f4 }
            if (r1 == 0) goto L_0x010f
            java.lang.String r1 = r1.toString()     // Catch:{ RemoteException -> 0x00f4 }
            android.content.pm.ApplicationInfo r0 = r0.applicationInfo     // Catch:{ RemoteException -> 0x00f1 }
            int r7 = r0.icon     // Catch:{ RemoteException -> 0x00f1 }
            if (r7 == 0) goto L_0x00ed
            android.graphics.drawable.Drawable r0 = r0.loadIcon(r3)     // Catch:{ RemoteException -> 0x00e9 }
            r11 = r5
            goto L_0x00ee
        L_0x00e9:
            r0 = move-exception
            r12 = r1
            r11 = r5
            goto L_0x00f5
        L_0x00ed:
            r0 = r6
        L_0x00ee:
            r6 = r0
            r12 = r1
            goto L_0x011c
        L_0x00f1:
            r0 = move-exception
            r12 = r1
            goto L_0x00f5
        L_0x00f4:
            r0 = move-exception
        L_0x00f5:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r7 = "Error while retrieving package info for package "
            r1.append(r7)
            r1.append(r5)
            r1.append(r13)
            r1.append(r10)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r14, r1, r0)
        L_0x010f:
            int r15 = r15 + 1
            r1 = r17
            r5 = 1
            r7 = 0
            goto L_0x00a8
        L_0x0116:
            r11 = r20
            r12 = r21
            r6 = r22
        L_0x011c:
            java.lang.String r0 = java.lang.Integer.toString(r17)
            if (r12 != 0) goto L_0x0123
            r12 = r0
        L_0x0123:
            if (r6 != 0) goto L_0x0129
            android.graphics.drawable.Drawable r6 = r3.getDefaultActivityIcon()
        L_0x0129:
            com.android.settings.fuelgauge.BatteryEntry$UidToDetail r1 = new com.android.settings.fuelgauge.BatteryEntry$UidToDetail
            r1.<init>()
            r1.name = r12
            r1.icon = r6
            r1.packageName = r11
            java.util.HashMap<java.lang.String, com.android.settings.fuelgauge.BatteryEntry$UidToDetail> r3 = sUidCache
            r3.put(r0, r1)
            if (r2 == 0) goto L_0x0145
            r1 = r19
            r3 = 1
            android.os.Message r0 = r2.obtainMessage(r3, r1)
            r2.sendMessage(r0)
        L_0x0145:
            com.android.settings.fuelgauge.BatteryEntry$NameAndIcon r0 = new com.android.settings.fuelgauge.BatteryEntry$NameAndIcon
            r1 = 0
            r0.<init>(r12, r11, r6, r1)
            return r0
        L_0x014c:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.fuelgauge.BatteryEntry.loadNameAndIcon(android.content.Context, int, android.os.Handler, com.android.settings.fuelgauge.BatteryEntry, java.lang.String, java.lang.String, android.graphics.drawable.Drawable):com.android.settings.fuelgauge.BatteryEntry$NameAndIcon");
    }

    public String getKey() {
        UidBatteryConsumer uidBatteryConsumer = this.mBatteryConsumer;
        if (uidBatteryConsumer instanceof UidBatteryConsumer) {
            return Integer.toString(uidBatteryConsumer.getUid());
        }
        if (uidBatteryConsumer instanceof UserBatteryConsumer) {
            return "U|" + this.mBatteryConsumer.getUserId();
        }
        return "S|" + this.mPowerComponentId;
    }

    public boolean isHidden() {
        return this.mIsHidden;
    }

    public boolean isAppEntry() {
        return this.mBatteryConsumer instanceof UidBatteryConsumer;
    }

    public boolean isUserEntry() {
        return this.mBatteryConsumer instanceof UserBatteryConsumer;
    }

    public String getDefaultPackageName() {
        return this.mDefaultPackageName;
    }

    public int getUid() {
        UidBatteryConsumer uidBatteryConsumer = this.mBatteryConsumer;
        if (uidBatteryConsumer instanceof UidBatteryConsumer) {
            return uidBatteryConsumer.getUid();
        }
        return -1;
    }

    public long getTimeInForegroundMs() {
        UidBatteryConsumer uidBatteryConsumer = this.mBatteryConsumer;
        if (uidBatteryConsumer instanceof UidBatteryConsumer) {
            return uidBatteryConsumer.getTimeInStateMs(0);
        }
        return this.mUsageDurationMs;
    }

    public long getTimeInBackgroundMs() {
        UidBatteryConsumer uidBatteryConsumer = this.mBatteryConsumer;
        if (uidBatteryConsumer instanceof UidBatteryConsumer) {
            return uidBatteryConsumer.getTimeInStateMs(1);
        }
        return 0;
    }

    public double getConsumedPower() {
        return this.mConsumedPower;
    }

    public void add(BatteryConsumer batteryConsumer) {
        this.mConsumedPower += batteryConsumer.getConsumedPower();
        if (this.mDefaultPackageName == null && (batteryConsumer instanceof UidBatteryConsumer)) {
            this.mDefaultPackageName = ((UidBatteryConsumer) batteryConsumer).getPackageWithHighestDrain();
        }
    }

    public static NameAndIcon getNameAndIconFromUserId(Context context, int i) {
        String str;
        Drawable drawable;
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        UserInfo userInfo = userManager.getUserInfo(i);
        if (userInfo != null) {
            drawable = Utils.getUserIcon(context, userManager, userInfo);
            str = Utils.getUserLabel(context, userInfo);
        } else {
            str = context.getResources().getString(R.string.running_process_item_removed_user_label);
            drawable = null;
        }
        return new NameAndIcon(str, drawable, 0);
    }

    public static NameAndIcon getNameAndIconFromUid(Context context, String str, int i) {
        Drawable drawable = context.getDrawable(R.drawable.ic_power_system);
        if (i == 0) {
            str = context.getResources().getString(R.string.process_kernel_label);
        } else if ("mediaserver".equals(str)) {
            str = context.getResources().getString(R.string.process_mediaserver_label);
        } else if ("dex2oat".equals(str)) {
            str = context.getResources().getString(R.string.process_dex2oat_label);
        }
        return new NameAndIcon(str, drawable, 0);
    }

    public static NameAndIcon getNameAndIconFromPowerComponent(Context context, int i) {
        String str;
        int i2 = R.drawable.ic_settings_display;
        if (i == 0) {
            str = context.getResources().getString(R.string.power_screen);
        } else if (i == 6) {
            str = context.getResources().getString(R.string.power_flashlight);
        } else if (i == 8) {
            str = context.getResources().getString(R.string.power_cell);
            i2 = R.drawable.ic_cellular_1_bar;
        } else if (i == 11) {
            str = context.getResources().getString(R.string.power_wifi);
            i2 = R.drawable.ic_settings_wireless;
        } else if (i == 2) {
            str = context.getResources().getString(R.string.power_bluetooth);
            i2 = 17302833;
        } else if (i != 3) {
            switch (i) {
                case 13:
                case 16:
                    str = context.getResources().getString(R.string.power_idle);
                    i2 = R.drawable.ic_settings_phone_idle;
                    break;
                case 14:
                    str = context.getResources().getString(R.string.power_phone);
                    i2 = R.drawable.ic_settings_voice_calls;
                    break;
                case 15:
                    str = context.getResources().getString(R.string.ambient_display_screen_title);
                    i2 = R.drawable.ic_settings_aod;
                    break;
                default:
                    str = DebugUtils.constantToString(BatteryConsumer.class, "POWER_COMPONENT_", i);
                    i2 = R.drawable.ic_power_system;
                    break;
            }
        } else {
            str = context.getResources().getString(R.string.power_camera);
            i2 = R.drawable.ic_settings_camera;
        }
        return new NameAndIcon(str, (Drawable) null, i2);
    }
}
