package com.android.settings.deviceinfo.storage;

import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.IntentFilter;
import android.text.format.Formatter;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.UsageProgressBarPreference;
import java.io.File;
import java.io.IOException;

public class StorageUsageProgressBarPreferenceController extends BasePreferenceController {
    private static final String TAG = "StorageProgressCtrl";
    private StorageEntry mStorageEntry;
    private final StorageStatsManager mStorageStatsManager;
    long mTotalBytes;
    private UsageProgressBarPreference mUsageProgressBarPreference;
    long mUsedBytes;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 1;
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

    public StorageUsageProgressBarPreferenceController(Context context, String str) {
        super(context, str);
        this.mStorageStatsManager = (StorageStatsManager) context.getSystemService(StorageStatsManager.class);
    }

    public void setSelectedStorageEntry(StorageEntry storageEntry) {
        this.mStorageEntry = storageEntry;
        getStorageStatsAndUpdateUi();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mUsageProgressBarPreference = (UsageProgressBarPreference) preferenceScreen.findPreference(getPreferenceKey());
        getStorageStatsAndUpdateUi();
    }

    private void getStorageStatsAndUpdateUi() {
        ThreadUtils.postOnBackgroundThread((Runnable) new C0876x8465e7c6(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getStorageStatsAndUpdateUi$1() {
        try {
            StorageEntry storageEntry = this.mStorageEntry;
            if (storageEntry == null || !storageEntry.isMounted()) {
                throw new IOException();
            }
            if (this.mStorageEntry.isPrivate()) {
                long totalBytes = this.mStorageStatsManager.getTotalBytes(this.mStorageEntry.getFsUuid());
                this.mTotalBytes = totalBytes;
                this.mUsedBytes = totalBytes - this.mStorageStatsManager.getFreeBytes(this.mStorageEntry.getFsUuid());
            } else {
                File path = this.mStorageEntry.getPath();
                if (path != null) {
                    long totalSpace = path.getTotalSpace();
                    this.mTotalBytes = totalSpace;
                    this.mUsedBytes = totalSpace - path.getFreeSpace();
                } else {
                    Log.d(TAG, "Mounted public storage has null root path: " + this.mStorageEntry);
                    throw new IOException();
                }
            }
            if (this.mUsageProgressBarPreference != null) {
                ThreadUtils.postOnMainThread(new C0877x8465e7c7(this));
            }
        } catch (IOException unused) {
            this.mTotalBytes = 0;
            this.mUsedBytes = 0;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getStorageStatsAndUpdateUi$0() {
        updateState(this.mUsageProgressBarPreference);
    }

    public void updateState(Preference preference) {
        this.mUsageProgressBarPreference.setUsageSummary(getStorageSummary(R.string.storage_usage_summary, this.mUsedBytes));
        this.mUsageProgressBarPreference.setTotalSummary(getStorageSummary(R.string.storage_total_summary, this.mTotalBytes));
        this.mUsageProgressBarPreference.setPercent(this.mUsedBytes, this.mTotalBytes);
    }

    private String getStorageSummary(int i, long j) {
        Formatter.BytesResult formatBytes = Formatter.formatBytes(this.mContext.getResources(), j, 1);
        return this.mContext.getString(i, new Object[]{formatBytes.value, formatBytes.units});
    }
}
