package com.android.settings.applications.specialaccess.notificationaccess;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.VersionedPackage;
import android.service.notification.NotificationListenerFilter;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.applications.AppStateBaseBridge;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class BridgedAppsPreferenceController extends BasePreferenceController implements LifecycleObserver, ApplicationsState.Callbacks, AppStateBaseBridge.Callback {
    private ApplicationsState mApplicationsState;
    private ComponentName mCn;
    private ApplicationsState.AppFilter mFilter;
    private NotificationListenerFilter mNlf;
    private NotificationBackend mNm;
    private PreferenceScreen mScreen;
    private ApplicationsState.Session mSession;
    private int mUserId;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
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

    public void onPackageSizeChanged(String str) {
    }

    public void onRunningStateChanged(boolean z) {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BridgedAppsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public BridgedAppsPreferenceController setAppState(ApplicationsState applicationsState) {
        this.mApplicationsState = applicationsState;
        return this;
    }

    public BridgedAppsPreferenceController setCn(ComponentName componentName) {
        this.mCn = componentName;
        return this;
    }

    public BridgedAppsPreferenceController setUserId(int i) {
        this.mUserId = i;
        return this;
    }

    public BridgedAppsPreferenceController setNm(NotificationBackend notificationBackend) {
        this.mNm = notificationBackend;
        return this;
    }

    public BridgedAppsPreferenceController setFilter(ApplicationsState.AppFilter appFilter) {
        this.mFilter = appFilter;
        return this;
    }

    public BridgedAppsPreferenceController setSession(Lifecycle lifecycle) {
        this.mSession = this.mApplicationsState.newSession(this, lifecycle);
        return this;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mScreen = preferenceScreen;
    }

    public void onExtraInfoUpdated() {
        rebuild();
    }

    public void onPackageListChanged() {
        rebuild();
    }

    public void onRebuildComplete(ArrayList<ApplicationsState.AppEntry> arrayList) {
        if (arrayList != null) {
            this.mNlf = this.mNm.getListenerFilter(this.mCn, this.mUserId);
            TreeSet treeSet = new TreeSet();
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ApplicationsState.AppEntry appEntry = arrayList.get(i);
                String str = appEntry.info.packageName + "|" + appEntry.info.uid;
                treeSet.add(str);
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mScreen.findPreference(str);
                if (checkBoxPreference == null) {
                    checkBoxPreference = new CheckBoxPreference(this.mScreen.getContext());
                    checkBoxPreference.setIcon(appEntry.icon);
                    checkBoxPreference.setTitle((CharSequence) appEntry.label);
                    checkBoxPreference.setKey(str);
                    this.mScreen.addPreference(checkBoxPreference);
                }
                checkBoxPreference.setOrder(i);
                NotificationListenerFilter notificationListenerFilter = this.mNlf;
                ApplicationInfo applicationInfo = appEntry.info;
                checkBoxPreference.setChecked(notificationListenerFilter.isPackageAllowed(new VersionedPackage(applicationInfo.packageName, applicationInfo.uid)));
                checkBoxPreference.setOnPreferenceChangeListener(new BridgedAppsPreferenceController$$ExternalSyntheticLambda0(this));
            }
            removeUselessPrefs(treeSet);
        }
    }

    public void onPackageIconChanged() {
        rebuild();
    }

    public void onLoadEntriesCompleted() {
        rebuild();
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean z = false;
        if (!(preference instanceof CheckBoxPreference)) {
            return false;
        }
        String substring = preference.getKey().substring(0, preference.getKey().indexOf("|"));
        int parseInt = Integer.parseInt(preference.getKey().substring(preference.getKey().indexOf("|") + 1));
        if (obj == Boolean.TRUE) {
            z = true;
        }
        NotificationListenerFilter listenerFilter = this.mNm.getListenerFilter(this.mCn, this.mUserId);
        this.mNlf = listenerFilter;
        if (z) {
            listenerFilter.removePackage(new VersionedPackage(substring, parseInt));
        } else {
            listenerFilter.addPackage(new VersionedPackage(substring, parseInt));
        }
        this.mNm.setListenerFilter(this.mCn, this.mUserId, this.mNlf);
        return true;
    }

    public void rebuild() {
        ArrayList<ApplicationsState.AppEntry> rebuild = this.mSession.rebuild(this.mFilter, ApplicationsState.ALPHA_COMPARATOR);
        if (rebuild != null) {
            onRebuildComplete(rebuild);
        }
    }

    private void removeUselessPrefs(Set<String> set) {
        int preferenceCount = this.mScreen.getPreferenceCount();
        if (preferenceCount > 0) {
            for (int i = preferenceCount - 1; i >= 0; i--) {
                Preference preference = this.mScreen.getPreference(i);
                if (!set.contains(preference.getKey())) {
                    this.mScreen.removePreference(preference);
                }
            }
        }
    }
}
