package com.android.settings.applications.appops;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.SparseArray;
import com.android.settings.R;
import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class AppOpsState {
    public static final OpsTemplate[] ALL_TEMPLATES;
    public static final OpsTemplate DEVICE_TEMPLATE;
    public static final Comparator<AppOpEntry> LABEL_COMPARATOR = new Comparator<AppOpEntry>() {
        private final Collator sCollator = Collator.getInstance();

        public int compare(AppOpEntry appOpEntry, AppOpEntry appOpEntry2) {
            return this.sCollator.compare(appOpEntry.getAppEntry().getLabel(), appOpEntry2.getAppEntry().getLabel());
        }
    };
    public static final OpsTemplate LOCATION_TEMPLATE;
    public static final OpsTemplate MEDIA_TEMPLATE;
    public static final OpsTemplate MESSAGING_TEMPLATE;
    public static final OpsTemplate PERSONAL_TEMPLATE;
    public static final Comparator<AppOpEntry> RECENCY_COMPARATOR = new Comparator<AppOpEntry>() {
        private final Collator sCollator = Collator.getInstance();

        public int compare(AppOpEntry appOpEntry, AppOpEntry appOpEntry2) {
            if (appOpEntry.getSwitchOrder() != appOpEntry2.getSwitchOrder()) {
                if (appOpEntry.getSwitchOrder() < appOpEntry2.getSwitchOrder()) {
                    return -1;
                }
                return 1;
            } else if (appOpEntry.isRunning() != appOpEntry2.isRunning()) {
                if (appOpEntry.isRunning()) {
                    return -1;
                }
                return 1;
            } else if (appOpEntry.getTime() == appOpEntry2.getTime()) {
                return this.sCollator.compare(appOpEntry.getAppEntry().getLabel(), appOpEntry2.getAppEntry().getLabel());
            } else {
                if (appOpEntry.getTime() > appOpEntry2.getTime()) {
                    return -1;
                }
                return 1;
            }
        }
    };
    public static final OpsTemplate RUN_IN_BACKGROUND_TEMPLATE;
    final AppOpsManager mAppOps;
    final Context mContext;
    final CharSequence[] mOpLabels;
    final CharSequence[] mOpSummaries;
    final PackageManager mPm;

    public AppOpsState(Context context) {
        this.mContext = context;
        this.mAppOps = (AppOpsManager) context.getSystemService("appops");
        this.mPm = context.getPackageManager();
        this.mOpSummaries = context.getResources().getTextArray(R.array.app_ops_summaries);
        this.mOpLabels = context.getResources().getTextArray(R.array.app_ops_labels);
    }

    public static class OpsTemplate implements Parcelable {
        public static final Parcelable.Creator<OpsTemplate> CREATOR = new Parcelable.Creator<OpsTemplate>() {
            public OpsTemplate createFromParcel(Parcel parcel) {
                return new OpsTemplate(parcel);
            }

            public OpsTemplate[] newArray(int i) {
                return new OpsTemplate[i];
            }
        };
        public final int[] ops;
        public final boolean[] showPerms;

        public int describeContents() {
            return 0;
        }

        public OpsTemplate(int[] iArr, boolean[] zArr) {
            this.ops = iArr;
            this.showPerms = zArr;
        }

        OpsTemplate(Parcel parcel) {
            this.ops = parcel.createIntArray();
            this.showPerms = parcel.createBooleanArray();
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeIntArray(this.ops);
            parcel.writeBooleanArray(this.showPerms);
        }
    }

    static {
        OpsTemplate opsTemplate = new OpsTemplate(new int[]{0, 1, 2, 10, 12, 41, 42}, new boolean[]{true, true, false, false, false, false, false});
        LOCATION_TEMPLATE = opsTemplate;
        OpsTemplate opsTemplate2 = new OpsTemplate(new int[]{4, 5, 6, 7, 8, 9, 29, 30}, new boolean[]{true, true, true, true, true, true, false, false});
        PERSONAL_TEMPLATE = opsTemplate2;
        OpsTemplate opsTemplate3 = new OpsTemplate(new int[]{14, 16, 17, 18, 19, 15, 20, 21, 22}, new boolean[]{true, true, true, true, true, true, true, true, true});
        MESSAGING_TEMPLATE = opsTemplate3;
        OpsTemplate opsTemplate4 = new OpsTemplate(new int[]{3, 26, 27, 28, 31, 32, 33, 34, 35, 36, 37, 38, 39, 64, 44}, new boolean[]{false, true, true, false, false, false, false, false, false, false, false, false, false, false});
        MEDIA_TEMPLATE = opsTemplate4;
        OpsTemplate opsTemplate5 = new OpsTemplate(new int[]{11, 25, 13, 23, 24, 40, 46, 47, 49, 50}, new boolean[]{false, true, true, true, true, true, false, false, false, false});
        DEVICE_TEMPLATE = opsTemplate5;
        OpsTemplate opsTemplate6 = new OpsTemplate(new int[]{63}, new boolean[]{false});
        RUN_IN_BACKGROUND_TEMPLATE = opsTemplate6;
        ALL_TEMPLATES = new OpsTemplate[]{opsTemplate, opsTemplate2, opsTemplate3, opsTemplate4, opsTemplate5, opsTemplate6};
    }

    public static class AppEntry {
        private final File mApkFile;
        private Drawable mIcon;
        private final ApplicationInfo mInfo;
        private String mLabel;
        private boolean mMounted;
        private final SparseArray<AppOpEntry> mOpSwitches = new SparseArray<>();
        private final SparseArray<AppOpsManager.OpEntry> mOps = new SparseArray<>();
        private final AppOpsState mState;

        public AppEntry(AppOpsState appOpsState, ApplicationInfo applicationInfo) {
            this.mState = appOpsState;
            this.mInfo = applicationInfo;
            this.mApkFile = new File(applicationInfo.sourceDir);
        }

        public void addOp(AppOpEntry appOpEntry, AppOpsManager.OpEntry opEntry) {
            this.mOps.put(opEntry.getOp(), opEntry);
            this.mOpSwitches.put(AppOpsManager.opToSwitch(opEntry.getOp()), appOpEntry);
        }

        public boolean hasOp(int i) {
            return this.mOps.indexOfKey(i) >= 0;
        }

        public AppOpEntry getOpSwitch(int i) {
            return this.mOpSwitches.get(AppOpsManager.opToSwitch(i));
        }

        public ApplicationInfo getApplicationInfo() {
            return this.mInfo;
        }

        public String getLabel() {
            return this.mLabel;
        }

        public Drawable getIcon() {
            Drawable drawable = this.mIcon;
            if (drawable == null) {
                if (this.mApkFile.exists()) {
                    Drawable loadIcon = this.mInfo.loadIcon(this.mState.mPm);
                    this.mIcon = loadIcon;
                    return loadIcon;
                }
                this.mMounted = false;
            } else if (this.mMounted) {
                return drawable;
            } else {
                if (this.mApkFile.exists()) {
                    this.mMounted = true;
                    Drawable loadIcon2 = this.mInfo.loadIcon(this.mState.mPm);
                    this.mIcon = loadIcon2;
                    return loadIcon2;
                }
            }
            return this.mState.mContext.getDrawable(17301651);
        }

        public String toString() {
            return this.mLabel;
        }

        /* access modifiers changed from: package-private */
        public void loadLabel(Context context) {
            if (this.mLabel != null && this.mMounted) {
                return;
            }
            if (!this.mApkFile.exists()) {
                this.mMounted = false;
                this.mLabel = this.mInfo.packageName;
                return;
            }
            this.mMounted = true;
            CharSequence loadLabel = this.mInfo.loadLabel(context.getPackageManager());
            this.mLabel = loadLabel != null ? loadLabel.toString() : this.mInfo.packageName;
        }
    }

    public static class AppOpEntry {
        private final AppEntry mApp;
        private final ArrayList<AppOpsManager.OpEntry> mOps;
        private int mOverriddenPrimaryMode = -1;
        private final AppOpsManager.PackageOps mPkgOps;
        private final ArrayList<AppOpsManager.OpEntry> mSwitchOps;
        private final int mSwitchOrder;

        public AppOpEntry(AppOpsManager.PackageOps packageOps, AppOpsManager.OpEntry opEntry, AppEntry appEntry, int i) {
            ArrayList<AppOpsManager.OpEntry> arrayList = new ArrayList<>();
            this.mOps = arrayList;
            ArrayList<AppOpsManager.OpEntry> arrayList2 = new ArrayList<>();
            this.mSwitchOps = arrayList2;
            this.mPkgOps = packageOps;
            this.mApp = appEntry;
            this.mSwitchOrder = i;
            appEntry.addOp(this, opEntry);
            arrayList.add(opEntry);
            arrayList2.add(opEntry);
        }

        private static void addOp(ArrayList<AppOpsManager.OpEntry> arrayList, AppOpsManager.OpEntry opEntry) {
            for (int i = 0; i < arrayList.size(); i++) {
                AppOpsManager.OpEntry opEntry2 = arrayList.get(i);
                if (opEntry2.isRunning() != opEntry.isRunning()) {
                    if (opEntry.isRunning()) {
                        arrayList.add(i, opEntry);
                        return;
                    }
                } else if (opEntry2.getTime() < opEntry.getTime()) {
                    arrayList.add(i, opEntry);
                    return;
                }
            }
            arrayList.add(opEntry);
        }

        public void addOp(AppOpsManager.OpEntry opEntry) {
            this.mApp.addOp(this, opEntry);
            addOp(this.mOps, opEntry);
            if (this.mApp.getOpSwitch(AppOpsManager.opToSwitch(opEntry.getOp())) == null) {
                addOp(this.mSwitchOps, opEntry);
            }
        }

        public AppEntry getAppEntry() {
            return this.mApp;
        }

        public int getSwitchOrder() {
            return this.mSwitchOrder;
        }

        public AppOpsManager.OpEntry getOpEntry(int i) {
            return this.mOps.get(i);
        }

        public int getPrimaryOpMode() {
            int i = this.mOverriddenPrimaryMode;
            return i >= 0 ? i : this.mOps.get(0).getMode();
        }

        public void overridePrimaryOpMode(int i) {
            this.mOverriddenPrimaryMode = i;
        }

        public CharSequence getTimeText(Resources resources, boolean z) {
            if (isRunning()) {
                return resources.getText(R.string.app_ops_running);
            }
            if (getTime() > 0) {
                return DateUtils.getRelativeTimeSpanString(getTime(), System.currentTimeMillis(), 60000, 262144);
            }
            return z ? resources.getText(R.string.app_ops_never_used) : "";
        }

        public boolean isRunning() {
            return this.mOps.get(0).isRunning();
        }

        public long getTime() {
            return this.mOps.get(0).getTime();
        }

        public String toString() {
            return this.mApp.getLabel();
        }
    }

    private void addOp(List<AppOpEntry> list, AppOpsManager.PackageOps packageOps, AppEntry appEntry, AppOpsManager.OpEntry opEntry, boolean z, int i) {
        if (z && list.size() > 0) {
            boolean z2 = true;
            AppOpEntry appOpEntry = list.get(list.size() - 1);
            if (appOpEntry.getAppEntry() == appEntry) {
                boolean z3 = appOpEntry.getTime() != 0;
                if (opEntry.getTime() == 0) {
                    z2 = false;
                }
                if (z3 == z2) {
                    appOpEntry.addOp(opEntry);
                    return;
                }
            }
        }
        AppOpEntry opSwitch = appEntry.getOpSwitch(opEntry.getOp());
        if (opSwitch != null) {
            opSwitch.addOp(opEntry);
        } else {
            list.add(new AppOpEntry(packageOps, opEntry, appEntry, i));
        }
    }

    public AppOpsManager getAppOpsManager() {
        return this.mAppOps;
    }

    private AppEntry getAppEntry(Context context, HashMap<String, AppEntry> hashMap, String str, ApplicationInfo applicationInfo) {
        AppEntry appEntry = hashMap.get(str);
        if (appEntry != null) {
            return appEntry;
        }
        if (applicationInfo == null) {
            try {
                applicationInfo = this.mPm.getApplicationInfo(str, 4194816);
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w("AppOpsState", "Unable to find info for package " + str);
                return null;
            }
        }
        AppEntry appEntry2 = new AppEntry(this, applicationInfo);
        appEntry2.loadLabel(context);
        hashMap.put(str, appEntry2);
        return appEntry2;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v15, resolved type: java.util.ArrayList} */
    /* JADX WARNING: type inference failed for: r5v11, types: [java.util.List] */
    /* JADX WARNING: type inference failed for: r5v13 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<com.android.settings.applications.appops.AppOpsState.AppOpEntry> buildState(com.android.settings.applications.appops.AppOpsState.OpsTemplate r27, int r28, java.lang.String r29, java.util.Comparator<com.android.settings.applications.appops.AppOpsState.AppOpEntry> r30) {
        /*
            r26 = this;
            r7 = r26
            r0 = r27
            r8 = r29
            android.content.Context r9 = r7.mContext
            java.util.HashMap r10 = new java.util.HashMap
            r10.<init>()
            java.util.ArrayList r11 = new java.util.ArrayList
            r11.<init>()
            java.util.ArrayList r12 = new java.util.ArrayList
            r12.<init>()
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>()
            r1 = 116(0x74, float:1.63E-43)
            int[] r14 = new int[r1]
            r15 = 0
            r1 = r15
        L_0x0022:
            int[] r2 = r0.ops
            int r3 = r2.length
            if (r1 >= r3) goto L_0x0052
            boolean[] r3 = r0.showPerms
            boolean r3 = r3[r1]
            if (r3 == 0) goto L_0x004f
            r2 = r2[r1]
            java.lang.String r2 = android.app.AppOpsManager.opToPermission(r2)
            if (r2 == 0) goto L_0x004f
            boolean r3 = r12.contains(r2)
            if (r3 != 0) goto L_0x004f
            r12.add(r2)
            int[] r2 = r0.ops
            r2 = r2[r1]
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r13.add(r2)
            int[] r2 = r0.ops
            r2 = r2[r1]
            r14[r2] = r1
        L_0x004f:
            int r1 = r1 + 1
            goto L_0x0022
        L_0x0052:
            if (r8 == 0) goto L_0x005d
            android.app.AppOpsManager r0 = r7.mAppOps
            r1 = r28
            java.util.List r0 = r0.getOpsForPackage(r1, r8, r2)
            goto L_0x0063
        L_0x005d:
            android.app.AppOpsManager r0 = r7.mAppOps
            java.util.List r0 = r0.getPackagesForOps(r2)
        L_0x0063:
            r6 = r0
            r5 = 0
            r16 = 1
            if (r6 == 0) goto L_0x00dc
            r4 = r15
        L_0x006a:
            int r0 = r6.size()
            if (r4 >= r0) goto L_0x00dc
            java.lang.Object r0 = r6.get(r4)
            r17 = r0
            android.app.AppOpsManager$PackageOps r17 = (android.app.AppOpsManager.PackageOps) r17
            java.lang.String r0 = r17.getPackageName()
            com.android.settings.applications.appops.AppOpsState$AppEntry r18 = r7.getAppEntry(r9, r10, r0, r5)
            if (r18 != 0) goto L_0x0089
        L_0x0082:
            r23 = r4
            r19 = r5
            r20 = r6
            goto L_0x00d5
        L_0x0089:
            r3 = r15
        L_0x008a:
            java.util.List r0 = r17.getOps()
            int r0 = r0.size()
            if (r3 >= r0) goto L_0x0082
            java.util.List r0 = r17.getOps()
            java.lang.Object r0 = r0.get(r3)
            r19 = r0
            android.app.AppOpsManager$OpEntry r19 = (android.app.AppOpsManager.OpEntry) r19
            if (r8 != 0) goto L_0x00a5
            r20 = r16
            goto L_0x00a7
        L_0x00a5:
            r20 = r15
        L_0x00a7:
            if (r8 != 0) goto L_0x00ac
            r21 = r15
            goto L_0x00b4
        L_0x00ac:
            int r0 = r19.getOp()
            r0 = r14[r0]
            r21 = r0
        L_0x00b4:
            r0 = r26
            r1 = r11
            r2 = r17
            r22 = r3
            r3 = r18
            r23 = r4
            r4 = r19
            r19 = r5
            r5 = r20
            r20 = r6
            r6 = r21
            r0.addOp(r1, r2, r3, r4, r5, r6)
            int r3 = r22 + 1
            r5 = r19
            r6 = r20
            r4 = r23
            goto L_0x008a
        L_0x00d5:
            int r4 = r23 + 1
            r5 = r19
            r6 = r20
            goto L_0x006a
        L_0x00dc:
            r19 = r5
            if (r8 == 0) goto L_0x00f1
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            android.content.pm.PackageManager r1 = r7.mPm     // Catch:{ NameNotFoundException -> 0x0100 }
            r2 = 4096(0x1000, float:5.74E-42)
            android.content.pm.PackageInfo r1 = r1.getPackageInfo(r8, r2)     // Catch:{ NameNotFoundException -> 0x0100 }
            r0.add(r1)     // Catch:{ NameNotFoundException -> 0x0100 }
            goto L_0x0100
        L_0x00f1:
            int r0 = r12.size()
            java.lang.String[] r0 = new java.lang.String[r0]
            r12.toArray(r0)
            android.content.pm.PackageManager r1 = r7.mPm
            java.util.List r0 = r1.getPackagesHoldingPermissions(r0, r15)
        L_0x0100:
            r6 = r0
            r5 = r15
        L_0x0102:
            int r0 = r6.size()
            if (r5 >= r0) goto L_0x021b
            java.lang.Object r0 = r6.get(r5)
            r4 = r0
            android.content.pm.PackageInfo r4 = (android.content.pm.PackageInfo) r4
            java.lang.String r0 = r4.packageName
            android.content.pm.ApplicationInfo r1 = r4.applicationInfo
            com.android.settings.applications.appops.AppOpsState$AppEntry r3 = r7.getAppEntry(r9, r10, r0, r1)
            if (r3 != 0) goto L_0x011b
            goto L_0x020d
        L_0x011b:
            java.lang.String[] r0 = r4.requestedPermissions
            if (r0 == 0) goto L_0x020d
            r2 = r15
            r0 = r19
            r1 = r0
        L_0x0123:
            java.lang.String[] r15 = r4.requestedPermissions
            int r15 = r15.length
            if (r2 >= r15) goto L_0x0207
            int[] r15 = r4.requestedPermissionsFlags
            if (r15 == 0) goto L_0x013f
            r15 = r15[r2]
            r15 = r15 & 2
            if (r15 != 0) goto L_0x013f
            r20 = r2
            r24 = r3
            r25 = r4
            r18 = r5
            r17 = r6
            r7 = 0
            goto L_0x01f9
        L_0x013f:
            r27 = r1
            r15 = 0
        L_0x0142:
            int r1 = r12.size()
            if (r15 >= r1) goto L_0x01ec
            java.lang.Object r1 = r12.get(r15)
            java.lang.String r1 = (java.lang.String) r1
            r18 = r5
            java.lang.String[] r5 = r4.requestedPermissions
            r5 = r5[r2]
            boolean r1 = r1.equals(r5)
            if (r1 != 0) goto L_0x015b
            goto L_0x016b
        L_0x015b:
            java.lang.Object r1 = r13.get(r15)
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            boolean r1 = r3.hasOp(r1)
            if (r1 == 0) goto L_0x0178
        L_0x016b:
            r21 = r27
            r20 = r2
            r24 = r3
            r25 = r4
            r17 = r6
            r7 = 0
            goto L_0x01da
        L_0x0178:
            if (r0 != 0) goto L_0x0190
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            android.app.AppOpsManager$PackageOps r1 = new android.app.AppOpsManager$PackageOps
            java.lang.String r5 = r4.packageName
            r20 = r2
            android.content.pm.ApplicationInfo r2 = r4.applicationInfo
            int r2 = r2.uid
            r1.<init>(r5, r2, r0)
            r5 = r0
            r21 = r1
            goto L_0x0195
        L_0x0190:
            r20 = r2
            r21 = r27
            r5 = r0
        L_0x0195:
            android.app.AppOpsManager$OpEntry r2 = new android.app.AppOpsManager$OpEntry
            java.lang.Object r0 = r13.get(r15)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            java.util.Map r1 = java.util.Collections.emptyMap()
            r7 = 0
            r2.<init>(r0, r7, r1)
            r5.add(r2)
            if (r8 != 0) goto L_0x01b1
            r17 = r16
            goto L_0x01b3
        L_0x01b1:
            r17 = r7
        L_0x01b3:
            if (r8 != 0) goto L_0x01b8
            r22 = r7
            goto L_0x01c0
        L_0x01b8:
            int r0 = r2.getOp()
            r0 = r14[r0]
            r22 = r0
        L_0x01c0:
            r0 = r26
            r1 = r11
            r23 = r2
            r2 = r21
            r24 = r3
            r25 = r4
            r4 = r23
            r23 = r5
            r5 = r17
            r17 = r6
            r6 = r22
            r0.addOp(r1, r2, r3, r4, r5, r6)
            r0 = r23
        L_0x01da:
            int r15 = r15 + 1
            r7 = r26
            r6 = r17
            r5 = r18
            r2 = r20
            r27 = r21
            r3 = r24
            r4 = r25
            goto L_0x0142
        L_0x01ec:
            r20 = r2
            r24 = r3
            r25 = r4
            r18 = r5
            r17 = r6
            r7 = 0
            r1 = r27
        L_0x01f9:
            int r2 = r20 + 1
            r7 = r26
            r6 = r17
            r5 = r18
            r3 = r24
            r4 = r25
            goto L_0x0123
        L_0x0207:
            r18 = r5
            r17 = r6
            r7 = 0
            goto L_0x0212
        L_0x020d:
            r18 = r5
            r17 = r6
            r7 = r15
        L_0x0212:
            int r5 = r18 + 1
            r15 = r7
            r6 = r17
            r7 = r26
            goto L_0x0102
        L_0x021b:
            r0 = r30
            java.util.Collections.sort(r11, r0)
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.appops.AppOpsState.buildState(com.android.settings.applications.appops.AppOpsState$OpsTemplate, int, java.lang.String, java.util.Comparator):java.util.List");
    }
}
