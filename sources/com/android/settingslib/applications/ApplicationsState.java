package com.android.settingslib.applications;

import android.app.ActivityManager;
import android.app.AppGlobals;
import android.app.Application;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.format.Formatter;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.util.SparseArray;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.internal.util.ArrayUtils;
import com.android.settingslib.R$string;
import com.android.settingslib.Utils;
import com.android.settingslib.utils.ThreadUtils;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class ApplicationsState {
    public static final Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<AppEntry>() {
        private final Collator sCollator = Collator.getInstance();

        public int compare(AppEntry appEntry, AppEntry appEntry2) {
            ApplicationInfo applicationInfo;
            int compare;
            int compare2 = this.sCollator.compare(appEntry.label, appEntry2.label);
            if (compare2 != 0) {
                return compare2;
            }
            ApplicationInfo applicationInfo2 = appEntry.info;
            if (applicationInfo2 == null || (applicationInfo = appEntry2.info) == null || (compare = this.sCollator.compare(applicationInfo2.packageName, applicationInfo.packageName)) == 0) {
                return appEntry.info.uid - appEntry2.info.uid;
            }
            return compare;
        }
    };
    public static final Comparator<AppEntry> EXTERNAL_SIZE_COMPARATOR = new Comparator<AppEntry>() {
        public int compare(AppEntry appEntry, AppEntry appEntry2) {
            long j = appEntry.externalSize;
            long j2 = appEntry2.externalSize;
            if (j < j2) {
                return 1;
            }
            if (j > j2) {
                return -1;
            }
            return ApplicationsState.ALPHA_COMPARATOR.compare(appEntry, appEntry2);
        }
    };
    public static final AppFilter FILTER_ALL_ENABLED = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            ApplicationInfo applicationInfo = appEntry.info;
            return applicationInfo.enabled && !AppUtils.isInstant(applicationInfo);
        }
    };
    public static final AppFilter FILTER_APPS_EXCEPT_GAMES = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            boolean filterApp;
            synchronized (appEntry) {
                filterApp = ApplicationsState.FILTER_GAMES.filterApp(appEntry);
            }
            return !filterApp;
        }
    };
    public static final AppFilter FILTER_AUDIO = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            boolean z;
            synchronized (appEntry) {
                z = true;
                if (appEntry.info.category != 1) {
                    z = false;
                }
            }
            return z;
        }
    };
    public static final AppFilter FILTER_DISABLED = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            ApplicationInfo applicationInfo = appEntry.info;
            return !applicationInfo.enabled && !AppUtils.isInstant(applicationInfo);
        }
    };
    public static final AppFilter FILTER_DOWNLOADED_AND_LAUNCHER = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            if (AppUtils.isInstant(appEntry.info)) {
                return false;
            }
            if (ApplicationsState.hasFlag(appEntry.info.flags, 128) || !ApplicationsState.hasFlag(appEntry.info.flags, 1) || appEntry.hasLauncherEntry) {
                return true;
            }
            if (!ApplicationsState.hasFlag(appEntry.info.flags, 1) || !appEntry.isHomeApp) {
                return false;
            }
            return true;
        }
    };
    public static final AppFilter FILTER_DOWNLOADED_AND_LAUNCHER_AND_INSTANT = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            return AppUtils.isInstant(appEntry.info) || ApplicationsState.FILTER_DOWNLOADED_AND_LAUNCHER.filterApp(appEntry);
        }
    };
    public static final AppFilter FILTER_EVERYTHING = new AppFilter() {
        public boolean filterApp(AppEntry appEntry) {
            return true;
        }

        public void init() {
        }
    };
    public static final AppFilter FILTER_GAMES = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            boolean z;
            synchronized (appEntry.info) {
                if (!ApplicationsState.hasFlag(appEntry.info.flags, 33554432)) {
                    if (appEntry.info.category != 0) {
                        z = false;
                    }
                }
                z = true;
            }
            return z;
        }
    };
    public static final AppFilter FILTER_INSTANT = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            return AppUtils.isInstant(appEntry.info);
        }
    };
    public static final AppFilter FILTER_MOVIES = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            boolean z;
            synchronized (appEntry) {
                z = appEntry.info.category == 2;
            }
            return z;
        }
    };
    public static final AppFilter FILTER_NOT_HIDE = new AppFilter() {
        private String[] mHidePackageNames;

        public void init() {
        }

        public void init(Context context) {
            this.mHidePackageNames = context.getResources().getStringArray(17236045);
        }

        public boolean filterApp(AppEntry appEntry) {
            if (!ArrayUtils.contains(this.mHidePackageNames, appEntry.info.packageName)) {
                return true;
            }
            ApplicationInfo applicationInfo = appEntry.info;
            if (applicationInfo.enabled && applicationInfo.enabledSetting != 4) {
                return true;
            }
            return false;
        }
    };
    public static final AppFilter FILTER_OTHER_APPS = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            boolean z;
            synchronized (appEntry) {
                if (!ApplicationsState.FILTER_AUDIO.filterApp(appEntry) && !ApplicationsState.FILTER_GAMES.filterApp(appEntry) && !ApplicationsState.FILTER_MOVIES.filterApp(appEntry)) {
                    if (!ApplicationsState.FILTER_PHOTOS.filterApp(appEntry)) {
                        z = false;
                    }
                }
                z = true;
            }
            return !z;
        }
    };
    public static final AppFilter FILTER_PERSONAL = new AppFilter() {
        private int mCurrentUser;

        public void init() {
            this.mCurrentUser = ActivityManager.getCurrentUser();
        }

        public boolean filterApp(AppEntry appEntry) {
            return UserHandle.getUserId(appEntry.info.uid) == this.mCurrentUser;
        }
    };
    public static final AppFilter FILTER_PHOTOS = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            boolean z;
            synchronized (appEntry) {
                z = appEntry.info.category == 3;
            }
            return z;
        }
    };
    public static final AppFilter FILTER_THIRD_PARTY = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            if (!ApplicationsState.hasFlag(appEntry.info.flags, 128) && ApplicationsState.hasFlag(appEntry.info.flags, 1)) {
                return false;
            }
            return true;
        }
    };
    public static final AppFilter FILTER_WITHOUT_DISABLED_UNTIL_USED = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            return appEntry.info.enabledSetting != 4;
        }
    };
    public static final AppFilter FILTER_WITH_DOMAIN_URLS = new AppFilter() {
        public void init() {
        }

        public boolean filterApp(AppEntry appEntry) {
            return !AppUtils.isInstant(appEntry.info) && ApplicationsState.hasFlag(appEntry.info.privateFlags, 16);
        }
    };
    public static final AppFilter FILTER_WORK = new AppFilter() {
        private int mCurrentUser;

        public void init() {
            this.mCurrentUser = ActivityManager.getCurrentUser();
        }

        public boolean filterApp(AppEntry appEntry) {
            return UserHandle.getUserId(appEntry.info.uid) != this.mCurrentUser;
        }
    };
    public static final Comparator<AppEntry> INTERNAL_SIZE_COMPARATOR = new Comparator<AppEntry>() {
        public int compare(AppEntry appEntry, AppEntry appEntry2) {
            long j = appEntry.internalSize;
            long j2 = appEntry2.internalSize;
            if (j < j2) {
                return 1;
            }
            if (j > j2) {
                return -1;
            }
            return ApplicationsState.ALPHA_COMPARATOR.compare(appEntry, appEntry2);
        }
    };
    private static final Pattern REMOVE_DIACRITICALS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    public static final Comparator<AppEntry> SIZE_COMPARATOR = new Comparator<AppEntry>() {
        public int compare(AppEntry appEntry, AppEntry appEntry2) {
            long j = appEntry.size;
            long j2 = appEntry2.size;
            if (j < j2) {
                return 1;
            }
            if (j > j2) {
                return -1;
            }
            return ApplicationsState.ALPHA_COMPARATOR.compare(appEntry, appEntry2);
        }
    };
    static ApplicationsState sInstance;
    private static final Object sLock = new Object();
    final ArrayList<WeakReference<Session>> mActiveSessions = new ArrayList<>();
    final int mAdminRetrieveFlags;
    final ArrayList<AppEntry> mAppEntries = new ArrayList<>();
    List<ApplicationInfo> mApplications = new ArrayList();
    final BackgroundHandler mBackgroundHandler;
    final Context mContext;
    String mCurComputingSizePkg;
    int mCurComputingSizeUserId;
    UUID mCurComputingSizeUuid;
    long mCurId = 1;
    final IconDrawableFactory mDrawableFactory;
    final SparseArray<HashMap<String, AppEntry>> mEntriesMap = new SparseArray<>();
    boolean mHaveDisabledApps;
    boolean mHaveInstantApps;
    private InterestingConfigChanges mInterestingConfigChanges = new InterestingConfigChanges();
    final IPackageManager mIpm;
    final MainHandler mMainHandler = new MainHandler(Looper.getMainLooper());
    PackageIntentReceiver mPackageIntentReceiver;
    final PackageManager mPm;
    final ArrayList<Session> mRebuildingSessions = new ArrayList<>();
    boolean mResumed;
    final int mRetrieveFlags;
    final ArrayList<Session> mSessions = new ArrayList<>();
    boolean mSessionsChanged;
    final StorageStatsManager mStats;
    final HashMap<String, Boolean> mSystemModules = new HashMap<>();
    final HandlerThread mThread;
    final UserManager mUm;

    public interface Callbacks {
        void onAllSizesComputed();

        void onLauncherInfoChanged();

        void onLoadEntriesCompleted();

        void onPackageIconChanged();

        void onPackageListChanged();

        void onPackageSizeChanged(String str);

        void onRebuildComplete(ArrayList<AppEntry> arrayList);

        void onRunningStateChanged(boolean z);
    }

    public static class SizeInfo {
        public long cacheSize;
        public long codeSize;
        public long dataSize;
        public long externalCacheSize;
        public long externalCodeSize;
        public long externalDataSize;
    }

    /* access modifiers changed from: private */
    public static boolean hasFlag(int i, int i2) {
        return (i & i2) != 0;
    }

    public static ApplicationsState getInstance(Application application) {
        return getInstance(application, AppGlobals.getPackageManager());
    }

    static ApplicationsState getInstance(Application application, IPackageManager iPackageManager) {
        ApplicationsState applicationsState;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new ApplicationsState(application, iPackageManager);
            }
            applicationsState = sInstance;
        }
        return applicationsState;
    }

    /* access modifiers changed from: package-private */
    public void setInterestingConfigChanges(InterestingConfigChanges interestingConfigChanges) {
        this.mInterestingConfigChanges = interestingConfigChanges;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x00de */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ApplicationsState(android.app.Application r8, android.content.pm.IPackageManager r9) {
        /*
            r7 = this;
            r7.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r7.mSessions = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r7.mRebuildingSessions = r0
            com.android.settingslib.applications.InterestingConfigChanges r0 = new com.android.settingslib.applications.InterestingConfigChanges
            r0.<init>()
            r7.mInterestingConfigChanges = r0
            android.util.SparseArray r0 = new android.util.SparseArray
            r0.<init>()
            r7.mEntriesMap = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r7.mAppEntries = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r7.mApplications = r0
            r0 = 1
            r7.mCurId = r0
            java.util.HashMap r2 = new java.util.HashMap
            r2.<init>()
            r7.mSystemModules = r2
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            r7.mActiveSessions = r2
            com.android.settingslib.applications.ApplicationsState$MainHandler r2 = new com.android.settingslib.applications.ApplicationsState$MainHandler
            android.os.Looper r3 = android.os.Looper.getMainLooper()
            r2.<init>(r3)
            r7.mMainHandler = r2
            r7.mContext = r8
            android.content.pm.PackageManager r2 = r8.getPackageManager()
            r7.mPm = r2
            android.util.IconDrawableFactory r2 = android.util.IconDrawableFactory.newInstance(r8)
            r7.mDrawableFactory = r2
            r7.mIpm = r9
            java.lang.Class<android.os.UserManager> r9 = android.os.UserManager.class
            java.lang.Object r9 = r8.getSystemService(r9)
            android.os.UserManager r9 = (android.os.UserManager) r9
            r7.mUm = r9
            java.lang.Class<android.app.usage.StorageStatsManager> r2 = android.app.usage.StorageStatsManager.class
            java.lang.Object r8 = r8.getSystemService(r2)
            android.app.usage.StorageStatsManager r8 = (android.app.usage.StorageStatsManager) r8
            r7.mStats = r8
            int r8 = android.os.UserHandle.myUserId()
            int[] r8 = r9.getProfileIdsWithDisabled(r8)
            int r9 = r8.length
            r2 = 0
            r3 = r2
        L_0x0079:
            if (r3 >= r9) goto L_0x008a
            r4 = r8[r3]
            android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r5 = r7.mEntriesMap
            java.util.HashMap r6 = new java.util.HashMap
            r6.<init>()
            r5.put(r4, r6)
            int r3 = r3 + 1
            goto L_0x0079
        L_0x008a:
            android.os.HandlerThread r8 = new android.os.HandlerThread
            java.lang.String r9 = "ApplicationsState.Loader"
            r8.<init>(r9)
            r7.mThread = r8
            r8.start()
            com.android.settingslib.applications.ApplicationsState$BackgroundHandler r9 = new com.android.settingslib.applications.ApplicationsState$BackgroundHandler
            android.os.Looper r8 = r8.getLooper()
            r9.<init>(r8)
            r7.mBackgroundHandler = r9
            r8 = 4227584(0x408200, float:5.924107E-39)
            r7.mAdminRetrieveFlags = r8
            r8 = 33280(0x8200, float:4.6635E-41)
            r7.mRetrieveFlags = r8
            android.content.pm.PackageManager r8 = r7.mPm
            java.util.List r8 = r8.getInstalledModules(r2)
            java.util.Iterator r8 = r8.iterator()
        L_0x00b5:
            boolean r9 = r8.hasNext()
            if (r9 == 0) goto L_0x00d3
            java.lang.Object r9 = r8.next()
            android.content.pm.ModuleInfo r9 = (android.content.pm.ModuleInfo) r9
            java.util.HashMap<java.lang.String, java.lang.Boolean> r2 = r7.mSystemModules
            java.lang.String r3 = r9.getPackageName()
            boolean r9 = r9.isHidden()
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            r2.put(r3, r9)
            goto L_0x00b5
        L_0x00d3:
            android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r8 = r7.mEntriesMap
            monitor-enter(r8)
            android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r7 = r7.mEntriesMap     // Catch:{ InterruptedException -> 0x00de }
            r7.wait(r0)     // Catch:{ InterruptedException -> 0x00de }
            goto L_0x00de
        L_0x00dc:
            r7 = move-exception
            goto L_0x00e0
        L_0x00de:
            monitor-exit(r8)     // Catch:{ all -> 0x00dc }
            return
        L_0x00e0:
            monitor-exit(r8)     // Catch:{ all -> 0x00dc }
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.applications.ApplicationsState.<init>(android.app.Application, android.content.pm.IPackageManager):void");
    }

    public Looper getBackgroundLooper() {
        return this.mThread.getLooper();
    }

    public Session newSession(Callbacks callbacks) {
        return newSession(callbacks, (Lifecycle) null);
    }

    public Session newSession(Callbacks callbacks, Lifecycle lifecycle) {
        Session session = new Session(callbacks, lifecycle);
        synchronized (this.mEntriesMap) {
            this.mSessions.add(session);
        }
        return session;
    }

    /* access modifiers changed from: package-private */
    public void doResumeIfNeededLocked() {
        if (!this.mResumed) {
            this.mResumed = true;
            if (this.mPackageIntentReceiver == null) {
                PackageIntentReceiver packageIntentReceiver = new PackageIntentReceiver();
                this.mPackageIntentReceiver = packageIntentReceiver;
                packageIntentReceiver.registerReceiver();
            }
            List<ApplicationInfo> list = this.mApplications;
            this.mApplications = new ArrayList();
            for (UserInfo userInfo : this.mUm.getProfiles(UserHandle.myUserId())) {
                try {
                    if (this.mEntriesMap.indexOfKey(userInfo.id) < 0) {
                        this.mEntriesMap.put(userInfo.id, new HashMap());
                    }
                    this.mApplications.addAll(this.mIpm.getInstalledApplications(userInfo.isAdmin() ? this.mAdminRetrieveFlags : this.mRetrieveFlags, userInfo.id).getList());
                } catch (Exception e) {
                    Log.e("ApplicationsState", "Error during doResumeIfNeededLocked", e);
                }
            }
            int i = 0;
            if (this.mInterestingConfigChanges.applyNewConfig(this.mContext.getResources())) {
                clearEntries();
            } else {
                for (int i2 = 0; i2 < this.mAppEntries.size(); i2++) {
                    this.mAppEntries.get(i2).sizeStale = true;
                }
            }
            this.mHaveDisabledApps = false;
            this.mHaveInstantApps = false;
            while (i < this.mApplications.size()) {
                ApplicationInfo applicationInfo = this.mApplications.get(i);
                if (!applicationInfo.enabled) {
                    if (applicationInfo.enabledSetting != 3) {
                        this.mApplications.remove(i);
                        i--;
                        i++;
                    } else {
                        this.mHaveDisabledApps = true;
                    }
                }
                if (isHiddenModule(applicationInfo.packageName)) {
                    this.mApplications.remove(i);
                    i--;
                } else {
                    if (!this.mHaveInstantApps && AppUtils.isInstant(applicationInfo)) {
                        this.mHaveInstantApps = true;
                    }
                    AppEntry appEntry = (AppEntry) this.mEntriesMap.get(UserHandle.getUserId(applicationInfo.uid)).get(applicationInfo.packageName);
                    if (appEntry != null) {
                        appEntry.info = applicationInfo;
                    }
                }
                i++;
            }
            if (anyAppIsRemoved(list, this.mApplications)) {
                clearEntries();
            }
            this.mCurComputingSizePkg = null;
            if (!this.mBackgroundHandler.hasMessages(2)) {
                this.mBackgroundHandler.sendEmptyMessage(2);
            }
        }
    }

    private static boolean anyAppIsRemoved(List<ApplicationInfo> list, List<ApplicationInfo> list2) {
        HashSet hashSet;
        if (list.size() == 0) {
            return false;
        }
        if (list2.size() < list.size()) {
            return true;
        }
        HashMap hashMap = new HashMap();
        for (ApplicationInfo next : list2) {
            String valueOf = String.valueOf(UserHandle.getUserId(next.uid));
            HashSet hashSet2 = (HashSet) hashMap.get(valueOf);
            if (hashSet2 == null) {
                hashSet2 = new HashSet();
                hashMap.put(valueOf, hashSet2);
            }
            if (hasFlag(next.flags, 8388608)) {
                hashSet2.add(next.packageName);
            }
        }
        for (ApplicationInfo next2 : list) {
            if (hasFlag(next2.flags, 8388608) && ((hashSet = (HashSet) hashMap.get(String.valueOf(UserHandle.getUserId(next2.uid)))) == null || !hashSet.remove(next2.packageName))) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void clearEntries() {
        for (int i = 0; i < this.mEntriesMap.size(); i++) {
            this.mEntriesMap.valueAt(i).clear();
        }
        this.mAppEntries.clear();
    }

    public boolean haveDisabledApps() {
        return this.mHaveDisabledApps;
    }

    public boolean haveInstantApps() {
        return this.mHaveInstantApps;
    }

    /* access modifiers changed from: package-private */
    public boolean isHiddenModule(String str) {
        Boolean bool = this.mSystemModules.get(str);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* access modifiers changed from: package-private */
    public boolean isSystemModule(String str) {
        return this.mSystemModules.containsKey(str);
    }

    /* access modifiers changed from: package-private */
    public void doPauseIfNeededLocked() {
        if (this.mResumed) {
            int i = 0;
            while (i < this.mSessions.size()) {
                if (!this.mSessions.get(i).mResumed) {
                    i++;
                } else {
                    return;
                }
            }
            doPauseLocked();
        }
    }

    /* access modifiers changed from: package-private */
    public void doPauseLocked() {
        this.mResumed = false;
        PackageIntentReceiver packageIntentReceiver = this.mPackageIntentReceiver;
        if (packageIntentReceiver != null) {
            packageIntentReceiver.unregisterReceiver();
            this.mPackageIntentReceiver = null;
        }
    }

    public AppEntry getEntry(String str, int i) {
        AppEntry appEntry;
        synchronized (this.mEntriesMap) {
            appEntry = (AppEntry) this.mEntriesMap.get(i).get(str);
            if (appEntry == null) {
                ApplicationInfo appInfoLocked = getAppInfoLocked(str, i);
                if (appInfoLocked == null) {
                    try {
                        appInfoLocked = this.mIpm.getApplicationInfo(str, 0, i);
                    } catch (RemoteException e) {
                        Log.w("ApplicationsState", "getEntry couldn't reach PackageManager", e);
                        return null;
                    }
                }
                if (appInfoLocked != null) {
                    appEntry = getEntryLocked(appInfoLocked);
                }
            }
        }
        return appEntry;
    }

    private ApplicationInfo getAppInfoLocked(String str, int i) {
        for (int i2 = 0; i2 < this.mApplications.size(); i2++) {
            ApplicationInfo applicationInfo = this.mApplications.get(i2);
            if (str.equals(applicationInfo.packageName) && i == UserHandle.getUserId(applicationInfo.uid)) {
                return applicationInfo;
            }
        }
        return null;
    }

    public void ensureIcon(AppEntry appEntry) {
        if (appEntry.icon == null) {
            synchronized (appEntry) {
                appEntry.ensureIconLocked(this.mContext);
            }
        }
    }

    public void ensureLabelDescription(AppEntry appEntry) {
        if (appEntry.labelDescription == null) {
            synchronized (appEntry) {
                appEntry.ensureLabelDescriptionLocked(this.mContext);
            }
        }
    }

    public void requestSize(String str, int i) {
        synchronized (this.mEntriesMap) {
            AppEntry appEntry = (AppEntry) this.mEntriesMap.get(i).get(str);
            if (appEntry != null && hasFlag(appEntry.info.flags, 8388608)) {
                this.mBackgroundHandler.post(new ApplicationsState$$ExternalSyntheticLambda0(this, appEntry, str, i));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$requestSize$0(AppEntry appEntry, String str, int i) {
        try {
            StorageStats queryStatsForPackage = this.mStats.queryStatsForPackage(appEntry.info.storageUuid, str, UserHandle.of(i));
            long cacheQuotaBytes = this.mStats.getCacheQuotaBytes(appEntry.info.storageUuid.toString(), appEntry.info.uid);
            PackageStats packageStats = new PackageStats(str, i);
            packageStats.codeSize = queryStatsForPackage.getAppBytes();
            packageStats.dataSize = queryStatsForPackage.getDataBytes();
            packageStats.cacheSize = Math.min(queryStatsForPackage.getCacheBytes(), cacheQuotaBytes);
            this.mBackgroundHandler.mStatsObserver.onGetStatsCompleted(packageStats, true);
        } catch (PackageManager.NameNotFoundException | IOException e) {
            Log.w("ApplicationsState", "Failed to query stats: " + e);
            try {
                this.mBackgroundHandler.mStatsObserver.onGetStatsCompleted((PackageStats) null, false);
            } catch (RemoteException unused) {
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int indexOfApplicationInfoLocked(String str, int i) {
        for (int size = this.mApplications.size() - 1; size >= 0; size--) {
            ApplicationInfo applicationInfo = this.mApplications.get(size);
            if (applicationInfo.packageName.equals(str) && UserHandle.getUserId(applicationInfo.uid) == i) {
                return size;
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addPackage(java.lang.String r4, int r5) {
        /*
            r3 = this;
            android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r0 = r3.mEntriesMap     // Catch:{ RemoteException -> 0x0063 }
            monitor-enter(r0)     // Catch:{ RemoteException -> 0x0063 }
            boolean r1 = r3.mResumed     // Catch:{ all -> 0x0060 }
            if (r1 != 0) goto L_0x0009
            monitor-exit(r0)     // Catch:{ all -> 0x0060 }
            return
        L_0x0009:
            int r1 = r3.indexOfApplicationInfoLocked(r4, r5)     // Catch:{ all -> 0x0060 }
            if (r1 < 0) goto L_0x0011
            monitor-exit(r0)     // Catch:{ all -> 0x0060 }
            return
        L_0x0011:
            android.content.pm.IPackageManager r1 = r3.mIpm     // Catch:{ all -> 0x0060 }
            android.os.UserManager r2 = r3.mUm     // Catch:{ all -> 0x0060 }
            boolean r2 = r2.isUserAdmin(r5)     // Catch:{ all -> 0x0060 }
            if (r2 == 0) goto L_0x001e
            int r2 = r3.mAdminRetrieveFlags     // Catch:{ all -> 0x0060 }
            goto L_0x0020
        L_0x001e:
            int r2 = r3.mRetrieveFlags     // Catch:{ all -> 0x0060 }
        L_0x0020:
            android.content.pm.ApplicationInfo r4 = r1.getApplicationInfo(r4, r2, r5)     // Catch:{ all -> 0x0060 }
            if (r4 != 0) goto L_0x0028
            monitor-exit(r0)     // Catch:{ all -> 0x0060 }
            return
        L_0x0028:
            boolean r5 = r4.enabled     // Catch:{ all -> 0x0060 }
            r1 = 1
            if (r5 != 0) goto L_0x0036
            int r5 = r4.enabledSetting     // Catch:{ all -> 0x0060 }
            r2 = 3
            if (r5 == r2) goto L_0x0034
            monitor-exit(r0)     // Catch:{ all -> 0x0060 }
            return
        L_0x0034:
            r3.mHaveDisabledApps = r1     // Catch:{ all -> 0x0060 }
        L_0x0036:
            boolean r5 = com.android.settingslib.applications.AppUtils.isInstant(r4)     // Catch:{ all -> 0x0060 }
            if (r5 == 0) goto L_0x003e
            r3.mHaveInstantApps = r1     // Catch:{ all -> 0x0060 }
        L_0x003e:
            java.util.List<android.content.pm.ApplicationInfo> r5 = r3.mApplications     // Catch:{ all -> 0x0060 }
            r5.add(r4)     // Catch:{ all -> 0x0060 }
            com.android.settingslib.applications.ApplicationsState$BackgroundHandler r4 = r3.mBackgroundHandler     // Catch:{ all -> 0x0060 }
            r5 = 2
            boolean r4 = r4.hasMessages(r5)     // Catch:{ all -> 0x0060 }
            if (r4 != 0) goto L_0x0051
            com.android.settingslib.applications.ApplicationsState$BackgroundHandler r4 = r3.mBackgroundHandler     // Catch:{ all -> 0x0060 }
            r4.sendEmptyMessage(r5)     // Catch:{ all -> 0x0060 }
        L_0x0051:
            com.android.settingslib.applications.ApplicationsState$MainHandler r4 = r3.mMainHandler     // Catch:{ all -> 0x0060 }
            boolean r4 = r4.hasMessages(r5)     // Catch:{ all -> 0x0060 }
            if (r4 != 0) goto L_0x005e
            com.android.settingslib.applications.ApplicationsState$MainHandler r3 = r3.mMainHandler     // Catch:{ all -> 0x0060 }
            r3.sendEmptyMessage(r5)     // Catch:{ all -> 0x0060 }
        L_0x005e:
            monitor-exit(r0)     // Catch:{ all -> 0x0060 }
            goto L_0x0063
        L_0x0060:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0060 }
            throw r3     // Catch:{ RemoteException -> 0x0063 }
        L_0x0063:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.applications.ApplicationsState.addPackage(java.lang.String, int):void");
    }

    public void removePackage(String str, int i) {
        synchronized (this.mEntriesMap) {
            int indexOfApplicationInfoLocked = indexOfApplicationInfoLocked(str, i);
            if (indexOfApplicationInfoLocked >= 0) {
                AppEntry appEntry = (AppEntry) this.mEntriesMap.get(i).get(str);
                if (appEntry != null) {
                    this.mEntriesMap.get(i).remove(str);
                    this.mAppEntries.remove(appEntry);
                }
                ApplicationInfo applicationInfo = this.mApplications.get(indexOfApplicationInfoLocked);
                this.mApplications.remove(indexOfApplicationInfoLocked);
                if (!applicationInfo.enabled) {
                    this.mHaveDisabledApps = false;
                    Iterator<ApplicationInfo> it = this.mApplications.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (!it.next().enabled) {
                                this.mHaveDisabledApps = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                if (AppUtils.isInstant(applicationInfo)) {
                    this.mHaveInstantApps = false;
                    Iterator<ApplicationInfo> it2 = this.mApplications.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            if (AppUtils.isInstant(it2.next())) {
                                this.mHaveInstantApps = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                if (!this.mMainHandler.hasMessages(2)) {
                    this.mMainHandler.sendEmptyMessage(2);
                }
            }
        }
    }

    public void invalidatePackage(String str, int i) {
        removePackage(str, i);
        addPackage(str, i);
    }

    /* access modifiers changed from: private */
    public void addUser(int i) {
        if (ArrayUtils.contains(this.mUm.getProfileIdsWithDisabled(UserHandle.myUserId()), i)) {
            synchronized (this.mEntriesMap) {
                this.mEntriesMap.put(i, new HashMap());
                if (this.mResumed) {
                    doPauseLocked();
                    doResumeIfNeededLocked();
                }
                if (!this.mMainHandler.hasMessages(2)) {
                    this.mMainHandler.sendEmptyMessage(2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void removeUser(int i) {
        synchronized (this.mEntriesMap) {
            HashMap hashMap = this.mEntriesMap.get(i);
            if (hashMap != null) {
                for (AppEntry appEntry : hashMap.values()) {
                    this.mAppEntries.remove(appEntry);
                    this.mApplications.remove(appEntry.info);
                }
                this.mEntriesMap.remove(i);
                if (!this.mMainHandler.hasMessages(2)) {
                    this.mMainHandler.sendEmptyMessage(2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public AppEntry getEntryLocked(ApplicationInfo applicationInfo) {
        int userId = UserHandle.getUserId(applicationInfo.uid);
        AppEntry appEntry = (AppEntry) this.mEntriesMap.get(userId).get(applicationInfo.packageName);
        if (appEntry == null) {
            if (isHiddenModule(applicationInfo.packageName)) {
                return null;
            }
            Context context = this.mContext;
            long j = this.mCurId;
            this.mCurId = 1 + j;
            AppEntry appEntry2 = new AppEntry(context, applicationInfo, j);
            this.mEntriesMap.get(userId).put(applicationInfo.packageName, appEntry2);
            this.mAppEntries.add(appEntry2);
            return appEntry2;
        } else if (appEntry.info == applicationInfo) {
            return appEntry;
        } else {
            appEntry.info = applicationInfo;
            return appEntry;
        }
    }

    /* access modifiers changed from: private */
    public long getTotalInternalSize(PackageStats packageStats) {
        if (packageStats != null) {
            return (packageStats.codeSize + packageStats.dataSize) - packageStats.cacheSize;
        }
        return -2;
    }

    /* access modifiers changed from: private */
    public long getTotalExternalSize(PackageStats packageStats) {
        if (packageStats != null) {
            return packageStats.externalCodeSize + packageStats.externalDataSize + packageStats.externalCacheSize + packageStats.externalMediaSize + packageStats.externalObbSize;
        }
        return -2;
    }

    /* access modifiers changed from: private */
    public String getSizeStr(long j) {
        if (j >= 0) {
            return Formatter.formatFileSize(this.mContext, j);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void rebuildActiveSessions() {
        synchronized (this.mEntriesMap) {
            if (this.mSessionsChanged) {
                this.mActiveSessions.clear();
                for (int i = 0; i < this.mSessions.size(); i++) {
                    Session session = this.mSessions.get(i);
                    if (session.mResumed) {
                        this.mActiveSessions.add(new WeakReference(session));
                    }
                }
            }
        }
    }

    public class Session implements LifecycleObserver {
        final Callbacks mCallbacks;
        /* access modifiers changed from: private */
        public int mFlags = 15;
        private final boolean mHasLifecycle;
        ArrayList<AppEntry> mLastAppList;
        boolean mRebuildAsync;
        Comparator<AppEntry> mRebuildComparator;
        AppFilter mRebuildFilter;
        boolean mRebuildForeground;
        boolean mRebuildRequested;
        ArrayList<AppEntry> mRebuildResult;
        final Object mRebuildSync = new Object();
        boolean mResumed;

        Session(Callbacks callbacks, Lifecycle lifecycle) {
            this.mCallbacks = callbacks;
            if (lifecycle != null) {
                lifecycle.addObserver(this);
                this.mHasLifecycle = true;
                return;
            }
            this.mHasLifecycle = false;
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onResume() {
            synchronized (ApplicationsState.this.mEntriesMap) {
                if (!this.mResumed) {
                    this.mResumed = true;
                    ApplicationsState applicationsState = ApplicationsState.this;
                    applicationsState.mSessionsChanged = true;
                    applicationsState.doPauseLocked();
                    ApplicationsState.this.doResumeIfNeededLocked();
                }
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onPause() {
            synchronized (ApplicationsState.this.mEntriesMap) {
                if (this.mResumed) {
                    this.mResumed = false;
                    ApplicationsState applicationsState = ApplicationsState.this;
                    applicationsState.mSessionsChanged = true;
                    applicationsState.mBackgroundHandler.removeMessages(1, this);
                    ApplicationsState.this.doPauseIfNeededLocked();
                }
            }
        }

        public ArrayList<AppEntry> getAllApps() {
            ArrayList<AppEntry> arrayList;
            synchronized (ApplicationsState.this.mEntriesMap) {
                arrayList = new ArrayList<>(ApplicationsState.this.mAppEntries);
            }
            return arrayList;
        }

        public ArrayList<AppEntry> rebuild(AppFilter appFilter, Comparator<AppEntry> comparator) {
            return rebuild(appFilter, comparator, true);
        }

        public ArrayList<AppEntry> rebuild(AppFilter appFilter, Comparator<AppEntry> comparator, boolean z) {
            synchronized (this.mRebuildSync) {
                synchronized (ApplicationsState.this.mRebuildingSessions) {
                    ApplicationsState.this.mRebuildingSessions.add(this);
                    this.mRebuildRequested = true;
                    this.mRebuildAsync = true;
                    this.mRebuildFilter = appFilter;
                    this.mRebuildComparator = comparator;
                    this.mRebuildForeground = z;
                    this.mRebuildResult = null;
                    if (!ApplicationsState.this.mBackgroundHandler.hasMessages(1)) {
                        ApplicationsState.this.mBackgroundHandler.sendMessage(ApplicationsState.this.mBackgroundHandler.obtainMessage(1));
                    }
                }
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0025, code lost:
            if (r1 == null) goto L_0x002e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0027, code lost:
            r1.init(r7.this$0.mContext);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x002e, code lost:
            r3 = r7.this$0.mEntriesMap;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
            monitor-enter(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            r0 = new java.util.ArrayList(r7.this$0.mAppEntries);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x003d, code lost:
            r3 = new java.util.ArrayList<>();
            r0 = r0.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x004a, code lost:
            if (r0.hasNext() == false) goto L_0x0072;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x004c, code lost:
            r4 = (com.android.settingslib.applications.ApplicationsState.AppEntry) r0.next();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0052, code lost:
            if (r4 == null) goto L_0x0046;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0054, code lost:
            if (r1 == null) goto L_0x005c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x005a, code lost:
            if (r1.filterApp(r4) == false) goto L_0x0046;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x005c, code lost:
            r5 = r7.this$0.mEntriesMap;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0060, code lost:
            monitor-enter(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0061, code lost:
            if (r2 == null) goto L_0x006a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
            r4.ensureLabel(r7.this$0.mContext);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x006a, code lost:
            r3.add(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x006d, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0072, code lost:
            if (r2 == null) goto L_0x0081;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0074, code lost:
            r0 = r7.this$0.mEntriesMap;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x0078, code lost:
            monitor-enter(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
            java.util.Collections.sort(r3, r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x007c, code lost:
            monitor-exit(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x0081, code lost:
            r0 = r7.mRebuildSync;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x0083, code lost:
            monitor-enter(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0086, code lost:
            if (r7.mRebuildRequested != false) goto L_0x00b0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x0088, code lost:
            r7.mLastAppList = r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x008c, code lost:
            if (r7.mRebuildAsync != false) goto L_0x0096;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x008e, code lost:
            r7.mRebuildResult = r3;
            r7.mRebuildSync.notifyAll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x009f, code lost:
            if (r7.this$0.mMainHandler.hasMessages(1, r7) != false) goto L_0x00b0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x00a1, code lost:
            r7.this$0.mMainHandler.sendMessage(r7.this$0.mMainHandler.obtainMessage(1, r7));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x00b0, code lost:
            monitor-exit(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:0x00b1, code lost:
            android.os.Process.setThreadPriority(10);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x00b6, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleRebuildList() {
            /*
                r7 = this;
                boolean r0 = r7.mResumed
                if (r0 != 0) goto L_0x0005
                return
            L_0x0005:
                java.lang.Object r0 = r7.mRebuildSync
                monitor-enter(r0)
                boolean r1 = r7.mRebuildRequested     // Catch:{ all -> 0x00bd }
                if (r1 != 0) goto L_0x000e
                monitor-exit(r0)     // Catch:{ all -> 0x00bd }
                return
            L_0x000e:
                com.android.settingslib.applications.ApplicationsState$AppFilter r1 = r7.mRebuildFilter     // Catch:{ all -> 0x00bd }
                java.util.Comparator<com.android.settingslib.applications.ApplicationsState$AppEntry> r2 = r7.mRebuildComparator     // Catch:{ all -> 0x00bd }
                r3 = 0
                r7.mRebuildRequested = r3     // Catch:{ all -> 0x00bd }
                r4 = 0
                r7.mRebuildFilter = r4     // Catch:{ all -> 0x00bd }
                r7.mRebuildComparator = r4     // Catch:{ all -> 0x00bd }
                boolean r4 = r7.mRebuildForeground     // Catch:{ all -> 0x00bd }
                if (r4 == 0) goto L_0x0024
                r4 = -2
                android.os.Process.setThreadPriority(r4)     // Catch:{ all -> 0x00bd }
                r7.mRebuildForeground = r3     // Catch:{ all -> 0x00bd }
            L_0x0024:
                monitor-exit(r0)     // Catch:{ all -> 0x00bd }
                if (r1 == 0) goto L_0x002e
                com.android.settingslib.applications.ApplicationsState r0 = com.android.settingslib.applications.ApplicationsState.this
                android.content.Context r0 = r0.mContext
                r1.init(r0)
            L_0x002e:
                com.android.settingslib.applications.ApplicationsState r0 = com.android.settingslib.applications.ApplicationsState.this
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r3 = r0.mEntriesMap
                monitor-enter(r3)
                java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ all -> 0x00ba }
                com.android.settingslib.applications.ApplicationsState r4 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00ba }
                java.util.ArrayList<com.android.settingslib.applications.ApplicationsState$AppEntry> r4 = r4.mAppEntries     // Catch:{ all -> 0x00ba }
                r0.<init>(r4)     // Catch:{ all -> 0x00ba }
                monitor-exit(r3)     // Catch:{ all -> 0x00ba }
                java.util.ArrayList r3 = new java.util.ArrayList
                r3.<init>()
                java.util.Iterator r0 = r0.iterator()
            L_0x0046:
                boolean r4 = r0.hasNext()
                if (r4 == 0) goto L_0x0072
                java.lang.Object r4 = r0.next()
                com.android.settingslib.applications.ApplicationsState$AppEntry r4 = (com.android.settingslib.applications.ApplicationsState.AppEntry) r4
                if (r4 == 0) goto L_0x0046
                if (r1 == 0) goto L_0x005c
                boolean r5 = r1.filterApp(r4)
                if (r5 == 0) goto L_0x0046
            L_0x005c:
                com.android.settingslib.applications.ApplicationsState r5 = com.android.settingslib.applications.ApplicationsState.this
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r5 = r5.mEntriesMap
                monitor-enter(r5)
                if (r2 == 0) goto L_0x006a
                com.android.settingslib.applications.ApplicationsState r6 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x006f }
                android.content.Context r6 = r6.mContext     // Catch:{ all -> 0x006f }
                r4.ensureLabel(r6)     // Catch:{ all -> 0x006f }
            L_0x006a:
                r3.add(r4)     // Catch:{ all -> 0x006f }
                monitor-exit(r5)     // Catch:{ all -> 0x006f }
                goto L_0x0046
            L_0x006f:
                r7 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x006f }
                throw r7
            L_0x0072:
                if (r2 == 0) goto L_0x0081
                com.android.settingslib.applications.ApplicationsState r0 = com.android.settingslib.applications.ApplicationsState.this
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r0 = r0.mEntriesMap
                monitor-enter(r0)
                java.util.Collections.sort(r3, r2)     // Catch:{ all -> 0x007e }
                monitor-exit(r0)     // Catch:{ all -> 0x007e }
                goto L_0x0081
            L_0x007e:
                r7 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x007e }
                throw r7
            L_0x0081:
                java.lang.Object r0 = r7.mRebuildSync
                monitor-enter(r0)
                boolean r1 = r7.mRebuildRequested     // Catch:{ all -> 0x00b7 }
                if (r1 != 0) goto L_0x00b0
                r7.mLastAppList = r3     // Catch:{ all -> 0x00b7 }
                boolean r1 = r7.mRebuildAsync     // Catch:{ all -> 0x00b7 }
                if (r1 != 0) goto L_0x0096
                r7.mRebuildResult = r3     // Catch:{ all -> 0x00b7 }
                java.lang.Object r7 = r7.mRebuildSync     // Catch:{ all -> 0x00b7 }
                r7.notifyAll()     // Catch:{ all -> 0x00b7 }
                goto L_0x00b0
            L_0x0096:
                com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00b7 }
                com.android.settingslib.applications.ApplicationsState$MainHandler r1 = r1.mMainHandler     // Catch:{ all -> 0x00b7 }
                r2 = 1
                boolean r1 = r1.hasMessages(r2, r7)     // Catch:{ all -> 0x00b7 }
                if (r1 != 0) goto L_0x00b0
                com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00b7 }
                com.android.settingslib.applications.ApplicationsState$MainHandler r1 = r1.mMainHandler     // Catch:{ all -> 0x00b7 }
                android.os.Message r1 = r1.obtainMessage(r2, r7)     // Catch:{ all -> 0x00b7 }
                com.android.settingslib.applications.ApplicationsState r7 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00b7 }
                com.android.settingslib.applications.ApplicationsState$MainHandler r7 = r7.mMainHandler     // Catch:{ all -> 0x00b7 }
                r7.sendMessage(r1)     // Catch:{ all -> 0x00b7 }
            L_0x00b0:
                monitor-exit(r0)     // Catch:{ all -> 0x00b7 }
                r7 = 10
                android.os.Process.setThreadPriority(r7)
                return
            L_0x00b7:
                r7 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x00b7 }
                throw r7
            L_0x00ba:
                r7 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x00ba }
                throw r7
            L_0x00bd:
                r7 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x00bd }
                throw r7
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.applications.ApplicationsState.Session.handleRebuildList():void");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void onDestroy() {
            if (!this.mHasLifecycle) {
                onPause();
            }
            synchronized (ApplicationsState.this.mEntriesMap) {
                ApplicationsState.this.mSessions.remove(this);
            }
        }
    }

    class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            ApplicationsState.this.rebuildActiveSessions();
            switch (message.what) {
                case 1:
                    Session session = (Session) message.obj;
                    Iterator<WeakReference<Session>> it = ApplicationsState.this.mActiveSessions.iterator();
                    while (it.hasNext()) {
                        Session session2 = (Session) it.next().get();
                        if (session2 != null && session2 == session) {
                            session.mCallbacks.onRebuildComplete(session.mLastAppList);
                        }
                    }
                    return;
                case 2:
                    Iterator<WeakReference<Session>> it2 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it2.hasNext()) {
                        Session session3 = (Session) it2.next().get();
                        if (session3 != null) {
                            session3.mCallbacks.onPackageListChanged();
                        }
                    }
                    return;
                case 3:
                    Iterator<WeakReference<Session>> it3 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it3.hasNext()) {
                        Session session4 = (Session) it3.next().get();
                        if (session4 != null) {
                            session4.mCallbacks.onPackageIconChanged();
                        }
                    }
                    return;
                case 4:
                    Iterator<WeakReference<Session>> it4 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it4.hasNext()) {
                        Session session5 = (Session) it4.next().get();
                        if (session5 != null) {
                            session5.mCallbacks.onPackageSizeChanged((String) message.obj);
                        }
                    }
                    return;
                case 5:
                    Iterator<WeakReference<Session>> it5 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it5.hasNext()) {
                        Session session6 = (Session) it5.next().get();
                        if (session6 != null) {
                            session6.mCallbacks.onAllSizesComputed();
                        }
                    }
                    return;
                case 6:
                    Iterator<WeakReference<Session>> it6 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it6.hasNext()) {
                        Session session7 = (Session) it6.next().get();
                        if (session7 != null) {
                            session7.mCallbacks.onRunningStateChanged(message.arg1 != 0);
                        }
                    }
                    return;
                case 7:
                    Iterator<WeakReference<Session>> it7 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it7.hasNext()) {
                        Session session8 = (Session) it7.next().get();
                        if (session8 != null) {
                            session8.mCallbacks.onLauncherInfoChanged();
                        }
                    }
                    return;
                case 8:
                    Iterator<WeakReference<Session>> it8 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it8.hasNext()) {
                        Session session9 = (Session) it8.next().get();
                        if (session9 != null) {
                            session9.mCallbacks.onLoadEntriesCompleted();
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private class BackgroundHandler extends Handler {
        boolean mRunning;
        final IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
            /* JADX WARNING: Code restructure failed: missing block: B:46:0x00fd, code lost:
                return;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onGetStatsCompleted(android.content.pm.PackageStats r13, boolean r14) {
                /*
                    r12 = this;
                    if (r14 != 0) goto L_0x0003
                    return
                L_0x0003:
                    com.android.settingslib.applications.ApplicationsState$BackgroundHandler r14 = com.android.settingslib.applications.ApplicationsState.BackgroundHandler.this
                    com.android.settingslib.applications.ApplicationsState r14 = com.android.settingslib.applications.ApplicationsState.this
                    android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r14 = r14.mEntriesMap
                    monitor-enter(r14)
                    com.android.settingslib.applications.ApplicationsState$BackgroundHandler r0 = com.android.settingslib.applications.ApplicationsState.BackgroundHandler.this     // Catch:{ all -> 0x00fe }
                    com.android.settingslib.applications.ApplicationsState r0 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00fe }
                    android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r0 = r0.mEntriesMap     // Catch:{ all -> 0x00fe }
                    int r1 = r13.userHandle     // Catch:{ all -> 0x00fe }
                    java.lang.Object r0 = r0.get(r1)     // Catch:{ all -> 0x00fe }
                    java.util.HashMap r0 = (java.util.HashMap) r0     // Catch:{ all -> 0x00fe }
                    if (r0 != 0) goto L_0x001c
                    monitor-exit(r14)     // Catch:{ all -> 0x00fe }
                    return
                L_0x001c:
                    java.lang.String r1 = r13.packageName     // Catch:{ all -> 0x00fe }
                    java.lang.Object r0 = r0.get(r1)     // Catch:{ all -> 0x00fe }
                    com.android.settingslib.applications.ApplicationsState$AppEntry r0 = (com.android.settingslib.applications.ApplicationsState.AppEntry) r0     // Catch:{ all -> 0x00fe }
                    if (r0 == 0) goto L_0x00db
                    monitor-enter(r0)     // Catch:{ all -> 0x00fe }
                    r1 = 0
                    r0.sizeStale = r1     // Catch:{ all -> 0x00d8 }
                    r2 = 0
                    r0.sizeLoadStart = r2     // Catch:{ all -> 0x00d8 }
                    long r2 = r13.externalCodeSize     // Catch:{ all -> 0x00d8 }
                    long r4 = r13.externalObbSize     // Catch:{ all -> 0x00d8 }
                    long r2 = r2 + r4
                    long r4 = r13.externalDataSize     // Catch:{ all -> 0x00d8 }
                    long r6 = r13.externalMediaSize     // Catch:{ all -> 0x00d8 }
                    long r4 = r4 + r6
                    long r6 = r2 + r4
                    com.android.settingslib.applications.ApplicationsState$BackgroundHandler r8 = com.android.settingslib.applications.ApplicationsState.BackgroundHandler.this     // Catch:{ all -> 0x00d8 }
                    com.android.settingslib.applications.ApplicationsState r8 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00d8 }
                    long r8 = r8.getTotalInternalSize(r13)     // Catch:{ all -> 0x00d8 }
                    long r6 = r6 + r8
                    long r8 = r0.size     // Catch:{ all -> 0x00d8 }
                    int r8 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
                    if (r8 != 0) goto L_0x0075
                    long r8 = r0.cacheSize     // Catch:{ all -> 0x00d8 }
                    long r10 = r13.cacheSize     // Catch:{ all -> 0x00d8 }
                    int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                    if (r8 != 0) goto L_0x0075
                    long r8 = r0.codeSize     // Catch:{ all -> 0x00d8 }
                    long r10 = r13.codeSize     // Catch:{ all -> 0x00d8 }
                    int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                    if (r8 != 0) goto L_0x0075
                    long r8 = r0.dataSize     // Catch:{ all -> 0x00d8 }
                    long r10 = r13.dataSize     // Catch:{ all -> 0x00d8 }
                    int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                    if (r8 != 0) goto L_0x0075
                    long r8 = r0.externalCodeSize     // Catch:{ all -> 0x00d8 }
                    int r8 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
                    if (r8 != 0) goto L_0x0075
                    long r8 = r0.externalDataSize     // Catch:{ all -> 0x00d8 }
                    int r8 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
                    if (r8 != 0) goto L_0x0075
                    long r8 = r0.externalCacheSize     // Catch:{ all -> 0x00d8 }
                    long r10 = r13.externalCacheSize     // Catch:{ all -> 0x00d8 }
                    int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                    if (r8 == 0) goto L_0x00be
                L_0x0075:
                    r0.size = r6     // Catch:{ all -> 0x00d8 }
                    long r8 = r13.cacheSize     // Catch:{ all -> 0x00d8 }
                    r0.cacheSize = r8     // Catch:{ all -> 0x00d8 }
                    long r8 = r13.codeSize     // Catch:{ all -> 0x00d8 }
                    r0.codeSize = r8     // Catch:{ all -> 0x00d8 }
                    long r8 = r13.dataSize     // Catch:{ all -> 0x00d8 }
                    r0.dataSize = r8     // Catch:{ all -> 0x00d8 }
                    r0.externalCodeSize = r2     // Catch:{ all -> 0x00d8 }
                    r0.externalDataSize = r4     // Catch:{ all -> 0x00d8 }
                    long r1 = r13.externalCacheSize     // Catch:{ all -> 0x00d8 }
                    r0.externalCacheSize = r1     // Catch:{ all -> 0x00d8 }
                    com.android.settingslib.applications.ApplicationsState$BackgroundHandler r1 = com.android.settingslib.applications.ApplicationsState.BackgroundHandler.this     // Catch:{ all -> 0x00d8 }
                    com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00d8 }
                    java.lang.String r1 = r1.getSizeStr(r6)     // Catch:{ all -> 0x00d8 }
                    r0.sizeStr = r1     // Catch:{ all -> 0x00d8 }
                    com.android.settingslib.applications.ApplicationsState$BackgroundHandler r1 = com.android.settingslib.applications.ApplicationsState.BackgroundHandler.this     // Catch:{ all -> 0x00d8 }
                    com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00d8 }
                    long r1 = r1.getTotalInternalSize(r13)     // Catch:{ all -> 0x00d8 }
                    r0.internalSize = r1     // Catch:{ all -> 0x00d8 }
                    com.android.settingslib.applications.ApplicationsState$BackgroundHandler r3 = com.android.settingslib.applications.ApplicationsState.BackgroundHandler.this     // Catch:{ all -> 0x00d8 }
                    com.android.settingslib.applications.ApplicationsState r3 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00d8 }
                    java.lang.String r1 = r3.getSizeStr(r1)     // Catch:{ all -> 0x00d8 }
                    r0.internalSizeStr = r1     // Catch:{ all -> 0x00d8 }
                    com.android.settingslib.applications.ApplicationsState$BackgroundHandler r1 = com.android.settingslib.applications.ApplicationsState.BackgroundHandler.this     // Catch:{ all -> 0x00d8 }
                    com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00d8 }
                    long r1 = r1.getTotalExternalSize(r13)     // Catch:{ all -> 0x00d8 }
                    r0.externalSize = r1     // Catch:{ all -> 0x00d8 }
                    com.android.settingslib.applications.ApplicationsState$BackgroundHandler r3 = com.android.settingslib.applications.ApplicationsState.BackgroundHandler.this     // Catch:{ all -> 0x00d8 }
                    com.android.settingslib.applications.ApplicationsState r3 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00d8 }
                    java.lang.String r1 = r3.getSizeStr(r1)     // Catch:{ all -> 0x00d8 }
                    r0.externalSizeStr = r1     // Catch:{ all -> 0x00d8 }
                    r1 = 1
                L_0x00be:
                    monitor-exit(r0)     // Catch:{ all -> 0x00d8 }
                    if (r1 == 0) goto L_0x00db
                    com.android.settingslib.applications.ApplicationsState$BackgroundHandler r0 = com.android.settingslib.applications.ApplicationsState.BackgroundHandler.this     // Catch:{ all -> 0x00fe }
                    com.android.settingslib.applications.ApplicationsState r0 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00fe }
                    com.android.settingslib.applications.ApplicationsState$MainHandler r0 = r0.mMainHandler     // Catch:{ all -> 0x00fe }
                    r1 = 4
                    java.lang.String r2 = r13.packageName     // Catch:{ all -> 0x00fe }
                    android.os.Message r0 = r0.obtainMessage(r1, r2)     // Catch:{ all -> 0x00fe }
                    com.android.settingslib.applications.ApplicationsState$BackgroundHandler r1 = com.android.settingslib.applications.ApplicationsState.BackgroundHandler.this     // Catch:{ all -> 0x00fe }
                    com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00fe }
                    com.android.settingslib.applications.ApplicationsState$MainHandler r1 = r1.mMainHandler     // Catch:{ all -> 0x00fe }
                    r1.sendMessage(r0)     // Catch:{ all -> 0x00fe }
                    goto L_0x00db
                L_0x00d8:
                    r12 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x00d8 }
                    throw r12     // Catch:{ all -> 0x00fe }
                L_0x00db:
                    com.android.settingslib.applications.ApplicationsState$BackgroundHandler r0 = com.android.settingslib.applications.ApplicationsState.BackgroundHandler.this     // Catch:{ all -> 0x00fe }
                    com.android.settingslib.applications.ApplicationsState r0 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00fe }
                    java.lang.String r0 = r0.mCurComputingSizePkg     // Catch:{ all -> 0x00fe }
                    if (r0 == 0) goto L_0x00fc
                    java.lang.String r1 = r13.packageName     // Catch:{ all -> 0x00fe }
                    boolean r0 = r0.equals(r1)     // Catch:{ all -> 0x00fe }
                    if (r0 == 0) goto L_0x00fc
                    com.android.settingslib.applications.ApplicationsState$BackgroundHandler r12 = com.android.settingslib.applications.ApplicationsState.BackgroundHandler.this     // Catch:{ all -> 0x00fe }
                    com.android.settingslib.applications.ApplicationsState r0 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x00fe }
                    int r1 = r0.mCurComputingSizeUserId     // Catch:{ all -> 0x00fe }
                    int r13 = r13.userHandle     // Catch:{ all -> 0x00fe }
                    if (r1 != r13) goto L_0x00fc
                    r13 = 0
                    r0.mCurComputingSizePkg = r13     // Catch:{ all -> 0x00fe }
                    r13 = 7
                    r12.sendEmptyMessage(r13)     // Catch:{ all -> 0x00fe }
                L_0x00fc:
                    monitor-exit(r14)     // Catch:{ all -> 0x00fe }
                    return
                L_0x00fe:
                    r12 = move-exception
                    monitor-exit(r14)     // Catch:{ all -> 0x00fe }
                    throw r12
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.applications.ApplicationsState.BackgroundHandler.C14561.onGetStatsCompleted(android.content.pm.PackageStats, boolean):void");
            }
        };

        BackgroundHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:224:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x00e6, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r19) {
            /*
                r18 = this;
                r0 = r18
                r1 = r19
                com.android.settingslib.applications.ApplicationsState r2 = com.android.settingslib.applications.ApplicationsState.this
                java.util.ArrayList<com.android.settingslib.applications.ApplicationsState$Session> r2 = r2.mRebuildingSessions
                monitor-enter(r2)
                com.android.settingslib.applications.ApplicationsState r3 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037e }
                java.util.ArrayList<com.android.settingslib.applications.ApplicationsState$Session> r3 = r3.mRebuildingSessions     // Catch:{ all -> 0x037e }
                int r3 = r3.size()     // Catch:{ all -> 0x037e }
                r4 = 0
                if (r3 <= 0) goto L_0x0025
                java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ all -> 0x037e }
                com.android.settingslib.applications.ApplicationsState r5 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037e }
                java.util.ArrayList<com.android.settingslib.applications.ApplicationsState$Session> r5 = r5.mRebuildingSessions     // Catch:{ all -> 0x037e }
                r3.<init>(r5)     // Catch:{ all -> 0x037e }
                com.android.settingslib.applications.ApplicationsState r5 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037e }
                java.util.ArrayList<com.android.settingslib.applications.ApplicationsState$Session> r5 = r5.mRebuildingSessions     // Catch:{ all -> 0x037e }
                r5.clear()     // Catch:{ all -> 0x037e }
                goto L_0x0026
            L_0x0025:
                r3 = r4
            L_0x0026:
                monitor-exit(r2)     // Catch:{ all -> 0x037e }
                if (r3 == 0) goto L_0x003d
                java.util.Iterator r2 = r3.iterator()
            L_0x002d:
                boolean r3 = r2.hasNext()
                if (r3 == 0) goto L_0x003d
                java.lang.Object r3 = r2.next()
                com.android.settingslib.applications.ApplicationsState$Session r3 = (com.android.settingslib.applications.ApplicationsState.Session) r3
                r3.handleRebuildList()
                goto L_0x002d
            L_0x003d:
                com.android.settingslib.applications.ApplicationsState r2 = com.android.settingslib.applications.ApplicationsState.this
                java.util.ArrayList<com.android.settingslib.applications.ApplicationsState$Session> r2 = r2.mSessions
                int r2 = r0.getCombinedSessionFlags(r2)
                int r3 = r1.what
                r5 = 8388608(0x800000, float:1.17549435E-38)
                r6 = 3
                r7 = 7
                r8 = 8
                r9 = 5
                r10 = 2
                r11 = 4
                r12 = 6
                r13 = 0
                r14 = 1
                switch(r3) {
                    case 2: goto L_0x02b9;
                    case 3: goto L_0x025f;
                    case 4: goto L_0x0190;
                    case 5: goto L_0x0190;
                    case 6: goto L_0x0116;
                    case 7: goto L_0x0058;
                    default: goto L_0x0056;
                }
            L_0x0056:
                goto L_0x037d
            L_0x0058:
                boolean r1 = com.android.settingslib.applications.ApplicationsState.hasFlag(r2, r11)
                if (r1 == 0) goto L_0x037d
                com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r1 = r1.mEntriesMap
                monitor-enter(r1)
                com.android.settingslib.applications.ApplicationsState r2 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0113 }
                java.lang.String r2 = r2.mCurComputingSizePkg     // Catch:{ all -> 0x0113 }
                if (r2 == 0) goto L_0x006b
                monitor-exit(r1)     // Catch:{ all -> 0x0113 }
                return
            L_0x006b:
                long r2 = android.os.SystemClock.uptimeMillis()     // Catch:{ all -> 0x0113 }
                r4 = r13
            L_0x0070:
                com.android.settingslib.applications.ApplicationsState r6 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0113 }
                java.util.ArrayList<com.android.settingslib.applications.ApplicationsState$AppEntry> r6 = r6.mAppEntries     // Catch:{ all -> 0x0113 }
                int r6 = r6.size()     // Catch:{ all -> 0x0113 }
                if (r4 >= r6) goto L_0x00ea
                com.android.settingslib.applications.ApplicationsState r6 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0113 }
                java.util.ArrayList<com.android.settingslib.applications.ApplicationsState$AppEntry> r6 = r6.mAppEntries     // Catch:{ all -> 0x0113 }
                java.lang.Object r6 = r6.get(r4)     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState$AppEntry r6 = (com.android.settingslib.applications.ApplicationsState.AppEntry) r6     // Catch:{ all -> 0x0113 }
                android.content.pm.ApplicationInfo r7 = r6.info     // Catch:{ all -> 0x0113 }
                int r7 = r7.flags     // Catch:{ all -> 0x0113 }
                boolean r7 = com.android.settingslib.applications.ApplicationsState.hasFlag(r7, r5)     // Catch:{ all -> 0x0113 }
                if (r7 == 0) goto L_0x00e7
                long r7 = r6.size     // Catch:{ all -> 0x0113 }
                r10 = -1
                int r7 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1))
                if (r7 == 0) goto L_0x009a
                boolean r7 = r6.sizeStale     // Catch:{ all -> 0x0113 }
                if (r7 == 0) goto L_0x00e7
            L_0x009a:
                long r4 = r6.sizeLoadStart     // Catch:{ all -> 0x0113 }
                r7 = 0
                int r7 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1))
                if (r7 == 0) goto L_0x00aa
                r7 = 20000(0x4e20, double:9.8813E-320)
                long r7 = r2 - r7
                int r4 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1))
                if (r4 >= 0) goto L_0x00e5
            L_0x00aa:
                boolean r4 = r0.mRunning     // Catch:{ all -> 0x0113 }
                if (r4 != 0) goto L_0x00c3
                r0.mRunning = r14     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState r4 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState$MainHandler r4 = r4.mMainHandler     // Catch:{ all -> 0x0113 }
                java.lang.Integer r5 = java.lang.Integer.valueOf(r14)     // Catch:{ all -> 0x0113 }
                android.os.Message r4 = r4.obtainMessage(r12, r5)     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState r5 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState$MainHandler r5 = r5.mMainHandler     // Catch:{ all -> 0x0113 }
                r5.sendMessage(r4)     // Catch:{ all -> 0x0113 }
            L_0x00c3:
                r6.sizeLoadStart = r2     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState r2 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0113 }
                android.content.pm.ApplicationInfo r3 = r6.info     // Catch:{ all -> 0x0113 }
                java.util.UUID r4 = r3.storageUuid     // Catch:{ all -> 0x0113 }
                r2.mCurComputingSizeUuid = r4     // Catch:{ all -> 0x0113 }
                java.lang.String r4 = r3.packageName     // Catch:{ all -> 0x0113 }
                r2.mCurComputingSizePkg = r4     // Catch:{ all -> 0x0113 }
                int r3 = r3.uid     // Catch:{ all -> 0x0113 }
                int r3 = android.os.UserHandle.getUserId(r3)     // Catch:{ all -> 0x0113 }
                r2.mCurComputingSizeUserId = r3     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState r2 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState$BackgroundHandler r2 = r2.mBackgroundHandler     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState$BackgroundHandler$$ExternalSyntheticLambda0 r3 = new com.android.settingslib.applications.ApplicationsState$BackgroundHandler$$ExternalSyntheticLambda0     // Catch:{ all -> 0x0113 }
                r3.<init>(r0)     // Catch:{ all -> 0x0113 }
                r2.post(r3)     // Catch:{ all -> 0x0113 }
            L_0x00e5:
                monitor-exit(r1)     // Catch:{ all -> 0x0113 }
                return
            L_0x00e7:
                int r4 = r4 + 1
                goto L_0x0070
            L_0x00ea:
                com.android.settingslib.applications.ApplicationsState r2 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState$MainHandler r2 = r2.mMainHandler     // Catch:{ all -> 0x0113 }
                boolean r2 = r2.hasMessages(r9)     // Catch:{ all -> 0x0113 }
                if (r2 != 0) goto L_0x0110
                com.android.settingslib.applications.ApplicationsState r2 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState$MainHandler r2 = r2.mMainHandler     // Catch:{ all -> 0x0113 }
                r2.sendEmptyMessage(r9)     // Catch:{ all -> 0x0113 }
                r0.mRunning = r13     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState r2 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState$MainHandler r2 = r2.mMainHandler     // Catch:{ all -> 0x0113 }
                java.lang.Integer r3 = java.lang.Integer.valueOf(r13)     // Catch:{ all -> 0x0113 }
                android.os.Message r2 = r2.obtainMessage(r12, r3)     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState r0 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0113 }
                com.android.settingslib.applications.ApplicationsState$MainHandler r0 = r0.mMainHandler     // Catch:{ all -> 0x0113 }
                r0.sendMessage(r2)     // Catch:{ all -> 0x0113 }
            L_0x0110:
                monitor-exit(r1)     // Catch:{ all -> 0x0113 }
                goto L_0x037d
            L_0x0113:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0113 }
                throw r0
            L_0x0116:
                boolean r1 = com.android.settingslib.applications.ApplicationsState.hasFlag(r2, r10)
                if (r1 == 0) goto L_0x018b
                com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r1 = r1.mEntriesMap
                monitor-enter(r1)
                r2 = r13
            L_0x0122:
                com.android.settingslib.applications.ApplicationsState r3 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0188 }
                java.util.ArrayList<com.android.settingslib.applications.ApplicationsState$AppEntry> r3 = r3.mAppEntries     // Catch:{ all -> 0x0188 }
                int r3 = r3.size()     // Catch:{ all -> 0x0188 }
                if (r13 >= r3) goto L_0x016d
                if (r2 >= r10) goto L_0x016d
                com.android.settingslib.applications.ApplicationsState r3 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x0188 }
                java.util.ArrayList<com.android.settingslib.applications.ApplicationsState$AppEntry> r3 = r3.mAppEntries     // Catch:{ all -> 0x0188 }
                java.lang.Object r3 = r3.get(r13)     // Catch:{ all -> 0x0188 }
                com.android.settingslib.applications.ApplicationsState$AppEntry r3 = (com.android.settingslib.applications.ApplicationsState.AppEntry) r3     // Catch:{ all -> 0x0188 }
                android.graphics.drawable.Drawable r4 = r3.icon     // Catch:{ all -> 0x0188 }
                if (r4 == 0) goto L_0x0140
                boolean r4 = r3.mounted     // Catch:{ all -> 0x0188 }
                if (r4 != 0) goto L_0x0167
            L_0x0140:
                monitor-enter(r3)     // Catch:{ all -> 0x0188 }
                com.android.settingslib.applications.ApplicationsState r4 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x016a }
                android.content.Context r4 = r4.mContext     // Catch:{ all -> 0x016a }
                boolean r4 = r3.ensureIconLocked(r4)     // Catch:{ all -> 0x016a }
                if (r4 == 0) goto L_0x0166
                boolean r4 = r0.mRunning     // Catch:{ all -> 0x016a }
                if (r4 != 0) goto L_0x0164
                r0.mRunning = r14     // Catch:{ all -> 0x016a }
                com.android.settingslib.applications.ApplicationsState r4 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x016a }
                com.android.settingslib.applications.ApplicationsState$MainHandler r4 = r4.mMainHandler     // Catch:{ all -> 0x016a }
                java.lang.Integer r5 = java.lang.Integer.valueOf(r14)     // Catch:{ all -> 0x016a }
                android.os.Message r4 = r4.obtainMessage(r12, r5)     // Catch:{ all -> 0x016a }
                com.android.settingslib.applications.ApplicationsState r5 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x016a }
                com.android.settingslib.applications.ApplicationsState$MainHandler r5 = r5.mMainHandler     // Catch:{ all -> 0x016a }
                r5.sendMessage(r4)     // Catch:{ all -> 0x016a }
            L_0x0164:
                int r2 = r2 + 1
            L_0x0166:
                monitor-exit(r3)     // Catch:{ all -> 0x016a }
            L_0x0167:
                int r13 = r13 + 1
                goto L_0x0122
            L_0x016a:
                r0 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x016a }
                throw r0     // Catch:{ all -> 0x0188 }
            L_0x016d:
                monitor-exit(r1)     // Catch:{ all -> 0x0188 }
                if (r2 <= 0) goto L_0x0181
                com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this
                com.android.settingslib.applications.ApplicationsState$MainHandler r1 = r1.mMainHandler
                boolean r1 = r1.hasMessages(r6)
                if (r1 != 0) goto L_0x0181
                com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this
                com.android.settingslib.applications.ApplicationsState$MainHandler r1 = r1.mMainHandler
                r1.sendEmptyMessage(r6)
            L_0x0181:
                if (r2 < r10) goto L_0x018b
                r0.sendEmptyMessage(r12)
                goto L_0x037d
            L_0x0188:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0188 }
                throw r0
            L_0x018b:
                r0.sendEmptyMessage(r7)
                goto L_0x037d
            L_0x0190:
                if (r3 != r11) goto L_0x0198
                boolean r3 = com.android.settingslib.applications.ApplicationsState.hasFlag(r2, r8)
                if (r3 != 0) goto L_0x01a4
            L_0x0198:
                int r3 = r1.what
                if (r3 != r9) goto L_0x024f
                r3 = 16
                boolean r2 = com.android.settingslib.applications.ApplicationsState.hasFlag(r2, r3)
                if (r2 == 0) goto L_0x024f
            L_0x01a4:
                android.content.Intent r2 = new android.content.Intent
                java.lang.String r3 = "android.intent.action.MAIN"
                r2.<init>(r3, r4)
                int r3 = r1.what
                if (r3 != r11) goto L_0x01b2
                java.lang.String r3 = "android.intent.category.LAUNCHER"
                goto L_0x01b4
            L_0x01b2:
                java.lang.String r3 = "android.intent.category.LEANBACK_LAUNCHER"
            L_0x01b4:
                r2.addCategory(r3)
                r3 = r13
            L_0x01b8:
                com.android.settingslib.applications.ApplicationsState r4 = com.android.settingslib.applications.ApplicationsState.this
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r4 = r4.mEntriesMap
                int r4 = r4.size()
                if (r3 >= r4) goto L_0x023e
                com.android.settingslib.applications.ApplicationsState r4 = com.android.settingslib.applications.ApplicationsState.this
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r4 = r4.mEntriesMap
                int r4 = r4.keyAt(r3)
                com.android.settingslib.applications.ApplicationsState r5 = com.android.settingslib.applications.ApplicationsState.this
                android.content.pm.PackageManager r5 = r5.mPm
                r6 = 786944(0xc0200, float:1.102743E-39)
                java.util.List r5 = r5.queryIntentActivitiesAsUser(r2, r6, r4)
                com.android.settingslib.applications.ApplicationsState r6 = com.android.settingslib.applications.ApplicationsState.this
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r6 = r6.mEntriesMap
                monitor-enter(r6)
                com.android.settingslib.applications.ApplicationsState r8 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x023b }
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r8 = r8.mEntriesMap     // Catch:{ all -> 0x023b }
                java.lang.Object r8 = r8.valueAt(r3)     // Catch:{ all -> 0x023b }
                java.util.HashMap r8 = (java.util.HashMap) r8     // Catch:{ all -> 0x023b }
                int r10 = r5.size()     // Catch:{ all -> 0x023b }
                r15 = r13
            L_0x01e9:
                if (r15 >= r10) goto L_0x0232
                java.lang.Object r16 = r5.get(r15)     // Catch:{ all -> 0x023b }
                r13 = r16
                android.content.pm.ResolveInfo r13 = (android.content.pm.ResolveInfo) r13     // Catch:{ all -> 0x023b }
                android.content.pm.ActivityInfo r12 = r13.activityInfo     // Catch:{ all -> 0x023b }
                java.lang.String r12 = r12.packageName     // Catch:{ all -> 0x023b }
                java.lang.Object r17 = r8.get(r12)     // Catch:{ all -> 0x023b }
                r9 = r17
                com.android.settingslib.applications.ApplicationsState$AppEntry r9 = (com.android.settingslib.applications.ApplicationsState.AppEntry) r9     // Catch:{ all -> 0x023b }
                if (r9 == 0) goto L_0x020d
                r9.hasLauncherEntry = r14     // Catch:{ all -> 0x023b }
                boolean r12 = r9.launcherEntryEnabled     // Catch:{ all -> 0x023b }
                android.content.pm.ActivityInfo r13 = r13.activityInfo     // Catch:{ all -> 0x023b }
                boolean r13 = r13.enabled     // Catch:{ all -> 0x023b }
                r12 = r12 | r13
                r9.launcherEntryEnabled = r12     // Catch:{ all -> 0x023b }
                goto L_0x022b
            L_0x020d:
                java.lang.String r9 = "ApplicationsState"
                java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x023b }
                r13.<init>()     // Catch:{ all -> 0x023b }
                java.lang.String r14 = "Cannot find pkg: "
                r13.append(r14)     // Catch:{ all -> 0x023b }
                r13.append(r12)     // Catch:{ all -> 0x023b }
                java.lang.String r12 = " on user "
                r13.append(r12)     // Catch:{ all -> 0x023b }
                r13.append(r4)     // Catch:{ all -> 0x023b }
                java.lang.String r12 = r13.toString()     // Catch:{ all -> 0x023b }
                android.util.Log.w(r9, r12)     // Catch:{ all -> 0x023b }
            L_0x022b:
                int r15 = r15 + 1
                r9 = 5
                r12 = 6
                r13 = 0
                r14 = 1
                goto L_0x01e9
            L_0x0232:
                monitor-exit(r6)     // Catch:{ all -> 0x023b }
                int r3 = r3 + 1
                r9 = 5
                r12 = 6
                r13 = 0
                r14 = 1
                goto L_0x01b8
            L_0x023b:
                r0 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x023b }
                throw r0
            L_0x023e:
                com.android.settingslib.applications.ApplicationsState r2 = com.android.settingslib.applications.ApplicationsState.this
                com.android.settingslib.applications.ApplicationsState$MainHandler r2 = r2.mMainHandler
                boolean r2 = r2.hasMessages(r7)
                if (r2 != 0) goto L_0x024f
                com.android.settingslib.applications.ApplicationsState r2 = com.android.settingslib.applications.ApplicationsState.this
                com.android.settingslib.applications.ApplicationsState$MainHandler r2 = r2.mMainHandler
                r2.sendEmptyMessage(r7)
            L_0x024f:
                int r1 = r1.what
                if (r1 != r11) goto L_0x0259
                r1 = 5
                r0.sendEmptyMessage(r1)
                goto L_0x037d
            L_0x0259:
                r1 = 6
                r0.sendEmptyMessage(r1)
                goto L_0x037d
            L_0x025f:
                r1 = r14
                boolean r2 = com.android.settingslib.applications.ApplicationsState.hasFlag(r2, r1)
                if (r2 == 0) goto L_0x02b4
                java.util.ArrayList r1 = new java.util.ArrayList
                r1.<init>()
                com.android.settingslib.applications.ApplicationsState r2 = com.android.settingslib.applications.ApplicationsState.this
                android.content.pm.PackageManager r2 = r2.mPm
                r2.getHomeActivities(r1)
                com.android.settingslib.applications.ApplicationsState r2 = com.android.settingslib.applications.ApplicationsState.this
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r2 = r2.mEntriesMap
                monitor-enter(r2)
                com.android.settingslib.applications.ApplicationsState r3 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x02b1 }
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r3 = r3.mEntriesMap     // Catch:{ all -> 0x02b1 }
                int r3 = r3.size()     // Catch:{ all -> 0x02b1 }
                r13 = 0
            L_0x0280:
                if (r13 >= r3) goto L_0x02af
                com.android.settingslib.applications.ApplicationsState r4 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x02b1 }
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r4 = r4.mEntriesMap     // Catch:{ all -> 0x02b1 }
                java.lang.Object r4 = r4.valueAt(r13)     // Catch:{ all -> 0x02b1 }
                java.util.HashMap r4 = (java.util.HashMap) r4     // Catch:{ all -> 0x02b1 }
                java.util.Iterator r5 = r1.iterator()     // Catch:{ all -> 0x02b1 }
            L_0x0290:
                boolean r6 = r5.hasNext()     // Catch:{ all -> 0x02b1 }
                if (r6 == 0) goto L_0x02ac
                java.lang.Object r6 = r5.next()     // Catch:{ all -> 0x02b1 }
                android.content.pm.ResolveInfo r6 = (android.content.pm.ResolveInfo) r6     // Catch:{ all -> 0x02b1 }
                android.content.pm.ActivityInfo r6 = r6.activityInfo     // Catch:{ all -> 0x02b1 }
                java.lang.String r6 = r6.packageName     // Catch:{ all -> 0x02b1 }
                java.lang.Object r6 = r4.get(r6)     // Catch:{ all -> 0x02b1 }
                com.android.settingslib.applications.ApplicationsState$AppEntry r6 = (com.android.settingslib.applications.ApplicationsState.AppEntry) r6     // Catch:{ all -> 0x02b1 }
                if (r6 == 0) goto L_0x0290
                r7 = 1
                r6.isHomeApp = r7     // Catch:{ all -> 0x02b1 }
                goto L_0x0290
            L_0x02ac:
                int r13 = r13 + 1
                goto L_0x0280
            L_0x02af:
                monitor-exit(r2)     // Catch:{ all -> 0x02b1 }
                goto L_0x02b4
            L_0x02b1:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x02b1 }
                throw r0
            L_0x02b4:
                r0.sendEmptyMessage(r11)
                goto L_0x037d
            L_0x02b9:
                com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r1 = r1.mEntriesMap
                monitor-enter(r1)
                r2 = 0
                r3 = 0
            L_0x02c0:
                com.android.settingslib.applications.ApplicationsState r4 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037a }
                java.util.List<android.content.pm.ApplicationInfo> r4 = r4.mApplications     // Catch:{ all -> 0x037a }
                int r4 = r4.size()     // Catch:{ all -> 0x037a }
                if (r3 >= r4) goto L_0x035d
                r4 = 6
                if (r2 >= r4) goto L_0x035d
                boolean r4 = r0.mRunning     // Catch:{ all -> 0x037a }
                if (r4 != 0) goto L_0x02e9
                r4 = 1
                r0.mRunning = r4     // Catch:{ all -> 0x037a }
                com.android.settingslib.applications.ApplicationsState r7 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037a }
                com.android.settingslib.applications.ApplicationsState$MainHandler r7 = r7.mMainHandler     // Catch:{ all -> 0x037a }
                java.lang.Integer r9 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x037a }
                r11 = 6
                android.os.Message r7 = r7.obtainMessage(r11, r9)     // Catch:{ all -> 0x037a }
                com.android.settingslib.applications.ApplicationsState r9 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037a }
                com.android.settingslib.applications.ApplicationsState$MainHandler r9 = r9.mMainHandler     // Catch:{ all -> 0x037a }
                r9.sendMessage(r7)     // Catch:{ all -> 0x037a }
                goto L_0x02ea
            L_0x02e9:
                r4 = 1
            L_0x02ea:
                com.android.settingslib.applications.ApplicationsState r7 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037a }
                java.util.List<android.content.pm.ApplicationInfo> r7 = r7.mApplications     // Catch:{ all -> 0x037a }
                java.lang.Object r7 = r7.get(r3)     // Catch:{ all -> 0x037a }
                android.content.pm.ApplicationInfo r7 = (android.content.pm.ApplicationInfo) r7     // Catch:{ all -> 0x037a }
                int r9 = r7.uid     // Catch:{ all -> 0x037a }
                int r9 = android.os.UserHandle.getUserId(r9)     // Catch:{ all -> 0x037a }
                com.android.settingslib.applications.ApplicationsState r11 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037a }
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r11 = r11.mEntriesMap     // Catch:{ all -> 0x037a }
                java.lang.Object r11 = r11.get(r9)     // Catch:{ all -> 0x037a }
                java.util.HashMap r11 = (java.util.HashMap) r11     // Catch:{ all -> 0x037a }
                java.lang.String r12 = r7.packageName     // Catch:{ all -> 0x037a }
                java.lang.Object r11 = r11.get(r12)     // Catch:{ all -> 0x037a }
                if (r11 != 0) goto L_0x0313
                int r2 = r2 + 1
                com.android.settingslib.applications.ApplicationsState r11 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037a }
                com.android.settingslib.applications.ApplicationsState.AppEntry unused = r11.getEntryLocked(r7)     // Catch:{ all -> 0x037a }
            L_0x0313:
                if (r9 == 0) goto L_0x0358
                com.android.settingslib.applications.ApplicationsState r9 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037a }
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r9 = r9.mEntriesMap     // Catch:{ all -> 0x037a }
                r11 = 0
                int r9 = r9.indexOfKey(r11)     // Catch:{ all -> 0x037a }
                if (r9 < 0) goto L_0x0356
                com.android.settingslib.applications.ApplicationsState r9 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037a }
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r9 = r9.mEntriesMap     // Catch:{ all -> 0x037a }
                java.lang.Object r9 = r9.get(r11)     // Catch:{ all -> 0x037a }
                java.util.HashMap r9 = (java.util.HashMap) r9     // Catch:{ all -> 0x037a }
                java.lang.String r11 = r7.packageName     // Catch:{ all -> 0x037a }
                java.lang.Object r9 = r9.get(r11)     // Catch:{ all -> 0x037a }
                com.android.settingslib.applications.ApplicationsState$AppEntry r9 = (com.android.settingslib.applications.ApplicationsState.AppEntry) r9     // Catch:{ all -> 0x037a }
                if (r9 == 0) goto L_0x0358
                android.content.pm.ApplicationInfo r11 = r9.info     // Catch:{ all -> 0x037a }
                int r11 = r11.flags     // Catch:{ all -> 0x037a }
                boolean r11 = com.android.settingslib.applications.ApplicationsState.hasFlag(r11, r5)     // Catch:{ all -> 0x037a }
                if (r11 != 0) goto L_0x0358
                com.android.settingslib.applications.ApplicationsState r11 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037a }
                android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settingslib.applications.ApplicationsState$AppEntry>> r11 = r11.mEntriesMap     // Catch:{ all -> 0x037a }
                r12 = 0
                java.lang.Object r11 = r11.get(r12)     // Catch:{ all -> 0x037a }
                java.util.HashMap r11 = (java.util.HashMap) r11     // Catch:{ all -> 0x037a }
                java.lang.String r7 = r7.packageName     // Catch:{ all -> 0x037a }
                r11.remove(r7)     // Catch:{ all -> 0x037a }
                com.android.settingslib.applications.ApplicationsState r7 = com.android.settingslib.applications.ApplicationsState.this     // Catch:{ all -> 0x037a }
                java.util.ArrayList<com.android.settingslib.applications.ApplicationsState$AppEntry> r7 = r7.mAppEntries     // Catch:{ all -> 0x037a }
                r7.remove(r9)     // Catch:{ all -> 0x037a }
                goto L_0x0359
            L_0x0356:
                r12 = r11
                goto L_0x0359
            L_0x0358:
                r12 = 0
            L_0x0359:
                int r3 = r3 + 1
                goto L_0x02c0
            L_0x035d:
                monitor-exit(r1)     // Catch:{ all -> 0x037a }
                r1 = 6
                if (r2 < r1) goto L_0x0365
                r0.sendEmptyMessage(r10)
                goto L_0x037d
            L_0x0365:
                com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this
                com.android.settingslib.applications.ApplicationsState$MainHandler r1 = r1.mMainHandler
                boolean r1 = r1.hasMessages(r8)
                if (r1 != 0) goto L_0x0376
                com.android.settingslib.applications.ApplicationsState r1 = com.android.settingslib.applications.ApplicationsState.this
                com.android.settingslib.applications.ApplicationsState$MainHandler r1 = r1.mMainHandler
                r1.sendEmptyMessage(r8)
            L_0x0376:
                r0.sendEmptyMessage(r6)
                goto L_0x037d
            L_0x037a:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x037a }
                throw r0
            L_0x037d:
                return
            L_0x037e:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x037e }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.applications.ApplicationsState.BackgroundHandler.handleMessage(android.os.Message):void");
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$0() {
            try {
                ApplicationsState applicationsState = ApplicationsState.this;
                StorageStats queryStatsForPackage = applicationsState.mStats.queryStatsForPackage(applicationsState.mCurComputingSizeUuid, applicationsState.mCurComputingSizePkg, UserHandle.of(applicationsState.mCurComputingSizeUserId));
                ApplicationsState applicationsState2 = ApplicationsState.this;
                PackageStats packageStats = new PackageStats(applicationsState2.mCurComputingSizePkg, applicationsState2.mCurComputingSizeUserId);
                packageStats.codeSize = queryStatsForPackage.getAppBytes();
                packageStats.dataSize = queryStatsForPackage.getDataBytes();
                packageStats.cacheSize = queryStatsForPackage.getCacheBytes();
                this.mStatsObserver.onGetStatsCompleted(packageStats, true);
            } catch (PackageManager.NameNotFoundException | IOException e) {
                Log.w("ApplicationsState", "Failed to query stats: " + e);
                try {
                    this.mStatsObserver.onGetStatsCompleted((PackageStats) null, false);
                } catch (RemoteException unused) {
                }
            }
        }

        private int getCombinedSessionFlags(List<Session> list) {
            int i;
            synchronized (ApplicationsState.this.mEntriesMap) {
                i = 0;
                for (Session access$300 : list) {
                    i |= access$300.mFlags;
                }
            }
            return i;
        }
    }

    private class PackageIntentReceiver extends BroadcastReceiver {
        private PackageIntentReceiver() {
        }

        /* access modifiers changed from: package-private */
        public void registerReceiver() {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
            intentFilter.addDataScheme("package");
            ApplicationsState.this.mContext.registerReceiver(this, intentFilter);
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
            intentFilter2.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
            ApplicationsState.this.mContext.registerReceiver(this, intentFilter2);
            IntentFilter intentFilter3 = new IntentFilter();
            intentFilter3.addAction("android.intent.action.USER_ADDED");
            intentFilter3.addAction("android.intent.action.USER_REMOVED");
            ApplicationsState.this.mContext.registerReceiver(this, intentFilter3);
        }

        /* access modifiers changed from: package-private */
        public void unregisterReceiver() {
            ApplicationsState.this.mContext.unregisterReceiver(this);
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int i = 0;
            if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
                String encodedSchemeSpecificPart = intent.getData().getEncodedSchemeSpecificPart();
                while (i < ApplicationsState.this.mEntriesMap.size()) {
                    ApplicationsState applicationsState = ApplicationsState.this;
                    applicationsState.addPackage(encodedSchemeSpecificPart, applicationsState.mEntriesMap.keyAt(i));
                    i++;
                }
            } else if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                String encodedSchemeSpecificPart2 = intent.getData().getEncodedSchemeSpecificPart();
                while (i < ApplicationsState.this.mEntriesMap.size()) {
                    ApplicationsState applicationsState2 = ApplicationsState.this;
                    applicationsState2.removePackage(encodedSchemeSpecificPart2, applicationsState2.mEntriesMap.keyAt(i));
                    i++;
                }
            } else if ("android.intent.action.PACKAGE_CHANGED".equals(action)) {
                String encodedSchemeSpecificPart3 = intent.getData().getEncodedSchemeSpecificPart();
                while (i < ApplicationsState.this.mEntriesMap.size()) {
                    ApplicationsState applicationsState3 = ApplicationsState.this;
                    applicationsState3.invalidatePackage(encodedSchemeSpecificPart3, applicationsState3.mEntriesMap.keyAt(i));
                    i++;
                }
            } else if ("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE".equals(action) || "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE".equals(action)) {
                String[] stringArrayExtra = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                if (stringArrayExtra != null && stringArrayExtra.length != 0 && "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE".equals(action)) {
                    for (String str : stringArrayExtra) {
                        for (int i2 = 0; i2 < ApplicationsState.this.mEntriesMap.size(); i2++) {
                            ApplicationsState applicationsState4 = ApplicationsState.this;
                            applicationsState4.invalidatePackage(str, applicationsState4.mEntriesMap.keyAt(i2));
                        }
                    }
                }
            } else if ("android.intent.action.USER_ADDED".equals(action)) {
                ApplicationsState.this.addUser(intent.getIntExtra("android.intent.extra.user_handle", -10000));
            } else if ("android.intent.action.USER_REMOVED".equals(action)) {
                ApplicationsState.this.removeUser(intent.getIntExtra("android.intent.extra.user_handle", -10000));
            }
        }
    }

    public static class AppEntry extends SizeInfo {
        public final File apkFile;
        public long externalSize;
        public String externalSizeStr;
        public Object extraInfo;
        public boolean hasLauncherEntry;
        public Drawable icon;

        /* renamed from: id */
        public final long f105id;
        public ApplicationInfo info;
        public long internalSize;
        public String internalSizeStr;
        public boolean isHomeApp;
        public String label;
        public String labelDescription;
        public boolean launcherEntryEnabled;
        public boolean mounted;
        public long size = -1;
        public long sizeLoadStart;
        public boolean sizeStale = true;
        public String sizeStr;

        public AppEntry(Context context, ApplicationInfo applicationInfo, long j) {
            this.apkFile = new File(applicationInfo.sourceDir);
            this.f105id = j;
            this.info = applicationInfo;
            ensureLabel(context);
            ThreadUtils.postOnBackgroundThread((Runnable) new ApplicationsState$AppEntry$$ExternalSyntheticLambda0(this, context));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(Context context) {
            if (this.icon == null) {
                ensureIconLocked(context);
            }
            if (this.labelDescription == null) {
                ensureLabelDescriptionLocked(context);
            }
        }

        public void ensureLabel(Context context) {
            if (this.label != null && this.mounted) {
                return;
            }
            if (!this.apkFile.exists()) {
                this.mounted = false;
                this.label = this.info.packageName;
                return;
            }
            this.mounted = true;
            CharSequence loadLabel = this.info.loadLabel(context.getPackageManager());
            this.label = loadLabel != null ? loadLabel.toString() : this.info.packageName;
        }

        /* access modifiers changed from: package-private */
        public boolean ensureIconLocked(Context context) {
            if (this.icon == null) {
                if (this.apkFile.exists()) {
                    this.icon = Utils.getBadgedIcon(context, this.info);
                    return true;
                }
                this.mounted = false;
                this.icon = context.getDrawable(17303669);
            } else if (!this.mounted && this.apkFile.exists()) {
                this.mounted = true;
                this.icon = Utils.getBadgedIcon(context, this.info);
                return true;
            }
            return false;
        }

        public void ensureLabelDescriptionLocked(Context context) {
            if (UserManager.get(context).isManagedProfile(UserHandle.getUserId(this.info.uid))) {
                this.labelDescription = context.getString(R$string.accessibility_work_profile_app_description, new Object[]{this.label});
                return;
            }
            this.labelDescription = this.label;
        }
    }

    public interface AppFilter {
        boolean filterApp(AppEntry appEntry);

        void init();

        void init(Context context) {
            init();
        }
    }

    public static class VolumeFilter implements AppFilter {
        private final String mVolumeUuid;

        public void init() {
        }

        public VolumeFilter(String str) {
            this.mVolumeUuid = str;
        }

        public boolean filterApp(AppEntry appEntry) {
            return Objects.equals(appEntry.info.volumeUuid, this.mVolumeUuid);
        }
    }

    public static class CompoundFilter implements AppFilter {
        private final AppFilter mFirstFilter;
        private final AppFilter mSecondFilter;

        public CompoundFilter(AppFilter appFilter, AppFilter appFilter2) {
            this.mFirstFilter = appFilter;
            this.mSecondFilter = appFilter2;
        }

        public void init(Context context) {
            this.mFirstFilter.init(context);
            this.mSecondFilter.init(context);
        }

        public void init() {
            this.mFirstFilter.init();
            this.mSecondFilter.init();
        }

        public boolean filterApp(AppEntry appEntry) {
            return this.mFirstFilter.filterApp(appEntry) && this.mSecondFilter.filterApp(appEntry);
        }
    }
}
