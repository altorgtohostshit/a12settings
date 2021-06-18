package com.android.settings.network;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.network.SubscriptionsPreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.wifi.WifiPickerTrackerHelper;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class NetworkMobileProviderController extends BasePreferenceController implements SubscriptionsPreferenceController.UpdateListener {
    private static final int PREFERENCE_START_ORDER = 10;
    public static final String PREF_KEY_PROVIDER_MOBILE_NETWORK = "provider_model_mobile_network";
    private static final String TAG = "NetworkMobileProviderController";
    private int mOriginalExpandedChildrenCount;
    private PreferenceCategory mPreferenceCategory;
    private PreferenceScreen mPreferenceScreen;
    private SubscriptionsPreferenceController mSubscriptionsController;

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

    public NetworkMobileProviderController(Context context, String str) {
        super(context, str);
    }

    public void init(Lifecycle lifecycle) {
        this.mSubscriptionsController = createSubscriptionsController(lifecycle);
    }

    /* access modifiers changed from: package-private */
    public SubscriptionsPreferenceController createSubscriptionsController(Lifecycle lifecycle) {
        SubscriptionsPreferenceController subscriptionsPreferenceController = this.mSubscriptionsController;
        return subscriptionsPreferenceController == null ? new SubscriptionsPreferenceController(this.mContext, lifecycle, this, PREF_KEY_PROVIDER_MOBILE_NETWORK, 10) : subscriptionsPreferenceController;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (this.mSubscriptionsController == null) {
            Log.e(TAG, "[displayPreference] SubscriptionsController is null.");
            return;
        }
        this.mPreferenceScreen = preferenceScreen;
        this.mOriginalExpandedChildrenCount = preferenceScreen.getInitialExpandedChildrenCount();
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(PREF_KEY_PROVIDER_MOBILE_NETWORK);
        this.mPreferenceCategory = preferenceCategory;
        preferenceCategory.setVisible(isAvailable());
        this.mSubscriptionsController.displayPreference(preferenceScreen);
    }

    public int getAvailabilityStatus() {
        SubscriptionsPreferenceController subscriptionsPreferenceController = this.mSubscriptionsController;
        if (subscriptionsPreferenceController != null && subscriptionsPreferenceController.isAvailable()) {
            return 0;
        }
        return 2;
    }

    public void onChildrenUpdated() {
        boolean isAvailable = isAvailable();
        int i = this.mOriginalExpandedChildrenCount;
        if (i != Integer.MAX_VALUE) {
            if (isAvailable) {
                this.mPreferenceScreen.setInitialExpandedChildrenCount(i + this.mPreferenceCategory.getPreferenceCount());
            } else {
                this.mPreferenceScreen.setInitialExpandedChildrenCount(i);
            }
        }
        this.mPreferenceCategory.setVisible(isAvailable);
    }

    public void setWifiPickerTrackerHelper(WifiPickerTrackerHelper wifiPickerTrackerHelper) {
        SubscriptionsPreferenceController subscriptionsPreferenceController = this.mSubscriptionsController;
        if (subscriptionsPreferenceController != null) {
            subscriptionsPreferenceController.setWifiPickerTrackerHelper(wifiPickerTrackerHelper);
        }
    }
}
