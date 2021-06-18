package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.slices.SliceBackgroundWorker;

public class DisableSimFooterPreferenceController extends TelephonyBasePreferenceController {
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

    public DisableSimFooterPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void init(int i) {
        this.mSubId = i;
    }

    public int getAvailabilityStatus(int i) {
        if (i == -1) {
            return 2;
        }
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
        for (SubscriptionInfo next : SubscriptionUtil.getAvailableSubscriptions(this.mContext)) {
            if (next.getSubscriptionId() == i) {
                if (next.isEmbedded() || SubscriptionUtil.showToggleForPhysicalSim(subscriptionManager)) {
                    return 2;
                }
                return 0;
            }
        }
        return 0;
    }
}
