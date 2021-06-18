package com.android.settings.applications;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.applications.AppStateBaseBridge;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.datausage.AppStateDataUsageBridge;
import com.android.settings.datausage.DataSaverBackend;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.ArrayList;
import java.util.Iterator;

public class SpecialAppAccessPreferenceController extends BasePreferenceController implements AppStateBaseBridge.Callback, ApplicationsState.Callbacks, LifecycleObserver, OnStart, OnStop, OnDestroy {
    private final ApplicationsState mApplicationsState;
    private final DataSaverBackend mDataSaverBackend;
    private final AppStateDataUsageBridge mDataUsageBridge;
    private boolean mExtraLoaded;
    private Preference mPreference;
    ApplicationsState.Session mSession;

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

    public void onLoadEntriesCompleted() {
    }

    public void onPackageIconChanged() {
    }

    public void onPackageListChanged() {
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

    public SpecialAppAccessPreferenceController(Context context, String str) {
        super(context, str);
        ApplicationsState instance = ApplicationsState.getInstance((Application) context.getApplicationContext());
        this.mApplicationsState = instance;
        DataSaverBackend dataSaverBackend = new DataSaverBackend(context);
        this.mDataSaverBackend = dataSaverBackend;
        this.mDataUsageBridge = new AppStateDataUsageBridge(instance, this, dataSaverBackend);
    }

    public void setSession(Lifecycle lifecycle) {
        this.mSession = this.mApplicationsState.newSession(this, lifecycle);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
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

    public void updateState(Preference preference) {
        updateSummary();
    }

    public void onExtraInfoUpdated() {
        this.mExtraLoaded = true;
        updateSummary();
    }

    private void updateSummary() {
        if (this.mExtraLoaded && this.mPreference != null) {
            Iterator<ApplicationsState.AppEntry> it = this.mSession.getAllApps().iterator();
            int i = 0;
            while (it.hasNext()) {
                ApplicationsState.AppEntry next = it.next();
                if (ApplicationsState.FILTER_DOWNLOADED_AND_LAUNCHER.filterApp(next)) {
                    Object obj = next.extraInfo;
                    if ((obj instanceof AppStateDataUsageBridge.DataUsageState) && ((AppStateDataUsageBridge.DataUsageState) obj).isDataSaverAllowlisted) {
                        i++;
                    }
                }
            }
            this.mPreference.setSummary((CharSequence) this.mContext.getResources().getQuantityString(R.plurals.special_access_summary, i, new Object[]{Integer.valueOf(i)}));
        }
    }

    public void onLauncherInfoChanged() {
        updateSummary();
    }
}
