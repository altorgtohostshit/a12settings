package com.android.settings.applications;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.applications.RunningProcessesView;
import com.android.settings.applications.RunningState;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Collections;

public class RunningServiceDetails extends InstrumentedFragment implements RunningState.OnRefreshUiListener {
    final ArrayList<ActiveDetail> mActiveDetails = new ArrayList<>();
    ViewGroup mAllDetails;
    ActivityManager mAm;
    StringBuilder mBuilder = new StringBuilder(128);
    boolean mHaveData;
    LayoutInflater mInflater;
    RunningState.MergedItem mMergedItem;
    int mNumProcesses;
    int mNumServices;
    String mProcessName;
    TextView mProcessesHeader;
    View mRootView;
    TextView mServicesHeader;
    boolean mShowBackground;
    ViewGroup mSnippet;
    RunningProcessesView.ActiveItem mSnippetActiveItem;
    RunningProcessesView.ViewHolder mSnippetViewHolder;
    RunningState mState;
    int mUid;
    int mUserId;

    public int getMetricsCategory() {
        return 85;
    }

    class ActiveDetail implements View.OnClickListener {
        RunningProcessesView.ActiveItem mActiveItem;
        ComponentName mInstaller;
        PendingIntent mManageIntent;
        Button mReportButton;
        View mRootView;
        RunningState.ServiceItem mServiceItem;
        Button mStopButton;
        RunningProcessesView.ViewHolder mViewHolder;

        ActiveDetail() {
        }

        /* access modifiers changed from: package-private */
        public void stopActiveService(boolean z) {
            RunningState.ServiceItem serviceItem = this.mServiceItem;
            if (z || (serviceItem.mServiceInfo.applicationInfo.flags & 1) == 0) {
                RunningServiceDetails.this.getActivity().stopService(new Intent().setComponent(serviceItem.mRunningService.service));
                RunningServiceDetails runningServiceDetails = RunningServiceDetails.this;
                RunningState.MergedItem mergedItem = runningServiceDetails.mMergedItem;
                if (mergedItem == null) {
                    runningServiceDetails.mState.updateNow();
                    RunningServiceDetails.this.finish();
                } else if (runningServiceDetails.mShowBackground || mergedItem.mServices.size() > 1) {
                    RunningServiceDetails.this.mState.updateNow();
                } else {
                    RunningServiceDetails.this.mState.updateNow();
                    RunningServiceDetails.this.finish();
                }
            } else {
                RunningServiceDetails.this.showConfirmStopDialog(serviceItem.mRunningService.service);
            }
        }

