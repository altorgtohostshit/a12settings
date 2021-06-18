package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;

public class NetworkProviderWifiCallingPreferenceController extends BasePreferenceController implements LifecycleObserver {
    private static final String PREFERENCE_CATEGORY_KEY = "provider_model_calling_category";
    private static final String TAG = "NetworkProviderWfcController";
    private NetworkProviderWifiCallingGroup mNetworkProviderWifiCallingGroup;
    private PreferenceCategory mPreferenceCategory;
    private PreferenceScreen mPreferenceScreen;

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

    public NetworkProviderWifiCallingPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void init(Lifecycle lifecycle) {
        this.mNetworkProviderWifiCallingGroup = createWifiCallingControllerForSub(lifecycle);
    }

    /* access modifiers changed from: protected */
    public NetworkProviderWifiCallingGroup createWifiCallingControllerForSub(Lifecycle lifecycle) {
        return new NetworkProviderWifiCallingGroup(this.mContext, lifecycle, PREFERENCE_CATEGORY_KEY);
    }

    public int getAvailabilityStatus() {
        NetworkProviderWifiCallingGroup networkProviderWifiCallingGroup = this.mNetworkProviderWifiCallingGroup;
        return (networkProviderWifiCallingGroup == null || !networkProviderWifiCallingGroup.isAvailable()) ? 3 : 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceScreen = preferenceScreen;
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(PREFERENCE_CATEGORY_KEY);
        this.mPreferenceCategory = preferenceCategory;
        preferenceCategory.setVisible(isAvailable());
        this.mNetworkProviderWifiCallingGroup.displayPreference(preferenceScreen);
    }
}
