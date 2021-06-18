package com.android.settings.wifi.details2;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.wifitrackerlib.WifiEntry;

public class WifiSubscriptionDetailPreferenceController2 extends BasePreferenceController {
    private static final String KEY_WIFI_SUBSCRIPTION_DETAIL = "subscription_detail";
    private WifiEntry mWifiEntry;

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

    public WifiSubscriptionDetailPreferenceController2(Context context) {
        super(context, KEY_WIFI_SUBSCRIPTION_DETAIL);
    }

    public void setWifiEntry(WifiEntry wifiEntry) {
        this.mWifiEntry = wifiEntry;
    }

    public int getAvailabilityStatus() {
        return this.mWifiEntry.canManageSubscription() ? 0 : 2;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!KEY_WIFI_SUBSCRIPTION_DETAIL.equals(preference.getKey())) {
            return false;
        }
        this.mWifiEntry.manageSubscription();
        return true;
    }
}
