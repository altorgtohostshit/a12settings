package com.android.settings.applications;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
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
import android.util.Log;
import android.util.SparseArray;
import com.android.settings.R;
import com.android.settingslib.Utils;
import com.android.settingslib.applications.InterestingConfigChanges;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class RunningState {
    static Object sGlobalLock = new Object();
    static RunningState sInstance;
    final ArrayList<ProcessItem> mAllProcessItems = new ArrayList<>();
    final ActivityManager mAm;
    final Context mApplicationContext;
    final Comparator<MergedItem> mBackgroundComparator = new Comparator<MergedItem>() {
        public int compare(MergedItem mergedItem, MergedItem mergedItem2) {
            int i = mergedItem.mUserId;
            int i2 = mergedItem2.mUserId;
            if (i != i2) {
                int i3 = RunningState.this.mMyUserId;
                if (i == i3) {
                    return -1;
                }
                if (i2 == i3) {
                    return 1;
                }
                if (i < i2) {
                    return -1;
                }
                return 1;
            }
            ProcessItem processItem = mergedItem.mProcess;
            ProcessItem processItem2 = mergedItem2.mProcess;
            if (processItem == processItem2) {
                String str = mergedItem.mLabel;
                String str2 = mergedItem2.mLabel;
                if (str == str2) {
                    return 0;
                }
                if (str != null) {
                    return str.compareTo(str2);
                }
                return -1;
            } else if (processItem == null) {
                return -1;
            } else {
                if (processItem2 == null) {
                    return 1;
                }
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = processItem.mRunningProcessInfo;
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo2 = processItem2.mRunningProcessInfo;
                boolean z = runningAppProcessInfo.importance >= 400;
                if (z == (runningAppProcessInfo2.importance >= 400)) {
                    boolean z2 = (runningAppProcessInfo.flags & 4) != 0;
                    if (z2 == ((runningAppProcessInfo2.flags & 4) != 0)) {
                        int i4 = runningAppProcessInfo.lru;
                        int i5 = runningAppProcessInfo2.lru;
                        if (i4 == i5) {
                            String str3 = processItem.mLabel;
                            String str4 = processItem2.mLabel;
                            if (str3 == str4) {
                                return 0;
                            }
                            if (str3 == null) {
                                return 1;
                            }
                            if (str4 == null) {
                                return -1;
                            }
                            return str3.compareTo(str4);
                        } else if (i4 < i5) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else if (z2) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (z) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    };
    final BackgroundHandler mBackgroundHandler;
    ArrayList<MergedItem> mBackgroundItems = new ArrayList<>();
    long mBackgroundProcessMemory;
    final HandlerThread mBackgroundThread;
    long mForegroundProcessMemory;
    final Handler mHandler = new Handler() {
        int mNextUpdate = 0;

        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
            removeMessages(4);
            sendMessageDelayed(obtainMessage(4), 1000);
            r3 = r2.this$0.mRefreshUiListener;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0027, code lost:
            if (r3 == null) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0029, code lost:
            r3.onRefreshUi(r2.mNextUpdate);
            r2.mNextUpdate = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r3) {
            /*
                r2 = this;
                int r0 = r3.what
                r1 = 3
                if (r0 == r1) goto L_0x0035
                r3 = 4
                if (r0 == r3) goto L_0x0009
                goto L_0x003e
            L_0x0009:
                com.android.settings.applications.RunningState r0 = com.android.settings.applications.RunningState.this
                java.lang.Object r0 = r0.mLock
                monitor-enter(r0)
                com.android.settings.applications.RunningState r1 = com.android.settings.applications.RunningState.this     // Catch:{ all -> 0x0032 }
                boolean r1 = r1.mResumed     // Catch:{ all -> 0x0032 }
                if (r1 != 0) goto L_0x0016
                monitor-exit(r0)     // Catch:{ all -> 0x0032 }
                return
            L_0x0016:
                monitor-exit(r0)     // Catch:{ all -> 0x0032 }
                r2.removeMessages(r3)
                android.os.Message r3 = r2.obtainMessage(r3)
                r0 = 1000(0x3e8, double:4.94E-321)
                r2.sendMessageDelayed(r3, r0)
                com.android.settings.applications.RunningState r3 = com.android.settings.applications.RunningState.this
                com.android.settings.applications.RunningState$OnRefreshUiListener r3 = r3.mRefreshUiListener
                if (r3 == 0) goto L_0x003e
                int r0 = r2.mNextUpdate
                r3.onRefreshUi(r0)
                r3 = 0
                r2.mNextUpdate = r3
                goto L_0x003e
            L_0x0032:
                r2 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0032 }
                throw r2
            L_0x0035:
                int r3 = r3.arg1
                if (r3 == 0) goto L_0x003b
                r3 = 2
                goto L_0x003c
            L_0x003b:
                r3 = 1
            L_0x003c:
                r2.mNextUpdate = r3
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.RunningState.C06832.handleMessage(android.os.Message):void");
        }
    };
    boolean mHaveData;
    final boolean mHideManagedProfiles;
    final InterestingConfigChanges mInterestingConfigChanges = new InterestingConfigChanges();
    final ArrayList<ProcessItem> mInterestingProcesses = new ArrayList<>();
    ArrayList<BaseItem> mItems = new ArrayList<>();
    final Object mLock = new Object();
    ArrayList<MergedItem> mMergedItems = new ArrayList<>();
    final int mMyUserId;
    int mNumBackgroundProcesses;
    int mNumForegroundProcesses;
    int mNumServiceProcesses;
    final SparseArray<MergedItem> mOtherUserBackgroundItems = new SparseArray<>();
    final SparseArray<MergedItem> mOtherUserMergedItems = new SparseArray<>();
    final PackageManager mPm;
    final ArrayList<ProcessItem> mProcessItems = new ArrayList<>();
    OnRefreshUiListener mRefreshUiListener;
    boolean mResumed;
    final SparseArray<ProcessItem> mRunningProcesses = new SparseArray<>();
    int mSequence = 0;
    final ServiceProcessComparator mServiceProcessComparator = new ServiceProcessComparator();
    long mServiceProcessMemory;
    final SparseArray<HashMap<String, ProcessItem>> mServiceProcessesByName = new SparseArray<>();
    final SparseArray<ProcessItem> mServiceProcessesByPid = new SparseArray<>();
    final SparseArray<AppProcessInfo> mTmpAppProcesses = new SparseArray<>();
    final UserManager mUm;
    private final UserManagerBroadcastReceiver mUmBroadcastReceiver;
    ArrayList<MergedItem> mUserBackgroundItems = new ArrayList<>();
    boolean mWatchingBackgroundItems;

    interface OnRefreshUiListener {
        void onRefreshUi(int i);
    }

    static class AppProcessInfo {
        boolean hasForegroundServices;
        boolean hasServices;
        final ActivityManager.RunningAppProcessInfo info;

        AppProcessInfo(ActivityManager.RunningAppProcessInfo runningAppProcessInfo) {
            this.info = runningAppProcessInfo;
        }
    }

    final class BackgroundHandler extends Handler {
        public BackgroundHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                RunningState.this.reset();
            } else if (i == 2) {
                synchronized (RunningState.this.mLock) {
                    RunningState runningState = RunningState.this;
                    if (runningState.mResumed) {
                        Message obtainMessage = runningState.mHandler.obtainMessage(3);
                        RunningState runningState2 = RunningState.this;
                        obtainMessage.arg1 = runningState2.update(runningState2.mApplicationContext, runningState2.mAm) ? 1 : 0;
                        RunningState.this.mHandler.sendMessage(obtainMessage);
                        removeMessages(2);
                        sendMessageDelayed(obtainMessage(2), 2000);
                    }
                }
            }
        }
    }

    private final class UserManagerBroadcastReceiver extends BroadcastReceiver {
        private volatile boolean usersChanged;

        private UserManagerBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            synchronized (RunningState.this.mLock) {
                RunningState runningState = RunningState.this;
                if (runningState.mResumed) {
                    runningState.mHaveData = false;
                    runningState.mBackgroundHandler.removeMessages(1);
                    RunningState.this.mBackgroundHandler.sendEmptyMessage(1);
                    RunningState.this.mBackgroundHandler.removeMessages(2);
                    RunningState.this.mBackgroundHandler.sendEmptyMessage(2);
                } else {
                    this.usersChanged = true;
                }
            }
        }

        public boolean checkUsersChangedLocked() {
            boolean z = this.usersChanged;
            this.usersChanged = false;
            return z;
        }

        /* access modifiers changed from: package-private */
        public void register(Context context) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_STOPPED");
            intentFilter.addAction("android.intent.action.USER_STARTED");
            intentFilter.addAction("android.intent.action.USER_INFO_CHANGED");
            context.registerReceiverAsUser(this, UserHandle.ALL, intentFilter, (String) null, (Handler) null);
        }
    }

    static class UserState {
        Drawable mIcon;
        UserInfo mInfo;
        String mLabel;

        UserState() {
        }
    }

    static class BaseItem {
        long mActiveSince;
        boolean mBackground;
        int mCurSeq;
        String mCurSizeStr;
        String mDescription;
        CharSequence mDisplayLabel;
        final boolean mIsProcess;
        String mLabel;
        boolean mNeedDivider;
        PackageItemInfo mPackageInfo;
        long mSize;
        String mSizeStr;
        final int mUserId;

        public BaseItem(boolean z, int i) {
            this.mIsProcess = z;
            this.mUserId = i;
        }

        public Drawable loadIcon(Context context, RunningState runningState) {
            PackageItemInfo packageItemInfo = this.mPackageInfo;
            if (packageItemInfo == null) {
                return null;
            }
            return runningState.mPm.getUserBadgedIcon(packageItemInfo.loadUnbadgedIcon(runningState.mPm), new UserHandle(this.mUserId));
        }
    }

    static class ServiceItem extends BaseItem {
        MergedItem mMergedItem;
        ActivityManager.RunningServiceInfo mRunningService;
        ServiceInfo mServiceInfo;
        boolean mShownAsStarted;

        public ServiceItem(int i) {
            super(false, i);
        }
    }

    static class ProcessItem extends BaseItem {
        long mActiveSince;
        ProcessItem mClient;
        final SparseArray<ProcessItem> mDependentProcesses = new SparseArray<>();
        boolean mInteresting;
        boolean mIsStarted;
        boolean mIsSystem;
        int mLastNumDependentProcesses;
        MergedItem mMergedItem;
        int mPid;
        final String mProcessName;
        ActivityManager.RunningAppProcessInfo mRunningProcessInfo;
        int mRunningSeq;
        final HashMap<ComponentName, ServiceItem> mServices = new HashMap<>();
        final int mUid;

        public ProcessItem(Context context, int i, String str) {
            super(true, UserHandle.getUserId(i));
            this.mDescription = context.getResources().getString(R.string.service_process_name, new Object[]{str});
            this.mUid = i;
            this.mProcessName = str;
        }

        /* access modifiers changed from: package-private */
        public void ensureLabel(PackageManager packageManager) {
            CharSequence text;
            if (this.mLabel == null) {
                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.mProcessName, 4194304);
                    if (applicationInfo.uid == this.mUid) {
                        CharSequence loadLabel = applicationInfo.loadLabel(packageManager);
                        this.mDisplayLabel = loadLabel;
                        this.mLabel = loadLabel.toString();
                        this.mPackageInfo = applicationInfo;
                        return;
                    }
                } catch (PackageManager.NameNotFoundException unused) {
                }
                String[] packagesForUid = packageManager.getPackagesForUid(this.mUid);
                if (packagesForUid.length == 1) {
                    try {
                        ApplicationInfo applicationInfo2 = packageManager.getApplicationInfo(packagesForUid[0], 4194304);
                        CharSequence loadLabel2 = applicationInfo2.loadLabel(packageManager);
                        this.mDisplayLabel = loadLabel2;
                        this.mLabel = loadLabel2.toString();
                        this.mPackageInfo = applicationInfo2;
                        return;
                    } catch (PackageManager.NameNotFoundException unused2) {
                    }
                }
                for (String str : packagesForUid) {
                    try {
                        PackageInfo packageInfo = packageManager.getPackageInfo(str, 0);
                        int i = packageInfo.sharedUserLabel;
                        if (!(i == 0 || (text = packageManager.getText(str, i, packageInfo.applicationInfo)) == null)) {
                            this.mDisplayLabel = text;
                            this.mLabel = text.toString();
                            this.mPackageInfo = packageInfo.applicationInfo;
                            return;
                        }
                    } catch (PackageManager.NameNotFoundException unused3) {
                    }
                }
                if (this.mServices.size() > 0) {
                    ApplicationInfo applicationInfo3 = this.mServices.values().iterator().next().mServiceInfo.applicationInfo;
                    this.mPackageInfo = applicationInfo3;
                    CharSequence loadLabel3 = applicationInfo3.loadLabel(packageManager);
                    this.mDisplayLabel = loadLabel3;
                    this.mLabel = loadLabel3.toString();
                    return;
                }
                try {
                    ApplicationInfo applicationInfo4 = packageManager.getApplicationInfo(packagesForUid[0], 4194304);
                    CharSequence loadLabel4 = applicationInfo4.loadLabel(packageManager);
                    this.mDisplayLabel = loadLabel4;
                    this.mLabel = loadLabel4.toString();
                    this.mPackageInfo = applicationInfo4;
                } catch (PackageManager.NameNotFoundException unused4) {
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean updateService(Context context, ActivityManager.RunningServiceInfo runningServiceInfo) {
            boolean z;
            PackageManager packageManager = context.getPackageManager();
            ServiceItem serviceItem = this.mServices.get(runningServiceInfo.service);
            boolean z2 = true;
            if (serviceItem == null) {
                serviceItem = new ServiceItem(this.mUserId);
                serviceItem.mRunningService = runningServiceInfo;
                try {
                    ServiceInfo serviceInfo = ActivityThread.getPackageManager().getServiceInfo(runningServiceInfo.service, 4194304, UserHandle.getUserId(runningServiceInfo.uid));
                    serviceItem.mServiceInfo = serviceInfo;
                    if (serviceInfo == null) {
                        Log.d("RunningService", "getServiceInfo returned null for: " + runningServiceInfo.service);
                        return false;
                    }
                } catch (RemoteException unused) {
                }
                serviceItem.mDisplayLabel = RunningState.makeLabel(packageManager, serviceItem.mRunningService.service.getClassName(), serviceItem.mServiceInfo);
                CharSequence charSequence = this.mDisplayLabel;
                this.mLabel = charSequence != null ? charSequence.toString() : null;
                serviceItem.mPackageInfo = serviceItem.mServiceInfo.applicationInfo;
                this.mServices.put(runningServiceInfo.service, serviceItem);
                z = true;
            } else {
                z = false;
            }
            serviceItem.mCurSeq = this.mCurSeq;
            serviceItem.mRunningService = runningServiceInfo;
            long j = runningServiceInfo.restarting == 0 ? runningServiceInfo.activeSince : -1;
            if (serviceItem.mActiveSince != j) {
                serviceItem.mActiveSince = j;
                z = true;
            }
            String str = runningServiceInfo.clientPackage;
            if (str == null || runningServiceInfo.clientLabel == 0) {
                if (!serviceItem.mShownAsStarted) {
                    serviceItem.mShownAsStarted = true;
                } else {
                    z2 = z;
                }
                serviceItem.mDescription = context.getResources().getString(R.string.service_started_by_app);
                return z2;
            }
            if (serviceItem.mShownAsStarted) {
                serviceItem.mShownAsStarted = false;
                z = true;
            }
            try {
                serviceItem.mDescription = context.getResources().getString(R.string.service_client_name, new Object[]{packageManager.getResourcesForApplication(str).getString(runningServiceInfo.clientLabel)});
                return z;
            } catch (PackageManager.NameNotFoundException unused2) {
                serviceItem.mDescription = null;
                return z;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean updateSize(Context context, long j, int i) {
            long j2 = j * 1024;
            this.mSize = j2;
            if (this.mCurSeq == i) {
                String formatShortFileSize = Formatter.formatShortFileSize(context, j2);
                if (!formatShortFileSize.equals(this.mSizeStr)) {
                    this.mSizeStr = formatShortFileSize;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean buildDependencyChain(Context context, PackageManager packageManager, int i) {
            int size = this.mDependentProcesses.size();
            boolean z = false;
            for (int i2 = 0; i2 < size; i2++) {
                ProcessItem valueAt = this.mDependentProcesses.valueAt(i2);
                if (valueAt.mClient != this) {
                    valueAt.mClient = this;
                    z = true;
                }
                valueAt.mCurSeq = i;
                valueAt.ensureLabel(packageManager);
                z |= valueAt.buildDependencyChain(context, packageManager, i);
            }
            if (this.mLastNumDependentProcesses == this.mDependentProcesses.size()) {
                return z;
            }
            this.mLastNumDependentProcesses = this.mDependentProcesses.size();
            return true;
        }

        /* access modifiers changed from: package-private */
        public void addDependentProcesses(ArrayList<BaseItem> arrayList, ArrayList<ProcessItem> arrayList2) {
            int size = this.mDependentProcesses.size();
            for (int i = 0; i < size; i++) {
                ProcessItem valueAt = this.mDependentProcesses.valueAt(i);
                valueAt.addDependentProcesses(arrayList, arrayList2);
                arrayList.add(valueAt);
                if (valueAt.mPid > 0) {
                    arrayList2.add(valueAt);
                }
            }
        }
    }

    static class MergedItem extends BaseItem {
        final ArrayList<MergedItem> mChildren = new ArrayList<>();
        private int mLastNumProcesses = -1;
        private int mLastNumServices = -1;
        final ArrayList<ProcessItem> mOtherProcesses = new ArrayList<>();
        ProcessItem mProcess;
        final ArrayList<ServiceItem> mServices = new ArrayList<>();
        UserState mUser;

        MergedItem(int i) {
            super(false, i);
        }

        private void setDescription(Context context, int i, int i2) {
            if (this.mLastNumProcesses != i || this.mLastNumServices != i2) {
                this.mLastNumProcesses = i;
                this.mLastNumServices = i2;
                int i3 = R.string.running_processes_item_description_s_s;
                if (i != 1) {
                    i3 = i2 != 1 ? R.string.running_processes_item_description_p_p : R.string.running_processes_item_description_p_s;
                } else if (i2 != 1) {
                    i3 = R.string.running_processes_item_description_s_p;
                }
                this.mDescription = context.getResources().getString(i3, new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
            }
        }

        /* access modifiers changed from: package-private */
        public boolean update(Context context, boolean z) {
            this.mBackground = z;
            if (this.mUser != null) {
                this.mPackageInfo = this.mChildren.get(0).mProcess.mPackageInfo;
                UserState userState = this.mUser;
                String str = userState != null ? userState.mLabel : null;
                this.mLabel = str;
                this.mDisplayLabel = str;
                this.mActiveSince = -1;
                int i = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < this.mChildren.size(); i3++) {
                    MergedItem mergedItem = this.mChildren.get(i3);
                    i += mergedItem.mLastNumProcesses;
                    i2 += mergedItem.mLastNumServices;
                    long j = mergedItem.mActiveSince;
                    if (j >= 0 && this.mActiveSince < j) {
                        this.mActiveSince = j;
                    }
                }
                if (!this.mBackground) {
                    setDescription(context, i, i2);
                }
            } else {
                ProcessItem processItem = this.mProcess;
                this.mPackageInfo = processItem.mPackageInfo;
                this.mDisplayLabel = processItem.mDisplayLabel;
                this.mLabel = processItem.mLabel;
                if (!z) {
                    setDescription(context, (processItem.mPid > 0 ? 1 : 0) + this.mOtherProcesses.size(), this.mServices.size());
                }
                this.mActiveSince = -1;
                for (int i4 = 0; i4 < this.mServices.size(); i4++) {
                    long j2 = this.mServices.get(i4).mActiveSince;
                    if (j2 >= 0 && this.mActiveSince < j2) {
                        this.mActiveSince = j2;
                    }
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean updateSize(Context context) {
            if (this.mUser != null) {
                this.mSize = 0;
                for (int i = 0; i < this.mChildren.size(); i++) {
                    MergedItem mergedItem = this.mChildren.get(i);
                    mergedItem.updateSize(context);
                    this.mSize += mergedItem.mSize;
                }
            } else {
                this.mSize = this.mProcess.mSize;
                for (int i2 = 0; i2 < this.mOtherProcesses.size(); i2++) {
                    this.mSize += this.mOtherProcesses.get(i2).mSize;
                }
            }
            String formatShortFileSize = Formatter.formatShortFileSize(context, this.mSize);
            if (!formatShortFileSize.equals(this.mSizeStr)) {
                this.mSizeStr = formatShortFileSize;
            }
            return false;
        }

        public Drawable loadIcon(Context context, RunningState runningState) {
            UserState userState = this.mUser;
            if (userState == null) {
                return super.loadIcon(context, runningState);
            }
            Drawable drawable = userState.mIcon;
            if (drawable == null) {
                return context.getDrawable(17302702);
            }
            Drawable.ConstantState constantState = drawable.getConstantState();
            if (constantState == null) {
                return this.mUser.mIcon;
            }
            return constantState.newDrawable();
        }
    }

    class ServiceProcessComparator implements Comparator<ProcessItem> {
        ServiceProcessComparator() {
        }

        public int compare(ProcessItem processItem, ProcessItem processItem2) {
            int i = processItem.mUserId;
            int i2 = processItem2.mUserId;
            if (i != i2) {
                int i3 = RunningState.this.mMyUserId;
                if (i == i3) {
                    return -1;
                }
                if (i2 != i3 && i < i2) {
                    return -1;
                }
                return 1;
            }
            boolean z = processItem.mIsStarted;
            if (z == processItem2.mIsStarted) {
                boolean z2 = processItem.mIsSystem;
                if (z2 == processItem2.mIsSystem) {
                    long j = processItem.mActiveSince;
                    long j2 = processItem2.mActiveSince;
                    if (j == j2) {
                        return 0;
                    }
                    if (j > j2) {
                        return -1;
                    }
                    return 1;
                } else if (z2) {
                    return 1;
                } else {
                    return -1;
                }
            } else if (z) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    static CharSequence makeLabel(PackageManager packageManager, String str, PackageItemInfo packageItemInfo) {
        CharSequence loadLabel;
        if (packageItemInfo != null && ((packageItemInfo.labelRes != 0 || packageItemInfo.nonLocalizedLabel != null) && (loadLabel = packageItemInfo.loadLabel(packageManager)) != null)) {
            return loadLabel;
        }
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf >= 0 ? str.substring(lastIndexOf + 1, str.length()) : str;
    }

    static RunningState getInstance(Context context) {
        RunningState runningState;
        synchronized (sGlobalLock) {
            if (sInstance == null) {
                sInstance = new RunningState(context);
            }
            runningState = sInstance;
        }
        return runningState;
    }

    private RunningState(Context context) {
        UserManagerBroadcastReceiver userManagerBroadcastReceiver = new UserManagerBroadcastReceiver();
        this.mUmBroadcastReceiver = userManagerBroadcastReceiver;
        Context applicationContext = context.getApplicationContext();
        this.mApplicationContext = applicationContext;
        this.mAm = (ActivityManager) applicationContext.getSystemService(ActivityManager.class);
        this.mPm = applicationContext.getPackageManager();
        UserManager userManager = (UserManager) applicationContext.getSystemService(UserManager.class);
        this.mUm = userManager;
        int myUserId = UserHandle.myUserId();
        this.mMyUserId = myUserId;
        UserInfo userInfo = userManager.getUserInfo(myUserId);
        this.mHideManagedProfiles = userInfo == null || !userInfo.canHaveProfile();
        this.mResumed = false;
        HandlerThread handlerThread = new HandlerThread("RunningState:Background");
        this.mBackgroundThread = handlerThread;
        handlerThread.start();
        this.mBackgroundHandler = new BackgroundHandler(handlerThread.getLooper());
        userManagerBroadcastReceiver.register(applicationContext);
    }

    /* access modifiers changed from: package-private */
    public void resume(OnRefreshUiListener onRefreshUiListener) {
        synchronized (this.mLock) {
            this.mResumed = true;
            this.mRefreshUiListener = onRefreshUiListener;
            boolean checkUsersChangedLocked = this.mUmBroadcastReceiver.checkUsersChangedLocked();
            boolean applyNewConfig = this.mInterestingConfigChanges.applyNewConfig(this.mApplicationContext.getResources());
            if (checkUsersChangedLocked || applyNewConfig) {
                this.mHaveData = false;
                this.mBackgroundHandler.removeMessages(1);
                this.mBackgroundHandler.removeMessages(2);
                this.mBackgroundHandler.sendEmptyMessage(1);
            }
            if (!this.mBackgroundHandler.hasMessages(2)) {
                this.mBackgroundHandler.sendEmptyMessage(2);
            }
            this.mHandler.sendEmptyMessage(4);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateNow() {
        synchronized (this.mLock) {
            this.mBackgroundHandler.removeMessages(2);
            this.mBackgroundHandler.sendEmptyMessage(2);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasData() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mHaveData;
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:2:0x0003 */
    /* JADX WARNING: Removed duplicated region for block: B:2:0x0003 A[LOOP:0: B:2:0x0003->B:13:0x0003, LOOP_START, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void waitForData() {
        /*
            r4 = this;
            java.lang.Object r0 = r4.mLock
            monitor-enter(r0)
        L_0x0003:
            boolean r1 = r4.mHaveData     // Catch:{ all -> 0x0011 }
            if (r1 != 0) goto L_0x000f
            java.lang.Object r1 = r4.mLock     // Catch:{ InterruptedException -> 0x0003 }
            r2 = 0
            r1.wait(r2)     // Catch:{ InterruptedException -> 0x0003 }
            goto L_0x0003
        L_0x000f:
            monitor-exit(r0)     // Catch:{ all -> 0x0011 }
            return
        L_0x0011:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0011 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.RunningState.waitForData():void");
    }

    /* access modifiers changed from: package-private */
    public void pause() {
        synchronized (this.mLock) {
            this.mResumed = false;
            this.mRefreshUiListener = null;
            this.mHandler.removeMessages(4);
        }
    }

    private boolean isInterestingProcess(ActivityManager.RunningAppProcessInfo runningAppProcessInfo) {
        int i;
        int i2 = runningAppProcessInfo.flags;
        if ((i2 & 1) != 0) {
            return true;
        }
        if ((i2 & 2) != 0 || (i = runningAppProcessInfo.importance) < 100 || i >= 350 || runningAppProcessInfo.importanceReasonCode != 0) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void reset() {
        this.mServiceProcessesByName.clear();
        this.mServiceProcessesByPid.clear();
        this.mInterestingProcesses.clear();
        this.mRunningProcesses.clear();
        this.mProcessItems.clear();
        this.mAllProcessItems.clear();
    }

    private void addOtherUserItem(Context context, ArrayList<MergedItem> arrayList, SparseArray<MergedItem> sparseArray, MergedItem mergedItem) {
        MergedItem mergedItem2 = sparseArray.get(mergedItem.mUserId);
        if (mergedItem2 == null || mergedItem2.mCurSeq != this.mSequence) {
            UserInfo userInfo = this.mUm.getUserInfo(mergedItem.mUserId);
            if (userInfo != null) {
                if (!this.mHideManagedProfiles || !userInfo.isManagedProfile()) {
                    if (mergedItem2 == null) {
                        mergedItem2 = new MergedItem(mergedItem.mUserId);
                        sparseArray.put(mergedItem.mUserId, mergedItem2);
                    } else {
                        mergedItem2.mChildren.clear();
                    }
                    mergedItem2.mCurSeq = this.mSequence;
                    UserState userState = new UserState();
                    mergedItem2.mUser = userState;
                    userState.mInfo = userInfo;
                    userState.mIcon = Utils.getUserIcon(context, this.mUm, userInfo);
                    mergedItem2.mUser.mLabel = Utils.getUserLabel(context, userInfo);
                    arrayList.add(mergedItem2);
                } else {
                    return;
                }
            } else {
                return;
            }
        }
        mergedItem2.mChildren.add(mergedItem);
    }

    /* access modifiers changed from: private */
    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:458)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processExcHandler(RegionMaker.java:1043)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:975)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    /* JADX WARNING: Removed duplicated region for block: B:348:0x0677  */
    /* JADX WARNING: Removed duplicated region for block: B:356:0x06a0  */
    /* JADX WARNING: Removed duplicated region for block: B:358:0x06a4  */
    /* JADX WARNING: Removed duplicated region for block: B:373:0x06ee  */
    /* JADX WARNING: Removed duplicated region for block: B:377:0x06f8 A[LOOP:29: B:375:0x06f0->B:377:0x06f8, LOOP_END] */
    public boolean update(android.content.Context r26, android.app.ActivityManager r27) {
        /*
            r25 = this;
            r0 = r25
            r1 = r26
            android.content.pm.PackageManager r2 = r26.getPackageManager()
            int r3 = r0.mSequence
            r4 = 1
            int r3 = r3 + r4
            r0.mSequence = r3
            r3 = 100
            r5 = r27
            java.util.List r3 = r5.getRunningServices(r3)
            if (r3 == 0) goto L_0x001d
            int r7 = r3.size()
            goto L_0x001e
        L_0x001d:
            r7 = 0
        L_0x001e:
            r8 = 0
        L_0x001f:
            if (r8 >= r7) goto L_0x0042
            java.lang.Object r9 = r3.get(r8)
            android.app.ActivityManager$RunningServiceInfo r9 = (android.app.ActivityManager.RunningServiceInfo) r9
            boolean r10 = r9.started
            if (r10 != 0) goto L_0x0033
            int r10 = r9.clientLabel
            if (r10 != 0) goto L_0x0033
            r3.remove(r8)
            goto L_0x003c
        L_0x0033:
            int r9 = r9.flags
            r9 = r9 & 8
            if (r9 == 0) goto L_0x0040
            r3.remove(r8)
        L_0x003c:
            int r8 = r8 + -1
            int r7 = r7 + -1
        L_0x0040:
            int r8 = r8 + r4
            goto L_0x001f
        L_0x0042:
            java.util.List r5 = r27.getRunningAppProcesses()
            if (r5 == 0) goto L_0x004d
            int r8 = r5.size()
            goto L_0x004e
        L_0x004d:
            r8 = 0
        L_0x004e:
            android.util.SparseArray<com.android.settings.applications.RunningState$AppProcessInfo> r9 = r0.mTmpAppProcesses
            r9.clear()
            r9 = 0
        L_0x0054:
            if (r9 >= r8) goto L_0x006b
            java.lang.Object r10 = r5.get(r9)
            android.app.ActivityManager$RunningAppProcessInfo r10 = (android.app.ActivityManager.RunningAppProcessInfo) r10
            android.util.SparseArray<com.android.settings.applications.RunningState$AppProcessInfo> r11 = r0.mTmpAppProcesses
            int r12 = r10.pid
            com.android.settings.applications.RunningState$AppProcessInfo r13 = new com.android.settings.applications.RunningState$AppProcessInfo
            r13.<init>(r10)
            r11.put(r12, r13)
            int r9 = r9 + 1
            goto L_0x0054
        L_0x006b:
            r9 = 0
        L_0x006c:
            r10 = 0
            if (r9 >= r7) goto L_0x0095
            java.lang.Object r12 = r3.get(r9)
            android.app.ActivityManager$RunningServiceInfo r12 = (android.app.ActivityManager.RunningServiceInfo) r12
            long r13 = r12.restarting
            int r10 = (r13 > r10 ? 1 : (r13 == r10 ? 0 : -1))
            if (r10 != 0) goto L_0x0092
            int r10 = r12.pid
            if (r10 <= 0) goto L_0x0092
            android.util.SparseArray<com.android.settings.applications.RunningState$AppProcessInfo> r11 = r0.mTmpAppProcesses
            java.lang.Object r10 = r11.get(r10)
            com.android.settings.applications.RunningState$AppProcessInfo r10 = (com.android.settings.applications.RunningState.AppProcessInfo) r10
            if (r10 == 0) goto L_0x0092
            r10.hasServices = r4
            boolean r11 = r12.foreground
            if (r11 == 0) goto L_0x0092
            r10.hasForegroundServices = r4
        L_0x0092:
            int r9 = r9 + 1
            goto L_0x006c
        L_0x0095:
            r9 = 0
            r12 = 0
        L_0x0097:
            if (r9 >= r7) goto L_0x015c
            java.lang.Object r13 = r3.get(r9)
            android.app.ActivityManager$RunningServiceInfo r13 = (android.app.ActivityManager.RunningServiceInfo) r13
            long r14 = r13.restarting
            int r14 = (r14 > r10 ? 1 : (r14 == r10 ? 0 : -1))
            if (r14 != 0) goto L_0x00ed
            int r14 = r13.pid
            if (r14 <= 0) goto L_0x00ed
            android.util.SparseArray<com.android.settings.applications.RunningState$AppProcessInfo> r15 = r0.mTmpAppProcesses
            java.lang.Object r14 = r15.get(r14)
            com.android.settings.applications.RunningState$AppProcessInfo r14 = (com.android.settings.applications.RunningState.AppProcessInfo) r14
            if (r14 == 0) goto L_0x00ed
            boolean r15 = r14.hasForegroundServices
            if (r15 != 0) goto L_0x00ed
            android.app.ActivityManager$RunningAppProcessInfo r14 = r14.info
            int r15 = r14.importance
            r6 = 300(0x12c, float:4.2E-43)
            if (r15 >= r6) goto L_0x00ed
            android.util.SparseArray<com.android.settings.applications.RunningState$AppProcessInfo> r6 = r0.mTmpAppProcesses
            int r14 = r14.importanceReasonPid
            java.lang.Object r6 = r6.get(r14)
            com.android.settings.applications.RunningState$AppProcessInfo r6 = (com.android.settings.applications.RunningState.AppProcessInfo) r6
        L_0x00c9:
            if (r6 == 0) goto L_0x00e7
            boolean r14 = r6.hasServices
            if (r14 != 0) goto L_0x00e5
            android.app.ActivityManager$RunningAppProcessInfo r14 = r6.info
            boolean r14 = r0.isInterestingProcess(r14)
            if (r14 == 0) goto L_0x00d8
            goto L_0x00e5
        L_0x00d8:
            android.util.SparseArray<com.android.settings.applications.RunningState$AppProcessInfo> r14 = r0.mTmpAppProcesses
            android.app.ActivityManager$RunningAppProcessInfo r6 = r6.info
            int r6 = r6.importanceReasonPid
            java.lang.Object r6 = r14.get(r6)
            com.android.settings.applications.RunningState$AppProcessInfo r6 = (com.android.settings.applications.RunningState.AppProcessInfo) r6
            goto L_0x00c9
        L_0x00e5:
            r6 = r4
            goto L_0x00e8
        L_0x00e7:
            r6 = 0
        L_0x00e8:
            if (r6 == 0) goto L_0x00ed
            r27 = r5
            goto L_0x0155
        L_0x00ed:
            android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settings.applications.RunningState$ProcessItem>> r6 = r0.mServiceProcessesByName
            int r14 = r13.uid
            java.lang.Object r6 = r6.get(r14)
            java.util.HashMap r6 = (java.util.HashMap) r6
            if (r6 != 0) goto L_0x0105
            java.util.HashMap r6 = new java.util.HashMap
            r6.<init>()
            android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settings.applications.RunningState$ProcessItem>> r14 = r0.mServiceProcessesByName
            int r15 = r13.uid
            r14.put(r15, r6)
        L_0x0105:
            java.lang.String r14 = r13.process
            java.lang.Object r14 = r6.get(r14)
            com.android.settings.applications.RunningState$ProcessItem r14 = (com.android.settings.applications.RunningState.ProcessItem) r14
            if (r14 != 0) goto L_0x011e
            com.android.settings.applications.RunningState$ProcessItem r14 = new com.android.settings.applications.RunningState$ProcessItem
            int r12 = r13.uid
            java.lang.String r15 = r13.process
            r14.<init>(r1, r12, r15)
            java.lang.String r12 = r13.process
            r6.put(r12, r14)
            r12 = r4
        L_0x011e:
            int r6 = r14.mCurSeq
            int r15 = r0.mSequence
            r27 = r5
            if (r6 == r15) goto L_0x0150
            long r4 = r13.restarting
            int r4 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
            if (r4 != 0) goto L_0x012f
            int r4 = r13.pid
            goto L_0x0130
        L_0x012f:
            r4 = 0
        L_0x0130:
            int r5 = r14.mPid
            if (r4 == r5) goto L_0x0147
            if (r5 == r4) goto L_0x0146
            if (r5 == 0) goto L_0x013d
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r12 = r0.mServiceProcessesByPid
            r12.remove(r5)
        L_0x013d:
            if (r4 == 0) goto L_0x0144
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r5 = r0.mServiceProcessesByPid
            r5.put(r4, r14)
        L_0x0144:
            r14.mPid = r4
        L_0x0146:
            r12 = 1
        L_0x0147:
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r4 = r14.mDependentProcesses
            r4.clear()
            int r4 = r0.mSequence
            r14.mCurSeq = r4
        L_0x0150:
            boolean r4 = r14.updateService(r1, r13)
            r12 = r12 | r4
        L_0x0155:
            int r9 = r9 + 1
            r5 = r27
            r4 = 1
            goto L_0x0097
        L_0x015c:
            r27 = r5
            r3 = 0
        L_0x015f:
            if (r3 >= r8) goto L_0x01c6
            r4 = r27
            java.lang.Object r5 = r4.get(r3)
            android.app.ActivityManager$RunningAppProcessInfo r5 = (android.app.ActivityManager.RunningAppProcessInfo) r5
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r7 = r0.mServiceProcessesByPid
            int r9 = r5.pid
            java.lang.Object r7 = r7.get(r9)
            com.android.settings.applications.RunningState$ProcessItem r7 = (com.android.settings.applications.RunningState.ProcessItem) r7
            if (r7 != 0) goto L_0x0199
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r7 = r0.mRunningProcesses
            int r9 = r5.pid
            java.lang.Object r7 = r7.get(r9)
            com.android.settings.applications.RunningState$ProcessItem r7 = (com.android.settings.applications.RunningState.ProcessItem) r7
            if (r7 != 0) goto L_0x0194
            com.android.settings.applications.RunningState$ProcessItem r7 = new com.android.settings.applications.RunningState$ProcessItem
            int r9 = r5.uid
            java.lang.String r12 = r5.processName
            r7.<init>(r1, r9, r12)
            int r9 = r5.pid
            r7.mPid = r9
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r12 = r0.mRunningProcesses
            r12.put(r9, r7)
            r12 = 1
        L_0x0194:
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r9 = r7.mDependentProcesses
            r9.clear()
        L_0x0199:
            boolean r9 = r0.isInterestingProcess(r5)
            if (r9 == 0) goto L_0x01b8
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r9 = r0.mInterestingProcesses
            boolean r9 = r9.contains(r7)
            if (r9 != 0) goto L_0x01ad
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r9 = r0.mInterestingProcesses
            r9.add(r7)
            r12 = 1
        L_0x01ad:
            int r9 = r0.mSequence
            r7.mCurSeq = r9
            r6 = 1
            r7.mInteresting = r6
            r7.ensureLabel(r2)
            goto L_0x01bb
        L_0x01b8:
            r9 = 0
            r7.mInteresting = r9
        L_0x01bb:
            int r9 = r0.mSequence
            r7.mRunningSeq = r9
            r7.mRunningProcessInfo = r5
            int r3 = r3 + 1
            r27 = r4
            goto L_0x015f
        L_0x01c6:
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r3 = r0.mRunningProcesses
            int r3 = r3.size()
            r4 = 0
        L_0x01cd:
            r5 = 0
            if (r4 >= r3) goto L_0x0212
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r7 = r0.mRunningProcesses
            java.lang.Object r7 = r7.valueAt(r4)
            com.android.settings.applications.RunningState$ProcessItem r7 = (com.android.settings.applications.RunningState.ProcessItem) r7
            int r8 = r7.mRunningSeq
            int r9 = r0.mSequence
            if (r8 != r9) goto L_0x0205
            android.app.ActivityManager$RunningAppProcessInfo r8 = r7.mRunningProcessInfo
            int r8 = r8.importanceReasonPid
            if (r8 == 0) goto L_0x0200
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r5 = r0.mServiceProcessesByPid
            java.lang.Object r5 = r5.get(r8)
            com.android.settings.applications.RunningState$ProcessItem r5 = (com.android.settings.applications.RunningState.ProcessItem) r5
            if (r5 != 0) goto L_0x01f6
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r5 = r0.mRunningProcesses
            java.lang.Object r5 = r5.get(r8)
            com.android.settings.applications.RunningState$ProcessItem r5 = (com.android.settings.applications.RunningState.ProcessItem) r5
        L_0x01f6:
            if (r5 == 0) goto L_0x0202
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r5 = r5.mDependentProcesses
            int r8 = r7.mPid
            r5.put(r8, r7)
            goto L_0x0202
        L_0x0200:
            r7.mClient = r5
        L_0x0202:
            int r4 = r4 + 1
            goto L_0x01cd
        L_0x0205:
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r5 = r0.mRunningProcesses
            int r7 = r5.keyAt(r4)
            r5.remove(r7)
            int r3 = r3 + -1
            r12 = 1
            goto L_0x01cd
        L_0x0212:
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r3 = r0.mInterestingProcesses
            int r3 = r3.size()
            r4 = 0
        L_0x0219:
            if (r4 >= r3) goto L_0x0241
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r7 = r0.mInterestingProcesses
            java.lang.Object r7 = r7.get(r4)
            com.android.settings.applications.RunningState$ProcessItem r7 = (com.android.settings.applications.RunningState.ProcessItem) r7
            boolean r8 = r7.mInteresting
            if (r8 == 0) goto L_0x0234
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r8 = r0.mRunningProcesses
            int r7 = r7.mPid
            java.lang.Object r7 = r8.get(r7)
            if (r7 != 0) goto L_0x0232
            goto L_0x0234
        L_0x0232:
            r6 = 1
            goto L_0x023f
        L_0x0234:
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r7 = r0.mInterestingProcesses
            r7.remove(r4)
            int r4 = r4 + -1
            int r3 = r3 + -1
            r6 = 1
            r12 = 1
        L_0x023f:
            int r4 = r4 + r6
            goto L_0x0219
        L_0x0241:
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r3 = r0.mServiceProcessesByPid
            int r3 = r3.size()
            r4 = 0
        L_0x0248:
            if (r4 >= r3) goto L_0x0261
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r7 = r0.mServiceProcessesByPid
            java.lang.Object r7 = r7.valueAt(r4)
            com.android.settings.applications.RunningState$ProcessItem r7 = (com.android.settings.applications.RunningState.ProcessItem) r7
            int r8 = r7.mCurSeq
            int r9 = r0.mSequence
            if (r8 != r9) goto L_0x025e
            boolean r7 = r7.buildDependencyChain(r1, r2, r9)
            r7 = r7 | r12
            r12 = r7
        L_0x025e:
            int r4 = r4 + 1
            goto L_0x0248
        L_0x0261:
            r4 = r5
            r3 = 0
        L_0x0263:
            android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settings.applications.RunningState$ProcessItem>> r7 = r0.mServiceProcessesByName
            int r7 = r7.size()
            if (r3 >= r7) goto L_0x02e6
            android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settings.applications.RunningState$ProcessItem>> r7 = r0.mServiceProcessesByName
            java.lang.Object r7 = r7.valueAt(r3)
            java.util.HashMap r7 = (java.util.HashMap) r7
            java.util.Collection r8 = r7.values()
            java.util.Iterator r8 = r8.iterator()
        L_0x027b:
            boolean r9 = r8.hasNext()
            if (r9 == 0) goto L_0x02e2
            java.lang.Object r9 = r8.next()
            com.android.settings.applications.RunningState$ProcessItem r9 = (com.android.settings.applications.RunningState.ProcessItem) r9
            int r13 = r9.mCurSeq
            int r14 = r0.mSequence
            if (r13 != r14) goto L_0x02ba
            r9.ensureLabel(r2)
            int r13 = r9.mPid
            if (r13 != 0) goto L_0x0299
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r13 = r9.mDependentProcesses
            r13.clear()
        L_0x0299:
            java.util.HashMap<android.content.ComponentName, com.android.settings.applications.RunningState$ServiceItem> r9 = r9.mServices
            java.util.Collection r9 = r9.values()
            java.util.Iterator r9 = r9.iterator()
        L_0x02a3:
            boolean r13 = r9.hasNext()
            if (r13 == 0) goto L_0x027b
            java.lang.Object r13 = r9.next()
            com.android.settings.applications.RunningState$ServiceItem r13 = (com.android.settings.applications.RunningState.ServiceItem) r13
            int r13 = r13.mCurSeq
            int r14 = r0.mSequence
            if (r13 == r14) goto L_0x02a3
            r9.remove()
            r12 = 1
            goto L_0x02a3
        L_0x02ba:
            r8.remove()
            int r12 = r7.size()
            if (r12 != 0) goto L_0x02d7
            if (r4 != 0) goto L_0x02ca
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
        L_0x02ca:
            android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settings.applications.RunningState$ProcessItem>> r12 = r0.mServiceProcessesByName
            int r12 = r12.keyAt(r3)
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)
            r4.add(r12)
        L_0x02d7:
            int r9 = r9.mPid
            if (r9 == 0) goto L_0x02e0
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r12 = r0.mServiceProcessesByPid
            r12.remove(r9)
        L_0x02e0:
            r12 = 1
            goto L_0x027b
        L_0x02e2:
            int r3 = r3 + 1
            goto L_0x0263
        L_0x02e6:
            if (r4 == 0) goto L_0x0301
            r2 = 0
        L_0x02e9:
            int r3 = r4.size()
            if (r2 >= r3) goto L_0x0301
            java.lang.Object r3 = r4.get(r2)
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
            android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settings.applications.RunningState$ProcessItem>> r7 = r0.mServiceProcessesByName
            r7.remove(r3)
            int r2 = r2 + 1
            goto L_0x02e9
        L_0x0301:
            if (r12 == 0) goto L_0x04b3
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            r3 = 0
        L_0x0309:
            android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settings.applications.RunningState$ProcessItem>> r4 = r0.mServiceProcessesByName
            int r4 = r4.size()
            if (r3 >= r4) goto L_0x037e
            android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.settings.applications.RunningState$ProcessItem>> r4 = r0.mServiceProcessesByName
            java.lang.Object r4 = r4.valueAt(r3)
            java.util.HashMap r4 = (java.util.HashMap) r4
            java.util.Collection r4 = r4.values()
            java.util.Iterator r4 = r4.iterator()
        L_0x0321:
            boolean r7 = r4.hasNext()
            if (r7 == 0) goto L_0x037a
            java.lang.Object r7 = r4.next()
            com.android.settings.applications.RunningState$ProcessItem r7 = (com.android.settings.applications.RunningState.ProcessItem) r7
            r8 = 0
            r7.mIsSystem = r8
            r6 = 1
            r7.mIsStarted = r6
            r8 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r7.mActiveSince = r8
            java.util.HashMap<android.content.ComponentName, com.android.settings.applications.RunningState$ServiceItem> r8 = r7.mServices
            java.util.Collection r8 = r8.values()
            java.util.Iterator r8 = r8.iterator()
        L_0x0344:
            boolean r9 = r8.hasNext()
            if (r9 == 0) goto L_0x0375
            java.lang.Object r9 = r8.next()
            com.android.settings.applications.RunningState$ServiceItem r9 = (com.android.settings.applications.RunningState.ServiceItem) r9
            android.content.pm.ServiceInfo r13 = r9.mServiceInfo
            if (r13 == 0) goto L_0x035e
            android.content.pm.ApplicationInfo r13 = r13.applicationInfo
            int r13 = r13.flags
            r6 = 1
            r13 = r13 & r6
            if (r13 == 0) goto L_0x035e
            r7.mIsSystem = r6
        L_0x035e:
            android.app.ActivityManager$RunningServiceInfo r9 = r9.mRunningService
            if (r9 == 0) goto L_0x0373
            int r13 = r9.clientLabel
            if (r13 == 0) goto L_0x0373
            r13 = 0
            r7.mIsStarted = r13
            long r13 = r7.mActiveSince
            long r5 = r9.activeSince
            int r9 = (r13 > r5 ? 1 : (r13 == r5 ? 0 : -1))
            if (r9 <= 0) goto L_0x0373
            r7.mActiveSince = r5
        L_0x0373:
            r5 = 0
            goto L_0x0344
        L_0x0375:
            r2.add(r7)
            r5 = 0
            goto L_0x0321
        L_0x037a:
            int r3 = r3 + 1
            r5 = 0
            goto L_0x0309
        L_0x037e:
            com.android.settings.applications.RunningState$ServiceProcessComparator r3 = r0.mServiceProcessComparator
            java.util.Collections.sort(r2, r3)
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r5 = r0.mProcessItems
            r5.clear()
            r5 = 0
        L_0x0393:
            int r6 = r2.size()
            if (r5 >= r6) goto L_0x043b
            java.lang.Object r6 = r2.get(r5)
            com.android.settings.applications.RunningState$ProcessItem r6 = (com.android.settings.applications.RunningState.ProcessItem) r6
            r7 = 0
            r6.mNeedDivider = r7
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r7 = r0.mProcessItems
            int r7 = r7.size()
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r8 = r0.mProcessItems
            r6.addDependentProcesses(r3, r8)
            r3.add(r6)
            int r8 = r6.mPid
            if (r8 <= 0) goto L_0x03b9
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r8 = r0.mProcessItems
            r8.add(r6)
        L_0x03b9:
            java.util.HashMap<android.content.ComponentName, com.android.settings.applications.RunningState$ServiceItem> r8 = r6.mServices
            java.util.Collection r8 = r8.values()
            java.util.Iterator r8 = r8.iterator()
            r9 = 0
            r13 = 0
        L_0x03c5:
            boolean r14 = r8.hasNext()
            if (r14 == 0) goto L_0x03dd
            java.lang.Object r14 = r8.next()
            com.android.settings.applications.RunningState$ServiceItem r14 = (com.android.settings.applications.RunningState.ServiceItem) r14
            r14.mNeedDivider = r9
            r3.add(r14)
            com.android.settings.applications.RunningState$MergedItem r9 = r14.mMergedItem
            if (r9 == 0) goto L_0x03db
            r13 = r9
        L_0x03db:
            r9 = 1
            goto L_0x03c5
        L_0x03dd:
            com.android.settings.applications.RunningState$MergedItem r8 = new com.android.settings.applications.RunningState$MergedItem
            int r9 = r6.mUserId
            r8.<init>(r9)
            java.util.HashMap<android.content.ComponentName, com.android.settings.applications.RunningState$ServiceItem> r9 = r6.mServices
            java.util.Collection r9 = r9.values()
            java.util.Iterator r9 = r9.iterator()
        L_0x03ee:
            boolean r13 = r9.hasNext()
            if (r13 == 0) goto L_0x0402
            java.lang.Object r13 = r9.next()
            com.android.settings.applications.RunningState$ServiceItem r13 = (com.android.settings.applications.RunningState.ServiceItem) r13
            java.util.ArrayList<com.android.settings.applications.RunningState$ServiceItem> r14 = r8.mServices
            r14.add(r13)
            r13.mMergedItem = r8
            goto L_0x03ee
        L_0x0402:
            r8.mProcess = r6
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r6 = r8.mOtherProcesses
            r6.clear()
        L_0x0409:
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r6 = r0.mProcessItems
            int r6 = r6.size()
            r9 = 1
            int r13 = r6 + -1
            if (r7 >= r13) goto L_0x0424
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r9 = r8.mOtherProcesses
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r13 = r0.mProcessItems
            java.lang.Object r13 = r13.get(r7)
            com.android.settings.applications.RunningState$ProcessItem r13 = (com.android.settings.applications.RunningState.ProcessItem) r13
            r9.add(r13)
            int r7 = r7 + 1
            goto L_0x0409
        L_0x0424:
            r7 = 0
            r8.update(r1, r7)
            int r7 = r8.mUserId
            int r9 = r0.mMyUserId
            if (r7 == r9) goto L_0x0434
            android.util.SparseArray<com.android.settings.applications.RunningState$MergedItem> r7 = r0.mOtherUserMergedItems
            r0.addOtherUserItem(r1, r4, r7, r8)
            goto L_0x0437
        L_0x0434:
            r4.add(r8)
        L_0x0437:
            int r5 = r5 + 1
            goto L_0x0393
        L_0x043b:
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r2 = r0.mInterestingProcesses
            int r2 = r2.size()
            r9 = 0
        L_0x0442:
            if (r9 >= r2) goto L_0x0486
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r5 = r0.mInterestingProcesses
            java.lang.Object r5 = r5.get(r9)
            com.android.settings.applications.RunningState$ProcessItem r5 = (com.android.settings.applications.RunningState.ProcessItem) r5
            com.android.settings.applications.RunningState$ProcessItem r7 = r5.mClient
            if (r7 != 0) goto L_0x0483
            java.util.HashMap<android.content.ComponentName, com.android.settings.applications.RunningState$ServiceItem> r7 = r5.mServices
            int r7 = r7.size()
            if (r7 > 0) goto L_0x0483
            com.android.settings.applications.RunningState$MergedItem r7 = r5.mMergedItem
            if (r7 != 0) goto L_0x0467
            com.android.settings.applications.RunningState$MergedItem r7 = new com.android.settings.applications.RunningState$MergedItem
            int r8 = r5.mUserId
            r7.<init>(r8)
            r5.mMergedItem = r7
            r7.mProcess = r5
        L_0x0467:
            com.android.settings.applications.RunningState$MergedItem r7 = r5.mMergedItem
            r8 = 0
            r7.update(r1, r8)
            com.android.settings.applications.RunningState$MergedItem r7 = r5.mMergedItem
            int r13 = r7.mUserId
            int r14 = r0.mMyUserId
            if (r13 == r14) goto L_0x047b
            android.util.SparseArray<com.android.settings.applications.RunningState$MergedItem> r13 = r0.mOtherUserMergedItems
            r0.addOtherUserItem(r1, r4, r13, r7)
            goto L_0x047e
        L_0x047b:
            r4.add(r8, r7)
        L_0x047e:
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r7 = r0.mProcessItems
            r7.add(r5)
        L_0x0483:
            int r9 = r9 + 1
            goto L_0x0442
        L_0x0486:
            android.util.SparseArray<com.android.settings.applications.RunningState$MergedItem> r2 = r0.mOtherUserMergedItems
            int r2 = r2.size()
            r9 = 0
        L_0x048d:
            if (r9 >= r2) goto L_0x04a6
            android.util.SparseArray<com.android.settings.applications.RunningState$MergedItem> r5 = r0.mOtherUserMergedItems
            java.lang.Object r5 = r5.valueAt(r9)
            com.android.settings.applications.RunningState$MergedItem r5 = (com.android.settings.applications.RunningState.MergedItem) r5
            int r7 = r5.mCurSeq
            int r8 = r0.mSequence
            if (r7 != r8) goto L_0x04a2
            r7 = 0
            r5.update(r1, r7)
            goto L_0x04a3
        L_0x04a2:
            r7 = 0
        L_0x04a3:
            int r9 = r9 + 1
            goto L_0x048d
        L_0x04a6:
            r7 = 0
            java.lang.Object r2 = r0.mLock
            monitor-enter(r2)
            r0.mItems = r3     // Catch:{ all -> 0x04b0 }
            r0.mMergedItems = r4     // Catch:{ all -> 0x04b0 }
            monitor-exit(r2)     // Catch:{ all -> 0x04b0 }
            goto L_0x04b4
        L_0x04b0:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x04b0 }
            throw r0
        L_0x04b3:
            r7 = 0
        L_0x04b4:
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r2 = r0.mAllProcessItems
            r2.clear()
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r2 = r0.mAllProcessItems
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r3 = r0.mProcessItems
            r2.addAll(r3)
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r2 = r0.mRunningProcesses
            int r2 = r2.size()
            r3 = r7
            r4 = r3
            r5 = r4
            r9 = r5
        L_0x04ca:
            r8 = 200(0xc8, float:2.8E-43)
            r13 = 400(0x190, float:5.6E-43)
            if (r9 >= r2) goto L_0x051e
            android.util.SparseArray<com.android.settings.applications.RunningState$ProcessItem> r14 = r0.mRunningProcesses
            java.lang.Object r14 = r14.valueAt(r9)
            com.android.settings.applications.RunningState$ProcessItem r14 = (com.android.settings.applications.RunningState.ProcessItem) r14
            int r15 = r14.mCurSeq
            int r6 = r0.mSequence
            if (r15 == r6) goto L_0x0519
            android.app.ActivityManager$RunningAppProcessInfo r6 = r14.mRunningProcessInfo
            int r6 = r6.importance
            if (r6 < r13) goto L_0x04ec
            int r4 = r4 + 1
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r6 = r0.mAllProcessItems
            r6.add(r14)
            goto L_0x051b
        L_0x04ec:
            if (r6 > r8) goto L_0x04f6
            int r5 = r5 + 1
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r6 = r0.mAllProcessItems
            r6.add(r14)
            goto L_0x051b
        L_0x04f6:
            java.lang.String r6 = "RunningState"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r13 = "Unknown non-service process: "
            r8.append(r13)
            java.lang.String r13 = r14.mProcessName
            r8.append(r13)
            java.lang.String r13 = " #"
            r8.append(r13)
            int r13 = r14.mPid
            r8.append(r13)
            java.lang.String r8 = r8.toString()
            android.util.Log.i(r6, r8)
            goto L_0x051b
        L_0x0519:
            int r3 = r3 + 1
        L_0x051b:
            int r9 = r9 + 1
            goto L_0x04ca
        L_0x051e:
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r2 = r0.mAllProcessItems     // Catch:{ RemoteException -> 0x065f }
            int r2 = r2.size()     // Catch:{ RemoteException -> 0x065f }
            int[] r6 = new int[r2]     // Catch:{ RemoteException -> 0x065f }
            r9 = r7
        L_0x0527:
            if (r9 >= r2) goto L_0x0540
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r14 = r0.mAllProcessItems     // Catch:{ RemoteException -> 0x0538 }
            java.lang.Object r14 = r14.get(r9)     // Catch:{ RemoteException -> 0x0538 }
            com.android.settings.applications.RunningState$ProcessItem r14 = (com.android.settings.applications.RunningState.ProcessItem) r14     // Catch:{ RemoteException -> 0x0538 }
            int r14 = r14.mPid     // Catch:{ RemoteException -> 0x0538 }
            r6[r9] = r14     // Catch:{ RemoteException -> 0x0538 }
            int r9 = r9 + 1
            goto L_0x0527
        L_0x0538:
            r9 = r7
            r16 = r10
            r18 = r16
            r2 = 0
            goto L_0x0665
        L_0x0540:
            android.app.IActivityManager r9 = android.app.ActivityManager.getService()     // Catch:{ RemoteException -> 0x065f }
            long[] r9 = r9.getProcessPss(r6)     // Catch:{ RemoteException -> 0x065f }
            r14 = r10
            r16 = r14
            r18 = r16
            r20 = r12
            r6 = 0
            r10 = r7
            r11 = r10
            r12 = r11
        L_0x0553:
            if (r10 >= r2) goto L_0x0659
            java.util.ArrayList<com.android.settings.applications.RunningState$ProcessItem> r7 = r0.mAllProcessItems     // Catch:{ RemoteException -> 0x0650 }
            java.lang.Object r7 = r7.get(r10)     // Catch:{ RemoteException -> 0x0650 }
            com.android.settings.applications.RunningState$ProcessItem r7 = (com.android.settings.applications.RunningState.ProcessItem) r7     // Catch:{ RemoteException -> 0x0650 }
            r21 = r14
            r13 = r9[r10]     // Catch:{ RemoteException -> 0x064e }
            int r15 = r0.mSequence     // Catch:{ RemoteException -> 0x064e }
            boolean r13 = r7.updateSize(r1, r13, r15)     // Catch:{ RemoteException -> 0x064e }
            r20 = r20 | r13
            int r13 = r7.mCurSeq     // Catch:{ RemoteException -> 0x064e }
            int r14 = r0.mSequence     // Catch:{ RemoteException -> 0x064e }
            if (r13 != r14) goto L_0x057c
            long r13 = r7.mSize     // Catch:{ RemoteException -> 0x064e }
            long r18 = r18 + r13
            r23 = r2
            r2 = r6
            r24 = r9
        L_0x0578:
            r14 = r21
            goto L_0x0642
        L_0x057c:
            android.app.ActivityManager$RunningAppProcessInfo r13 = r7.mRunningProcessInfo     // Catch:{ RemoteException -> 0x064e }
            int r13 = r13.importance     // Catch:{ RemoteException -> 0x064e }
            r14 = 400(0x190, float:5.6E-43)
            if (r13 < r14) goto L_0x0633
            long r14 = r7.mSize     // Catch:{ RemoteException -> 0x064e }
            long r13 = r21 + r14
            if (r6 == 0) goto L_0x05af
            com.android.settings.applications.RunningState$MergedItem r15 = new com.android.settings.applications.RunningState$MergedItem     // Catch:{ RemoteException -> 0x062d }
            int r8 = r7.mUserId     // Catch:{ RemoteException -> 0x062d }
            r15.<init>(r8)     // Catch:{ RemoteException -> 0x062d }
            r7.mMergedItem = r15     // Catch:{ RemoteException -> 0x062d }
            r15.mProcess = r7     // Catch:{ RemoteException -> 0x062d }
            int r7 = r15.mUserId     // Catch:{ RemoteException -> 0x062d }
            int r8 = r0.mMyUserId     // Catch:{ RemoteException -> 0x062d }
            if (r7 == r8) goto L_0x059d
            r7 = 1
            goto L_0x059e
        L_0x059d:
            r7 = 0
        L_0x059e:
            r7 = r7 | r11
            r6.add(r15)     // Catch:{ RemoteException -> 0x05ab }
            r23 = r2
            r8 = r6
            r24 = r9
            r2 = 1
            r9 = r7
            goto L_0x061b
        L_0x05ab:
            r2 = r6
            r9 = r7
            goto L_0x062f
        L_0x05af:
            java.util.ArrayList<com.android.settings.applications.RunningState$MergedItem> r8 = r0.mBackgroundItems     // Catch:{ RemoteException -> 0x062d }
            int r8 = r8.size()     // Catch:{ RemoteException -> 0x062d }
            if (r12 >= r8) goto L_0x05d5
            java.util.ArrayList<com.android.settings.applications.RunningState$MergedItem> r8 = r0.mBackgroundItems     // Catch:{ RemoteException -> 0x062d }
            java.lang.Object r8 = r8.get(r12)     // Catch:{ RemoteException -> 0x062d }
            com.android.settings.applications.RunningState$MergedItem r8 = (com.android.settings.applications.RunningState.MergedItem) r8     // Catch:{ RemoteException -> 0x062d }
            com.android.settings.applications.RunningState$ProcessItem r8 = r8.mProcess     // Catch:{ RemoteException -> 0x062d }
            if (r8 == r7) goto L_0x05c4
            goto L_0x05d5
        L_0x05c4:
            java.util.ArrayList<com.android.settings.applications.RunningState$MergedItem> r7 = r0.mBackgroundItems     // Catch:{ RemoteException -> 0x062d }
            java.lang.Object r7 = r7.get(r12)     // Catch:{ RemoteException -> 0x062d }
            r15 = r7
            com.android.settings.applications.RunningState$MergedItem r15 = (com.android.settings.applications.RunningState.MergedItem) r15     // Catch:{ RemoteException -> 0x062d }
            r23 = r2
            r8 = r6
            r24 = r9
            r9 = r11
        L_0x05d3:
            r2 = 1
            goto L_0x061b
        L_0x05d5:
            java.util.ArrayList r8 = new java.util.ArrayList     // Catch:{ RemoteException -> 0x062d }
            r8.<init>(r4)     // Catch:{ RemoteException -> 0x062d }
            r6 = 0
        L_0x05db:
            if (r6 >= r12) goto L_0x05fd
            java.util.ArrayList<com.android.settings.applications.RunningState$MergedItem> r15 = r0.mBackgroundItems     // Catch:{ RemoteException -> 0x062b }
            java.lang.Object r15 = r15.get(r6)     // Catch:{ RemoteException -> 0x062b }
            com.android.settings.applications.RunningState$MergedItem r15 = (com.android.settings.applications.RunningState.MergedItem) r15     // Catch:{ RemoteException -> 0x062b }
            r23 = r2
            int r2 = r15.mUserId     // Catch:{ RemoteException -> 0x062b }
            r24 = r9
            int r9 = r0.mMyUserId     // Catch:{ RemoteException -> 0x062b }
            if (r2 == r9) goto L_0x05f1
            r2 = 1
            goto L_0x05f2
        L_0x05f1:
            r2 = 0
        L_0x05f2:
            r11 = r11 | r2
            r8.add(r15)     // Catch:{ RemoteException -> 0x062b }
            int r6 = r6 + 1
            r2 = r23
            r9 = r24
            goto L_0x05db
        L_0x05fd:
            r23 = r2
            r24 = r9
            com.android.settings.applications.RunningState$MergedItem r15 = new com.android.settings.applications.RunningState$MergedItem     // Catch:{ RemoteException -> 0x062b }
            int r2 = r7.mUserId     // Catch:{ RemoteException -> 0x062b }
            r15.<init>(r2)     // Catch:{ RemoteException -> 0x062b }
            r7.mMergedItem = r15     // Catch:{ RemoteException -> 0x062b }
            r15.mProcess = r7     // Catch:{ RemoteException -> 0x062b }
            int r2 = r15.mUserId     // Catch:{ RemoteException -> 0x062b }
            int r6 = r0.mMyUserId     // Catch:{ RemoteException -> 0x062b }
            if (r2 == r6) goto L_0x0614
            r6 = 1
            goto L_0x0615
        L_0x0614:
            r6 = 0
        L_0x0615:
            r9 = r11 | r6
            r8.add(r15)     // Catch:{ RemoteException -> 0x0629 }
            goto L_0x05d3
        L_0x061b:
            r15.update(r1, r2)     // Catch:{ RemoteException -> 0x0629 }
            r15.updateSize(r1)     // Catch:{ RemoteException -> 0x0629 }
            int r12 = r12 + 1
            r2 = r8
            r11 = r9
            r14 = r13
            r8 = 200(0xc8, float:2.8E-43)
            goto L_0x0642
        L_0x0629:
            r2 = r8
            goto L_0x062f
        L_0x062b:
            r2 = r8
            goto L_0x062e
        L_0x062d:
            r2 = r6
        L_0x062e:
            r9 = r11
        L_0x062f:
            r10 = r13
            r12 = r20
            goto L_0x0665
        L_0x0633:
            r23 = r2
            r2 = r6
            r24 = r9
            r8 = 200(0xc8, float:2.8E-43)
            if (r13 > r8) goto L_0x0578
            long r13 = r7.mSize     // Catch:{ RemoteException -> 0x0653 }
            long r16 = r16 + r13
            goto L_0x0578
        L_0x0642:
            int r10 = r10 + 1
            r6 = r2
            r2 = r23
            r9 = r24
            r7 = 0
            r13 = 400(0x190, float:5.6E-43)
            goto L_0x0553
        L_0x064e:
            r2 = r6
            goto L_0x0653
        L_0x0650:
            r2 = r6
            r21 = r14
        L_0x0653:
            r9 = r11
            r12 = r20
            r10 = r21
            goto L_0x0665
        L_0x0659:
            r2 = r6
            r21 = r14
            r7 = r16
            goto L_0x066b
        L_0x065f:
            r16 = r10
            r18 = r16
            r2 = 0
            r9 = 0
        L_0x0665:
            r14 = r10
            r20 = r12
            r7 = r16
            r11 = r9
        L_0x066b:
            r9 = r18
            if (r2 != 0) goto L_0x06a0
            java.util.ArrayList<com.android.settings.applications.RunningState$MergedItem> r12 = r0.mBackgroundItems
            int r12 = r12.size()
            if (r12 <= r4) goto L_0x06a0
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>(r4)
            r12 = r11
            r11 = 0
        L_0x067e:
            if (r11 >= r4) goto L_0x069c
            java.util.ArrayList<com.android.settings.applications.RunningState$MergedItem> r13 = r0.mBackgroundItems
            java.lang.Object r13 = r13.get(r11)
            com.android.settings.applications.RunningState$MergedItem r13 = (com.android.settings.applications.RunningState.MergedItem) r13
            int r6 = r13.mUserId
            r16 = r9
            int r9 = r0.mMyUserId
            if (r6 == r9) goto L_0x0692
            r6 = 1
            goto L_0x0693
        L_0x0692:
            r6 = 0
        L_0x0693:
            r12 = r12 | r6
            r2.add(r13)
            int r11 = r11 + 1
            r9 = r16
            goto L_0x067e
        L_0x069c:
            r16 = r9
            r11 = r12
            goto L_0x06a2
        L_0x06a0:
            r16 = r9
        L_0x06a2:
            if (r2 == 0) goto L_0x06ee
            if (r11 != 0) goto L_0x06a8
            r9 = r2
            goto L_0x06ef
        L_0x06a8:
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            int r6 = r2.size()
            r10 = 0
        L_0x06b2:
            if (r10 >= r6) goto L_0x06cc
            java.lang.Object r11 = r2.get(r10)
            com.android.settings.applications.RunningState$MergedItem r11 = (com.android.settings.applications.RunningState.MergedItem) r11
            int r12 = r11.mUserId
            int r13 = r0.mMyUserId
            if (r12 == r13) goto L_0x06c6
            android.util.SparseArray<com.android.settings.applications.RunningState$MergedItem> r12 = r0.mOtherUserBackgroundItems
            r0.addOtherUserItem(r1, r9, r12, r11)
            goto L_0x06c9
        L_0x06c6:
            r9.add(r11)
        L_0x06c9:
            int r10 = r10 + 1
            goto L_0x06b2
        L_0x06cc:
            android.util.SparseArray<com.android.settings.applications.RunningState$MergedItem> r6 = r0.mOtherUserBackgroundItems
            int r10 = r6.size()
            r11 = 0
        L_0x06d3:
            if (r11 >= r10) goto L_0x06ef
            android.util.SparseArray<com.android.settings.applications.RunningState$MergedItem> r6 = r0.mOtherUserBackgroundItems
            java.lang.Object r6 = r6.valueAt(r11)
            r12 = r6
            com.android.settings.applications.RunningState$MergedItem r12 = (com.android.settings.applications.RunningState.MergedItem) r12
            int r6 = r12.mCurSeq
            int r13 = r0.mSequence
            if (r6 != r13) goto L_0x06eb
            r6 = 1
            r12.update(r1, r6)
            r12.updateSize(r1)
        L_0x06eb:
            int r11 = r11 + 1
            goto L_0x06d3
        L_0x06ee:
            r9 = 0
        L_0x06ef:
            r10 = 0
        L_0x06f0:
            java.util.ArrayList<com.android.settings.applications.RunningState$MergedItem> r11 = r0.mMergedItems
            int r11 = r11.size()
            if (r10 >= r11) goto L_0x0706
            java.util.ArrayList<com.android.settings.applications.RunningState$MergedItem> r11 = r0.mMergedItems
            java.lang.Object r11 = r11.get(r10)
            com.android.settings.applications.RunningState$MergedItem r11 = (com.android.settings.applications.RunningState.MergedItem) r11
            r11.updateSize(r1)
            int r10 = r10 + 1
            goto L_0x06f0
        L_0x0706:
            java.lang.Object r1 = r0.mLock
            monitor-enter(r1)
            r0.mNumBackgroundProcesses = r4     // Catch:{ all -> 0x0731 }
            r0.mNumForegroundProcesses = r5     // Catch:{ all -> 0x0731 }
            r0.mNumServiceProcesses = r3     // Catch:{ all -> 0x0731 }
            r0.mBackgroundProcessMemory = r14     // Catch:{ all -> 0x0731 }
            r0.mForegroundProcessMemory = r7     // Catch:{ all -> 0x0731 }
            r3 = r16
            r0.mServiceProcessMemory = r3     // Catch:{ all -> 0x0731 }
            if (r2 == 0) goto L_0x0723
            r0.mBackgroundItems = r2     // Catch:{ all -> 0x0731 }
            r0.mUserBackgroundItems = r9     // Catch:{ all -> 0x0731 }
            boolean r2 = r0.mWatchingBackgroundItems     // Catch:{ all -> 0x0731 }
            if (r2 == 0) goto L_0x0723
            r20 = 1
        L_0x0723:
            boolean r2 = r0.mHaveData     // Catch:{ all -> 0x0731 }
            if (r2 != 0) goto L_0x072f
            r2 = 1
            r0.mHaveData = r2     // Catch:{ all -> 0x0731 }
            java.lang.Object r0 = r0.mLock     // Catch:{ all -> 0x0731 }
            r0.notifyAll()     // Catch:{ all -> 0x0731 }
        L_0x072f:
            monitor-exit(r1)     // Catch:{ all -> 0x0731 }
            return r20
        L_0x0731:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0731 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.RunningState.update(android.content.Context, android.app.ActivityManager):boolean");
    }

    /* access modifiers changed from: package-private */
    public void setWatchingBackgroundItems(boolean z) {
        synchronized (this.mLock) {
            this.mWatchingBackgroundItems = z;
        }
    }

    /* access modifiers changed from: package-private */
    public ArrayList<MergedItem> getCurrentMergedItems() {
        ArrayList<MergedItem> arrayList;
        synchronized (this.mLock) {
            arrayList = this.mMergedItems;
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public ArrayList<MergedItem> getCurrentBackgroundItems() {
        ArrayList<MergedItem> arrayList;
        synchronized (this.mLock) {
            arrayList = this.mUserBackgroundItems;
        }
        return arrayList;
    }
}
