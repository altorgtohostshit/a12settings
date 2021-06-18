package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class TelephonyTogglePreferenceController extends TogglePreferenceController implements TelephonyAvailabilityCallback, TelephonyAvailabilityHandler {
    private AtomicInteger mAvailabilityStatus = new AtomicInteger(0);
    private AtomicInteger mSetSessionCount = new AtomicInteger(0);
    protected int mSubId = -1;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public abstract /* synthetic */ int getAvailabilityStatus(int i);

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

    public boolean isSliceable() {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public TelephonyTogglePreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        if (this.mSetSessionCount.get() <= 0) {
            this.mAvailabilityStatus.set(MobileNetworkUtils.getAvailability(this.mContext, this.mSubId, new TelephonyTogglePreferenceController$$ExternalSyntheticLambda0(this)));
        }
        return this.mAvailabilityStatus.get();
    }

    public void setAvailabilityStatus(int i) {
        this.mAvailabilityStatus.set(i);
        this.mSetSessionCount.getAndIncrement();
    }

    public void unsetAvailabilityStatus() {
        this.mSetSessionCount.getAndDecrement();
    }

    public PersistableBundle getCarrierConfigForSubId(int i) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            return null;
        }
        return ((CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class)).getConfigForSubId(i);
    }

    public Resources getResourcesForSubId() {
        return SubscriptionManager.getResourcesForSubId(this.mContext, this.mSubId);
    }
}
