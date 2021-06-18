package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.text.TextUtils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class CarrierSettingsVersionPreferenceController extends BasePreferenceController {
    private CarrierConfigManager mManager;
    private int mSubscriptionId = -1;

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

    public CarrierSettingsVersionPreferenceController(Context context, String str) {
        super(context, str);
        this.mManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    public void init(int i) {
        this.mSubscriptionId = i;
    }

    public CharSequence getSummary() {
        PersistableBundle configForSubId = this.mManager.getConfigForSubId(this.mSubscriptionId);
        if (configForSubId == null) {
            return null;
        }
        return configForSubId.getString("carrier_config_version_string");
    }

    public int getAvailabilityStatus() {
        return TextUtils.isEmpty(getSummary()) ? 3 : 0;
    }
}
