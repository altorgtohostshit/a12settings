package com.android.settings.applications;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.app.procstats.ProcessState;
import com.android.internal.app.procstats.ProcessStats;
import com.android.internal.app.procstats.ServiceState;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class ProcStatsEntry implements Parcelable {
    public static final Parcelable.Creator<ProcStatsEntry> CREATOR = new Parcelable.Creator<ProcStatsEntry>() {
        public ProcStatsEntry createFromParcel(Parcel parcel) {
            return new ProcStatsEntry(parcel);
        }

        public ProcStatsEntry[] newArray(int i) {
            return new ProcStatsEntry[i];
        }
    };
    private static boolean DEBUG = false;
    final long mAvgBgMem;
    final long mAvgRunMem;
    String mBestTargetPackage;
    final long mBgDuration;
    final double mBgWeight;
    public CharSequence mLabel;
    final long mMaxBgMem;
    final long mMaxRunMem;
    final String mName;
    final String mPackage;
    final ArrayList<String> mPackages;
    final long mRunDuration;
    final double mRunWeight;
    ArrayMap<String, ArrayList<Service>> mServices;
    final int mUid;

    public int describeContents() {
        return 0;
    }

    public ProcStatsEntry(ProcessState processState, String str, ProcessStats.ProcessDataCollection processDataCollection, ProcessStats.ProcessDataCollection processDataCollection2, boolean z) {
        ArrayList<String> arrayList = new ArrayList<>();
        this.mPackages = arrayList;
        this.mServices = new ArrayMap<>(1);
        processState.computeProcessData(processDataCollection, 0);
        processState.computeProcessData(processDataCollection2, 0);
        this.mPackage = processState.getPackage();
        this.mUid = processState.getUid();
        this.mName = processState.getName();
        arrayList.add(str);
        long j = processDataCollection.totalTime;
        this.mBgDuration = j;
        long j2 = z ? processDataCollection.avgUss : processDataCollection.avgPss;
        this.mAvgBgMem = j2;
        this.mMaxBgMem = z ? processDataCollection.maxUss : processDataCollection.maxPss;
        double d = ((double) j2) * ((double) j);
        this.mBgWeight = d;
        long j3 = processDataCollection2.totalTime;
        this.mRunDuration = j3;
        long j4 = z ? processDataCollection2.avgUss : processDataCollection2.avgPss;
        this.mAvgRunMem = j4;
        this.mMaxRunMem = z ? processDataCollection2.maxUss : processDataCollection2.maxPss;
        this.mRunWeight = ((double) j4) * ((double) j3);
        if (DEBUG) {
            Log.d("ProcStatsEntry", "New proc entry " + processState.getName() + ": dur=" + j + " avgpss=" + j2 + " weight=" + d);
        }
    }

    public ProcStatsEntry(String str, int i, String str2, long j, long j2, long j3) {
        this.mPackages = new ArrayList<>();
        this.mServices = new ArrayMap<>(1);
        this.mPackage = str;
        this.mUid = i;
        this.mName = str2;
        this.mRunDuration = j;
        this.mBgDuration = j;
        this.mMaxRunMem = j2;
        this.mAvgRunMem = j2;
        this.mMaxBgMem = j2;
        this.mAvgBgMem = j2;
        double d = ((double) j3) * ((double) j2);
        this.mRunWeight = d;
        this.mBgWeight = d;
        if (DEBUG) {
            Log.d("ProcStatsEntry", "New proc entry " + str2 + ": dur=" + j + " avgpss=" + j2 + " weight=" + d);
        }
    }

    public ProcStatsEntry(Parcel parcel) {
        ArrayList<String> arrayList = new ArrayList<>();
        this.mPackages = arrayList;
        this.mServices = new ArrayMap<>(1);
        this.mPackage = parcel.readString();
        this.mUid = parcel.readInt();
        this.mName = parcel.readString();
        parcel.readStringList(arrayList);
        this.mBgDuration = parcel.readLong();
        this.mAvgBgMem = parcel.readLong();
        this.mMaxBgMem = parcel.readLong();
        this.mBgWeight = parcel.readDouble();
        this.mRunDuration = parcel.readLong();
        this.mAvgRunMem = parcel.readLong();
        this.mMaxRunMem = parcel.readLong();
        this.mRunWeight = parcel.readDouble();
        this.mBestTargetPackage = parcel.readString();
        int readInt = parcel.readInt();
        if (readInt > 0) {
            this.mServices.ensureCapacity(readInt);
            for (int i = 0; i < readInt; i++) {
                String readString = parcel.readString();
                ArrayList arrayList2 = new ArrayList();
                parcel.readTypedList(arrayList2, Service.CREATOR);
                this.mServices.append(readString, arrayList2);
            }
        }
    }

    public void addPackage(String str) {
        this.mPackages.add(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:107:0x0388  */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x03b6  */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x03f1  */
    /* JADX WARNING: Removed duplicated region for block: B:144:0x0412 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void evaluateTargetPackage(android.content.pm.PackageManager r24, com.android.internal.app.procstats.ProcessStats r25, com.android.internal.app.procstats.ProcessStats.ProcessDataCollection r26, com.android.internal.app.procstats.ProcessStats.ProcessDataCollection r27, java.util.Comparator<com.android.settings.applications.ProcStatsEntry> r28, boolean r29) {
        /*
            r23 = this;
            r0 = r23
            r1 = 0
            r0.mBestTargetPackage = r1
            java.util.ArrayList<java.lang.String> r2 = r0.mPackages
            int r2 = r2.size()
            java.lang.String r3 = "Eval pkg of "
            r4 = 1
            java.lang.String r5 = "ProcStatsEntry"
            r6 = 0
            if (r2 != r4) goto L_0x0046
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x003b
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r3)
            java.lang.String r2 = r0.mName
            r1.append(r2)
            java.lang.String r2 = ": single pkg "
            r1.append(r2)
            java.util.ArrayList<java.lang.String> r2 = r0.mPackages
            java.lang.Object r2 = r2.get(r6)
            java.lang.String r2 = (java.lang.String) r2
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r5, r1)
        L_0x003b:
            java.util.ArrayList<java.lang.String> r1 = r0.mPackages
            java.lang.Object r1 = r1.get(r6)
            java.lang.String r1 = (java.lang.String) r1
            r0.mBestTargetPackage = r1
            return
        L_0x0046:
            r2 = r6
        L_0x0047:
            java.util.ArrayList<java.lang.String> r7 = r0.mPackages
            int r7 = r7.size()
            if (r2 >= r7) goto L_0x006b
            java.util.ArrayList<java.lang.String> r7 = r0.mPackages
            java.lang.Object r7 = r7.get(r2)
            java.lang.String r8 = "android"
            boolean r7 = r8.equals(r7)
            if (r7 == 0) goto L_0x0068
            java.util.ArrayList<java.lang.String> r1 = r0.mPackages
            java.lang.Object r1 = r1.get(r2)
            java.lang.String r1 = (java.lang.String) r1
            r0.mBestTargetPackage = r1
            return
        L_0x0068:
            int r2 = r2 + 1
            goto L_0x0047
        L_0x006b:
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            r7 = r6
        L_0x0071:
            java.util.ArrayList<java.lang.String> r8 = r0.mPackages
            int r8 = r8.size()
            if (r7 >= r8) goto L_0x0149
            r8 = r25
            com.android.internal.app.ProcessMap r9 = r8.mPackages
            java.util.ArrayList<java.lang.String> r10 = r0.mPackages
            java.lang.Object r10 = r10.get(r7)
            java.lang.String r10 = (java.lang.String) r10
            int r11 = r0.mUid
            java.lang.Object r9 = r9.get(r10, r11)
            android.util.LongSparseArray r9 = (android.util.LongSparseArray) r9
            r10 = r6
        L_0x008e:
            int r11 = r9.size()
            if (r10 >= r11) goto L_0x0145
            java.lang.Object r11 = r9.valueAt(r10)
            com.android.internal.app.procstats.ProcessStats$PackageState r11 = (com.android.internal.app.procstats.ProcessStats.PackageState) r11
            boolean r12 = DEBUG
            if (r12 == 0) goto L_0x00bf
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r12.append(r3)
            java.lang.String r13 = r0.mName
            r12.append(r13)
            java.lang.String r13 = ", pkg "
            r12.append(r13)
            r12.append(r11)
            java.lang.String r13 = ":"
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            android.util.Log.d(r5, r12)
        L_0x00bf:
            java.lang.String r12 = "/"
            if (r11 != 0) goto L_0x00f2
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r13 = "No package state found for "
            r11.append(r13)
            java.util.ArrayList<java.lang.String> r13 = r0.mPackages
            java.lang.Object r13 = r13.get(r7)
            java.lang.String r13 = (java.lang.String) r13
            r11.append(r13)
            r11.append(r12)
            int r12 = r0.mUid
            r11.append(r12)
            java.lang.String r12 = " in process "
            r11.append(r12)
            java.lang.String r12 = r0.mName
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            android.util.Log.w(r5, r11)
            goto L_0x0141
        L_0x00f2:
            android.util.ArrayMap r13 = r11.mProcesses
            java.lang.String r14 = r0.mName
            java.lang.Object r13 = r13.get(r14)
            r15 = r13
            com.android.internal.app.procstats.ProcessState r15 = (com.android.internal.app.procstats.ProcessState) r15
            if (r15 != 0) goto L_0x012e
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r13 = "No process "
            r11.append(r13)
            java.lang.String r13 = r0.mName
            r11.append(r13)
            java.lang.String r13 = " found in package state "
            r11.append(r13)
            java.util.ArrayList<java.lang.String> r13 = r0.mPackages
            java.lang.Object r13 = r13.get(r7)
            java.lang.String r13 = (java.lang.String) r13
            r11.append(r13)
            r11.append(r12)
            int r12 = r0.mUid
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            android.util.Log.w(r5, r11)
            goto L_0x0141
        L_0x012e:
            com.android.settings.applications.ProcStatsEntry r12 = new com.android.settings.applications.ProcStatsEntry
            java.lang.String r11 = r11.mPackageName
            r14 = r12
            r16 = r11
            r17 = r26
            r18 = r27
            r19 = r29
            r14.<init>(r15, r16, r17, r18, r19)
            r2.add(r12)
        L_0x0141:
            int r10 = r10 + 1
            goto L_0x008e
        L_0x0145:
            int r7 = r7 + 1
            goto L_0x0071
        L_0x0149:
            int r7 = r2.size()
            if (r7 <= r4) goto L_0x0437
            r7 = r28
            java.util.Collections.sort(r2, r7)
            java.lang.Object r7 = r2.get(r6)
            com.android.settings.applications.ProcStatsEntry r7 = (com.android.settings.applications.ProcStatsEntry) r7
            double r7 = r7.mRunWeight
            java.lang.Object r9 = r2.get(r4)
            com.android.settings.applications.ProcStatsEntry r9 = (com.android.settings.applications.ProcStatsEntry) r9
            double r9 = r9.mRunWeight
            r11 = 4613937818241073152(0x4008000000000000, double:3.0)
            double r9 = r9 * r11
            int r7 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            java.lang.String r8 = " weight "
            if (r7 <= 0) goto L_0x01cc
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x01c1
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r3)
            java.lang.String r3 = r0.mName
            r1.append(r3)
            java.lang.String r3 = ": best pkg "
            r1.append(r3)
            java.lang.Object r3 = r2.get(r6)
            com.android.settings.applications.ProcStatsEntry r3 = (com.android.settings.applications.ProcStatsEntry) r3
            java.lang.String r3 = r3.mPackage
            r1.append(r3)
            r1.append(r8)
            java.lang.Object r3 = r2.get(r6)
            com.android.settings.applications.ProcStatsEntry r3 = (com.android.settings.applications.ProcStatsEntry) r3
            double r9 = r3.mRunWeight
            r1.append(r9)
            java.lang.String r3 = " better than "
            r1.append(r3)
            java.lang.Object r3 = r2.get(r4)
            com.android.settings.applications.ProcStatsEntry r3 = (com.android.settings.applications.ProcStatsEntry) r3
            java.lang.String r3 = r3.mPackage
            r1.append(r3)
            r1.append(r8)
            java.lang.Object r3 = r2.get(r4)
            com.android.settings.applications.ProcStatsEntry r3 = (com.android.settings.applications.ProcStatsEntry) r3
            double r3 = r3.mRunWeight
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r5, r1)
        L_0x01c1:
            java.lang.Object r1 = r2.get(r6)
            com.android.settings.applications.ProcStatsEntry r1 = (com.android.settings.applications.ProcStatsEntry) r1
            java.lang.String r1 = r1.mPackage
            r0.mBestTargetPackage = r1
            return
        L_0x01cc:
            java.lang.Object r7 = r2.get(r6)
            com.android.settings.applications.ProcStatsEntry r7 = (com.android.settings.applications.ProcStatsEntry) r7
            double r9 = r7.mRunWeight
            r11 = -1
            r7 = r6
            r13 = r7
        L_0x01d8:
            int r14 = r2.size()
            if (r7 >= r14) goto L_0x041f
            java.lang.Object r14 = r2.get(r7)
            com.android.settings.applications.ProcStatsEntry r14 = (com.android.settings.applications.ProcStatsEntry) r14
            r16 = r2
            double r1 = r14.mRunWeight
            r17 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r17 = r9 / r17
            int r1 = (r1 > r17 ? 1 : (r1 == r17 ? 0 : -1))
            java.lang.String r2 = ": pkg "
            if (r1 >= 0) goto L_0x0227
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x021f
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r3)
            java.lang.String r15 = r0.mName
            r1.append(r15)
            r1.append(r2)
            java.lang.String r2 = r14.mPackage
            r1.append(r2)
            r1.append(r8)
            double r14 = r14.mRunWeight
            r1.append(r14)
            java.lang.String r2 = " too small"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r5, r1)
        L_0x021f:
            r15 = r24
        L_0x0221:
            r18 = r7
        L_0x0223:
            r27 = r8
            goto L_0x02c9
        L_0x0227:
            java.lang.String r1 = r14.mPackage     // Catch:{ NameNotFoundException -> 0x03e5 }
            r15 = r24
            android.content.pm.ApplicationInfo r1 = r15.getApplicationInfo(r1, r6)     // Catch:{ NameNotFoundException -> 0x03e7 }
            int r4 = r1.icon     // Catch:{ NameNotFoundException -> 0x03e7 }
            if (r4 != 0) goto L_0x0259
            boolean r1 = DEBUG     // Catch:{ NameNotFoundException -> 0x03e7 }
            if (r1 == 0) goto L_0x0221
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ NameNotFoundException -> 0x03e7 }
            r1.<init>()     // Catch:{ NameNotFoundException -> 0x03e7 }
            r1.append(r3)     // Catch:{ NameNotFoundException -> 0x03e7 }
            java.lang.String r4 = r0.mName     // Catch:{ NameNotFoundException -> 0x03e7 }
            r1.append(r4)     // Catch:{ NameNotFoundException -> 0x03e7 }
            r1.append(r2)     // Catch:{ NameNotFoundException -> 0x03e7 }
            java.lang.String r4 = r14.mPackage     // Catch:{ NameNotFoundException -> 0x03e7 }
            r1.append(r4)     // Catch:{ NameNotFoundException -> 0x03e7 }
            java.lang.String r4 = " has no icon"
            r1.append(r4)     // Catch:{ NameNotFoundException -> 0x03e7 }
            java.lang.String r1 = r1.toString()     // Catch:{ NameNotFoundException -> 0x03e7 }
            android.util.Log.d(r5, r1)     // Catch:{ NameNotFoundException -> 0x03e7 }
            goto L_0x0221
        L_0x0259:
            int r1 = r1.flags     // Catch:{ NameNotFoundException -> 0x03e7 }
            r1 = r1 & 8
            java.lang.String r4 = " not as good as last "
            if (r1 == 0) goto L_0x02cd
            r18 = r7
            long r6 = r14.mRunDuration     // Catch:{ NameNotFoundException -> 0x03e9 }
            if (r13 == 0) goto L_0x029d
            int r19 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
            if (r19 <= 0) goto L_0x026c
            goto L_0x029d
        L_0x026c:
            boolean r19 = DEBUG     // Catch:{ NameNotFoundException -> 0x03e9 }
            if (r19 == 0) goto L_0x0223
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ NameNotFoundException -> 0x03e9 }
            r1.<init>()     // Catch:{ NameNotFoundException -> 0x03e9 }
            r1.append(r3)     // Catch:{ NameNotFoundException -> 0x03e9 }
            r27 = r8
            java.lang.String r8 = r0.mName     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r8)     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r2)     // Catch:{ NameNotFoundException -> 0x03eb }
            java.lang.String r8 = r14.mPackage     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r8)     // Catch:{ NameNotFoundException -> 0x03eb }
            java.lang.String r8 = " pers run time "
            r1.append(r8)     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r6)     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r4)     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r11)     // Catch:{ NameNotFoundException -> 0x03eb }
            java.lang.String r1 = r1.toString()     // Catch:{ NameNotFoundException -> 0x03eb }
            android.util.Log.d(r5, r1)     // Catch:{ NameNotFoundException -> 0x03eb }
            goto L_0x02c9
        L_0x029d:
            r27 = r8
            boolean r1 = DEBUG     // Catch:{ NameNotFoundException -> 0x03eb }
            if (r1 == 0) goto L_0x02c7
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.<init>()     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r3)     // Catch:{ NameNotFoundException -> 0x03eb }
            java.lang.String r4 = r0.mName     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r4)     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r2)     // Catch:{ NameNotFoundException -> 0x03eb }
            java.lang.String r4 = r14.mPackage     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r4)     // Catch:{ NameNotFoundException -> 0x03eb }
            java.lang.String r4 = " new best pers run time "
            r1.append(r4)     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r6)     // Catch:{ NameNotFoundException -> 0x03eb }
            java.lang.String r1 = r1.toString()     // Catch:{ NameNotFoundException -> 0x03eb }
            android.util.Log.d(r5, r1)     // Catch:{ NameNotFoundException -> 0x03eb }
        L_0x02c7:
            r11 = r6
            r13 = 1
        L_0x02c9:
            r19 = r9
            goto L_0x0412
        L_0x02cd:
            r18 = r7
            r27 = r8
            if (r13 == 0) goto L_0x02f9
            boolean r1 = DEBUG     // Catch:{ NameNotFoundException -> 0x03eb }
            if (r1 == 0) goto L_0x02c9
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.<init>()     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r3)     // Catch:{ NameNotFoundException -> 0x03eb }
            java.lang.String r4 = r0.mName     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r4)     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r2)     // Catch:{ NameNotFoundException -> 0x03eb }
            java.lang.String r4 = r14.mPackage     // Catch:{ NameNotFoundException -> 0x03eb }
            r1.append(r4)     // Catch:{ NameNotFoundException -> 0x03eb }
            java.lang.String r4 = " is not persistent"
            r1.append(r4)     // Catch:{ NameNotFoundException -> 0x03eb }
            java.lang.String r1 = r1.toString()     // Catch:{ NameNotFoundException -> 0x03eb }
            android.util.Log.d(r5, r1)     // Catch:{ NameNotFoundException -> 0x03eb }
            goto L_0x02c9
        L_0x02f9:
            android.util.ArrayMap<java.lang.String, java.util.ArrayList<com.android.settings.applications.ProcStatsEntry$Service>> r1 = r0.mServices
            int r6 = r1.size()
            r7 = 0
        L_0x0300:
            if (r7 >= r6) goto L_0x0326
            android.util.ArrayMap<java.lang.String, java.util.ArrayList<com.android.settings.applications.ProcStatsEntry$Service>> r1 = r0.mServices
            java.lang.Object r1 = r1.valueAt(r7)
            r8 = r1
            java.util.ArrayList r8 = (java.util.ArrayList) r8
            r1 = 0
            java.lang.Object r19 = r8.get(r1)
            r1 = r19
            com.android.settings.applications.ProcStatsEntry$Service r1 = (com.android.settings.applications.ProcStatsEntry.Service) r1
            java.lang.String r1 = r1.mPackage
            r28 = r6
            java.lang.String r6 = r14.mPackage
            boolean r1 = r1.equals(r6)
            if (r1 == 0) goto L_0x0321
            goto L_0x0327
        L_0x0321:
            int r7 = r7 + 1
            r6 = r28
            goto L_0x0300
        L_0x0326:
            r8 = 0
        L_0x0327:
            if (r8 == 0) goto L_0x037e
            int r1 = r8.size()
            r6 = 0
        L_0x032e:
            if (r6 >= r1) goto L_0x037e
            java.lang.Object r7 = r8.get(r6)
            com.android.settings.applications.ProcStatsEntry$Service r7 = (com.android.settings.applications.ProcStatsEntry.Service) r7
            r19 = r9
            r10 = r8
            long r8 = r7.mDuration
            r21 = 0
            int r8 = (r8 > r21 ? 1 : (r8 == r21 ? 0 : -1))
            if (r8 <= 0) goto L_0x0378
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0375
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r3)
            java.lang.String r6 = r0.mName
            r1.append(r6)
            r1.append(r2)
            java.lang.String r6 = r14.mPackage
            r1.append(r6)
            java.lang.String r6 = " service "
            r1.append(r6)
            java.lang.String r6 = r7.mName
            r1.append(r6)
            java.lang.String r6 = " run time is "
            r1.append(r6)
            long r8 = r7.mDuration
            r1.append(r8)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r5, r1)
        L_0x0375:
            long r6 = r7.mDuration
            goto L_0x0384
        L_0x0378:
            int r6 = r6 + 1
            r8 = r10
            r9 = r19
            goto L_0x032e
        L_0x037e:
            r19 = r9
            r21 = 0
            r6 = r21
        L_0x0384:
            int r1 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
            if (r1 <= 0) goto L_0x03b6
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x03b0
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r3)
            java.lang.String r4 = r0.mName
            r1.append(r4)
            r1.append(r2)
            java.lang.String r2 = r14.mPackage
            r1.append(r2)
            java.lang.String r2 = " new best run time "
            r1.append(r2)
            r1.append(r6)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r5, r1)
        L_0x03b0:
            java.lang.String r1 = r14.mPackage
            r0.mBestTargetPackage = r1
            r11 = r6
            goto L_0x0412
        L_0x03b6:
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0412
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r3)
            java.lang.String r8 = r0.mName
            r1.append(r8)
            r1.append(r2)
            java.lang.String r2 = r14.mPackage
            r1.append(r2)
            java.lang.String r2 = " run time "
            r1.append(r2)
            r1.append(r6)
            r1.append(r4)
            r1.append(r11)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r5, r1)
            goto L_0x0412
        L_0x03e5:
            r15 = r24
        L_0x03e7:
            r18 = r7
        L_0x03e9:
            r27 = r8
        L_0x03eb:
            r19 = r9
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0412
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r3)
            java.lang.String r4 = r0.mName
            r1.append(r4)
            r1.append(r2)
            java.lang.String r2 = r14.mPackage
            r1.append(r2)
            java.lang.String r2 = " failed finding app info"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r5, r1)
        L_0x0412:
            int r7 = r18 + 1
            r8 = r27
            r2 = r16
            r9 = r19
            r1 = 0
            r4 = 1
            r6 = 0
            goto L_0x01d8
        L_0x041f:
            r16 = r2
            java.lang.String r1 = r0.mBestTargetPackage
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x044a
            r1 = r16
            r2 = 0
            java.lang.Object r1 = r1.get(r2)
            com.android.settings.applications.ProcStatsEntry r1 = (com.android.settings.applications.ProcStatsEntry) r1
            java.lang.String r1 = r1.mPackage
            r0.mBestTargetPackage = r1
            goto L_0x044a
        L_0x0437:
            r1 = r2
            r2 = r6
            int r3 = r1.size()
            r4 = 1
            if (r3 != r4) goto L_0x044a
            java.lang.Object r1 = r1.get(r2)
            com.android.settings.applications.ProcStatsEntry r1 = (com.android.settings.applications.ProcStatsEntry) r1
            java.lang.String r1 = r1.mPackage
            r0.mBestTargetPackage = r1
        L_0x044a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.ProcStatsEntry.evaluateTargetPackage(android.content.pm.PackageManager, com.android.internal.app.procstats.ProcessStats, com.android.internal.app.procstats.ProcessStats$ProcessDataCollection, com.android.internal.app.procstats.ProcessStats$ProcessDataCollection, java.util.Comparator, boolean):void");
    }

    public void addService(ServiceState serviceState) {
        ArrayList arrayList = this.mServices.get(serviceState.getPackage());
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.mServices.put(serviceState.getPackage(), arrayList);
        }
        arrayList.add(new Service(serviceState));
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mPackage);
        parcel.writeInt(this.mUid);
        parcel.writeString(this.mName);
        parcel.writeStringList(this.mPackages);
        parcel.writeLong(this.mBgDuration);
        parcel.writeLong(this.mAvgBgMem);
        parcel.writeLong(this.mMaxBgMem);
        parcel.writeDouble(this.mBgWeight);
        parcel.writeLong(this.mRunDuration);
        parcel.writeLong(this.mAvgRunMem);
        parcel.writeLong(this.mMaxRunMem);
        parcel.writeDouble(this.mRunWeight);
        parcel.writeString(this.mBestTargetPackage);
        int size = this.mServices.size();
        parcel.writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            parcel.writeString(this.mServices.keyAt(i2));
            parcel.writeTypedList(this.mServices.valueAt(i2));
        }
    }

    public int getUid() {
        return this.mUid;
    }

    public static final class Service implements Parcelable {
        public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>() {
            public Service createFromParcel(Parcel parcel) {
                return new Service(parcel);
            }

            public Service[] newArray(int i) {
                return new Service[i];
            }
        };
        final long mDuration;
        final String mName;
        final String mPackage;
        final String mProcess;

        public int describeContents() {
            return 0;
        }

        public Service(ServiceState serviceState) {
            this.mPackage = serviceState.getPackage();
            this.mName = serviceState.getName();
            this.mProcess = serviceState.getProcessName();
            this.mDuration = serviceState.dumpTime((PrintWriter) null, (String) null, 0, -1, 0, 0);
        }

        public Service(Parcel parcel) {
            this.mPackage = parcel.readString();
            this.mName = parcel.readString();
            this.mProcess = parcel.readString();
            this.mDuration = parcel.readLong();
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mPackage);
            parcel.writeString(this.mName);
            parcel.writeString(this.mProcess);
            parcel.writeLong(this.mDuration);
        }
    }
}
