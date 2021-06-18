package com.android.settings.deviceinfo;

import android.content.Context;
import android.content.IntentFilter;
import android.os.storage.StorageManager;
import android.text.format.Formatter;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.deviceinfo.PrivateStorageInfo;
import com.android.settingslib.deviceinfo.StorageManagerVolumeProvider;
import com.android.settingslib.utils.ThreadUtils;
import java.text.NumberFormat;
import java.util.concurrent.Future;

public class TopLevelStoragePreferenceController extends BasePreferenceController {
    private final StorageManager mStorageManager;
    private final StorageManagerVolumeProvider mStorageManagerVolumeProvider;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
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

    public TopLevelStoragePreferenceController(Context context, String str) {
        super(context, str);
        StorageManager storageManager = (StorageManager) this.mContext.getSystemService(StorageManager.class);
        this.mStorageManager = storageManager;
        this.mStorageManagerVolumeProvider = new StorageManagerVolumeProvider(storageManager);
    }

    /* access modifiers changed from: protected */
    public void refreshSummary(Preference preference) {
        if (preference != null) {
            refreshSummaryThread(preference);
        }
    }

    /* access modifiers changed from: protected */
    public Future refreshSummaryThread(Preference preference) {
        return ThreadUtils.postOnBackgroundThread((Runnable) new TopLevelStoragePreferenceController$$ExternalSyntheticLambda0(this, preference));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshSummaryThread$1(Preference preference) {
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        PrivateStorageInfo privateStorageInfo = PrivateStorageInfo.getPrivateStorageInfo(getStorageManagerVolumeProvider());
        ThreadUtils.postOnMainThread(new TopLevelStoragePreferenceController$$ExternalSyntheticLambda1(this, preference, percentInstance, (double) (privateStorageInfo.totalBytes - privateStorageInfo.freeBytes), privateStorageInfo));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshSummaryThread$0(Preference preference, NumberFormat numberFormat, double d, PrivateStorageInfo privateStorageInfo) {
        preference.setSummary((CharSequence) this.mContext.getString(R.string.storage_summary, new Object[]{numberFormat.format(d / ((double) privateStorageInfo.totalBytes)), Formatter.formatFileSize(this.mContext, privateStorageInfo.freeBytes)}));
    }

    /* access modifiers changed from: protected */
    public StorageManagerVolumeProvider getStorageManagerVolumeProvider() {
        return this.mStorageManagerVolumeProvider;
    }
}
