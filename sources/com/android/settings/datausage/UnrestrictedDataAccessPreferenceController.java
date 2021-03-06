package com.android.settings.datausage;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.UserHandle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.applications.AppStateBaseBridge;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class UnrestrictedDataAccessPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, OnDestroy, ApplicationsState.Callbacks, AppStateBaseBridge.Callback, Preference.OnPreferenceChangeListener {
    private final ApplicationsState mApplicationsState;
    private final DataSaverBackend mDataSaverBackend;
    private final AppStateDataUsageBridge mDataUsageBridge;
    private boolean mExtraLoaded;
    private ApplicationsState.AppFilter mFilter;
    private DashboardFragment mParentFragment;
    private PreferenceScreen mScreen;
    private ApplicationsState.Session mSession;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
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

    public void onPackageListChanged() {
    }

    public void onPackageSizeChanged(String str) {
    }

    public void onRunningStateChanged(boolean z) {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public UnrestrictedDataAccessPreferenceController(Context context, String str) {
        super(context, str);
        ApplicationsState instance = ApplicationsState.getInstance((Application) context.getApplicationContext());
        this.mApplicationsState = instance;
        DataSaverBackend dataSaverBackend = new DataSaverBackend(context);
        this.mDataSaverBackend = dataSaverBackend;
        this.mDataUsageBridge = new AppStateDataUsageBridge(instance, this, dataSaverBackend);
    }

    public void setFilter(ApplicationsState.AppFilter appFilter) {
        this.mFilter = appFilter;
    }

    public void setParentFragment(DashboardFragment dashboardFragment) {
        this.mParentFragment = dashboardFragment;
    }

    public void setSession(Lifecycle lifecycle) {
        this.mSession = this.mApplicationsState.newSession(this, lifecycle);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mScreen = preferenceScreen;
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_data_saver) ? 1 : 3;
    }

    public void onStart() {
        this.mDataUsageBridge.resume();
    }

    public void onStop() {
        this.mDataUsageBridge.pause();
    }

    public void onDestroy() {
        this.mDataUsageBridge.release();
    }

    public void onExtraInfoUpdated() {
        this.mExtraLoaded = true;
        rebuild();
    }

    public void onRebuildComplete(ArrayList<ApplicationsState.AppEntry> arrayList) {
        if (arrayList != null) {
            TreeSet treeSet = new TreeSet();
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ApplicationsState.AppEntry appEntry = arrayList.get(i);
                if (shouldAddPreference(appEntry)) {
                    String generateKey = UnrestrictedDataAccessPreference.generateKey(appEntry);
                    treeSet.add(generateKey);
                    UnrestrictedDataAccessPreference unrestrictedDataAccessPreference = (UnrestrictedDataAccessPreference) this.mScreen.findPreference(generateKey);
                    if (unrestrictedDataAccessPreference == null) {
                        unrestrictedDataAccessPreference = new UnrestrictedDataAccessPreference(this.mScreen.getContext(), appEntry, this.mApplicationsState, this.mDataSaverBackend, this.mParentFragment);
                        unrestrictedDataAccessPreference.setOnPreferenceChangeListener(this);
                        this.mScreen.addPreference(unrestrictedDataAccessPreference);
                    } else {
                        Context context = this.mContext;
                        ApplicationInfo applicationInfo = appEntry.info;
                        unrestrictedDataAccessPreference.setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfMeteredDataRestricted(context, applicationInfo.packageName, UserHandle.getUserId(applicationInfo.uid)));
                        unrestrictedDataAccessPreference.updateState();
                    }
                    unrestrictedDataAccessPreference.setOrder(i);
                }
            }
            removeUselessPrefs(treeSet);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean z = false;
        if (!(preference instanceof UnrestrictedDataAccessPreference)) {
            return false;
        }
        UnrestrictedDataAccessPreference unrestrictedDataAccessPreference = (UnrestrictedDataAccessPreference) preference;
        if (obj == Boolean.TRUE) {
            z = true;
        }
        logSpecialPermissionChange(z, unrestrictedDataAccessPreference.getEntry().info.packageName);
        this.mDataSaverBackend.setIsAllowlisted(unrestrictedDataAccessPreference.getEntry().info.uid, unrestrictedDataAccessPreference.getEntry().info.packageName, z);
        unrestrictedDataAccessPreference.getDataUsageState().isDataSaverAllowlisted = z;
        return true;
    }

    public void rebuild() {
        ArrayList<ApplicationsState.AppEntry> rebuild;
        if (this.mExtraLoaded && (rebuild = this.mSession.rebuild(this.mFilter, ApplicationsState.ALPHA_COMPARATOR)) != null) {
            onRebuildComplete(rebuild);
        }
    }

    private void removeUselessPrefs(Set<String> set) {
        int preferenceCount = this.mScreen.getPreferenceCount();
        if (preferenceCount > 0) {
            for (int i = preferenceCount - 1; i >= 0; i--) {
                Preference preference = this.mScreen.getPreference(i);
                String key = preference.getKey();
                if (set.isEmpty() || !set.contains(key)) {
                    this.mScreen.removePreference(preference);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void logSpecialPermissionChange(boolean z, String str) {
        FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider().action(this.mContext, z ? 781 : 782, str);
    }

    static boolean shouldAddPreference(ApplicationsState.AppEntry appEntry) {
        return appEntry != null && UserHandle.isApp(appEntry.info.uid);
    }
}
