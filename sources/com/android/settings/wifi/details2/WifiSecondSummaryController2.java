package com.android.settings.wifi.details2;

import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.wifitrackerlib.WifiEntry;

public class WifiSecondSummaryController2 extends BasePreferenceController {
    private static final String KEY_WIFI_SECOND_SUMMARY = "second_summary";
    private CharSequence mSecondSummary;

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

    public WifiSecondSummaryController2(Context context) {
        super(context, KEY_WIFI_SECOND_SUMMARY);
    }

    public void setWifiEntry(WifiEntry wifiEntry) {
        this.mSecondSummary = wifiEntry.getSecondSummary();
    }

    public int getAvailabilityStatus() {
        return TextUtils.isEmpty(this.mSecondSummary) ? 2 : 0;
    }

    public CharSequence getSummary() {
        return this.mSecondSummary;
    }
}
