package com.android.settings.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.Utils;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.RestrictedAppPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import java.util.List;
import java.util.Map;

public class LocationInjectedServicesPreferenceController extends LocationBasePreferenceController implements LifecycleObserver, OnResume, OnPause {
    static final IntentFilter INTENT_FILTER_INJECTED_SETTING_CHANGED = new IntentFilter("android.location.InjectedSettingChanged");
    private static final String TAG = "LocationPrefCtrl";
    protected PreferenceCategory mCategoryLocationServices;
    BroadcastReceiver mInjectedSettingsReceiver;
    AppSettingsInjector mInjector;

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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public LocationInjectedServicesPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void init(DashboardFragment dashboardFragment) {
        super.init(dashboardFragment);
        this.mInjector = new AppSettingsInjector(this.mContext, getMetricsCategory());
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mCategoryLocationServices = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        this.mCategoryLocationServices.removeAll();
        boolean z = false;
        for (Map.Entry next : getLocationServices().entrySet()) {
            for (Preference preference2 : (List) next.getValue()) {
                if (preference2 instanceof RestrictedAppPreference) {
                    ((RestrictedAppPreference) preference2).checkRestrictionAndSetDisabled();
                }
            }
            if (((Integer) next.getKey()).intValue() == UserHandle.myUserId()) {
                if (this.mCategoryLocationServices != null) {
                    LocationSettings.addPreferencesSorted((List) next.getValue(), this.mCategoryLocationServices);
                }
                z = true;
            }
        }
        this.mCategoryLocationServices.setVisible(z);
    }

    public void onLocationModeChanged(int i, boolean z) {
        this.mInjector.reloadStatusMessages();
    }

    public void onResume() {
        if (this.mInjectedSettingsReceiver == null) {
            this.mInjectedSettingsReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (Log.isLoggable(LocationInjectedServicesPreferenceController.TAG, 3)) {
                        Log.d(LocationInjectedServicesPreferenceController.TAG, "Received settings change intent: " + intent);
                    }
                    LocationInjectedServicesPreferenceController.this.mInjector.reloadStatusMessages();
                }
            };
        }
        this.mContext.registerReceiver(this.mInjectedSettingsReceiver, INTENT_FILTER_INJECTED_SETTING_CHANGED);
    }

    public void onPause() {
        this.mContext.unregisterReceiver(this.mInjectedSettingsReceiver);
    }

    /* access modifiers changed from: protected */
    public Map<Integer, List<Preference>> getLocationServices() {
        int managedProfileId = Utils.getManagedProfileId(this.mUserManager, UserHandle.myUserId());
        return this.mInjector.getInjectedSettings(this.mFragment.getPreferenceManager().getContext(), (managedProfileId == -10000 || this.mLocationEnabler.getShareLocationEnforcedAdmin(managedProfileId) == null) ? -2 : UserHandle.myUserId());
    }
}