        /* JADX WARNING: type inference failed for: r6v4 */
        /* JADX WARNING: type inference failed for: r6v5, types: [java.io.FileOutputStream] */
        /* JADX WARNING: type inference failed for: r6v7, types: [java.io.FileInputStream] */
        /* JADX WARNING: type inference failed for: r6v8 */
        /* JADX WARNING: type inference failed for: r6v12 */
        /* JADX WARNING: Can't wrap try/catch for region: R(13:2|(1:4)(1:5)|6|(1:8)(1:9)|10|(5:11|12|13|14|15)|16|17|27|28|(4:29|30|31|32)|44|46) */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x00ba, code lost:
            if (r7 != null) goto L_0x0097;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x00df, code lost:
            r3 = e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
            r6.close();
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x00bd */
        /* JADX WARNING: Multi-variable type inference failed */
        /* JADX WARNING: Removed duplicated region for block: B:42:0x00f6 A[SYNTHETIC, Splitter:B:42:0x00f6] */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x0132 A[SYNTHETIC, Splitter:B:48:0x0132] */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x0138 A[SYNTHETIC, Splitter:B:53:0x0138] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onClick(android.view.View r13) {
            /*
                r12 = this;
                android.widget.Button r0 = r12.mReportButton
                r1 = 0
                java.lang.String r2 = "RunningServicesDetails"
                if (r13 != r0) goto L_0x013c
                android.app.ApplicationErrorReport r13 = new android.app.ApplicationErrorReport
                r13.<init>()
                r0 = 5
                r13.type = r0
                com.android.settings.applications.RunningState$ServiceItem r0 = r12.mServiceItem
                android.content.pm.ServiceInfo r0 = r0.mServiceInfo
                java.lang.String r0 = r0.packageName
                r13.packageName = r0
                android.content.ComponentName r0 = r12.mInstaller
                java.lang.String r0 = r0.getPackageName()
                r13.installerPackageName = r0
                com.android.settings.applications.RunningState$ServiceItem r0 = r12.mServiceItem
                android.app.ActivityManager$RunningServiceInfo r0 = r0.mRunningService
                java.lang.String r0 = r0.process
                r13.processName = r0
                long r3 = java.lang.System.currentTimeMillis()
                r13.time = r3
                com.android.settings.applications.RunningState$ServiceItem r0 = r12.mServiceItem
                android.content.pm.ServiceInfo r0 = r0.mServiceInfo
                android.content.pm.ApplicationInfo r0 = r0.applicationInfo
                int r0 = r0.flags
                r3 = 1
                r0 = r0 & r3
                if (r0 == 0) goto L_0x003b
                r0 = r3
                goto L_0x003c
            L_0x003b:
                r0 = r1
            L_0x003c:
                r13.systemApp = r0
                android.app.ApplicationErrorReport$RunningServiceInfo r0 = new android.app.ApplicationErrorReport$RunningServiceInfo
                r0.<init>()
                com.android.settings.applications.RunningProcessesView$ActiveItem r4 = r12.mActiveItem
                long r4 = r4.mFirstRunTime
                r6 = 0
                int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                if (r4 < 0) goto L_0x0059
                long r4 = android.os.SystemClock.elapsedRealtime()
                com.android.settings.applications.RunningProcessesView$ActiveItem r6 = r12.mActiveItem
                long r6 = r6.mFirstRunTime
                long r4 = r4 - r6
                r0.durationMillis = r4
                goto L_0x005d
            L_0x0059:
                r4 = -1
                r0.durationMillis = r4
            L_0x005d:
                android.content.ComponentName r4 = new android.content.ComponentName
                com.android.settings.applications.RunningState$ServiceItem r5 = r12.mServiceItem
                android.content.pm.ServiceInfo r5 = r5.mServiceInfo
                java.lang.String r6 = r5.packageName
                java.lang.String r5 = r5.name
                r4.<init>(r6, r5)
                com.android.settings.applications.RunningServiceDetails r5 = com.android.settings.applications.RunningServiceDetails.this
                androidx.fragment.app.FragmentActivity r5 = r5.getActivity()
                java.lang.String r6 = "service_dump.txt"
                java.io.File r5 = r5.getFileStreamPath(r6)
                r6 = 0
                java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00a4, all -> 0x00a1 }
                r7.<init>(r5)     // Catch:{ IOException -> 0x00a4, all -> 0x00a1 }
                java.lang.String r8 = "activity"
                java.io.FileDescriptor r9 = r7.getFD()     // Catch:{ IOException -> 0x009f }
                r10 = 3
                java.lang.String[] r10 = new java.lang.String[r10]     // Catch:{ IOException -> 0x009f }
                java.lang.String r11 = "-a"
                r10[r1] = r11     // Catch:{ IOException -> 0x009f }
                java.lang.String r1 = "service"
                r10[r3] = r1     // Catch:{ IOException -> 0x009f }
                r1 = 2
                java.lang.String r3 = r4.flattenToString()     // Catch:{ IOException -> 0x009f }
                r10[r1] = r3     // Catch:{ IOException -> 0x009f }
                android.os.Debug.dumpService(r8, r9, r10)     // Catch:{ IOException -> 0x009f }
            L_0x0097:
                r7.close()     // Catch:{ IOException -> 0x00bd }
                goto L_0x00bd
            L_0x009b:
                r12 = move-exception
                r6 = r7
                goto L_0x0136
            L_0x009f:
                r1 = move-exception
                goto L_0x00a6
            L_0x00a1:
                r12 = move-exception
                goto L_0x0136
            L_0x00a4:
                r1 = move-exception
                r7 = r6
            L_0x00a6:
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x009b }
                r3.<init>()     // Catch:{ all -> 0x009b }
                java.lang.String r8 = "Can't dump service: "
                r3.append(r8)     // Catch:{ all -> 0x009b }
                r3.append(r4)     // Catch:{ all -> 0x009b }
                java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x009b }
                android.util.Log.w(r2, r3, r1)     // Catch:{ all -> 0x009b }
                if (r7 == 0) goto L_0x00bd
                goto L_0x0097
            L_0x00bd:
                java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ IOException -> 0x00df }
                r1.<init>(r5)     // Catch:{ IOException -> 0x00df }
                long r6 = r5.length()     // Catch:{ IOException -> 0x00da, all -> 0x00d7 }
                int r3 = (int) r6     // Catch:{ IOException -> 0x00da, all -> 0x00d7 }
                byte[] r3 = new byte[r3]     // Catch:{ IOException -> 0x00da, all -> 0x00d7 }
                r1.read(r3)     // Catch:{ IOException -> 0x00da, all -> 0x00d7 }
                java.lang.String r6 = new java.lang.String     // Catch:{ IOException -> 0x00da, all -> 0x00d7 }
                r6.<init>(r3)     // Catch:{ IOException -> 0x00da, all -> 0x00d7 }
                r0.serviceDetails = r6     // Catch:{ IOException -> 0x00da, all -> 0x00d7 }
                r1.close()     // Catch:{ IOException -> 0x00f9 }
                goto L_0x00f9
            L_0x00d7:
                r12 = move-exception
                r6 = r1
                goto L_0x0130
            L_0x00da:
                r3 = move-exception
                r6 = r1
                goto L_0x00e0
            L_0x00dd:
                r12 = move-exception
                goto L_0x0130
            L_0x00df:
                r3 = move-exception
            L_0x00e0:
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00dd }
                r1.<init>()     // Catch:{ all -> 0x00dd }
                java.lang.String r7 = "Can't read service dump: "
                r1.append(r7)     // Catch:{ all -> 0x00dd }
                r1.append(r4)     // Catch:{ all -> 0x00dd }
                java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00dd }
                android.util.Log.w(r2, r1, r3)     // Catch:{ all -> 0x00dd }
                if (r6 == 0) goto L_0x00f9
                r6.close()     // Catch:{ IOException -> 0x00f9 }
            L_0x00f9:
                r5.delete()
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r3 = "Details: "
                r1.append(r3)
                java.lang.String r3 = r0.serviceDetails
                r1.append(r3)
                java.lang.String r1 = r1.toString()
                android.util.Log.i(r2, r1)
                r13.runningServiceInfo = r0
                android.content.Intent r0 = new android.content.Intent
                java.lang.String r1 = "android.intent.action.APP_ERROR"
                r0.<init>(r1)
                android.content.ComponentName r1 = r12.mInstaller
                r0.setComponent(r1)
                java.lang.String r1 = "android.intent.extra.BUG_REPORT"
                r0.putExtra(r1, r13)
                r13 = 268435456(0x10000000, float:2.5243549E-29)
                r0.addFlags(r13)
                com.android.settings.applications.RunningServiceDetails r12 = com.android.settings.applications.RunningServiceDetails.this
                r12.startActivity(r0)
                return
            L_0x0130:
                if (r6 == 0) goto L_0x0135
                r6.close()     // Catch:{ IOException -> 0x0135 }
            L_0x0135:
                throw r12
            L_0x0136:
                if (r6 == 0) goto L_0x013b
                r6.close()     // Catch:{ IOException -> 0x013b }
            L_0x013b:
                throw r12
            L_0x013c:
                android.app.PendingIntent r13 = r12.mManageIntent
                if (r13 == 0) goto L_0x0165
                com.android.settings.applications.RunningServiceDetails r13 = com.android.settings.applications.RunningServiceDetails.this     // Catch:{ SendIntentException -> 0x0160, IllegalArgumentException -> 0x015b, ActivityNotFoundException -> 0x0156 }
                androidx.fragment.app.FragmentActivity r3 = r13.getActivity()     // Catch:{ SendIntentException -> 0x0160, IllegalArgumentException -> 0x015b, ActivityNotFoundException -> 0x0156 }
                android.app.PendingIntent r12 = r12.mManageIntent     // Catch:{ SendIntentException -> 0x0160, IllegalArgumentException -> 0x015b, ActivityNotFoundException -> 0x0156 }
                android.content.IntentSender r4 = r12.getIntentSender()     // Catch:{ SendIntentException -> 0x0160, IllegalArgumentException -> 0x015b, ActivityNotFoundException -> 0x0156 }
                r5 = 0
                r6 = 268959744(0x10080000, float:2.682127E-29)
                r7 = 524288(0x80000, float:7.34684E-40)
                r8 = 0
                r3.startIntentSender(r4, r5, r6, r7, r8)     // Catch:{ SendIntentException -> 0x0160, IllegalArgumentException -> 0x015b, ActivityNotFoundException -> 0x0156 }
                goto L_0x0196
            L_0x0156:
                r12 = move-exception
                android.util.Log.w(r2, r12)
                goto L_0x0196
            L_0x015b:
                r12 = move-exception
                android.util.Log.w(r2, r12)
                goto L_0x0196
            L_0x0160:
                r12 = move-exception
                android.util.Log.w(r2, r12)
                goto L_0x0196
            L_0x0165:
                com.android.settings.applications.RunningState$ServiceItem r13 = r12.mServiceItem
                if (r13 == 0) goto L_0x016d
                r12.stopActiveService(r1)
                goto L_0x0196
            L_0x016d:
                com.android.settings.applications.RunningProcessesView$ActiveItem r13 = r12.mActiveItem
                com.android.settings.applications.RunningState$BaseItem r13 = r13.mItem
                boolean r0 = r13.mBackground
                if (r0 == 0) goto L_0x0186
                com.android.settings.applications.RunningServiceDetails r0 = com.android.settings.applications.RunningServiceDetails.this
                android.app.ActivityManager r0 = r0.mAm
                android.content.pm.PackageItemInfo r13 = r13.mPackageInfo
                java.lang.String r13 = r13.packageName
                r0.killBackgroundProcesses(r13)
                com.android.settings.applications.RunningServiceDetails r12 = com.android.settings.applications.RunningServiceDetails.this
                r12.finish()
                goto L_0x0196
            L_0x0186:
                com.android.settings.applications.RunningServiceDetails r0 = com.android.settings.applications.RunningServiceDetails.this
                android.app.ActivityManager r0 = r0.mAm
                android.content.pm.PackageItemInfo r13 = r13.mPackageInfo
                java.lang.String r13 = r13.packageName
                r0.forceStopPackage(r13)
                com.android.settings.applications.RunningServiceDetails r12 = com.android.settings.applications.RunningServiceDetails.this
                r12.finish()
            L_0x0196:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.RunningServiceDetails.ActiveDetail.onClick(android.view.View):void");
        }
    }

    /* access modifiers changed from: package-private */
    public boolean findMergedItem() {
        RunningState.MergedItem mergedItem;
        int i;
        String str;
        RunningState.ProcessItem processItem;
        RunningState.ProcessItem processItem2;
        ArrayList<RunningState.MergedItem> currentBackgroundItems = this.mShowBackground ? this.mState.getCurrentBackgroundItems() : this.mState.getCurrentMergedItems();
        if (currentBackgroundItems != null) {
            int i2 = 0;
            while (true) {
                if (i2 >= currentBackgroundItems.size()) {
                    break;
                }
                mergedItem = currentBackgroundItems.get(i2);
                if (mergedItem.mUserId == this.mUserId && (((i = this.mUid) < 0 || (processItem2 = mergedItem.mProcess) == null || processItem2.mUid == i) && ((str = this.mProcessName) == null || ((processItem = mergedItem.mProcess) != null && str.equals(processItem.mProcessName))))) {
                    break;
                }
                i2++;
            }
        }
        mergedItem = null;
        if (this.mMergedItem == mergedItem) {
            return false;
        }
        this.mMergedItem = mergedItem;
        return true;
    }

    /* access modifiers changed from: package-private */
    public void addServicesHeader() {
        if (this.mNumServices == 0) {
            TextView textView = (TextView) this.mInflater.inflate(R.layout.preference_category, this.mAllDetails, false);
            this.mServicesHeader = textView;
            textView.setText(R.string.runningservicedetails_services_title);
            this.mAllDetails.addView(this.mServicesHeader);
        }
        this.mNumServices++;
    }

    /* access modifiers changed from: package-private */
    public void addProcessesHeader() {
        if (this.mNumProcesses == 0) {
            TextView textView = (TextView) this.mInflater.inflate(R.layout.preference_category, this.mAllDetails, false);
            this.mProcessesHeader = textView;
            textView.setText(R.string.runningservicedetails_processes_title);
            this.mAllDetails.addView(this.mProcessesHeader);
        }
        this.mNumProcesses++;
    }

    /* JADX WARNING: type inference failed for: r9v0, types: [com.android.settings.applications.RunningState$MergedItem, com.android.settings.applications.RunningState$BaseItem] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addServiceDetailsView(com.android.settings.applications.RunningState.ServiceItem r8, com.android.settings.applications.RunningState.MergedItem r9, boolean r10, boolean r11) {
        /*
            r7 = this;
            if (r10 == 0) goto L_0x0006
            r7.addServicesHeader()
            goto L_0x0011
        L_0x0006:
            int r0 = r9.mUserId
            int r1 = android.os.UserHandle.myUserId()
            if (r0 == r1) goto L_0x0011
            r7.addProcessesHeader()
        L_0x0011:
            if (r8 == 0) goto L_0x0015
            r0 = r8
            goto L_0x0016
        L_0x0015:
            r0 = r9
        L_0x0016:
            com.android.settings.applications.RunningServiceDetails$ActiveDetail r1 = new com.android.settings.applications.RunningServiceDetails$ActiveDetail
            r1.<init>()
            android.view.LayoutInflater r2 = r7.mInflater
            r3 = 2131100142(0x7f0601ee, float:1.7812657E38)
            android.view.ViewGroup r4 = r7.mAllDetails
            r5 = 0
            android.view.View r2 = r2.inflate(r3, r4, r5)
            android.view.ViewGroup r3 = r7.mAllDetails
            r3.addView(r2)
            r1.mRootView = r2
            r1.mServiceItem = r8
            com.android.settings.applications.RunningProcessesView$ViewHolder r3 = new com.android.settings.applications.RunningProcessesView$ViewHolder
            r3.<init>(r2)
            r1.mViewHolder = r3
            com.android.settings.applications.RunningState r4 = r7.mState
            java.lang.StringBuilder r6 = r7.mBuilder
            com.android.settings.applications.RunningProcessesView$ActiveItem r0 = r3.bind(r4, r0, r6)
            r1.mActiveItem = r0
            r0 = 8
            if (r11 != 0) goto L_0x004f
            r11 = 2131559603(0x7f0d04b3, float:1.8744555E38)
            android.view.View r11 = r2.findViewById(r11)
            r11.setVisibility(r0)
        L_0x004f:
            if (r8 == 0) goto L_0x0061
            android.app.ActivityManager$RunningServiceInfo r11 = r8.mRunningService
            int r3 = r11.clientLabel
            if (r3 == 0) goto L_0x0061
            android.app.ActivityManager r3 = r7.mAm
            android.content.ComponentName r11 = r11.service
            android.app.PendingIntent r11 = r3.getRunningServiceControlPanel(r11)
            r1.mManageIntent = r11
        L_0x0061:
            r11 = 2131558756(0x7f0d0164, float:1.8742837E38)
            android.view.View r11 = r2.findViewById(r11)
            android.widget.TextView r11 = (android.widget.TextView) r11
            r3 = 2131559173(0x7f0d0305, float:1.8743683E38)
            android.view.View r3 = r2.findViewById(r3)
            android.widget.Button r3 = (android.widget.Button) r3
            r1.mStopButton = r3
            r3 = 2131559542(0x7f0d0476, float:1.874443E38)
            android.view.View r3 = r2.findViewById(r3)
            android.widget.Button r3 = (android.widget.Button) r3
            r1.mReportButton = r3
            if (r10 == 0) goto L_0x0099
            int r10 = r9.mUserId
            int r3 = android.os.UserHandle.myUserId()
            if (r10 == r3) goto L_0x0099
            r11.setVisibility(r0)
            r8 = 2131558781(0x7f0d017d, float:1.8742888E38)
            android.view.View r8 = r2.findViewById(r8)
            r8.setVisibility(r0)
            goto L_0x0164
        L_0x0099:
            r10 = 1
            if (r8 == 0) goto L_0x00ba
            android.content.pm.ServiceInfo r0 = r8.mServiceInfo
            int r0 = r0.descriptionRes
            if (r0 == 0) goto L_0x00ba
            androidx.fragment.app.FragmentActivity r9 = r7.getActivity()
            android.content.pm.PackageManager r9 = r9.getPackageManager()
            android.content.pm.ServiceInfo r0 = r8.mServiceInfo
            java.lang.String r2 = r0.packageName
            int r3 = r0.descriptionRes
            android.content.pm.ApplicationInfo r0 = r0.applicationInfo
            java.lang.CharSequence r9 = r9.getText(r2, r3, r0)
            r11.setText(r9)
            goto L_0x0108
        L_0x00ba:
            boolean r9 = r9.mBackground
            if (r9 == 0) goto L_0x00c5
            r9 = 2130969365(0x7f040315, float:1.754741E38)
            r11.setText(r9)
            goto L_0x0108
        L_0x00c5:
            android.app.PendingIntent r9 = r1.mManageIntent
            if (r9 == 0) goto L_0x00f4
            androidx.fragment.app.FragmentActivity r9 = r7.getActivity()     // Catch:{ NameNotFoundException -> 0x0108 }
            android.content.pm.PackageManager r9 = r9.getPackageManager()     // Catch:{ NameNotFoundException -> 0x0108 }
            android.app.ActivityManager$RunningServiceInfo r0 = r8.mRunningService     // Catch:{ NameNotFoundException -> 0x0108 }
            java.lang.String r0 = r0.clientPackage     // Catch:{ NameNotFoundException -> 0x0108 }
            android.content.res.Resources r9 = r9.getResourcesForApplication(r0)     // Catch:{ NameNotFoundException -> 0x0108 }
            android.app.ActivityManager$RunningServiceInfo r0 = r8.mRunningService     // Catch:{ NameNotFoundException -> 0x0108 }
            int r0 = r0.clientLabel     // Catch:{ NameNotFoundException -> 0x0108 }
            java.lang.String r9 = r9.getString(r0)     // Catch:{ NameNotFoundException -> 0x0108 }
            androidx.fragment.app.FragmentActivity r0 = r7.getActivity()     // Catch:{ NameNotFoundException -> 0x0108 }
            r2 = 2130972982(0x7f041136, float:1.7554746E38)
            java.lang.Object[] r3 = new java.lang.Object[r10]     // Catch:{ NameNotFoundException -> 0x0108 }
            r3[r5] = r9     // Catch:{ NameNotFoundException -> 0x0108 }
            java.lang.String r9 = r0.getString(r2, r3)     // Catch:{ NameNotFoundException -> 0x0108 }
            r11.setText(r9)     // Catch:{ NameNotFoundException -> 0x0108 }
            goto L_0x0108
        L_0x00f4:
            androidx.fragment.app.FragmentActivity r9 = r7.getActivity()
            if (r8 == 0) goto L_0x00fe
            r0 = 2130972987(0x7f04113b, float:1.7554756E38)
            goto L_0x0101
        L_0x00fe:
            r0 = 2130971030(0x7f040996, float:1.7550787E38)
        L_0x0101:
            java.lang.CharSequence r9 = r9.getText(r0)
            r11.setText(r9)
        L_0x0108:
            android.widget.Button r9 = r1.mStopButton
            r9.setOnClickListener(r1)
            android.widget.Button r9 = r1.mStopButton
            androidx.fragment.app.FragmentActivity r11 = r7.getActivity()
            android.app.PendingIntent r0 = r1.mManageIntent
            if (r0 == 0) goto L_0x011b
            r0 = 2130972981(0x7f041135, float:1.7554744E38)
            goto L_0x011e
        L_0x011b:
            r0 = 2130972986(0x7f04113a, float:1.7554754E38)
        L_0x011e:
            java.lang.CharSequence r11 = r11.getText(r0)
            r9.setText(r11)
            android.widget.Button r9 = r1.mReportButton
            r9.setOnClickListener(r1)
            android.widget.Button r9 = r1.mReportButton
            r11 = 17041256(0x1040768, float:2.4249885E-38)
            r9.setText(r11)
            androidx.fragment.app.FragmentActivity r9 = r7.getActivity()
            android.content.ContentResolver r9 = r9.getContentResolver()
            java.lang.String r11 = "send_action_app_error"
            int r9 = android.provider.Settings.Global.getInt(r9, r11, r5)
            if (r9 == 0) goto L_0x015f
            if (r8 == 0) goto L_0x015f
            androidx.fragment.app.FragmentActivity r9 = r7.getActivity()
            android.content.pm.ServiceInfo r8 = r8.mServiceInfo
            java.lang.String r11 = r8.packageName
            android.content.pm.ApplicationInfo r8 = r8.applicationInfo
            int r8 = r8.flags
            android.content.ComponentName r8 = android.app.ApplicationErrorReport.getErrorReportReceiver(r9, r11, r8)
            r1.mInstaller = r8
            android.widget.Button r9 = r1.mReportButton
            if (r8 == 0) goto L_0x015b
            r5 = r10
        L_0x015b:
            r9.setEnabled(r5)
            goto L_0x0164
        L_0x015f:
            android.widget.Button r8 = r1.mReportButton
            r8.setEnabled(r5)
        L_0x0164:
            java.util.ArrayList<com.android.settings.applications.RunningServiceDetails$ActiveDetail> r7 = r7.mActiveDetails
            r7.add(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.RunningServiceDetails.addServiceDetailsView(com.android.settings.applications.RunningState$ServiceItem, com.android.settings.applications.RunningState$MergedItem, boolean, boolean):void");
    }

    /* access modifiers changed from: package-private */
    public void addProcessDetailsView(RunningState.ProcessItem processItem, boolean z) {
        int i;
        addProcessesHeader();
        ActiveDetail activeDetail = new ActiveDetail();
        View inflate = this.mInflater.inflate(R.layout.running_service_details_process, this.mAllDetails, false);
        this.mAllDetails.addView(inflate);
        activeDetail.mRootView = inflate;
        RunningProcessesView.ViewHolder viewHolder = new RunningProcessesView.ViewHolder(inflate);
        activeDetail.mViewHolder = viewHolder;
        activeDetail.mActiveItem = viewHolder.bind(this.mState, processItem, this.mBuilder);
        TextView textView = (TextView) inflate.findViewById(R.id.comp_description);
        if (processItem.mUserId != UserHandle.myUserId()) {
            textView.setVisibility(8);
        } else if (z) {
            textView.setText(R.string.main_running_process_description);
        } else {
            CharSequence charSequence = null;
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = processItem.mRunningProcessInfo;
            ComponentName componentName = runningAppProcessInfo.importanceReasonComponent;
            int i2 = runningAppProcessInfo.importanceReasonCode;
            if (i2 == 1) {
                i = R.string.process_provider_in_use_description;
                if (componentName != null) {
                    ProviderInfo providerInfo = getActivity().getPackageManager().getProviderInfo(runningAppProcessInfo.importanceReasonComponent, 0);
                    charSequence = RunningState.makeLabel(getActivity().getPackageManager(), providerInfo.name, providerInfo);
                }
            } else if (i2 != 2) {
                i = 0;
            } else {
                i = R.string.process_service_in_use_description;
                if (componentName != null) {
                    try {
                        ServiceInfo serviceInfo = getActivity().getPackageManager().getServiceInfo(runningAppProcessInfo.importanceReasonComponent, 0);
                        charSequence = RunningState.makeLabel(getActivity().getPackageManager(), serviceInfo.name, serviceInfo);
                    } catch (PackageManager.NameNotFoundException unused) {
                    }
                }
            }
            if (!(i == 0 || charSequence == null)) {
                textView.setText(getActivity().getString(i, new Object[]{charSequence}));
            }
        }
        this.mActiveDetails.add(activeDetail);
    }

    /* access modifiers changed from: package-private */
    public void addDetailsViews(RunningState.MergedItem mergedItem, boolean z, boolean z2) {
        RunningState.ProcessItem processItem;
        if (mergedItem != null) {
            boolean z3 = true;
            if (z) {
                for (int i = 0; i < mergedItem.mServices.size(); i++) {
                    addServiceDetailsView(mergedItem.mServices.get(i), mergedItem, true, true);
                }
            }
            if (!z2) {
                return;
            }
            if (mergedItem.mServices.size() <= 0) {
                if (mergedItem.mUserId == UserHandle.myUserId()) {
                    z3 = false;
                }
                addServiceDetailsView((RunningState.ServiceItem) null, mergedItem, false, z3);
                return;
            }
            int i2 = -1;
            while (i2 < mergedItem.mOtherProcesses.size()) {
                if (i2 < 0) {
                    processItem = mergedItem.mProcess;
                } else {
                    processItem = mergedItem.mOtherProcesses.get(i2);
                }
                if (processItem == null || processItem.mPid > 0) {
                    addProcessDetailsView(processItem, i2 < 0);
                }
                i2++;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void addDetailViews() {
        ArrayList<RunningState.MergedItem> arrayList;
        for (int size = this.mActiveDetails.size() - 1; size >= 0; size--) {
            this.mAllDetails.removeView(this.mActiveDetails.get(size).mRootView);
        }
        this.mActiveDetails.clear();
        TextView textView = this.mServicesHeader;
        if (textView != null) {
            this.mAllDetails.removeView(textView);
            this.mServicesHeader = null;
        }
        TextView textView2 = this.mProcessesHeader;
        if (textView2 != null) {
            this.mAllDetails.removeView(textView2);
            this.mProcessesHeader = null;
        }
        this.mNumProcesses = 0;
        this.mNumServices = 0;
        RunningState.MergedItem mergedItem = this.mMergedItem;
        if (mergedItem == null) {
            return;
        }
        if (mergedItem.mUser != null) {
            if (this.mShowBackground) {
                arrayList = new ArrayList<>(this.mMergedItem.mChildren);
                Collections.sort(arrayList, this.mState.mBackgroundComparator);
            } else {
                arrayList = mergedItem.mChildren;
            }
            for (int i = 0; i < arrayList.size(); i++) {
                addDetailsViews(arrayList.get(i), true, false);
            }
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                addDetailsViews(arrayList.get(i2), false, true);
            }
            return;
        }
        addDetailsViews(mergedItem, true, true);
    }

    /* access modifiers changed from: package-private */
    public void refreshUi(boolean z) {
        if (findMergedItem()) {
            z = true;
        }
        if (z) {
            RunningState.MergedItem mergedItem = this.mMergedItem;
            if (mergedItem != null) {
                this.mSnippetActiveItem = this.mSnippetViewHolder.bind(this.mState, mergedItem, this.mBuilder);
            } else {
                RunningProcessesView.ActiveItem activeItem = this.mSnippetActiveItem;
                if (activeItem != null) {
                    activeItem.mHolder.size.setText("");
                    this.mSnippetActiveItem.mHolder.uptime.setText("");
                    this.mSnippetActiveItem.mHolder.description.setText(R.string.no_services);
                } else {
                    finish();
                    return;
                }
            }
            addDetailViews();
        }
    }

    /* access modifiers changed from: private */
    public void finish() {
        ThreadUtils.postOnMainThread(new RunningServiceDetails$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$finish$0() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.onBackPressed();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        this.mUid = getArguments().getInt("uid", -1);
        this.mUserId = getArguments().getInt("user_id", 0);
        this.mProcessName = getArguments().getString("process", (String) null);
        this.mShowBackground = getArguments().getBoolean("background", false);
        this.mAm = (ActivityManager) getActivity().getSystemService("activity");
        this.mInflater = (LayoutInflater) getActivity().getSystemService("layout_inflater");
        this.mState = RunningState.getInstance(getActivity());
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.running_service_details, viewGroup, false);
        Utils.prepareCustomPreferencesList(viewGroup, inflate, inflate, false);
        this.mRootView = inflate;
        this.mAllDetails = (ViewGroup) inflate.findViewById(R.id.all_details);
        ViewGroup viewGroup2 = (ViewGroup) inflate.findViewById(R.id.snippet);
        this.mSnippet = viewGroup2;
        this.mSnippetViewHolder = new RunningProcessesView.ViewHolder(viewGroup2);
        ensureData();
        return inflate;
    }

    public void onPause() {
        super.onPause();
        this.mHaveData = false;
        this.mState.pause();
    }

    public void onResume() {
        super.onResume();
        ensureData();
    }

    /* access modifiers changed from: package-private */
    public ActiveDetail activeDetailForService(ComponentName componentName) {
        ActivityManager.RunningServiceInfo runningServiceInfo;
        for (int i = 0; i < this.mActiveDetails.size(); i++) {
            ActiveDetail activeDetail = this.mActiveDetails.get(i);
            RunningState.ServiceItem serviceItem = activeDetail.mServiceItem;
            if (serviceItem != null && (runningServiceInfo = serviceItem.mRunningService) != null && componentName.equals(runningServiceInfo.service)) {
                return activeDetail;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void showConfirmStopDialog(ComponentName componentName) {
        MyAlertDialogFragment newConfirmStop = MyAlertDialogFragment.newConfirmStop(1, componentName);
        newConfirmStop.setTargetFragment(this, 0);
        newConfirmStop.show(getFragmentManager(), "confirmstop");
    }

    public static class MyAlertDialogFragment extends InstrumentedDialogFragment {
        public int getMetricsCategory() {
            return 536;
        }

        public static MyAlertDialogFragment newConfirmStop(int i, ComponentName componentName) {
            MyAlertDialogFragment myAlertDialogFragment = new MyAlertDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", i);
            bundle.putParcelable("comp", componentName);
            myAlertDialogFragment.setArguments(bundle);
            return myAlertDialogFragment;
        }

        /* access modifiers changed from: package-private */
        public RunningServiceDetails getOwner() {
            return (RunningServiceDetails) getTargetFragment();
        }

        public Dialog onCreateDialog(Bundle bundle) {
            int i = getArguments().getInt("id");
            if (i == 1) {
                final ComponentName componentName = (ComponentName) getArguments().getParcelable("comp");
                if (getOwner().activeDetailForService(componentName) == null) {
                    return null;
                }
                return new AlertDialog.Builder(getActivity()).setTitle((CharSequence) getActivity().getString(R.string.runningservicedetails_stop_dlg_title)).setMessage((CharSequence) getActivity().getString(R.string.runningservicedetails_stop_dlg_text)).setPositiveButton((int) R.string.dlg_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActiveDetail activeDetailForService = MyAlertDialogFragment.this.getOwner().activeDetailForService(componentName);
                        if (activeDetailForService != null) {
                            activeDetailForService.stopActiveService(true);
                        }
                    }
                }).setNegativeButton((int) R.string.dlg_cancel, (DialogInterface.OnClickListener) null).create();
            }
            throw new IllegalArgumentException("unknown id " + i);
        }
    }

    /* access modifiers changed from: package-private */
    public void ensureData() {
        if (!this.mHaveData) {
            this.mHaveData = true;
            this.mState.resume(this);
            this.mState.waitForData();
            refreshUi(true);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateTimes() {
        RunningProcessesView.ActiveItem activeItem = this.mSnippetActiveItem;
        if (activeItem != null) {
            activeItem.updateTime(getActivity(), this.mBuilder);
        }
        for (int i = 0; i < this.mActiveDetails.size(); i++) {
            this.mActiveDetails.get(i).mActiveItem.updateTime(getActivity(), this.mBuilder);
        }
    }

    public void onRefreshUi(int i) {
        if (getActivity() != null) {
            if (i == 0) {
                updateTimes();
            } else if (i == 1) {
                refreshUi(false);
                updateTimes();
            } else if (i == 2) {
                refreshUi(true);
                updateTimes();
            }
        }
    }
}
