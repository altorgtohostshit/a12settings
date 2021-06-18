package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.telecom.PhoneAccountHandle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import com.android.settings.Utils;
import com.android.settings.slices.SliceBackgroundWorker;

public class SmsDefaultSubscriptionController extends DefaultSubscriptionController {
    private final boolean mIsAskEverytimeSupported = this.mContext.getResources().getBoolean(17891633);

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public PhoneAccountHandle getDefaultCallingAccountHandle() {
        return null;
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

    public SmsDefaultSubscriptionController(Context context, String str) {
        super(context, str);
    }

    /* access modifiers changed from: protected */
    public SubscriptionInfo getDefaultSubscriptionInfo() {
        return this.mManager.getActiveSubscriptionInfo(getDefaultSubscriptionId());
    }

    /* access modifiers changed from: protected */
    public int getDefaultSubscriptionId() {
        return SubscriptionManager.getDefaultSmsSubscriptionId();
    }

    /* access modifiers changed from: protected */
    public void setDefaultSubscription(int i) {
        this.mManager.setDefaultSmsSubId(i);
    }

    /* access modifiers changed from: protected */
    public boolean isAskEverytimeSupported() {
        return this.mIsAskEverytimeSupported;
    }

    public CharSequence getSummary() {
        if (Utils.isProviderModelEnabled(this.mContext)) {
            return MobileNetworkUtils.getPreferredStatus(this.mContext, this.mManager, false);
        }
        return super.getSummary();
    }
}
