package com.android.settings.applications.appinfo;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.om.OverlayInfo;
import android.content.om.OverlayManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.Utils;
import com.android.settings.applications.ApplicationFeatureProvider;
import com.android.settings.applications.appinfo.ButtonActionDialogFragment;
import com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.instrumentation.SettingsStatsLog;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.widget.ActionButtonsPreference;
import java.util.ArrayList;
import java.util.HashSet;

public class AppButtonsPreferenceController extends BasePreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnResume, OnDestroy, ApplicationsState.Callbacks {
    public static final String APP_CHG = "chg";
    private static final String KEY_ACTION_BUTTONS = "action_buttons";
    public static final String KEY_REMOVE_TASK_WHEN_FINISHING = "remove_task_when_finishing";
    private static final boolean LOCAL_LOGV = false;
    private static final String TAG = "AppButtonsPrefCtl";
    /* access modifiers changed from: private */
    public boolean mAccessedFromAutoRevoke;
    /* access modifiers changed from: private */
    public final SettingsActivity mActivity;
    ApplicationsState.AppEntry mAppEntry;
    private Intent mAppLaunchIntent;
    private final ApplicationFeatureProvider mApplicationFeatureProvider;
    /* access modifiers changed from: private */
    public RestrictedLockUtils.EnforcedAdmin mAppsControlDisallowedAdmin;
    /* access modifiers changed from: private */
    public boolean mAppsControlDisallowedBySystem;
    ActionButtonsPreference mButtonsPref;
    private final BroadcastReceiver mCheckKillProcessesReceiver;
    boolean mDisableAfterUninstall;
    /* access modifiers changed from: private */
    public final DevicePolicyManager mDpm;
    /* access modifiers changed from: private */
    public boolean mFinishing;
    /* access modifiers changed from: private */
    public final InstrumentedPreferenceFragment mFragment;
    final HashSet<String> mHomePackages = new HashSet<>();
    private boolean mListeningToPackageRemove;
    /* access modifiers changed from: private */
    public final MetricsFeatureProvider mMetricsFeatureProvider;
    private final OverlayManager mOverlayManager;
    PackageInfo mPackageInfo;
    String mPackageName;
    private final BroadcastReceiver mPackageRemovedReceiver;
    /* access modifiers changed from: private */
    public final PackageManager mPm;
    /* access modifiers changed from: private */
    public final int mRequestRemoveDeviceAdmin;
    private final int mRequestUninstall;
    private PreferenceScreen mScreen;
    private ApplicationsState.Session mSession;
    /* access modifiers changed from: private */
    public long mSessionId;
    ApplicationsState mState;
    /* access modifiers changed from: private */
    public boolean mUpdatedSysApp;
    /* access modifiers changed from: private */
    public final int mUserId;
    private final UserManager mUserManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return KEY_ACTION_BUTTONS;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public void onAllSizesComputed() {
    }

    public void onLauncherInfoChanged() {
    }

    public void onLoadEntriesCompleted() {
    }

    public void onPackageIconChanged() {
    }

    public void onPackageSizeChanged(String str) {
    }

    public void onRebuildComplete(ArrayList<ApplicationsState.AppEntry> arrayList) {
    }

