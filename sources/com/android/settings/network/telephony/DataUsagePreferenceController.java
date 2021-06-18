package com.android.settings.network.telephony;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkTemplate;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.datausage.DataUsageUtils;
import com.android.settings.datausage.lib.DataUsageLib;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.net.DataUsageController;
import com.android.settingslib.utils.ThreadUtils;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class DataUsagePreferenceController extends TelephonyBasePreferenceController {
    private static final String LOG_TAG = "DataUsagePreferCtrl";
    private Future<Long> mHistoricalUsageLevel;
    private AtomicReference<NetworkTemplate> mTemplate = new AtomicReference<>();
    private Future<NetworkTemplate> mTemplateFuture;

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

    public DataUsagePreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus(int i) {
        return SubscriptionManager.isValidSubscriptionId(i) ^ true ? 1 : 0;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        Intent intent = new Intent("android.settings.MOBILE_DATA_USAGE");
        intent.putExtra("network_template", getNetworkTemplate());
        intent.putExtra("android.provider.extra.SUB_ID", this.mSubId);
        this.mContext.startActivity(intent);
        return true;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            preference.setEnabled(false);
            return;
        }
        CharSequence dataUsageSummary = getDataUsageSummary(this.mContext, this.mSubId);
        if (dataUsageSummary == null) {
            preference.setEnabled(false);
            return;
        }
        preference.setEnabled(true);
        preference.setSummary(dataUsageSummary);
    }

    public void init(int i) {
        this.mSubId = i;
        this.mTemplate.set((Object) null);
        this.mTemplateFuture = ThreadUtils.postOnBackgroundThread((Callable) new DataUsagePreferenceController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$init$0() throws Exception {
        return fetchMobileTemplate(this.mContext, this.mSubId);
    }

    private NetworkTemplate fetchMobileTemplate(Context context, int i) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            return null;
        }
        return DataUsageLib.getMobileTemplate(context, i);
    }

    private NetworkTemplate getNetworkTemplate() {
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            return null;
        }
        NetworkTemplate networkTemplate = this.mTemplate.get();
        if (networkTemplate != null) {
            return networkTemplate;
        }
        try {
            NetworkTemplate networkTemplate2 = this.mTemplateFuture.get();
            try {
                this.mTemplate.set(networkTemplate2);
                return networkTemplate2;
            } catch (InterruptedException | NullPointerException | ExecutionException e) {
                e = e;
                networkTemplate = networkTemplate2;
            }
        } catch (InterruptedException | NullPointerException | ExecutionException e2) {
            e = e2;
            Log.e(LOG_TAG, "Fail to get data usage template", e);
            return networkTemplate;
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public DataUsageController.DataUsageInfo getDataUsageInfo(DataUsageController dataUsageController) {
        return dataUsageController.getDataUsageInfo(getNetworkTemplate());
    }

    private CharSequence getDataUsageSummary(Context context, int i) {
        DataUsageController dataUsageController = new DataUsageController(context);
        dataUsageController.setSubscriptionId(i);
        this.mHistoricalUsageLevel = ThreadUtils.postOnBackgroundThread((Callable) new DataUsagePreferenceController$$ExternalSyntheticLambda1(this, dataUsageController));
        DataUsageController.DataUsageInfo dataUsageInfo = getDataUsageInfo(dataUsageController);
        long j = dataUsageInfo.usageLevel;
        if (j <= 0) {
            try {
                j = this.mHistoricalUsageLevel.get().longValue();
            } catch (Exception unused) {
            }
        }
        if (j <= 0) {
            return null;
        }
        return context.getString(R.string.data_usage_template, new Object[]{DataUsageUtils.formatDataUsage(context, j), dataUsageInfo.period});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$getDataUsageSummary$1(DataUsageController dataUsageController) throws Exception {
        return Long.valueOf(dataUsageController.getHistoricalUsageLevel(getNetworkTemplate()));
    }
}