    public void onRunningStateChanged(boolean z) {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AppButtonsPreferenceController(SettingsActivity settingsActivity, InstrumentedPreferenceFragment instrumentedPreferenceFragment, Lifecycle lifecycle, String str, ApplicationsState applicationsState, int i, int i2) {
        super(settingsActivity, KEY_ACTION_BUTTONS);
        boolean z = LOCAL_LOGV;
        this.mDisableAfterUninstall = LOCAL_LOGV;
        this.mUpdatedSysApp = LOCAL_LOGV;
        this.mListeningToPackageRemove = LOCAL_LOGV;
        this.mFinishing = LOCAL_LOGV;
        this.mCheckKillProcessesReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                boolean z = getResultCode() != 0 ? true : AppButtonsPreferenceController.LOCAL_LOGV;
                Log.d(AppButtonsPreferenceController.TAG, "Got broadcast response: Restart status for " + AppButtonsPreferenceController.this.mAppEntry.info.packageName + " " + z);
                AppButtonsPreferenceController.this.updateForceStopButtonInner(z);
            }
        };
        this.mPackageRemovedReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
                if (!AppButtonsPreferenceController.this.mFinishing && AppButtonsPreferenceController.this.mAppEntry.info.packageName.equals(schemeSpecificPart)) {
                    AppButtonsPreferenceController.this.mActivity.finishAndRemoveTask();
                }
            }
        };
        if (instrumentedPreferenceFragment instanceof ButtonActionDialogFragment.AppButtonsDialogListener) {
            FeatureFactory factory = FeatureFactory.getFactory(settingsActivity);
            this.mMetricsFeatureProvider = factory.getMetricsFeatureProvider();
            this.mApplicationFeatureProvider = factory.getApplicationFeatureProvider(settingsActivity);
            this.mState = applicationsState;
            this.mDpm = (DevicePolicyManager) settingsActivity.getSystemService("device_policy");
            this.mUserManager = (UserManager) settingsActivity.getSystemService("user");
            PackageManager packageManager = settingsActivity.getPackageManager();
            this.mPm = packageManager;
            this.mOverlayManager = (OverlayManager) settingsActivity.getSystemService(OverlayManager.class);
            this.mPackageName = str;
            this.mActivity = settingsActivity;
            this.mFragment = instrumentedPreferenceFragment;
            int myUserId = UserHandle.myUserId();
            this.mUserId = myUserId;
            this.mRequestUninstall = i;
            this.mRequestRemoveDeviceAdmin = i2;
            this.mAppLaunchIntent = packageManager.getLaunchIntentForPackage(this.mPackageName);
            long longExtra = settingsActivity.getIntent().getLongExtra("android.intent.action.AUTO_REVOKE_PERMISSIONS", 0);
            this.mSessionId = longExtra;
            this.mAccessedFromAutoRevoke = longExtra != 0 ? true : z;
            if (str != null) {
                this.mAppEntry = this.mState.getEntry(str, myUserId);
                this.mSession = this.mState.newSession(this, lifecycle);
                lifecycle.addObserver(this);
                return;
            }
            this.mFinishing = true;
            return;
        }
        throw new IllegalArgumentException("Fragment should implement AppButtonsDialogListener");
    }

    public int getAvailabilityStatus() {
        return (this.mFinishing || isInstantApp() || isSystemModule()) ? 4 : 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mScreen = preferenceScreen;
        if (isAvailable()) {
            initButtonPreference();
        }
    }

    public void onResume() {
        if (isAvailable()) {
            this.mAppsControlDisallowedBySystem = RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mActivity, "no_control_apps", this.mUserId);
            this.mAppsControlDisallowedAdmin = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mActivity, "no_control_apps", this.mUserId);
            if (!refreshUi()) {
                setIntentAndFinish(true, LOCAL_LOGV);
            }
        }
    }

    public void onDestroy() {
        stopListeningToPackageRemove();
    }

    private class UninstallAndDisableButtonListener implements View.OnClickListener {
        private UninstallAndDisableButtonListener() {
        }

        public void onClick(View view) {
            if (AppButtonsPreferenceController.this.mAccessedFromAutoRevoke) {
                Log.i(AppButtonsPreferenceController.TAG, "sessionId: " + AppButtonsPreferenceController.this.mSessionId + " uninstalling " + AppButtonsPreferenceController.this.mPackageName + " with uid " + AppButtonsPreferenceController.this.getUid() + ", reached from auto revoke");
                SettingsStatsLog.write(272, AppButtonsPreferenceController.this.mSessionId, AppButtonsPreferenceController.this.getUid(), AppButtonsPreferenceController.this.mPackageName, 5);
            }
            AppButtonsPreferenceController appButtonsPreferenceController = AppButtonsPreferenceController.this;
            String str = appButtonsPreferenceController.mAppEntry.info.packageName;
            if (appButtonsPreferenceController.mDpm.packageHasActiveAdmins(AppButtonsPreferenceController.this.mPackageInfo.packageName)) {
                AppButtonsPreferenceController.this.stopListeningToPackageRemove();
                Intent intent = new Intent(AppButtonsPreferenceController.this.mActivity, DeviceAdminAdd.class);
                intent.putExtra("android.app.extra.DEVICE_ADMIN_PACKAGE_NAME", str);
                AppButtonsPreferenceController.this.mMetricsFeatureProvider.action((Context) AppButtonsPreferenceController.this.mActivity, 873, (Pair<Integer, Object>[]) new Pair[0]);
                AppButtonsPreferenceController.this.mFragment.startActivityForResult(intent, AppButtonsPreferenceController.this.mRequestRemoveDeviceAdmin);
                return;
            }
            RestrictedLockUtils.EnforcedAdmin checkIfUninstallBlocked = RestrictedLockUtilsInternal.checkIfUninstallBlocked(AppButtonsPreferenceController.this.mActivity, str, AppButtonsPreferenceController.this.mUserId);
            boolean z = AppButtonsPreferenceController.this.mAppsControlDisallowedBySystem || RestrictedLockUtilsInternal.hasBaseUserRestriction(AppButtonsPreferenceController.this.mActivity, str, AppButtonsPreferenceController.this.mUserId);
            if (checkIfUninstallBlocked == null || z) {
                AppButtonsPreferenceController appButtonsPreferenceController2 = AppButtonsPreferenceController.this;
                ApplicationInfo applicationInfo = appButtonsPreferenceController2.mAppEntry.info;
                int i = applicationInfo.flags;
                if ((i & 1) != 0) {
                    if (!applicationInfo.enabled || appButtonsPreferenceController2.isDisabledUntilUsed()) {
                        AppButtonsPreferenceController.this.mMetricsFeatureProvider.action((Context) AppButtonsPreferenceController.this.mActivity, AppButtonsPreferenceController.this.mAppEntry.info.enabled ? 874 : 875, (Pair<Integer, Object>[]) new Pair[0]);
                        AppButtonsPreferenceController appButtonsPreferenceController3 = AppButtonsPreferenceController.this;
                        AsyncTask.execute(new DisableChangerRunnable(appButtonsPreferenceController3.mPm, AppButtonsPreferenceController.this.mAppEntry.info.packageName, 0));
                    } else if (!AppButtonsPreferenceController.this.mUpdatedSysApp || !AppButtonsPreferenceController.this.isSingleUser()) {
                        AppButtonsPreferenceController.this.showDialogInner(0);
                    } else {
                        AppButtonsPreferenceController.this.showDialogInner(1);
                    }
                } else if ((8388608 & i) == 0) {
                    appButtonsPreferenceController2.uninstallPkg(str, true, AppButtonsPreferenceController.LOCAL_LOGV);
                } else {
                    appButtonsPreferenceController2.uninstallPkg(str, AppButtonsPreferenceController.LOCAL_LOGV, AppButtonsPreferenceController.LOCAL_LOGV);
                }
            } else {
                RestrictedLockUtils.sendShowAdminSupportDetailsIntent(AppButtonsPreferenceController.this.mActivity, checkIfUninstallBlocked);
            }
        }
    }

    private class ForceStopButtonListener implements View.OnClickListener {
        private ForceStopButtonListener() {
        }

        public void onClick(View view) {
            if (AppButtonsPreferenceController.this.mAppsControlDisallowedAdmin == null || AppButtonsPreferenceController.this.mAppsControlDisallowedBySystem) {
                AppButtonsPreferenceController.this.showDialogInner(2);
            } else {
                RestrictedLockUtils.sendShowAdminSupportDetailsIntent(AppButtonsPreferenceController.this.mActivity, AppButtonsPreferenceController.this.mAppsControlDisallowedAdmin);
            }
        }
    }

    public void handleActivityResult(int i, int i2, Intent intent) {
        if (i == this.mRequestUninstall) {
            if (this.mDisableAfterUninstall) {
                this.mDisableAfterUninstall = LOCAL_LOGV;
                AsyncTask.execute(new DisableChangerRunnable(this.mPm, this.mAppEntry.info.packageName, 3));
            }
            refreshAndFinishIfPossible(true);
        } else if (i == this.mRequestRemoveDeviceAdmin) {
            refreshAndFinishIfPossible(LOCAL_LOGV);
        }
    }

    public void handleDialogClick(int i) {
        if (i == 0) {
            this.mMetricsFeatureProvider.action((Context) this.mActivity, 874, (Pair<Integer, Object>[]) new Pair[0]);
            AsyncTask.execute(new DisableChangerRunnable(this.mPm, this.mAppEntry.info.packageName, 3));
        } else if (i == 1) {
            this.mMetricsFeatureProvider.action((Context) this.mActivity, 874, (Pair<Integer, Object>[]) new Pair[0]);
            uninstallPkg(this.mAppEntry.info.packageName, LOCAL_LOGV, true);
        } else if (i == 2) {
            forceStopPackage(this.mAppEntry.info.packageName);
        }
    }

    public void onPackageListChanged() {
        if (isAvailable()) {
            refreshUi();
        }
    }

    /* access modifiers changed from: package-private */
    public void retrieveAppEntry() {
        ApplicationsState.AppEntry entry = this.mState.getEntry(this.mPackageName, this.mUserId);
        this.mAppEntry = entry;
        if (entry != null) {
            try {
                this.mPackageInfo = this.mPm.getPackageInfo(entry.info.packageName, 4198976);
                this.mPackageName = this.mAppEntry.info.packageName;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Exception when retrieving package:" + this.mAppEntry.info.packageName, e);
                this.mPackageInfo = null;
            }
        } else {
            this.mPackageInfo = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void updateOpenButton() {
        Intent launchIntentForPackage = this.mPm.getLaunchIntentForPackage(this.mPackageName);
        this.mAppLaunchIntent = launchIntentForPackage;
        this.mButtonsPref.setButton1Visible(launchIntentForPackage != null ? true : LOCAL_LOGV);
    }

    /* access modifiers changed from: package-private */
    public void updateUninstallButton() {
        boolean z;
        ApplicationInfo applicationInfo;
        OverlayInfo overlayInfo;
        boolean z2 = true;
        int i = this.mAppEntry.info.flags & 1;
        boolean z3 = LOCAL_LOGV;
        boolean z4 = i != 0;
        if (z4) {
            z = handleDisableable();
        } else {
            z = (this.mPackageInfo.applicationInfo.flags & 8388608) != 0 || this.mUserManager.getUsers().size() < 2;
        }
        if (z4 && this.mDpm.packageHasActiveAdmins(this.mPackageInfo.packageName)) {
            z = false;
        }
        if (!isSystemPackage(this.mActivity.getResources(), this.mPm, this.mPackageInfo) ? Utils.isProfileOrDeviceOwner(this.mDpm, this.mPackageInfo.packageName, this.mUserId) : Utils.isProfileOrDeviceOwner(this.mUserManager, this.mDpm, this.mPackageInfo.packageName)) {
            z = false;
        }
        if (com.android.settingslib.Utils.isDeviceProvisioningPackage(this.mContext.getResources(), this.mAppEntry.info.packageName)) {
            z = false;
        }
        if (this.mDpm.isUninstallInQueue(this.mPackageName)) {
            z = false;
        }
        if (z && this.mHomePackages.contains(this.mPackageInfo.packageName)) {
            if (z4) {
                z = false;
            } else {
                ComponentName homeActivities = this.mPm.getHomeActivities(new ArrayList());
                if (homeActivities == null) {
                    if (this.mHomePackages.size() <= 1) {
                        z2 = false;
                    }
                    z = z2;
                } else {
                    z = !this.mPackageInfo.packageName.equals(homeActivities.getPackageName());
                }
            }
        }
        if (this.mAppsControlDisallowedBySystem) {
            z = false;
        }
        if (!this.mAppEntry.info.isResourceOverlay() || (!z4 && ((overlayInfo = this.mOverlayManager.getOverlayInfo((applicationInfo = this.mAppEntry.info).packageName, UserHandle.getUserHandleForUid(applicationInfo.uid))) == null || !overlayInfo.isEnabled() || this.mState.getEntry(overlayInfo.targetPackageName, UserHandle.getUserId(this.mAppEntry.info.uid)) == null))) {
            z3 = z;
        }
        this.mButtonsPref.setButton2Enabled(z3);
    }

    private void setIntentAndFinish(boolean z, boolean z2) {
        Intent intent = new Intent();
        intent.putExtra(APP_CHG, z);
        intent.putExtra(KEY_REMOVE_TASK_WHEN_FINISHING, z2);
        this.mActivity.finishPreferencePanel(-1, intent);
        this.mFinishing = true;
    }

    private void refreshAndFinishIfPossible(boolean z) {
        if (!refreshUi()) {
            setIntentAndFinish(true, z);
        } else {
            startListeningToPackageRemove();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateForceStopButton() {
        if (this.mDpm.packageHasActiveAdmins(this.mPackageInfo.packageName)) {
            Log.w(TAG, "User can't force stop device admin");
            updateForceStopButtonInner(LOCAL_LOGV);
        } else if ((this.mAppEntry.info.flags & 2097152) == 0) {
            Log.w(TAG, "App is not explicitly stopped");
            updateForceStopButtonInner(true);
        } else {
            Intent intent = new Intent("android.intent.action.QUERY_PACKAGE_RESTART", Uri.fromParts("package", this.mAppEntry.info.packageName, (String) null));
            intent.putExtra("android.intent.extra.PACKAGES", new String[]{this.mAppEntry.info.packageName});
            intent.putExtra("android.intent.extra.UID", this.mAppEntry.info.uid);
            intent.putExtra("android.intent.extra.user_handle", UserHandle.getUserId(this.mAppEntry.info.uid));
            Log.d(TAG, "Sending broadcast to query restart status for " + this.mAppEntry.info.packageName);
            this.mActivity.sendOrderedBroadcastAsUser(intent, UserHandle.CURRENT, (String) null, this.mCheckKillProcessesReceiver, (Handler) null, 0, (String) null, (Bundle) null);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateForceStopButtonInner(boolean z) {
        if (this.mAppsControlDisallowedBySystem) {
            this.mButtonsPref.setButton3Enabled(LOCAL_LOGV);
        } else {
            this.mButtonsPref.setButton3Enabled(z);
        }
    }

    /* access modifiers changed from: package-private */
    public void uninstallPkg(String str, boolean z, boolean z2) {
        stopListeningToPackageRemove();
        Intent intent = new Intent("android.intent.action.UNINSTALL_PACKAGE", Uri.parse("package:" + str));
        intent.putExtra("android.intent.extra.UNINSTALL_ALL_USERS", z);
        this.mMetricsFeatureProvider.action((Context) this.mActivity, 872, (Pair<Integer, Object>[]) new Pair[0]);
        this.mFragment.startActivityForResult(intent, this.mRequestUninstall);
        this.mDisableAfterUninstall = z2;
    }

    /* access modifiers changed from: package-private */
    public void forceStopPackage(String str) {
        MetricsFeatureProvider metricsFeatureProvider = this.mMetricsFeatureProvider;
        metricsFeatureProvider.action(metricsFeatureProvider.getAttribution(this.mActivity), 807, this.mFragment.getMetricsCategory(), str, 0);
        Log.d(TAG, "Stopping package " + str);
        ((ActivityManager) this.mActivity.getSystemService("activity")).forceStopPackage(str);
        int userId = UserHandle.getUserId(this.mAppEntry.info.uid);
        this.mState.invalidatePackage(str, userId);
        ApplicationsState.AppEntry entry = this.mState.getEntry(str, userId);
        if (entry != null) {
            this.mAppEntry = entry;
        }
        updateForceStopButton();
    }

    /* access modifiers changed from: package-private */
    public boolean handleDisableable() {
        if (this.mHomePackages.contains(this.mAppEntry.info.packageName) || isSystemPackage(this.mActivity.getResources(), this.mPm, this.mPackageInfo)) {
            this.mButtonsPref.setButton2Text(R.string.disable_text).setButton2Icon(R.drawable.ic_settings_disable);
            return LOCAL_LOGV;
        } else if (!this.mAppEntry.info.enabled || isDisabledUntilUsed()) {
            this.mButtonsPref.setButton2Text(R.string.enable_text).setButton2Icon(R.drawable.ic_settings_enable);
            return true;
        } else {
            this.mButtonsPref.setButton2Text(R.string.disable_text).setButton2Icon(R.drawable.ic_settings_disable);
            return true ^ this.mApplicationFeatureProvider.getKeepEnabledPackages().contains(this.mAppEntry.info.packageName);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isSystemPackage(Resources resources, PackageManager packageManager, PackageInfo packageInfo) {
        return com.android.settingslib.Utils.isSystemPackage(resources, packageManager, packageInfo);
    }

    /* access modifiers changed from: private */
    public boolean isDisabledUntilUsed() {
        if (this.mAppEntry.info.enabledSetting == 4) {
            return true;
        }
        return LOCAL_LOGV;
    }

    /* access modifiers changed from: private */
    public void showDialogInner(int i) {
        ButtonActionDialogFragment newInstance = ButtonActionDialogFragment.newInstance(i);
        newInstance.setTargetFragment(this.mFragment, 0);
        FragmentManager supportFragmentManager = this.mActivity.getSupportFragmentManager();
        newInstance.show(supportFragmentManager, "dialog " + i);
    }

    /* access modifiers changed from: private */
    public boolean isSingleUser() {
        if (this.mUserManager.getUserCount() == 1) {
            return true;
        }
        return LOCAL_LOGV;
    }

    private boolean signaturesMatch(String str, String str2) {
        if (str == null || str2 == null) {
            return LOCAL_LOGV;
        }
        try {
            if (this.mPm.checkSignatures(str, str2) >= 0) {
                return true;
            }
            return LOCAL_LOGV;
        } catch (Exception unused) {
            return LOCAL_LOGV;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v2, types: [int] */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean refreshUi() {
        /*
            r6 = this;
            java.lang.String r0 = r6.mPackageName
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            r6.retrieveAppEntry()
            com.android.settingslib.applications.ApplicationsState$AppEntry r0 = r6.mAppEntry
            if (r0 == 0) goto L_0x0066
            android.content.pm.PackageInfo r0 = r6.mPackageInfo
            if (r0 != 0) goto L_0x0012
            goto L_0x0066
        L_0x0012:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            android.content.pm.PackageManager r2 = r6.mPm
            r2.getHomeActivities(r0)
            java.util.HashSet<java.lang.String> r2 = r6.mHomePackages
            r2.clear()
            int r2 = r0.size()
        L_0x0025:
            if (r1 >= r2) goto L_0x0050
            java.lang.Object r3 = r0.get(r1)
            android.content.pm.ResolveInfo r3 = (android.content.pm.ResolveInfo) r3
            android.content.pm.ActivityInfo r4 = r3.activityInfo
            java.lang.String r4 = r4.packageName
            java.util.HashSet<java.lang.String> r5 = r6.mHomePackages
            r5.add(r4)
            android.content.pm.ActivityInfo r3 = r3.activityInfo
            android.os.Bundle r3 = r3.metaData
            if (r3 == 0) goto L_0x004d
            java.lang.String r5 = "android.app.home.alternate"
            java.lang.String r3 = r3.getString(r5)
            boolean r4 = r6.signaturesMatch(r3, r4)
            if (r4 == 0) goto L_0x004d
            java.util.HashSet<java.lang.String> r4 = r6.mHomePackages
            r4.add(r3)
        L_0x004d:
            int r1 = r1 + 1
            goto L_0x0025
        L_0x0050:
            com.android.settingslib.widget.ActionButtonsPreference r0 = r6.mButtonsPref
            r1 = 1
            if (r0 != 0) goto L_0x005d
            r6.initButtonPreference()
            com.android.settingslib.widget.ActionButtonsPreference r0 = r6.mButtonsPref
            r0.setVisible(r1)
        L_0x005d:
            r6.updateOpenButton()
            r6.updateUninstallButton()
            r6.updateForceStopButton()
        L_0x0066:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.appinfo.AppButtonsPreferenceController.refreshUi():boolean");
    }

    private void initButtonPreference() {
        this.mButtonsPref = ((ActionButtonsPreference) this.mScreen.findPreference(KEY_ACTION_BUTTONS)).setButton1Text(R.string.launch_instant_app).setButton1Icon(R.drawable.ic_settings_open).setButton1OnClickListener(new AppButtonsPreferenceController$$ExternalSyntheticLambda0(this)).setButton2Text(R.string.uninstall_text).setButton2Icon(R.drawable.ic_settings_delete).setButton2OnClickListener(new UninstallAndDisableButtonListener()).setButton3Text(R.string.force_stop).setButton3Icon(R.drawable.ic_settings_force_stop).setButton3OnClickListener(new ForceStopButtonListener()).setButton3Enabled(LOCAL_LOGV);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initButtonPreference$0(View view) {
        launchApplication();
    }

    private void startListeningToPackageRemove() {
        if (!this.mListeningToPackageRemove) {
            this.mListeningToPackageRemove = true;
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            this.mActivity.registerReceiver(this.mPackageRemovedReceiver, intentFilter);
        }
    }

    /* access modifiers changed from: private */
    public void stopListeningToPackageRemove() {
        if (this.mListeningToPackageRemove) {
            this.mListeningToPackageRemove = LOCAL_LOGV;
            this.mActivity.unregisterReceiver(this.mPackageRemovedReceiver);
        }
    }

    private void launchApplication() {
        if (this.mAppLaunchIntent != null) {
            if (this.mAccessedFromAutoRevoke) {
                Log.i(TAG, "sessionId: " + this.mSessionId + " uninstalling " + this.mPackageName + " with uid " + getUid() + ", reached from auto revoke");
                SettingsStatsLog.write(272, this.mSessionId, getUid(), this.mPackageName, 6);
            }
            this.mContext.startActivityAsUser(this.mAppLaunchIntent, new UserHandle(this.mUserId));
        }
    }

    /* access modifiers changed from: private */
    public int getUid() {
        if (this.mPackageInfo == null) {
            retrieveAppEntry();
        }
        PackageInfo packageInfo = this.mPackageInfo;
        if (packageInfo != null) {
            return packageInfo.applicationInfo.uid;
        }
        return -1;
    }

    private boolean isInstantApp() {
        ApplicationsState.AppEntry appEntry = this.mAppEntry;
        if (appEntry == null || !AppUtils.isInstant(appEntry.info)) {
            return LOCAL_LOGV;
        }
        return true;
    }

    private boolean isSystemModule() {
        ApplicationsState.AppEntry appEntry = this.mAppEntry;
        if (appEntry == null || (!AppUtils.isSystemModule(this.mContext, appEntry.info.packageName) && !AppUtils.isMainlineModule(this.mPm, this.mAppEntry.info.packageName))) {
            return LOCAL_LOGV;
        }
        return true;
    }

    private class DisableChangerRunnable implements Runnable {
        final String mPackageName;
        final PackageManager mPm;
        final int mState;

        public DisableChangerRunnable(PackageManager packageManager, String str, int i) {
            this.mPm = packageManager;
            this.mPackageName = str;
            this.mState = i;
        }

        public void run() {
            this.mPm.setApplicationEnabledSetting(this.mPackageName, this.mState, 0);
        }
    }
}
